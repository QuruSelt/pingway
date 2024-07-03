package fr.epita.assistants;

import fr.epita.assistants.myide.domain.entity.*;
import fr.epita.assistants.myide.domain.entity.report.SearchFeatureReport;
import fr.epita.assistants.myide.domain.service.NodeService;
import fr.epita.assistants.myide.domain.service.ProjectService;
import fr.epita.assistants.myide.domain.service.ProjectServiceImpl;
import io.quarkus.runtime.annotations.QuarkusMain;

import java.nio.file.Paths;

@QuarkusMain
public class Main {
    public static void printChildren(Node node) {
        for (var truc: node.getChildren()) {
            System.out.println(truc.getPath().toString());
            if (truc.getType() == Node.Types.FOLDER)
                printChildren(truc);
        }
    }

    public static void searchTest(String ... params) {
        String testRoot = "./ping/tests/testSimple";

        ProjectService projectService = new ProjectServiceImpl();

        NodeService nodeService = projectService.getNodeService();

        Project project = projectService.load(Paths.get(testRoot));

        Feature.ExecutionReport report = projectService.execute(project, Mandatory.Features.Any.SEARCH, params);

        SearchFeatureReport searchFeatureReport = (SearchFeatureReport) report;

        System.out.println(searchFeatureReport.isSuccess());

        for (Node node: searchFeatureReport.getResults())
            System.out.println("Found in " + node.getPath().toString());
    }

    public static void cleanTest() {
        String testRoot = "./ping/tests/testSimple";

        ProjectService projectService = new ProjectServiceImpl();

        NodeService nodeService = projectService.getNodeService();

        Project project = projectService.load(Paths.get(testRoot));

        Feature.ExecutionReport report = projectService.execute(project, Mandatory.Features.Any.CLEANUP);

        System.out.println(report.isSuccess());
    }

    public static void main(String[] args) {
        cleanTest();
        //printChildren(project.getRootNode());
    }
}
