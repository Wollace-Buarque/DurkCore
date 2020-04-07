package dev.cromo29.durkcore.SpecificUtils;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;
import java.util.Objects;


public class GsonUtil {
    public static List<String> getJsonFileNamesInsideFolder(String folderPath) {
        List<String> fileNames = Lists.newArrayList();
        File folder = new File(folderPath);

        if (folder.exists())
            for (String fileName : Objects.requireNonNull(folder.list()))
                if (fileName.contains(".json")) fileNames.add(fileName.replace(".json", ""));

        return fileNames;
    }

    public static List<String> getYAMLFileNamesInsideFolder(String folderPath) {
        List<String> fileNames = Lists.newArrayList();
        File folder = new File(folderPath);

        if (folder.exists())
            for (String fileName : Objects.requireNonNull(folder.list()))
                if (fileName.contains(".yml")) fileNames.add(fileName.replace(".yml", ""));

        return fileNames;
    }
}
