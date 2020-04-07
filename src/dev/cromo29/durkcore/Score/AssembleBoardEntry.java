package dev.cromo29.durkcore.Score;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class AssembleBoardEntry {

	AssembleBoard board;
	String text, identifier;
	Team team;

	public AssembleBoardEntry(AssembleBoard board, String text) {
		this.board = board;
		this.text = text;
		identifier = board.getUniqueIdentifier(text);

		setup();
	}

	public void setup() {
		Scoreboard scoreboard = board.scoreboard;

		if (scoreboard == null) return;

		String teamName = identifier;

		// This shouldn't happen, but just in case
		if (teamName.length() > 16)
			teamName = teamName.substring(0, 16);


		Team team = scoreboard.getTeam(teamName);

		// Register the team if it does not exist
		if (team == null)
			team = scoreboard.registerNewTeam(teamName);


		// Add the entry to the team
		if (!team.getEntries().contains(identifier))
			team.addEntry(identifier);


		// Add the entry if it does not exist
		if (!board.entries.contains(this)) {
			board.entries.add(this);
		}

		this.team = team;
	}

	public void send(int position) {
		if (text.length() > 16) {
			String prefix = text.substring(0, 16);
			String suffix;

			if (prefix.charAt(15) == ChatColor.COLOR_CHAR) {
				prefix = prefix.substring(0, 15);
				suffix = text.substring(15);
			} else if (prefix.charAt(14) == ChatColor.COLOR_CHAR) {
				prefix = prefix.substring(0, 14);
				suffix = text.substring(14);
			} else {
				if (ChatColor.getLastColors(prefix).equalsIgnoreCase(ChatColor.getLastColors(identifier)))
					suffix = text.substring(16);
				 else
					suffix = ChatColor.getLastColors(prefix) + text.substring(16);

			}

			if (suffix.length() > 16) {
				suffix = suffix.substring(0, 16);
			}

			team.setPrefix(prefix);
			team.setSuffix(suffix);
		} else {
			team.setPrefix(text);
			team.setSuffix("");
		}

		Score score = board.objective.getScore(identifier);
		score.setScore(position);
	}

	public void remove() {
		board.identifiers.remove(identifier);
		board.scoreboard.resetScores(identifier);
	}

}
