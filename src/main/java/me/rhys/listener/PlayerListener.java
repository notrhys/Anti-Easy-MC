package me.rhys.listener;

import me.rhys.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerListener implements Listener {

    private final List<UUID> ignoreDisconnectMessages = new CopyOnWriteArrayList<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        if (this.ignoreDisconnectMessages.contains(event.getPlayer().getUniqueId())) {
            event.setQuitMessage(null);
            this.ignoreDisconnectMessages.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        if (Plugin.getInstance().getConfigValues().isUseJoinEvent()
                && Plugin.getInstance().isLoginBad(event.getPlayer().getUniqueId())) {

            // won't work for every server setup
            if (Plugin.getInstance().getConfigValues().isCancelJoinMessage()) {
                this.ignoreDisconnectMessages.add(event.getPlayer().getUniqueId());
                event.setJoinMessage(null);
            }

            // kick the player on the next tick
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().kickPlayer(Plugin.getInstance().getConfigValues().getKickMessage());
                }
            }.runTask(Plugin.getInstance());
        }
    }

    // this is optional if the user wants
    // since this apparently now forces the servers main thread to sync??
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreJoin(PlayerPreLoginEvent event) {
        if (Plugin.getInstance().getConfigValues().isUsePreLoginEvent()
                && Plugin.getInstance().isLoginBad(event.getUniqueId())) {

            event.setKickMessage(Plugin.getInstance().getConfigValues().getKickMessage());
            event.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }
}
