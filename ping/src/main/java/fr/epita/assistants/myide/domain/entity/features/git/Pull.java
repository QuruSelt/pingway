package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.report.ReportUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class Pull implements Feature {
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        try {
            Git git = Git.open(new File(project.getRootNode().getPath().toString()));
            git.pull().call();

            return ReportUtils.SUCCESS;
        } catch (IOException | GitAPIException e) {
            return ReportUtils.FAILURE;
        }
    }

    @Override
    public Type type() {
        return Mandatory.Features.Git.PULL;
    }
}
