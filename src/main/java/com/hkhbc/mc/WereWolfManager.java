package com.hkhbc.mc;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class WereWolfManager {
	public final Main plugin;
	
	public WereWolfManager() {
		plugin = JavaPlugin.getPlugin(Main.class);
	}
	
	public final WereWolfUtils getUtils() {
		if(plugin == null) {
			plugin.getLogger().log(Level.INFO, "Plugin is null");
		}
		if(JavaPlugin.getPlugin(Main.class).getWereWolf() == null) {
			JavaPlugin.getPlugin(Main.class).getLogger().log(Level.INFO, "WereWolf is null");
		}
		if(plugin.getWereWolf().getUtils() == null) {
			plugin.getLogger().log(Level.INFO, "Utils is null");
		}
		return JavaPlugin.getPlugin(Main.class).getWereWolf().getUtils();
	}
	
	public final WereWolfQueue getQueue() {
		return plugin.getWereWolf().getQueue();
	}
	
	public final WereWolf getWereWolf() {
		return plugin.getWereWolf();
	}
	
	public final WereWolfHouses getHouses() {
		return plugin.getWereWolf().getHouses();
	}
	
	public final WereWolfRoles getRoles() {
		return plugin.getWereWolf().getRoles();
	}
	
	public final WereWolfRound getRound() {
		return plugin.getWereWolf().getRound();
	}
	
	public final WereWolfLocation getLocation() {
		return plugin.getWereWolf().getLocation();
	}
}
