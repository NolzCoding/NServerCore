package io.github.nolzcoding.nservercore;

import io.github.nolzcoding.nservercore.Commands.CreateCommand;
import io.github.nolzcoding.nservercore.Commands.StartCommand;
import io.github.nolzcoding.nservercore.Data.FileManager;
import io.github.nolzcoding.nservercore.Utils.DataBase;
import io.github.nolzcoding.nservercore.Utils.ServerUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;

public final class NServerCore extends Plugin {
    private static NServerCore nServerCore;


    private final HashMap<String, Process> processes = new HashMap<>();
    private ServerUtils serverUtils;
    private FileManager fileManager;
    private DataBase dataBase;

    public static NServerCore getnServerCore() {
        return nServerCore;
    }

    @Override
    public void onEnable() {
        nServerCore = this;
        getProxy().getPluginManager().registerCommand(this, new CreateCommand(
                "create",
                "nolzserver.create",
                "createserver"
        ));

        getProxy().getPluginManager().registerCommand(this, new StartCommand(
                "start",
                "nolzserver.start",
                "startserver"
        ));

        try {
            fileManager = new FileManager(this);
        } catch (Exception e) {
            fileManager = null;
            e.printStackTrace();
            onDisable();
        }

        dataBase = new DataBase(this, fileManager);


        serverUtils = new ServerUtils(dataBase, fileManager);


    }

    @Override
    public void onDisable() {
        endProcesses();
    }

    private void endProcesses() {
        for (Process process : processes.values()) {
            process.destroy();
        }
    }

    public ServerUtils getServerUtils() {
        return serverUtils;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public HashMap<String, Process> getProcesses() {
        return processes;
    }

    public void addProcess(Process process, String name) {
        processes.put(name, process);
    }

}
