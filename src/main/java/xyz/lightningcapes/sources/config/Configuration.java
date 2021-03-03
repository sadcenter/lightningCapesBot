package xyz.lightningcapes.sources.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;

import java.io.*;

public final class Configuration {

    private final ConfigurationStorage configurationStorage;

    @SneakyThrows
    public Configuration() {
        File file = new File("config.yml");
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        if (!file.exists()) {
            this.configurationStorage = new ConfigurationStorage();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                gson.toJson(configurationStorage, writer);
                writer.flush();
            }
        } else {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                this.configurationStorage = gson.fromJson(bufferedReader, ConfigurationStorage.class);
            }
        }
    }

    public ConfigurationStorage getConfigurationStorage() {
        return configurationStorage;
    }
}
