package dev.cromo29.durkcore.score;

import dev.cromo29.durkcore.score.events.AssembleBoardCreatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AssembleBoard {

	// We assign a unique identifier (random string of ChatColor values)
	// to each board entry to: bypass the 32 char limit, using
	// a team's prefix & suffix and a team entry's display name, and to
	// track the order of entries;
	List<AssembleBoardEntry> entries = new ArrayList<>();
	List<String> identifiers = new ArrayList<>();
	Scoreboard scoreboard;
	Objective objective;
	UUID uuid;

	private Assemble assemble;

	public AssembleBoard(Player player, Assemble assemble) {
		this.assemble = assemble;
		setup(player);
		uuid = player.getUniqueId();
	}

	private void setup(Player player) {

		// Register new scoreboard if needed
		if (assemble.hook || !(player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard()))
			scoreboard = player.getScoreboard();
		 else scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();


		// Setup sidebar objective
        if (scoreboard.getObjective("Default") != null)
            scoreboard.getObjective("Default").unregister();


        objective = scoreboard.registerNewObjective("Default", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		String title = assemble.adapter.getTitle(player);

		if (title.length() > 32) title = title.substring(0, 32);

		objective.setDisplayName(title);

		// Update scoreboard
		player.setScoreboard(scoreboard);

		//Send Update
		AssembleBoardCreatedEvent createdEvent = new AssembleBoardCreatedEvent(this);
		Bukkit.getPluginManager().callEvent(createdEvent);
	}

	public AssembleBoardEntry getEntryAtPosition(int pos) {

		if (pos >= entries.size()) return null;
		 else return entries.get(pos);

	}

	public String getUniqueIdentifier(String text) {
		String identifier = getRandomChatColor() + ChatColor.WHITE;

		while (identifiers.contains(identifier)) identifier = identifier + getRandomChatColor() + ChatColor.WHITE;


		// This is rare, but just in case, make the method recursive
		if (identifier.length() > 16) return getUniqueIdentifier(text);


		// Add our identifier to the list so there are no duplicates
		this.identifiers.add(identifier);

		return identifier;
	}

	// Gets a random ChatColor and returns the String value of it
	private static String getRandomChatColor() {
		return ChatColor.values()[ThreadLocalRandom.current().nextInt(ChatColor.values().length)].toString();
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}
}
