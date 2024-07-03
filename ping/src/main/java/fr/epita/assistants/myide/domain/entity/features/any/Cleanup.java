package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.report.ReportUtils;
import fr.epita.assistants.myide.domain.service.NodeService;
import fr.epita.assistants.myide.domain.service.NodeServiceImpl;
import org.jboss.resteasy.spi.NotImplementedYetException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Cleanup implements Feature {
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        String path = project.getRootNode().getPath().resolve(".myideignore").toString();
        File myIdeIgnore = new File(path);

        // Checking if the file exists and is valid
        if (!myIdeIgnore.exists())
            return ReportUtils.SUCCESS;
        if (!myIdeIgnore.isFile())
            return ReportUtils.FAILURE;

        try {
            List<String> lines = Files.readAllLines(myIdeIgnore.toPath());
            lines = removeForbidden(lines);
            NodeService nodeService = new NodeServiceImpl();
            // For each line (aka file or folder) in .myideignore
            for (String pathToFile: lines) {
                String[] brokenPath = pathToFile.split(System.getProperty("file.separator"));
                Node node = project.getRootNode();
                String fullPath = node.getPath().toString();
                // Counting the number of nodes we got through to check if we found the file later
                int subPathsFound = 0;
                // We go to the final path from rootNode
                for (String subPath: brokenPath) {
                    for (Node n: node.getChildren()) {
                        String childPath = n.getPath().toString();
                        String wantedPath = node.getPath().toString() + System.getProperty("file.separator") + subPath;

                        if (childPath.equals(wantedPath)) {
                            node = n;
                            subPathsFound++;
                            break;
                        }
                    }
                }

                // If we did not go through as many nodes as sub paths, it is not a node to delete
                if (subPathsFound == brokenPath.length)
                    nodeService.delete(node);
                    //System.out.println("Removing " + node.getPath().toString());
            }
        } catch (IOException e) {
            return ReportUtils.FAILURE;
        }

        return ReportUtils.SUCCESS;
    }

    @Override
    public Type type() {
        return Mandatory.Features.Any.CLEANUP;
    }

    private List<String> removeForbidden(List<String> list) {
        Set<String> forbidden = new HashSet<>() {{
            add(".myideignore");
            add(".git");
            add("pom.xml");
        }};
        
        List<String> cleanList = new ArrayList<>();

        // Removing trailing "/" at beginning and end
        for (String path: list) {
            String replacement = path;
            if (path.startsWith("/"))
                replacement = replacement.substring(1);
            if (path.endsWith("/"))
                replacement = replacement.substring(0, replacement.length() - 1);
            
            if (!forbidden.contains(replacement))
                cleanList.add(replacement);
        }

        return cleanList;
    }
}
