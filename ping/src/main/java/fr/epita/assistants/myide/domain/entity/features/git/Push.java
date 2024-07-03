package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.report.ReportUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class Push implements Feature {
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File(project.getRootNode().getPath().toString()))
                    .readEnvironment()
                    .findGitDir()
                    .build();

            Git git = new Git(repository);
            git.fetch().call();

            // Getting local and remote branches
            Ref localBranch = repository.findRef("refs/heads/main");
            Ref remoteBranch = repository.findRef("refs/remotes/origin/main");

            boolean isUpToDate = localBranch.getObjectId().equals(remoteBranch.getObjectId());

            if (!isUpToDate)
                git.push().call();

            return ReportUtils.SUCCESS;
        } catch (IOException | GitAPIException e) {
            return ReportUtils.FAILURE;
        }
    }

    @Override
    public Type type() {
        return Mandatory.Features.Git.PUSH;
    }
}
