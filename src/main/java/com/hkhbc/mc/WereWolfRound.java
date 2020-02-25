package com.hkhbc.mc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.hkhbc.mc.WereWolfRole.Priority;
import com.hkhbc.mc.WereWolfRole.Team;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;

public class WereWolfRound extends WereWolfManager {
	private int round;
	private Stage stage;
	
	public enum Stage {
		FIRST,
		SECOND,
		THIRD,
		FOURTH,
		VOTE
	}
	
	private List<WereWolfPlayer> players;
	private List<WereWolfRole> roles;
	
	public List<Integer> actionPlayers;
	public List<Integer> actionedPlayers;
	
	public List<NPC> voteNPCs;
	
	public ItemStack votingPaper;
	public Map<Integer, Integer> vote = new HashMap<Integer, Integer>();
	
	/** Timer Handles **/
	private Map<String, BukkitRunnable> timers;
	
	public WereWolfRound() {
		players = new ArrayList<WereWolfPlayer>();
		actionPlayers = new ArrayList<Integer>();
		actionedPlayers = new ArrayList<Integer>();
		
		voteNPCs = new ArrayList<NPC>();
		
		timers = new HashMap<String, BukkitRunnable>();
		
		timers.put("allActioned", null);
		timers.put("nextRound", null);
		timers.put("noActionPlayers", null);
		timers.put("timeout", null);
		
		reset();
	}
	
	public void start(List<Player> queuePlayers) {
		int index = 0;
		
		CitizensAPI.getNPCRegistry().deregisterAll();
		
		votingPaper = getUtils().getItem(Material.PAPER, "&4&lThe Death Paper");
		
		roles = getRoles().getRandomRoles(queuePlayers.size());
		for(Player player : queuePlayers) {
			WereWolfRole role = roles.get(index);
			WereWolfHouse house = getHouses().houses().get(index);
			WereWolfPlayer p = new WereWolfPlayer(player, role, house);
			
			player.stopSound(Sound.MUSIC_CREDITS);
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0F, 1.0F);
			player.playSound(player.getLocation(), Sound.MUSIC_CREDITS, 0.1F, 1.0F);
			player.teleport(house.getSpawn());
			player.setVelocity(house.getSpawn().getDirection().multiply(2));
			player.getPlayer().setBedSpawnLocation(house.getSpawn(), true);
			
			getUtils().sendMessage(player, getUtils().formatColor(role.roleMessage));
			players.add(p);
			index++;
		}
		
