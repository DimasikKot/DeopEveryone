package com.koolda.deopEveryone;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
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
        event.setCancelled(true);
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode() == GameMode.CREATIVE || event.getNewGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            event.getPlayer().setGameMode(GameMode.SURVIVAL);

            getLogger().warning(
                    "Blocked: " + event.getPlayer().getName()
            );
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
                if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                    player.setGameMode(GameMode.SURVIVAL);
                    getLogger().warning(
                            "Force-removed CREATIVE or SPECTATOR: " + player.getName()
                    );
                }
            }), 0L, 20L * 10); // every 10 seconds
    }
}
