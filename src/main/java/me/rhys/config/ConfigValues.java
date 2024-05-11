package me.rhys.config;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConfigValues {
    private boolean usePreLoginEvent, useJoinEvent, cancelJoinMessage;
    private String kickMessage;
}
