package fr.epita.assistants.myide.presentation.rest;

import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.*;
import fr.epita.assistants.myide.domain.service.NodeService;
import fr.epita.assistants.myide.domain.service.ProjectService;
import fr.epita.assistants.myide.domain.service.ProjectServiceImpl;
import io.quarkus.logging.Log;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import fr.epita.assistants.myide.utils.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MyIdeEndpoint {

    @GET @Path("/hello")
    public Response helloWorld()
    {
        Logger.log("Saying hello !");
        return Response.ok("Hello World !").build();
    }

    @POST @Path("/open/project")
    public Response open_project(SimpleRequest request)
    {
        String string_path = request.getPath();
        java.nio.file.Path path = Paths.get(string_path);
        ProjectService ps = MyIde.init(new MyIde.Configuration(path, path));
        project_service = (ProjectServiceImpl) ps;
        ProjectImpl loaded_project = (ProjectImpl) ps.load(path);
        current_project = loaded_project;
        Logger.log("Opening project at path " + string_path + " !");
        return Response.ok(loaded_project).build();
    }

    @POST @Path("/open/file")
    public Response open_file(SimpleRequest request)
    {
        String string_path = request.getPath();
        java.nio.file.Path path = Paths.get(string_path);

        NodeImpl n = (NodeImpl) current_project.getNodeAtPath(current_project.getRootNode(), path);

        if (n == null)
        {
            Logger.logError("Node at path " + string_path + " not found!");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        current_file = n;

        Logger.log("Opening file at path " + string_path + " !");
        return Response.ok(n).build();
    }

    @POST @Path("/create/file")
    public Response create_file(SimpleRequest request)
    {
        String string_path = request.getPath();
        java.nio.file.Path path = Paths.get(string_path);
        NodeService ns = project_service.getNodeService();

        NodeImpl n = (NodeImpl) current_project.getNodeAtPath(current_project.getRootNode(), path);

        if (n != null)
        {
            Logger.logError("Node at path " + string_path + " already exists !");
            return Response.status(Response.Status.CONFLICT).build();
        }

        Node newfile = ns.create(new NodeImpl(path, current_file.getParent()), path.getFileName().toString(), Node.Types.FILE);
        Logger.log("Creating file at path " + string_path + " !");
        return Response.ok(newfile).build();
    }

    @POST @Path("/create/folder")
    public Response create_folder(SimpleRequest request)
    {
        String string_path = request.getPath();
        java.nio.file.Path path = Paths.get(string_path);
        NodeService ns = project_service.getNodeService();

        Node n = current_project.getNodeAtPath(current_project.getRootNode(), path);

        if (n != null)
        {
            Logger.logError("Node at path " + string_path + " already exists !");
            return Response.status(Response.Status.CONFLICT).build();
        }

        Node newfolder = ns.create(new NodeImpl(path, current_file.getParent()), path.getFileName().toString(), Node.Types.FOLDER);
        Logger.log("Creating folder at path " + string_path + " !");
        return Response.ok(newfolder).build();
    }

    @POST @Path("/delete/file")
    public Response delete_file(SimpleRequest request)
    {
        String string_path = request.getPath();
        java.nio.file.Path path = Paths.get(string_path);
        NodeService ns = project_service.getNodeService();

        Node n = current_project.getNodeAtPath(current_project.getRootNode(), path);

        if (n == null)
        {
            Logger.logError("Node at path " + string_path + " not found!");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        boolean deleted = ns.delete(n);

        Logger.log("Deleting file at path " + string_path + " !");
        return Response.ok(deleted).build();
    }

    @POST @Path("/delete/folder")
    public Response delete_folder(SimpleRequest request)
    {
        String string_path = request.getPath();
        java.nio.file.Path path = Paths.get(string_path);
        NodeService ns = project_service.getNodeService();

        Node n = current_project.getNodeAtPath(current_project.getRootNode(), path);

        if (n == null)
        {
            Logger.logError("Node at path " + string_path + " not found!");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        boolean deleted = ns.delete(n);

        Logger.log("Deleting folder at path " + string_path + " !");
        return Response.ok(deleted).build();
    }

    @POST @Path("/execFeature")
    public Response exec_feat(FeatureRequest request)
    {
        String feature = request.getFeature();
        List<String> params = request.getParams();

        Feature.Type feat = null;
        switch (feature)
        {
            case "cleanup":
                feat = Mandatory.Features.Any.CLEANUP;
                break;

            case "dist":
                feat = Mandatory.Features.Any.DIST;
                break;

            case "search":
                feat = Mandatory.Features.Any.SEARCH;
                break;

            case "pull":
                feat = Mandatory.Features.Git.PULL;
                break;

            case "push":
                feat = Mandatory.Features.Git.PUSH;
                break;

            case "add":
                feat = Mandatory.Features.Git.ADD;
                break;

            case "commit":
                feat = Mandatory.Features.Git.COMMIT;
                break;

            case "compile":
                feat = Mandatory.Features.Maven.COMPILE;
                break;

            case "clean":
                feat = Mandatory.Features.Maven.CLEAN;
                break;

            case "test":
                feat = Mandatory.Features.Maven.TEST;
                break;

            case "package":
                feat = Mandatory.Features.Maven.PACKAGE;
                break;

            case "install":
                feat = Mandatory.Features.Maven.INSTALL;
                break;

            case "exec":
                feat = Mandatory.Features.Maven.EXEC;
                break;

            case "tree":
                feat = Mandatory.Features.Maven.TREE;
                break;
        }

        Feature.ExecutionReport report = project_service.execute(current_project, feat, params.toArray());

        if (report.isSuccess())
        {
            Logger.log("Executing feature " + request.getFeature() + " with parameters " + request.getParams() + " on project " + request.getProject() + " !");
        }
        else
        {
            Logger.logError("Cannot execute feature " + request.getFeature() + " with parameters " + request.getParams() + " on project " + request.getProject() + " !");
            return Response.status(Response.Status.CONFLICT).build();
        }
        return Response.ok().build();
    }

    @POST @Path("/move")
    public Response move_node(MoveRequest request)
    {
        java.nio.file.Path src = Paths.get(request.getSrc());
        java.nio.file.Path dst = Paths.get(request.getDst());

        Node nres;
        if (Files.exists(src) && Files.isDirectory(dst))
        {
            Node nsrc = current_project.getNodeAtPath(current_project.getRootNode(), src);
            Node ndst = current_project.getNodeAtPath(current_project.getRootNode(), dst);

            if (ndst == null || nsrc == null)
            {
                Logger.logError("Node at path " + request.getSrc() + " or " + request.getDst() + " not found!");
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            ProjectService ps = MyIde.init(new MyIde.Configuration(src, dst));
            NodeService ns = ps.getNodeService();

            nres = ns.move(nsrc, ndst);
        }
        else
        {
            Logger.logError("Node at path " + request.getSrc() + " or " + request.getDst() + " not found!");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Logger.log("Moving node at path " + request.getSrc() +  " to "  + request.getDst() + " !");
        return Response.ok(nres).build();
    }

    @POST @Path("/update")
    public Response update_node(UpdateRequest request)
    {
        java.nio.file.Path path = Paths.get(request.getPath());

        ProjectService ps = MyIde.init(new MyIde.Configuration(path, path));
        NodeService ns = ps.getNodeService();

        Node n = current_project.getNodeAtPath(current_project.getRootNode(), path);

        if (n == null)
        {
            Logger.logError("Node at path " + request.getPath() + " not found!");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Logger.log("Update node at path " + request.getPath() + " from " + request.getFrom() + " to " + request.getTo() + " by content : " + request.getContent());
        Node nres = ns.update(n, request.getFrom(), request.getTo(), request.getContent().getBytes());

        return Response.ok(nres).build();
    }

    private NodeImpl current_file;
    private ProjectImpl current_project;
    private ProjectServiceImpl project_service;
}