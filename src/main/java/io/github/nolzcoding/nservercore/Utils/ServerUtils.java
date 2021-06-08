package io.github.nolzcoding.nservercore.Utils;

import io.github.nolzcoding.nservercore.Data.FileManager;
import io.github.nolzcoding.nservercore.JSON.ServerMeta;
import io.github.nolzcoding.nservercore.NServerCore;
import net.md_5.bungee.Util;
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
            } catch (IOException e) {
                e.printStackTrace();
            }

            Process process = null;

            try {
                process = Runtime.getRuntime().exec("java -Xms2G -Xmx2G -jar paper.jar --nogui", null,  folder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Process finalProcess = process;
            nServerCore.getProxy().getScheduler().runAsync(nServerCore, new Runnable() {
                @Override
                public void run() {
                    try {
                        finalProcess.waitFor();
                        Path copiedEula = new File(folder, "eula.txt").toPath();
                        Files.copy(fileManager.getEula().toPath(), copiedEula, StandardCopyOption.REPLACE_EXISTING);



                        File propfile = new File(folder, "server.properties");

                        int port = findOpenPort();

                        FileInputStream fileInputStream = new FileInputStream(propfile);
                        Properties properties = new Properties();
                        properties.load(fileInputStream);
                        fileInputStream.close();

                        FileOutputStream fileOutputStream = new FileOutputStream(propfile);
                        properties.setProperty("server-port", Integer.toString(port));
                        properties.store(fileOutputStream, null);
                        fileOutputStream.close();

                        Process newProcess = Runtime.getRuntime().exec("java -Xms2G -Xmx2G -jar paper.jar --nogui", null,  folder);

                        nServerCore.getProxy().constructServerInfo(name, Util.getAddr("localhost:" + port), "idk", false);


                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }

                }
            });

//java -Xms2G -Xmx2G -jar paper.jar --nogui

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
