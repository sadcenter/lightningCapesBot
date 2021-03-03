package xyz.lightningcapes.sources.utils;

import lombok.SneakyThrows;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

public class ReaderUtils {

    @SneakyThrows
    public static String readJson(File file) {
        return Files.readString(file.toPath(), StandardCharsets.US_ASCII);
    }

    public static File found(File folder, String foundingName) {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().equals(foundingName)) {
                return file;
            }
        }
        return null;
    }

}
