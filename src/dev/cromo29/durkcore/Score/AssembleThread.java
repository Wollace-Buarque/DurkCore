package dev.cromo29.durkcore.Score;

import dev.cromo29.durkcore.Util.TXT;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.List;

public class AssembleThread extends Thread {

    private Assemble assemble;

    AssembleThread(Assemble assemble) {
        this.assemble = assemble;
        start();
    }

    @Override
    public void run() {
        while(true) {
            //Tick
            try {
                tick();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            //Thread Sleep
            try {
                sleep(assemble.scoreUpdateTick * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tick() {
        for (Player player : assemble.plugin.getServer().getOnlinePlayers()) {
            AssembleBoard board = assemble.boards.get(player.getUniqueId());

            // This shouldn't happen, but just in case
            if (board == null)
                continue;

            Scoreboard scoreboard = board.scoreboard;


            // Just make a variable so we don't have to
            // process the same thing twice
//            final String title = TXT.parse(this.assemble.adapter.getTitle(player));
//
//            // Update the title if needed
//            if (!objective.getDisplayName().equals(title)) {
//                objective.setDisplayName(title);
//            }

            List<String> newLines = assemble.adapter.getLines(player);

            // Allow adapter to return null/empty list to display nothing
            if (newLines == null || newLines.isEmpty()) {
                board.entries.forEach(AssembleBoardEntry::remove);
                board.entries.clear();
            } else {
                // Reverse the lines because scoreboard scores are in descending order
                if (!assemble.assembleStyle.decending)
                    Collections.reverse(newLines);


                // Remove excessive amount of board entries
                if (board.entries.size() > newLines.size()) {
                    for (int i = newLines.size(); i < board.entries.size(); i++) {
                        AssembleBoardEntry entry = board.getEntryAtPosition(i);

                        if (entry != null)
                            entry.remove();

                    }
                }

                // Update existing entries / add new entries
                int cache = assemble.assembleStyle.startNumber;
                for (int i = 0; i < newLines.size(); i++) {
                    AssembleBoardEntry entry = board.getEntryAtPosition(i);

                    // Translate any colors
                    String newLine = TXT.parse(newLines.get(i));
                    if (newLine != null && newLine.length() > 32) newLine = newLine.substring(0, 32);
                    String line = newLine;

                    // If the entry is null, just create a new one.
                    // Creating a new AssembleBoardEntry instance will add
                    // itself to the provided board's entries list.
                    if (entry == null)
                        entry = new AssembleBoardEntry(board, line);

                    // Update text, setup the team, and update the display values
                    entry.text = line;
                    entry.setup();
                    entry.send(assemble.assembleStyle.decending ? cache-- : cache++);
                }
            }

            if (player.getScoreboard() != scoreboard) player.setScoreboard(scoreboard);
        }
    }
}
