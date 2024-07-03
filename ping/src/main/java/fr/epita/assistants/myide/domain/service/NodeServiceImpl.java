package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.NodeImpl;
import fr.epita.assistants.myide.utils.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NodeServiceImpl implements NodeService {

    @Override
    public Node update(Node node, int from, int to, byte[] insertedContent) {

        if (!Files.isRegularFile(node.getPath()) || node.isFolder())
        {
            throw new IllegalArgumentException("node is not a file");
        }

        long filesize;
        try {
            filesize = Files.size(node.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (from < 0 || from > filesize || to < 0 || to > filesize || to < from)
        {
            throw new IllegalArgumentException("Invalid range");
        }

        byte[] filecontent;
        try {
            filecontent = Files.readAllBytes(node.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Byte> contentlist = new ArrayList<>();
        for (byte b : filecontent)
        {
            contentlist.add(b);
        }

        contentlist.subList(from, to).clear();

        for (int i = 0; i < insertedContent.length; i++)
        {
            contentlist.add(from + i, insertedContent[i]);
        }

        byte[] modified = new byte[contentlist.size()];
        for (int i = 0; i < contentlist.size(); i++)
        {
            modified[i] = contentlist.get(i);
        }

        try {
            Files.write(node.getPath(), modified);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    @Override
    public boolean delete(Node node) {

        if (!Files.exists(node.getPath()))
        {
            return false;
        }

        if (node.isFolder())
        {
            List<Node> children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                Node child = children.get(i);
                if (!delete(child)) {
                    return false;
                }
                children.remove(child);
                i--;
            }
        }

        NodeImpl mynode = (NodeImpl) node;
        NodeImpl parent = (NodeImpl) mynode.getParent();
        if (parent != null)
        {
            parent.removeChild(node);
        }

        try {
            Files.delete(node.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (Files.exists(node.getPath()))
        {
            return false;
        }

        return true;
    }

    @Override
    public Node create(Node folder, String name, Node.Type type) {

        Path filepath = folder.getPath().resolve(name);
        NodeImpl newnode = new NodeImpl(filepath, folder);

        if (type == Node.Types.FILE)
        {
            try {
                Files.createFile(filepath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (type == Node.Types.FOLDER)
        {
            try {
                Files.createDirectories(filepath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        NodeImpl parent = (NodeImpl) folder;
        parent.addChild(newnode);

        return newnode;
    }

    @Override
    public Node move(Node nodeToMove, Node destinationFolder) {
        Path currpath = nodeToMove.getPath();
        Path destpath = destinationFolder.getPath().resolve(currpath.getFileName().toString());

        Node movedNode;
        if (Files.exists(currpath) && Files.isDirectory(destinationFolder.getPath()))
        {
            try {
                Files.move(currpath, destpath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            NodeImpl nodeimpl = (NodeImpl) nodeToMove;
            Node parent = nodeimpl.getParent();
            if (parent != null)
            {
                parent.getChildren().remove(nodeToMove);
            }
            delete(nodeToMove);

            movedNode = new NodeImpl(destpath, destinationFolder);
        }
        else
        {
            throw new IllegalArgumentException("source doesn't exist or destination is not a folder");
        }

        return movedNode;
    }
}
