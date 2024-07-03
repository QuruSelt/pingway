package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.report.ReportUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class Commit implements Feature {
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        // Getting commit message
        String commitMessage = "";
        if (params != null && params.length == 1 && params[0] instanceof String)
            commitMessage = (String) params[0];
        else
            return ReportUtils.FAILURE;

        // Doing the actual commit
        try {
            Git git = Git.open(new File(project.getRootNode().getPath().toString()));
            git.commit()
                .setMessage(commitMessage)
                .call();

            return ReportUtils.SUCCESS;
        } catch (IOException | GitAPIException e) {
            return ReportUtils.FAILURE;
        }
    }

    @Override
    public Type type() {
        return Mandatory.Features.Git.COMMIT;
    }
}
