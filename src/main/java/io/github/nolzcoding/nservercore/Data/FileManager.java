package io.github.nolzcoding.nservercore.Data;

import io.github.nolzcoding.nservercore.NServerCore;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class FileManager {
    private final NServerCore nServerCore;
    private final File paperJar;
    private final File serverFolder;
    private final File eula;
    private Configuration config;


    public FileManager(NServerCore nServerCore) throws Exception {
        this.nServerCore = nServerCore;
        File dataFolder = nServerCore.getDataFolder();

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File jarDir = new File(dataFolder.getPath() + "/jar");

        if (!jarDir.exists()) {
            jarDir.mkdir();
        }

        File jar = new File(jarDir.getPath(), "paper.jar");

        if (!jar.exists()) {
            throw new FileNotFoundException("Did not find " + jar.getAbsolutePath());
        }
        paperJar = jar;

        File server = new File(dataFolder + "/servers");

        if (!server.exists()) {
            server.mkdir();
        }

        File eula = new File(dataFolder, "eula.txt");

        if (!eula.exists()) {
            throw new FileNotFoundException("Did not find " + eula.getAbsolutePath());
        }

        this.eula = eula;

        serverFolder = server;

        setupConfig();

    }

    private void setupConfig() {
        if (!nServerCore.getDataFolder().exists()) nServerCore.getDataFolder().mkdir();
        File file = new File(nServerCore.getDataFolder(), "config.yml");
        try {
            if (!file.exists())
                Files.copy(nServerCore.getResourceAsStream("config.yml"), file.toPath());

            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getPaperJar() {
        return paperJar;
    }

    public File getServerFolder() {
        return serverFolder;
    }

    public Configuration getConfig() {
        return config;
    }

    public File getEula() {
        return eula;
    }
}
