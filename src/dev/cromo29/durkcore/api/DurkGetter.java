package dev.cromo29.durkcore.api;

import java.util.List;

public class DurkGetter {

    private String command, description, permission;
    private List<String> aliases;

    // Permission and aliases can return null.

    public DurkGetter(String command, String description, String permission, List<String> aliases) {
        this.command = command;
        this.description = description == null ? "Nenhuma." : description;
        this.permission = permission;
        this.aliases = aliases;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
