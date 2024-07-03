package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.report.ReportUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Add implements Feature {
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        // Checking that the files exists and are valid
        List<String> filesToAdd = new ArrayList<>();
        if (params != null && params.length > 0) {
            for (var file : params) {
                if (!(file instanceof String))
                    return ReportUtils.FAILURE;
                File f = new File(project.getRootNode().getPath().resolve((String) file).toString());
                if (!f.exists() || !(f.isDirectory() || f.isFile()))
                    return ReportUtils.FAILURE;
                filesToAdd.add((String) file);
            }
        }

        try {
            Git git = Git.open(new File(project.getRootNode().getPath().toString()));
            for (String file : filesToAdd) {
                git.add().addFilepattern(file).call();

            }
            return ReportUtils.SUCCESS;
        } catch (IOException | GitAPIException e) {
            return ReportUtils.FAILURE;
        }
    }

    @Override
    public Type type() {
        return Mandatory.Features.Git.ADD;
    }
}
