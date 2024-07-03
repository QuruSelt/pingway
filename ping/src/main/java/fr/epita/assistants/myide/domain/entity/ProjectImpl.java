package fr.epita.assistants.myide.domain.entity;

import fr.epita.assistants.myide.domain.entity.aspects.Any;
import fr.epita.assistants.myide.domain.entity.aspects.Git;
import fr.epita.assistants.myide.domain.entity.aspects.Maven;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ProjectImpl implements Project{

    public ProjectImpl(Node rootNode) {
        this.rootNode = rootNode;
        updateAspects();
    }

    /**
     * Update the aspects Set by looking at the working directory
     */
    private void updateAspects() {
        // A project always has an Any Aspect
        aspects = new HashSet<>();
        aspects.add(new Any());

        List<Node> workingDir = rootNode.getChildren();
        for (Node subFile: workingDir) {
            Path subPath = subFile.getPath();
            // Check for Git project
            if (subPath.getFileName().toString().equals(".git") &&
                    Files.isDirectory(subPath)) {
                aspects.add(new Git());
            }
            // Check for Maven project
            if (subPath.getFileName().toString().equals("pom.xml") &&
                    Files.isRegularFile(subPath)) {
                aspects.add(new Maven());
            }
        }
    }

    @Override
    public Node getRootNode() {
        return rootNode;
    }

    @Override
    public Set<Aspect> getAspects() {
        return aspects;
    }

    @Override
    public Optional<Feature> getFeature(Feature.Type featureType) {
        for (Aspect asp: aspects) {
            for (Feature feature: asp.getFeatureList()) {
                if (feature.type() == featureType)
                    return Optional.of(feature);
            }
        }
        return Optional.empty();
    }

    public Node getNodeAtPath(Node start, Path path)
    {
        for (Node child: start.getChildren())
        {
            if (child.getPath().equals(path))
            {
                return child;
            }
            else
            {
                Node newChild = getNodeAtPath(child, path);
                if (newChild != null)
                {
                    return newChild;
                }
            }
        }
        return null;
    }

    private Node rootNode;
    private Set<Aspect> aspects;
}
