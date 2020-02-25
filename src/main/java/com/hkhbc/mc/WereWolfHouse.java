package com.hkhbc.mc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class WereWolfHouse {
	public Location spawn;
	public List<Location>npcs = new ArrayList<Location>();
	
	public WereWolfHouse(Location s) {
		this.spawn = s;
	}
	
	public WereWolfHouse(Location s, List<Location>n) {
		this.spawn = s;
		this.npcs = n;
	}
	
	public Location getSpawn(){
		return this.spawn;
	}

	public List<Location> getNPCSpawns(){
		return this.npcs;
	}
}
