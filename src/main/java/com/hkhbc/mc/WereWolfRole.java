package com.hkhbc.mc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WereWolfRole extends WereWolfManager {
	
	public enum Priority {
		FIRST,
		SECOND,
		THIRD,
		FOURTH,
		EVERYONE
	}
	
	public enum Team {
		WOLF,
		HUMAN,
		TANNER
	}
	
	Priority priority;
	Team team;
	boolean instantAbility;
	boolean abilityOnNPCs;
	int actionPerRound;
	int abilityLimit;
	int noActionPenalty;
	ChatColor color;
	String name;
	String roleMessage;
	String deathMessage;
	List<ItemStack> abilityItems = new ArrayList<ItemStack>();

	public abstract boolean killed(WereWolfPlayer victim, WereWolfPlayer killer);
	
	public abstract void ability(WereWolfPlayer target, WereWolfPlayer source);
}