		for(WereWolfPlayer player : players) {
			int i = 1;
			for(WereWolfPlayer npcs : players) {
				NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, npcs.getPlayer().getName());
				if(player == npcs) {
					npc.getTrait(WereWolfNPC.class);
					npc.spawn(player.getHouse().getNPCSpawns().get(0));
					player.setHouseNPC(npc);
				} else {
					npc.spawn(player.getHouse().getNPCSpawns().get(i++));
					player.getNPCs().add(npc);
				}
			}
		}
		
		nextRound();
	}
	
	public void nextRound() {
		round++;
		getLocation().getSpawnLocation().getWorld().setTime(18000);
		getUtils().broadcastW(players, "&4&lNight &f&l" + round);
		
		for(WereWolfPlayer player : players) {
			if(!player.alive) continue;
			player.getPlayer().teleport(player.getHouse().getSpawn());
			player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.AMBIENT_CAVE, 1.0F, 1.0F);
			player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5F, 1.0F);
		}
		
		timers.put("nextRound", new BukkitRunnable() {
			public void run() {
				stage = Stage.FIRST;
				stage();
				this.cancel();
			}
		});
		
		timers.get("nextRound").runTaskLater(plugin, 100);
	}
	
	public void stage() {
		stageClear();
		//getUtils().broadcastW(players, "Stage &b&l" + stage.name());
		
		switch(stage) {
			case FIRST:
			case SECOND:
			case THIRD:
			case FOURTH:
				for(WereWolfPlayer player : players) {
					if(!player.alive) continue;
					for(WereWolfPlayer target : players) {
						if(!target.alive) continue;
						player.getPlayer().hidePlayer(plugin, target.getPlayer());
						target.getPlayer().setGlowing(false);
					}
				}
				performAction();
				break;
			case VOTE:
				for(WereWolfPlayer player : players) {
					for(WereWolfPlayer target : players) {
						player.getPlayer().showPlayer(plugin, target.getPlayer());
						target.getPlayer().setGlowing(false);
					}
				}
				vote();
				break;
		}
	}
	
	private void performAction() {
		final Priority priority = getPriority();
		for(WereWolfPlayer player : players) {
			if(!player.alive) continue;
			if(player.getRole().priority != priority) continue;
			
			getUtils().sendTitle(player, "&e&lWake Up", "&aIt's your turn now", 10, 100, 10);
			player.getPlayer().sendMessage(getUtils().formatColor(plugin.prefix + player.getRole().roleMessage));
			
			for(ItemStack item : player.getRole().abilityItems) {
				player.getPlayer().getInventory().addItem(item);
			}
			
			actionPlayers.add(players.indexOf(player));
		}
		
		for(WereWolfPlayer player : players) {
			if(player.getRole().priority != priority) continue;
			
			if(!player.getRole().abilityOnNPCs) {
				for(WereWolfPlayer target : players) {
					player.getPlayer().showPlayer(plugin, target.getPlayer());
					target.getPlayer().setGlowing(true);
				}
			}
		}
		
		if(actionPlayers.size() == 0 && stage != Stage.VOTE) {
			timers.put("noActionPlayers", new BukkitRunnable() {
				public void run() {
					//getUtils().broadcastW(players, "To next stage because stage &b&l" + stage.name() + " &f has no actionPlayers");
					nextStage();
					this.cancel();
				}
			});
			timers.get("noActionPlayers").runTaskLater(plugin, 200);
			//timers.get("noActionPlayers").runTaskLater(plugin, 20);
		}
		
		if(actionPlayers.size() > 0) {
			timers.put("timeout", new BukkitRunnable() {
	        	@Override
	        	public void run() {
					//getUtils().broadcastW(players, "To next stage because timeout");
					
					for(int playerIndex : actionPlayers) {
						if(!actionedPlayers.contains(playerIndex)) {
							WereWolfPlayer player = players.get(playerIndex);
							player.getPlayer().teleport(player.getHouse().getSpawn());
						}
					}
					
	    			nextStage();
	    			this.cancel();
	        	}
	        });
			//timers.get("timeout").runTaskLater(plugin, 20);
			timers.get("timeout").runTaskLater(plugin, 900);
		}
	}
	
	public void abilityPlayer(Player source, Player targetPlayer) {
		if(stage == Stage.VOTE) return;
		WereWolfPlayer origin = null;
		
		for(WereWolfPlayer p : players) {
			if(p.getPlayer().equals(source)) {
				origin = p;
			}
		}
		
		if(origin.getRole().abilityOnNPCs) {
			return;
		}
		
		for(WereWolfPlayer target : players) {
			if(target.getPlayer().equals(targetPlayer)) {
				//Check if it is a NPC
				for(NPC npc : target.getNPCs()) {
					if(npc.getEntity().getEntityId() == targetPlayer.getEntityId()) {
						return;
					}
				}
				
				ability(origin, target);
			}
		}
	}
	
	public void abilityNPC(Player source, NPC npc) {
		if(stage == Stage.VOTE) return;
		WereWolfPlayer origin = null;
		
		for(WereWolfPlayer p : players) {
			if(p.getPlayer().equals(source)) {
				origin = p;
			}
		}
		
		if(!origin.getRole().abilityOnNPCs) {
			return;
		}
		
		for(WereWolfPlayer target : players) {
			if(target.getHouseNPC().getEntity().getEntityId() == npc.getEntity().getEntityId()) {
				ability(origin, target);
			}
		}
	}
	
	private void ability(WereWolfPlayer origin, WereWolfPlayer target) {
		//getUtils().broadcastW(players, origin.getPlayer().getName() + " applied ability on " + target.getPlayer().getName());

		boolean itemExists = false;
		for(ItemStack item : origin.getRole().abilityItems) {
			if(item.getType().equals(origin.getPlayer().getInventory().getItemInMainHand().getType())) {
				itemExists = true;
			}
		}
		
		if(!itemExists) {
			return;
		}

		if(origin.getAbilityTargets().contains(target)) {
			origin.getPlayer().sendMessage(plugin.prefix + getUtils().formatColor("&cYou have already chosen &b&l" + target.getPlayer().getName() + " &fplease select someone else."));
			return;
		} else {
			origin.addAbilityTargets(target);
		}

		origin.getPlayer().getInventory().getItemInMainHand().setAmount(origin.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
		origin.getPlayer().sendMessage(getUtils().formatColor(plugin.prefix + "You picked &b&l" + target.getPlayer().getName()));
		origin.performedAction();
		
		if(origin.getRole().actionPerRound == origin.performedAction) {
			if(origin.getRole().instantAbility) {
				origin.getRole().ability(target, origin);
			}
			
			if(origin.getRole().abilityLimit != -1) {
				origin.performedAbility();
			}

			actionedPlayers.add(players.indexOf(origin));
			
			if(actionedPlayers.size() == actionPlayers.size()) {
    			//getUtils().broadcastW(players, "ready to next stage");
				timers.put("allActioned", new BukkitRunnable() {
					public void run() {
	        			nextStage();
	        			//getUtils().broadcastW(players, "to next stage because actionedPlayers = actionPlayers");
	        			this.cancel();
					}
				});
				timers.get("allActioned").runTaskLater(plugin, 80);
			} else {
    			//getUtils().broadcastW(players, "actionedPlayers = " + actionedPlayers.size() + "actionPlayers = " + actionPlayers.size());
			}
		}
	}
	
	private void vote() {
		int index = 0;
		getLocation().getSpawnLocation().getWorld().setTime(1000);
		for(WereWolfPlayer player : players) {
			if(!player.alive) continue;
			
			player.getPlayer().getInventory().addItem(votingPaper);
			player.getPlayer().sendMessage(getUtils().formatColor(plugin.prefix + "&b&lVoting Time!"));
			getUtils().sendTitle(player, "&a&lVoting Time!", "", 10, 100, 10);
			actionPlayers.add(players.indexOf(player));
			
			NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, player.getPlayer().getName());
			npc.getTrait(WereWolfNPC.class);
			npc.spawn(getLocation().getVoteNPCLocation().get(index++));
			voteNPCs.add(npc);
		}
		
		if(actionPlayers.size() > 0) {
			timers.put("timeout", new BukkitRunnable() {
	        	@Override
	        	public void run() {
					//getUtils().broadcastW(players, "To next stage because timeout");
        			voteEnd();
	    			this.cancel();
	        	}
	        });
			timers.get("timeout").runTaskLater(plugin, 1800);
		}
	}
	
	public void voted(Player source, NPC target) {
		if(!voteNPCs.contains(target)) return;
		
		for(WereWolfPlayer origin : players) {
			if(origin.getPlayer().equals(source)) {
				for(WereWolfPlayer player : players) {
					if(player.equals(origin)) continue;
					if(!player.alive) continue;
					if(player.getPlayer().getName().equals(target.getName())) {
						if(!vote.containsKey(players.indexOf(player))) {
							vote.put(players.indexOf(player), 1);
						} else {
							vote.put(players.indexOf(player), vote.get(players.indexOf(player)) + 1);
						}

						origin.getPlayer().playSound(source.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 0.2F, 1.0F);
						origin.getPlayer().getInventory().remove(votingPaper);
						
						getUtils().broadcastW(players, String.format("&b&l%s &fvoted &c&l%s &fto death (&a%d&f/&a%d&f)" , origin.getPlayer().getName(), player.getPlayer().getName(), vote.get(players.indexOf(player)), actionedPlayers.size() + 1));
						actionedPlayers.add(players.indexOf(origin));
					}
				}
			}
		}
		if(actionedPlayers.size() == actionPlayers.size() && actionedPlayers.size() != 0) {
			timers.put("allActioned", new BukkitRunnable() {
				public void run() {
        			voteEnd();
        			this.cancel();
				}
			});
			timers.get("allActioned").runTaskLater(plugin, 200);
		}
	}
	
	private void voteEnd() {
		int max = -1;
		int finalKey = -1;
			
		for (Map.Entry<Integer, Integer> entry : vote.entrySet()) {
			int key = entry.getKey();
			int value = entry.getValue();
			if(value > max) {
			    max = value;
			    finalKey = key;
			}else if(value == max) {
			    finalKey = -1;
			}
		}
			
		if(finalKey != -1) {
			WereWolfPlayer execute = players.get(finalKey);
			if(execute.getRole().killed(execute, null)) {
				execute.kill(null);
				getUtils().broadcastW(players, String.format("&c&l%s &fhas been voted to death by villigars, and his role is: %s%s", execute.getPlayer().getName(), execute.getRole().color, execute.getRole().name));
			} else {
				if(execute.getRole().getClass().equals(WereWolfRoleTanner.class)) {
					getUtils().broadcastW(players, String.format("&c&l%s &fsaid: ez game, cuz i am a %s%s", execute.getPlayer().getName(), execute.getRole().color, execute.getRole().name));
					getWereWolf().win(Team.TANNER);
				}
			}
		} else {
			getUtils().broadcastW(players, "&c&lVote failed&f, no one has to die :O");
		}
		
		winCheck();
		
		for(NPC npc : voteNPCs) {
			npc.despawn();
			npc.destroy();
		}

		vote.clear();
		voteNPCs.clear();
		
		timers.put("timeout", new BukkitRunnable() {
        	@Override
        	public void run() {
        		nextStage();
    			this.cancel();
        	}
        });
		
		timers.get("timeout").runTaskLater(plugin, 100);
	}
	
	private void winCheck() {
		int TeamWolfCount = 0;
		int TeamHumanCount = 0;
		for(WereWolfPlayer player : players) {
			if(!player.alive) continue;
			
			if(player.getRole().getClass().equals(WereWolfRoleWolf.class)) {
				TeamWolfCount++;
			} else {
				TeamHumanCount++;
			}
		}
		
		if(TeamWolfCount >= TeamHumanCount) {
			plugin.getWereWolf().win(WereWolfRole.Team.WOLF);
		}else if(TeamWolfCount == 0) {
			plugin.getWereWolf().win(WereWolfRole.Team.HUMAN);
		}
	}
	
	public void nextStage() {
		switch(stage) {
			case FIRST:
				stage = Stage.SECOND;
				break;
			case SECOND:
				stage = Stage.THIRD;
				break;
			case THIRD:
				stage = Stage.FOURTH;
				break;
			case FOURTH:
				applyAbility();
				checkDeaths();
				winCheck();
				stage = Stage.VOTE;
				break;
			case VOTE:
				nextRound();
				return;
		}
		stage();
	}
	
	private void applyAbility() {
		Priority[] priorities = Priority.values();
		for(int i = 0; i < priorities.length; i++) {
			if(priorities[i] == Priority.EVERYONE) continue;
			for(WereWolfPlayer player : players) {
				if(player.getRole().instantAbility) continue;
				if(player.health == 0 || !player.alive) continue;
				
				if(player.getRole().priority == priorities[i]) {
					for(WereWolfPlayer target : player.getAbilityTargets()) {
        				getUtils().broadcastW(players, "used ability on " + target.getPlayer().getName());
						player.getRole().ability(target, player);
        				target.setLastAbilitied(player);
					}
				}
			}
		}
	}
	
	private void checkDeaths() {
		boolean deaths = false;
		for(WereWolfPlayer player : players) {
			if(player.health <= 0 && player.alive == true) {
				deaths = true;
				player.kill(player.lastAbilitiedBy);
			}
			player.clearAbilityTargets();
		}
		
		if(!deaths) {
			getUtils().broadcastW(players, "No one dies in this night :O");
		}
	}
	
	private void stageClear() {
		clearTimers();
		
		for(WereWolfPlayer player : players) {
			player.getPlayer().getInventory().clear();
		}
		
		actionPlayers.clear();
		actionedPlayers.clear();
	}
	
	public List<WereWolfPlayer> getPlayers(){
		return players;
	}
	
	private Priority getPriority() {
		switch(stage) {
			case FIRST: 
				return Priority.FIRST;
			case SECOND:
				return Priority.SECOND;
			case THIRD:
				return Priority.THIRD;
			case FOURTH:
				return Priority.FOURTH;
			case VOTE:
				return Priority.EVERYONE;
			default:
				return Priority.EVERYONE;
		}
	}
	
	public void reset() {
		round = 0;
		stage = Stage.FIRST;
		players.clear();
		CitizensAPI.getNPCRegistry().deregisterAll();
		
		/** Timer Handles Reset **/
		clearTimers();
	}
	
	public void clearTimers() {
		for(Map.Entry<String, BukkitRunnable> entry : timers.entrySet()) {
			if(entry.getValue() != null) {
				entry.getValue().cancel();
				entry.setValue(null);
			}
		}
	}
	
	public Stage getStage() {
		return this.stage;
	}
	
	public List<WereWolfRole> getRoundRoles(){
		return this.roles;
	}
}
