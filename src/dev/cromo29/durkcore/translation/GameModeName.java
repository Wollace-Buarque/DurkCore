package dev.cromo29.durkcore.translation;

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

    public String getName() {
        return firstLetter + name;
    }

    public String getConvenientName() {
        return firstLetter + "".toUpperCase() + name;
    }

    public char getFirstLetter() {
        return firstLetter;
    }

    public static GameModeName valueOf(GameMode gamemode) {
        return valueOf(gamemode.name());
    }

    public static GameMode of(GameModeName gameModeName) {
        return GameMode.valueOf(gameModeName.name());
    }

    public static GameMode of(String gameModeName) {
        GameMode gameMode = getGameModeByName(gameModeName);

        if (gameMode != null) return gameMode;

        for (GameModeName gameModeName1 : values()) {

            if (gameModeName1.getName().equalsIgnoreCase(gameModeName)) {
                gameMode = getGameModeByName(gameModeName1.name());
                break;
            }

        }

        return gameMode;
    }

    private static GameMode getGameModeByName(String gameModeName) {

        for (GameMode gameMode : GameMode.values()) {

            if (gameMode.name().equalsIgnoreCase(gameModeName)) return gameMode;

        }

        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
