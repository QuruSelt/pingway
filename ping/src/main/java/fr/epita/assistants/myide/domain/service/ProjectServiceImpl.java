package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectServiceImpl implements ProjectService {
    @Override
    public Project load(Path root) {
        if (!Files.isDirectory(root))
            throw new RuntimeException("Error loading project: given root is not a directory.");

        Node rootNode = new NodeImpl(root, null);
        Project project = new ProjectImpl(rootNode);
        // Create ServiceNode (Singleton)
        // Create Project to return
        return project;
    }

    @Override
    public Feature.ExecutionReport execute(Project project, Feature.Type featureType, Object... params) {
        Feature feature = project.getFeature(featureType).get();
        return feature.execute(project, params);
    }

    @Override
    public NodeService getNodeService() {
        return nodeService;
    }

    private NodeService nodeService = new NodeServiceImpl();
}
