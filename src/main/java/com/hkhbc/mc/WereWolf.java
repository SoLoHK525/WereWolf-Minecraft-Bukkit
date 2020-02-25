package com.hkhbc.mc;

import java.io.IOException;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.hkhbc.mc.WereWolfRole.Team;

public class WereWolf {
	public final Main plugin;
	
	public enum State {
		WAITING,
		STARTED,
		DISABLED
	}
	
	public State state;

	private WereWolfUtils utils;
	private WereWolfQueue queue;
	private WereWolfRound round;
	private WereWolfHouses houses;
	private WereWolfRoles roles;
	private WereWolfLocation location;
	private Webhook webhook;
	
	public WereWolf(Main instance) {
		plugin = instance;
		state = State.WAITING;

		utils = new WereWolfUtils();
		queue = new WereWolfQueue();
		round = new WereWolfRound();
		houses = new WereWolfHouses();
		roles = new WereWolfRoles();
		location = new WereWolfLocation();
		webhook = new Webhook();
	}
	
	public void start(List<Player> players) {
		state = State.STARTED;
		
		utils.broadcast(players, "&b&lGame Started");
		round.start(players);
	}
	
	public void reset() {
		round.reset();
		queue.reset();
		state = State.WAITING;
	}
	
	public WereWolfUtils getUtils() {
		return this.utils;
	}
	
	public WereWolfQueue getQueue() {
		return this.queue;
	}
	
	public WereWolfRound getRound() {
		return this.round;
	}
	
	public WereWolfHouses getHouses() {
		return this.houses;
	}
	
	public WereWolfRoles getRoles() {
		return this.roles;
	}
	
	public WereWolfLocation getLocation() {
		return this.location;
	}

	public void win(Team team) {
		int alive = 0;
		boolean LoverExists = false;
		
		for(WereWolfPlayer player : round.getPlayers()) {
			if(player.alive) {
				if(player.related != null) {
					LoverExists = true;
				}
				alive++;
			}
			player.getPlayer().teleport(getLocation().getSpawnLocation());
			player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.1F, 1.0F);
			player.getPlayer().setBedSpawnLocation(getLocation().getSpawnLocation(), true);
			for(WereWolfPlayer target : round.getPlayers()) {
				player.getPlayer().showPlayer(plugin, target.getPlayer());
				target.getPlayer().setGlowing(false);
			}
		}

		String teamName = null;
		if(alive == 2 && LoverExists) {
			teamName = "&c&lLovers";
		} else {
			switch(team) {
			case WOLF:
				teamName = "&c&lWolves";
				break;
			case HUMAN:
				teamName = "&a&lHumans";
				break;
			case TANNER:
				teamName = "&e&lTanner";
				break;
			}
		}
		getUtils().broadcastW(round.getPlayers(), teamName + "&f wins the game!");
		
		getUtils().broadcastWP(round.getPlayers(), "&f&l====== &b&lMatch Stats &f&l======");
		getUtils().broadcastWP(round.getPlayers(), "&b&lPlayer   &f&lStatus    &8&lRole   &d&lResult");
		
		for(WereWolfPlayer player : round.getPlayers()) {
			boolean won;
			
			if(player.getRole().team == team) {
				won = true;
			} else {
				won = false;
			}
			
			if(team == Team.WOLF && player.related != null && player.getRole().team != player.related.getRole().team && player.related.getRole().team == team) {
				won = true;
			}
			
			if(team == Team.HUMAN && player.getRole().team == Team.TANNER) {
				won = true;
			}
			
			String aliveString = player.alive ? "&aAlive" : "&cDead";
			String relatedString = player.related == null ? "" : "&d&l<3";
			String wonString = won ? "&aWon" : "&cFailed";
			
			getUtils().broadcastWP(round.getPlayers(), String.format("&b&l%s " + aliveString + " " + player.getRole().color + player.getRole().name + " " + relatedString + " " + wonString, player.getPlayer().getName()));
		}

		getUtils().broadcastWP(round.getPlayers(), "&f&l====== &b&lMatch Stats &f&l======");
		getUtils().broadcastWP(round.getPlayers(), "Alive: &a(" + alive + "/" + round.getPlayers().size() + ")");
		
		if(round.getPlayers().size() != 0) {
			try {
			    webhook.sendData(WebhookData.toString(round, team));
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		reset();
	}

	public void end() {
		if(state != State.STARTED) return;
	
		getUtils().broadcastW(round.getPlayers(), "&c&lAdmin force ended the game!");
		for(WereWolfPlayer player : round.getPlayers()) {
			player.getPlayer().teleport(getLocation().getSpawnLocation());
			player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.1F, 1.0F);
			player.getPlayer().setBedSpawnLocation(getLocation().getSpawnLocation(), true);
			for(WereWolfPlayer target : round.getPlayers()) {
				player.getPlayer().showPlayer(plugin, target.getPlayer());
				target.getPlayer().setGlowing(false);
			}
		}
		
		try {
			webhook.sendData(WebhookData.toString(round, Team.WOLF));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		reset();
	}
}
