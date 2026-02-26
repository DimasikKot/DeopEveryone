package com.koolda.deopEveryone;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeopEveryone extends JavaPlugin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp()) {
            event.getPlayer().setOp(false);
            getLogger().warning("Removed OP on join: " + event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().toLowerCase().startsWith("/op")) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        String cmd = event.getCommand().toLowerCase();

        if (cmd.startsWith("op ")) {
            event.setCancelled(true);
        }
    }
}
