package fr.epita.assistants.myide.domain.entity.features.maven;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.report.ReportUtils;
import org.jboss.resteasy.spi.NotImplementedYetException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Test implements Feature
{
    ArrayList<String> commands = new ArrayList<>(Arrays.asList("mvn", "test"));

    @Override
    public ExecutionReport execute(Project project, Object... params) {
        try {
            for (Object param : params)
            {
                commands.add((String) param);
            }

            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.directory(new File(project.getRootNode().getPath().toString()));
            pb.redirectError(project.getRootNode().getPath().resolve(".stderr.txt").toFile());
            pb.redirectOutput(project.getRootNode().getPath().resolve(".stdout.txt").toFile());
            Process process = pb.start();
            int returnVal = process.waitFor();
            if (returnVal != 0) {
                return ReportUtils.FAILURE;
            }
        } catch (Exception e) {
            return ReportUtils.FAILURE;
        }
        return ReportUtils.SUCCESS;
    }

    @Override
    public Type type() {
        return Mandatory.Features.Maven.TEST;
    }
}
