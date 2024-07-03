package fr.epita.assistants.myide.domain.entity;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class NodeImpl implements Node {

    public NodeImpl(Path path, Node parent) {
        this.path = path;
        this.parent = parent;
        children = new ArrayList<>();
        if (Files.isDirectory(this.path)) {
            type = Types.FOLDER;
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.path)) {
                for (Path subPath : stream) {
                    children.add(new NodeImpl(subPath, this));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            type = Types.FILE;
        }
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public List<@NotNull Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public void removeChild(Node child) {
        children.remove(child);
    }

    private final List<Node> children;
    private final Type type;
    private final Path path;
    @Getter
    private final Node parent;
}
