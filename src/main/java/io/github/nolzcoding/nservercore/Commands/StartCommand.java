package io.github.nolzcoding.nservercore.Commands;

import io.github.nolzcoding.nservercore.NServerCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StartCommand extends Command {
    public StartCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (args.length == 1) {
                NServerCore.getnServerCore().getServerUtils().startServer(player, args[0]);
            }
        }
    }
}
