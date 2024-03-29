package dev.cromo29.durkcore.score;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Assemble {

	JavaPlugin plugin;
	AssembleAdapter adapter;
	Map<UUID, AssembleBoard> boards;
	AssembleThread thread;
    AssembleTitleThread titleThread;
	AssembleListener listeners;

	public long scoreUpdateTick = 20;
    public long titleUpdateTick = 20;
	boolean hook = false;
	public boolean shouldShowForPlayerOnJoin = true;
    public AssembleStyle assembleStyle = AssembleStyle.MODERN;

    public void removeScoreFrom(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());

        boards.remove(player.getUniqueId());
    }
    public void showScoreFor(Player player) {
        if (boards.containsKey(player.getUniqueId())) return;

        boards.put(player.getUniqueId(), new AssembleBoard(player, this));
    }

	public Assemble(JavaPlugin plugin, AssembleAdapter adapter) {
		if (plugin == null) {
			throw new RuntimeException("Assemble can not be instantiated without a plugin instance!");
		}

		this.plugin = plugin;
		this.adapter = adapter;
		boards = new ConcurrentHashMap<>();

		setup();
	}

	private void setup() {
		listeners = new AssembleListener(this);
		//Register Events
		plugin.getServer().getPluginManager().registerEvents(listeners, plugin);

		//Ensure that the thread has stopped running
		if (thread != null) {
			thread.stop();
			thread = null;
		}

        if (titleThread != null) {
            titleThread.stop();
            titleThread = null;
        }

		//Start Thread
		thread = new AssembleThread(this);
        titleThread = new AssembleTitleThread(this);
	}

	public void cleanup() {

		if (thread != null) {
			thread.stop();
			thread = null;
		}

        if (titleThread != null) {
            titleThread.stop();
            titleThread = null;
        }

		if (listeners != null) {
			HandlerList.unregisterAll(listeners);
			listeners = null;
		}
	}

	public AssembleBoard getBoard(UUID uuid) {
    	return boards.get(uuid);
	}

	public Map<UUID, AssembleBoard> getBoards() {
		return boards;
	}
}
