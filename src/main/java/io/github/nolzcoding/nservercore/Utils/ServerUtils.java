package io.github.nolzcoding.nservercore.Utils;

import io.github.nolzcoding.nservercore.Data.FileManager;
import io.github.nolzcoding.nservercore.JSON.ServerMeta;
import io.github.nolzcoding.nservercore.NServerCore;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Properties;

public class ServerUtils {
    private final DataBase dataBase;
    private final FileManager fileManager;
    private final NServerCore nServerCore = NServerCore.getnServerCore();

    public void createServer(ProxiedPlayer player, String name) {

        ArrayList<ServerMeta> data = dataBase.getServerInfo(name);

        if (data.size() == 0) {

            File folder = new File(fileManager.getServerFolder() + "/" + name);

            folder.mkdir();

            Path original = fileManager.getPaperJar().toPath();
            Path copied = new File(folder, "paper.jar").toPath();

            try {
                Files.copy(original, copied, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(fileManager.getServerProperties().toPath(), new File(folder, "server.properties").toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(fileManager.getSpigotyml().toPath() , new File(folder, "spigot.yml").toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Process process = null;
            int port = findOpenPort();
            try {
                process = Runtime.getRuntime().exec("java -Xms2G -Xmx2G -Dcom.mojang.eula.agree=true -jar paper.jar --nogui -p " + port, null,  folder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nServerCore.addProcess(process);

            ServerInfo serverInfo = nServerCore.getProxy().constructServerInfo(name, new InetSocketAddress("localhost", port), "idk", false);

            ProxyServer.getInstance().getServers().put(name, serverInfo);



        }
        else {
            //msg err
        }


    }

    public ServerUtils(DataBase dataBase, FileManager fileManager) {
        this.dataBase = dataBase;
        this.fileManager = fileManager;
    }

    private int findOpenPort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {

            return serverSocket.getLocalPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 5667;
    }



}
