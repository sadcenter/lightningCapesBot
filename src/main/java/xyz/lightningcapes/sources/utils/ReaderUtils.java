package xyz.lightningcapes.sources.utils;

import java.io.File;
import java.util.Objects;

public class ReaderUtils {

    /*@SneakyThrows
    public static String readJson(File file) {
        return Files.readString(file.toPath(), StandardCharsets.US_ASCII);
    }*/

    public static File found(File folder, String foundingName) {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().equals(foundingName)) {
                return file;
            }
        }
        return null;
    }

}
