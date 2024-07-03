package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.report.SearchFeatureReport;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Search implements Feature {
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        List<String> searchArgs = Arrays.stream(params)
                .map(param -> (String) param)
                .collect(Collectors.toList());

        String searchEntry = String.join(" ", searchArgs);
        List<Node> results = new ArrayList<>();

        searchInFiles(project.getRootNode(), searchEntry, results);

        if (results.isEmpty())
            return new SearchFeatureReport(results, false);

        return new SearchFeatureReport(results, true);
    }

    @Override
    public Type type() {
        return Mandatory.Features.Any.SEARCH;
    }

    private void searchInFiles(Node node, String search, List<Node> found) {
        if (node.isFolder())
            for (Node child: node.getChildren()) {
                searchInFiles(child, search, found);
            }
        else {
            try (Stream<String> lines = Files.lines(node.getPath())) {
                if (lines.anyMatch(line -> line.contains(search)))
                    found.add(node);
            } catch (IOException ignored) {

            }
        }
    }
}
