package xyz.lightningcapes.sources.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;

@Getter
public class Configuration {

    private final ConfigurationStorage configurationStorage;

    @SneakyThrows
    public Configuration() {
        File file = new File("config.yml");
        Gson gson = new GsonBuilder().disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
        if (file.createNewFile()) {
            configurationStorage = new ConfigurationStorage();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                gson.toJson(configurationStorage, writer);
                writer.flush();
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                configurationStorage = gson.fromJson(reader, ConfigurationStorage.class);
            }
        }
    }
}