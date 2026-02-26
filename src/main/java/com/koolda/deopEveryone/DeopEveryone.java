package com.koolda.deopEveryone;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeopEveryone extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        startOpCleaner();
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().toLowerCase();

        if (cmd.startsWith("/op")) {
            event.setCancelled(true);
            getLogger().warning("Blocked /op from chat: " + cmd);
        }

        if (cmd.startsWith("/gamemode")) {
            event.setCancelled(true);
            getLogger().warning("Blocked /gamemode from chat: " + cmd);
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        String cmd = event.getCommand().toLowerCase();

        if (cmd.startsWith("op ")) {
            event.setCancelled(true);
            getLogger().warning("Blocked /op from console: " + cmd);
        }

        if (cmd.startsWith("/op ")) {
            event.setCancelled(true);
            getLogger().warning("Blocked //op from console: " + cmd);
        }

        if (cmd.startsWith("gamemode ")) {
            event.setCancelled(true);
            getLogger().warning("Blocked /gamemode from console: " + cmd);
        }

        if (cmd.startsWith("/gamemode ")) {
            event.setCancelled(true);
            getLogger().warning("Blocked //gamemode from console: " + cmd);
        }
    }

    private void startOpCleaner() {
        Bukkit.getScheduler().runTaskTimer(this, () ->
            Bukkit.getOnlinePlayers().forEach(player -> {

                // Remove OP
                if (player.isOp()) {
                    player.setOp(false);
                    getLogger().warning(
                            "Force-removed OP: " + player.getName()
                    );
                }

                // Remove CREATIVE
                if (player.getGameMode() == GameMode.CREATIVE) {
                    player.setGameMode(GameMode.SURVIVAL);
                    getLogger().warning(
                            "Force-removed CREATIVE: " + player.getName()
                    );
                }
            }), 0L, 20L * 10); // every 10 seconds
    }
}
