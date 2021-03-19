package dev.cromo29.durkcore.SpecificUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GsonUtil {

    public static List<String> getJsonFileNamesInsideFolder(String folderPath) {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(folderPath);

        if (folder.exists())
            for (String fileName : Objects.requireNonNull(folder.list()))
                if (fileName.contains(".json")) fileNames.add(fileName.replace(".json", ""));

        return fileNames;
    }

    public static List<String> getYAMLFileNamesInsideFolder(String folderPath) {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(folderPath);

        if (folder.exists())
            for (String fileName : Objects.requireNonNull(folder.list()))
                if (fileName.contains(".yml")) fileNames.add(fileName.replace(".yml", ""));

        return fileNames;
    }
}
