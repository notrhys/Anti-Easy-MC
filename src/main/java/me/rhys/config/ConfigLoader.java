package me.rhys.config;

import me.rhys.Plugin;

import java.util.Objects;

public class ConfigLoader {
    public void load() {
        Plugin.getInstance().getConfig().options().copyDefaults(true);
        Plugin.getInstance().saveConfig();

        Plugin.getInstance().getConfigValues().setUsePreLoginEvent(
                Plugin.getInstance().getConfig().getBoolean("events.usePreLoginEvent")
        );

        Plugin.getInstance().getConfigValues().setUseJoinEvent(
                Plugin.getInstance().getConfig().getBoolean("events.joinEvent.enabled")
        );

        Plugin.getInstance().getConfigValues().setCancelJoinMessage(
                Plugin.getInstance().getConfig().getBoolean("events.joinEvent.cancelSpigotJoinMessage")
        );

        Plugin.getInstance().getConfigValues().setKickMessage(
                this.toMinecraftColors(Objects.requireNonNull(
                        Plugin.getInstance().getConfig().getString("messages.kickMessage")))
        );
    }

    private String toMinecraftColors(String message) {
        return message.replace("&", "§");
    }
}
