package com.hkhbc.mc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.npc.NPC;

public class WereWolfPlayer extends WereWolfManager {
	public boolean alive;
	public int health;
	
	int performedAction;
	int performedAbility;
	
	private Player player;
	private WereWolfRole role;
	private WereWolfHouse house;
	private NPC houseNPC;
	private List<NPC> npcs;
	private List<WereWolfPlayer> abilityTargets;
	
	public WereWolfPlayer related;
	public WereWolfPlayer lastAbilitiedBy;
	
	public WereWolfPlayer(Player player, WereWolfRole role, WereWolfHouse house) {
		this.alive = true;
		this.health = 1;
		this.performedAbility = 0;
		this.performedAction = 0;
		this.player = player;
		this.role = role;
		this.house = house;
		npcs = new ArrayList<NPC>();
		abilityTargets = new ArrayList<WereWolfPlayer>();
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public WereWolfRole getRole() {
		return this.role;
	}
	
	public WereWolfHouse getHouse() {
		return this.house;
	}
	
	public List<NPC> getNPCs() {
		return this.npcs;
	}
	
	public void setRole(WereWolfRole role) {
		this.role = role;
	}
	
	/*
	public void setHouse(WereWolfHouse house) {
		this.house = house;
	}
	*/
	
	public boolean addAbilityTargets(WereWolfPlayer target) {
		if(abilityTargets.contains(target)) {
			return false;
		} else {
			abilityTargets.add(target);
			return true;
		}
	}
	
	public List<WereWolfPlayer> getAbilityTargets(){
		return this.abilityTargets;
	}

	public void clearAbilityTargets() {
		abilityTargets.clear();
	}
	
	public void killed(WereWolfPlayer killer) {
		if(killer.getRole().getClass().equals(WereWolfRoleWolf.class) && this.getRole().getClass().equals(WereWolfRoleTheCursed.class)) {
			this.setRole(new WereWolfRoleWolf());
			this.getPlayer().sendMessage(getUtils().formatColor("Since you were killed by a &c&lWolf&f, you have become a &c&lWolf"));
		} else {
			this.health--;
		}
	}
	
	public void kill(WereWolfPlayer killer) {
		if(killer == null) {
			this.alive = false;
			player.teleport(getLocation().getDeathLocation());
			for(WereWolfPlayer player : getRound().getPlayers()) {
				player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_DEATH, 10.0F, 1.0F);
			}
		} else if(killer.equals(this.related)) {
			this.alive = false;
			player.teleport(getLocation().getDeathLocation());
			for(WereWolfPlayer player : getRound().getPlayers()) {
				player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_DEATH, 10.0F, 1.0F);
			}
			getUtils().broadcastW(getRound().getPlayers(), String.format("%s died since his best lover is dead, his role is %s", player.getName(), role.color + role.name));
		} else {
			this.alive = false;
			player.teleport(getLocation().getDeathLocation());
			for(WereWolfPlayer player : getRound().getPlayers()) {
				player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_DEATH, 10.0F, 1.0F);
			}
			getUtils().broadcastW(getRound().getPlayers(), String.format(killer.getRole().deathMessage, player.getName(), role.color + role.name));
		}
		if(this.related != null) {
			if(this.related.alive) {
				this.related.kill(this);
			}
		}
	}
	
	public void ability(WereWolfPlayer target) {
		
	}
	
	public void related(WereWolfPlayer target) {
		getUtils().sendMessage(player, "You have been linked with &c" + target.getPlayer().getName() + " &fby &dCupid <3 &f and his role is: " + target.getRole().color);
		related = target;
	}
	
	public void performedAction() {
		player.teleport(house.getSpawn());
		player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_HIT, 10.0F, 1.0F);
		this.performedAction++;
	}
	
	public void performedAbility() {
		this.performedAbility++;
	}
	
	public void setLastAbilitied(WereWolfPlayer player) {
		this.lastAbilitiedBy = player;
	}
	
	public NPC getHouseNPC() {
		return this.houseNPC;
	}
	
	public void setHouseNPC(NPC npc) {
		this.houseNPC = npc;
	}
}
