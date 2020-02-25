package com.hkhbc.mc;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WereWolfQueue extends WereWolfManager {
	public int minPlayers;
	public int countdown;
	
	private boolean handled;
	private int countdownTime;
	private BukkitRunnable countdownTimer;
	
	private List<Player> players = new ArrayList<Player>();
	
	public WereWolfQueue() {
		reset();
	}
	
	public boolean join(Player player) {
		if(players.contains(player)) {
			return false;
		} else {
			boolean success = players.add(player);
			for(Player p : plugin.getServer().getOnlinePlayers()) {
				p.sendMessage(getUtils().formatColor("&b&l" + player.getName() + " &fjoined the WereWolf game."));
			}
			player.teleport(getLocation().getSpawnLocation());
			player.setGameMode(GameMode.ADVENTURE);
			player.setGlowing(false);
			player.setBedSpawnLocation(getLocation().getSpawnLocation(), true);
			queueCheck();
			return success;
		}
	}
	
	private void queueCheck() {
		if(players.size() >= minPlayers && !handled) {
			countdown();
			handled = true;
		}
	}
	
	private void countdown() {
		countdownTime = countdown;
		
		countdownTimer = new BukkitRunnable() {
			public void run() {
				if(countdownTime > 0) {
					if(countdownTime < 10) {
						getUtils().broadcastSound(players, Sound.BLOCK_NOTE_BLOCK_SNARE, 10.0F, 1.0F);
						getUtils().broadcastTitle(players, "&a&l" + countdownTime, null, 0, 20, 0);
					}
					getUtils().broadcast(players, "The werewolf game will start in &b&l" + countdownTime + "&f seconds");
					countdownTime--;
				} else if(countdownTime == 0) {
					Collections.shuffle(players);
					getWereWolf().start(players);
					handled = true;
					this.cancel();
				}
			}
		};
		
		countdownTimer.runTaskTimer(plugin, 20, 20);
	}
	
	public void reset() {
		handled = false;
		countdown = 10;
		minPlayers = 1;
		players.clear();
		
		if(countdownTimer != null) {
			countdownTimer.cancel();
			countdownTimer = null;
		}
	}
}
