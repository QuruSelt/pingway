package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.report.ReportUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.*;

public class Dist implements Feature {
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        Cleanup cleanup = new Cleanup();

        ExecutionReport cleanupReport = cleanup.execute(project);

        if (!cleanupReport.isSuccess())
            return ReportUtils.FAILURE;

        try (FileOutputStream fos = new FileOutputStream(pathToZip(project));
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            String rootFolderName = project.getRootNode().getPath().getFileName().toString();

            Files.walk(project.getRootNode().getPath())
                .forEach(path -> {
                    String entry = rootFolderName + "/" + project.getRootNode().getPath().relativize(path).toString();
                    try {
                        if (Files.isDirectory(path)) {
                            zos.putNextEntry(new ZipEntry(entry + "/"));
                            zos.closeEntry();
                        } else {
                            zos.putNextEntry(new ZipEntry(entry));
                            Files.copy(path, zos);
                            zos.closeEntry();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        } catch (IOException e) {
            return ReportUtils.FAILURE;
        }

        return ReportUtils.SUCCESS;
    }

    @Override
    public Type type() {
        return Mandatory.Features.Any.DIST;
    }

    private String pathToZip(Project project) {
        return project.getRootNode().getPath().toString() + ".zip";
    }
}
