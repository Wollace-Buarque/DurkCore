package dev.cromo29.durkcore.Score;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import dev.cromo29.durkcore.Util.TXT;

public class AssembleTitleThread extends Thread {

    private Assemble assemble;

    AssembleTitleThread(Assemble assemble) {
        this.assemble = assemble;
        start();
    }

    @Override
    public void run() {
        while(true) {

            //Tick
            try {
                tick();
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }

            //Thread Sleep
            try {
                sleep(assemble.titleUpdateTick * 50);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void tick() {
        for (Player player : assemble.plugin.getServer().getOnlinePlayers()) {
            AssembleBoard board = assemble.boards.get(player.getUniqueId());

            // This shouldn't happen, but just in case
            if (board == null) continue;


            Scoreboard scoreboard = board.scoreboard;
            Objective objective = board.objective;

            // Just make a variable so we don't have to
            // process the same thing twice
            String title = TXT.parse(assemble.adapter.getTitle(player));
            if (title == null) title = "null";

            // Update the title if needed
            if (!objective.getDisplayName().equals(title)) {
                if (title.length() > 32) title = title.substring(0, 32);

                objective.setDisplayName(title);
            }

            if (player.getScoreboard() != scoreboard) player.setScoreboard(scoreboard);
        }
    }

}
