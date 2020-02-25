package com.hkhbc.mc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class WereWolfLocation extends WereWolfManager {
	private List<Location> npcsLocation;
	private Location deathLocation;
	private Location spawnLocation;
	
	private File file;
	private FileConfiguration config;
	
	public WereWolfLocation() {
		npcsLocation = new ArrayList<Location>();
		
		file = new File(plugin.getDataFolder(), "location.yml");
		config = YamlConfiguration.loadConfiguration(file);

		loadConfig();
		saveConfig();
	}
	
	public List<Location> getVoteNPCLocation(){
		return this.npcsLocation;
	}
	
	public Location getDeathLocation() {
		return this.deathLocation;
	}
	
	public Location getSpawnLocation() {
		return this.spawnLocation;
	}
	
	private void loadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection vote = config.getConfigurationSection("vote");
		ConfigurationSection death = config.getConfigurationSection("death");
		ConfigurationSection s = config.getConfigurationSection("spawn");
		
		if(vote == null){
			npcsLocation.add(new Location(
				plugin.getServer().getWorld("world"),
				0,
				0,
				0,
				0,
				0
			));
		} else {
			Set<String> spawnsKeys = vote.getKeys(false);
			
			for(String spawnKey : spawnsKeys) {
				ConfigurationSection spawn = vote.getConfigurationSection(spawnKey);
				String worldName = spawn.getString("world");
				double x = spawn.getDouble("x");
				double y = spawn.getDouble("y");
				double z = spawn.getDouble("z");
				float yaw = (float)spawn.getDouble("yaw");
				float pitch = (float)spawn.getDouble("pitch");
				
				npcsLocation.add(new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch));
			}
		}
		
		if(death == null) {
			deathLocation = new Location(
				plugin.getServer().getWorld("world"),
				0,
				0,
				0,
				0,
				0
			);
		} else {
			deathLocation = new Location(
				plugin.getServer().getWorld(death.getString("world")),
				death.getDouble("x"),
				death.getDouble("y"),
				death.getDouble("z"),
				(float)death.getDouble("yaw"),
				(float)death.getDouble("pitch")
			);
		}
		
		if(s == null) {
			spawnLocation = new Location(
				plugin.getServer().getWorld("world"),
				0,
				0,
				0,
				0,
				0
			);
		} else {
			spawnLocation = new Location(
				plugin.getServer().getWorld(s.getString("world")),
				s.getDouble("x"),
				s.getDouble("y"),
				s.getDouble("z"),
				(float)s.getDouble("yaw"),
				(float)s.getDouble("pitch")
			);
		}
	}
	
	private boolean saveConfig() {
		config.createSection("vote");
		config.createSection("death");
		config.createSection("spawn");

		ConfigurationSection vote = config.getConfigurationSection("vote");
		ConfigurationSection death = config.getConfigurationSection("death");
		ConfigurationSection s = config.getConfigurationSection("spawn");
		int index = 0;
		for(Location loc : npcsLocation) {
			ConfigurationSection spawn = vote.createSection((index++) + "");
			spawn.set("world", loc.getWorld().getName());
			spawn.set("x", loc.getX());
			spawn.set("y", loc.getY());
			spawn.set("z", loc.getZ());
			spawn.set("yaw", loc.getYaw());
			spawn.set("pitch", loc.getPitch());
		}
		
		if(deathLocation != null) {
			death.set("world", deathLocation.getWorld().getName());
			death.set("x", deathLocation.getX());
			death.set("y", deathLocation.getY());
			death.set("z", deathLocation.getZ());
			death.set("yaw", deathLocation.getYaw());
			death.set("pitch", deathLocation.getPitch());
		}
		
		if(spawnLocation != null) {
			s.set("world", spawnLocation.getWorld().getName());
			s.set("x", spawnLocation.getX());
			s.set("y", spawnLocation.getY());
			s.set("z", spawnLocation.getZ());
			s.set("yaw", spawnLocation.getYaw());
			s.set("pitch", spawnLocation.getPitch());
		}
		
		try {
			config.save(file);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addVoteNPCSpawn(Location loc) {
		boolean succ = npcsLocation.add(loc);
		saveConfig();
		return succ;
	}
	
	public void setDeath(Location loc) {
		deathLocation = loc;
		saveConfig();
	}
	
	public void setSpawnLocation(Location loc) {
		spawnLocation = loc;
		saveConfig();
	}
}
