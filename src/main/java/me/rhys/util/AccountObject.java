package me.rhys.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter @AllArgsConstructor
public final class AccountObject {
    private final String username;
    private final UUID uuid;
}
