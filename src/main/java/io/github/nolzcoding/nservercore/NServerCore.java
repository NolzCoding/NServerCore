package io.github.nolzcoding.nservercore;

import io.github.nolzcoding.nservercore.Commands.StartCommand;
import io.github.nolzcoding.nservercore.Data.FileManager;
import io.github.nolzcoding.nservercore.Utils.DataBase;
import io.github.nolzcoding.nservercore.Utils.ServerUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class NServerCore extends Plugin {

    private ServerUtils serverUtils;
    private FileManager fileManager;
    private DataBase dataBase;

    @Override
    public void onEnable() {

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new StartCommand(
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


        serverUtils = new ServerUtils(dataBase);


    }

    @Override
    public void onDisable() {

    }
}
