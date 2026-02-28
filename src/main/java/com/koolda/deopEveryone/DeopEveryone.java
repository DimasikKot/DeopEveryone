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

    private final String[] allow = {"prism wand", "auraskills", "fly", "me", "tell", "help", "op"};

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        startOpCleaner();
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase();

        // Убираем слеш в начале команды если он есть
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        // Специальная обработка для /tp
        if (command.startsWith("tp")) {
            if (!isValidTeleport(command)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cРазрешён только телепорт к игроку! (/tp <игрок>)");
            }
            return; // Разрешаем команду
        }

        // Проверяем, начинается ли команда с разрешенных строк
        boolean isAllowed = false;
        for (String allowedCommand : allow) {
            if (command.startsWith(allowedCommand)) {
                isAllowed = true;
                break;
            }
        }

        // Отменяем событие только если команда не разрешена
        if (!isAllowed) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cЭта команда запрещена!");
        }
    }

    private boolean isValidTeleport(String command) {
        // Убираем "tp" из начала
        String args = command.substring(2).trim();

        // Если после tp ничего нет - это просто "/tp", разрешаем?
        if (args.isEmpty()) {
            return true; // или false, если хотите запретить просто /tp
        }

        // Разбиваем на аргументы
        String[] parts = args.split("\\s+");

        // Проверяем количество аргументов
        if (parts.length == 1) {
            // Только один аргумент - должен быть игроком (не числом)
            return !isNumeric(parts[0]);
        }

        // Если больше одного аргумента - запрещаем
        return false;
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        String command = event.getCommand().toLowerCase();

        // Убираем слеш в начале команды если он есть
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        // Проверяем, начинается ли команда с разрешенных строк
        boolean isAllowed = false;
        for (String allowedCommand : allow) {
            if (command.startsWith(allowedCommand)) {
                isAllowed = true;
                break;
            }
        }

        // Отменяем событие только если команда не разрешена
        if (!isAllowed) {
            event.setCancelled(true);
            event.getSender().sendMessage("Эта команда запрещена!");
        }
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode() == GameMode.CREATIVE || event.getNewGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            event.getPlayer().setGameMode(GameMode.SURVIVAL);

            getLogger().warning("Blocked: " + event.getPlayer().getName());
        }
    }

    private void startOpCleaner() {
        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(player -> {

            // Remove CREATIVE
            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                player.setGameMode(GameMode.SURVIVAL);
                getLogger().warning("Force-removed CREATIVE or SPECTATOR: " + player.getName());
            }
        }), 0L, 20L * 10); // every 10 seconds
    }
}
