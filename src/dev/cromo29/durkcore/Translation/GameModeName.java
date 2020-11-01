package dev.cromo29.durkcore.Translation;

import org.bukkit.GameMode;

public enum GameModeName {

    ADVENTURE('a', "ventura"), CREATIVE('c', "riativo"),
    SPECTATOR('e', "spectador"), SURVIVAL('s', "obrevivência");

    private char firstLetter;
    private String name;

    GameModeName(char firstLetter, String name) {
        this.firstLetter = firstLetter;
        this.name = name;
    }

    public String getName() { return firstLetter + name; }
    public String getConvenientName() { return firstLetter + "".toUpperCase() + name; }
    public char getFirstLetter() { return firstLetter; }

    public static GameModeName valueOf(GameMode gamemode) {
        return valueOf(gamemode.name());
    }

    @Override
    public String toString() {
        return this.name;
    }

}
