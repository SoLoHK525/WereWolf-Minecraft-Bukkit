package com.hkhbc.mc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class WereWolfHouses extends WereWolfManager {
	private File file;
	private FileConfiguration config;
	
	private List<WereWolfHouse> houses;
	public WereWolfHouses() {
		
		file = new File(plugin.getDataFolder(), "house.yml");
		plugin.getLogger().log(Level.INFO, plugin.getDataFolder().getAbsolutePath().toString());
		config = YamlConfiguration.loadConfiguration(file);
		houses = new ArrayList<WereWolfHouse>();
		loadConfig();
		saveConfig();
	}
	
	public void loadConfig() {
		houses = new ArrayList<WereWolfHouse>();
		config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection entry = config.getConfigurationSection("houses");
		Set<String> houseKeys = entry.getKeys(false);
		for(String houseKey : houseKeys) {
			plugin.getLogger().log(Level.INFO, plugin.prefix + "loading house: " + houseKey);
			ConfigurationSection houseEntry = entry.getConfigurationSection(houseKey);
			ConfigurationSection spawn = houseEntry.getConfigurationSection("spawn");
			
			String worldName = spawn.getString("world");
			double x = spawn.getDouble("x");
			double y = spawn.getDouble("y");
			double z = spawn.getDouble("z");
			float yaw = (float)spawn.getDouble("yaw");
			float pitch = (float)spawn.getDouble("pitch");
			
			WereWolfHouse house = new WereWolfHouse(
					new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch)
			);
			
			ConfigurationSection npcs = houseEntry.getConfigurationSection("npc");
			if(npcs != null) {
				for(String npcKey : npcs.getKeys(false)) {
					plugin.getLogger().log(Level.INFO, plugin.prefix + "loading npc: " + npcKey);
					ConfigurationSection npc = npcs.getConfigurationSection(npcKey);
					String npcWorldName = npc.getString("world");
					double npcX = npc.getDouble("x");
					double npcY = npc.getDouble("y");
					double npcZ = npc.getDouble("z");
					float npcYaw = (float)npc.getDouble("yaw");
					float npcPitch = (float)npc.getDouble("pitch");
					house.npcs.add(new Location(plugin.getServer().getWorld(npcWorldName), npcX, npcY, npcZ, npcYaw, npcPitch));
				}
			}
			houses.add(house);
		}
	}
	
	public boolean saveConfig() {
		int index = 0;
		try {
			
			for(WereWolfHouse house : houses) {
				String key = "houses." + (index++) + ".";
				config.set(key + "spawn.world", house.getSpawn().getWorld().getName());
				config.set(key + "spawn.x", house.getSpawn().getX());
				config.set(key + "spawn.y", house.getSpawn().getY());
				config.set(key + "spawn.z", house.getSpawn().getZ());
				config.set(key + "spawn.yaw", house.getSpawn().getYaw());
				config.set(key + "spawn.pitch", house.getSpawn().getPitch());
				int npcIndex = 0;
				for(Location npc : house.getNPCSpawns()) {
					String npcKey = key + "npc." + (npcIndex++) + ".";
					config.set(npcKey + "world", npc.getWorld().getName());
					config.set(npcKey + "x", npc.getX());
					config.set(npcKey + "y", npc.getY());
					config.set(npcKey + "z", npc.getZ());
					config.set(npcKey + "yaw", npc.getYaw());
					config.set(npcKey + "pitch", npc.getPitch());
				}
			}
			
			config.save(file);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean add(WereWolfHouse house) {
		houses.add(house);
		return saveConfig();
	}
	
	public boolean edit(int index, WereWolfHouse house) {
		houses.set(index, house);
		return saveConfig();
	}

	public boolean remove(int index){
		try {
			houses.remove(index);
		} catch(IndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}
	
	public int size() {
		return houses.size();
	}
	
	public List<WereWolfHouse> houses(){
		return houses;
	}
}
