package com.hkhbc.mc;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.hkhbc.mc.WereWolfRole.Team;

public class WebhookData {
	public class Data {
		int alive;
		String winTeam;
		boolean loverExists;
		List<Player> players;
		
		public Data(int a, String wt, boolean le, List<Player> p) {
			alive = a;
			winTeam = wt;
			loverExists = le;
			players = p;
		}
	}
	
	public class Player {
		String name;
		String role;
		boolean alive;
		boolean won;
		String related;
		String killedBy;
		
		public Player(String n, String role, boolean a, boolean w, String r, String k) {
			name = n;
			this.role = role;
			alive = a;
			won = w;
			related = r;
			killedBy = k;
		}
	}
	
	public static String toString(WereWolfRound round, Team team) {
		WebhookData data = new WebhookData();
		int alive = 0;
		boolean LoverExists = false;
		String teamName = null;
		List<Player> players = new ArrayList<Player>();
		
		for(WereWolfPlayer player : round.getPlayers()) {
			if(player.alive) {
				if(player.related != null) {
					LoverExists = true;
				}
				alive++;
			}
		}

		
		if(alive == 2 && LoverExists) {
			teamName = "Lovers";
		} else {
			switch(team) {
			case WOLF:
				teamName = "Wolves";
				break;
			case HUMAN:
				teamName = "Humans";
				break;
			case TANNER:
				teamName = "Tanner";
				break;
			}
		}
		
		
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

			String related = "";
			String killedBy = "";
			
			if(player.related != null) {
				related = player.related.getPlayer().getName();
			}
			
			if(!player.alive) {
				if(player.lastAbilitiedBy != null) {
					killedBy = player.lastAbilitiedBy.getPlayer().getName();
				}
			}
			
			players.add(data.new Player(player.getPlayer().getName(), player.getRole().name, player.alive, won, related, killedBy));
		}
		WebhookData.Data d = data.new Data(alive, teamName, LoverExists, players);
		Gson gson = new Gson();
		return gson.toJson(d);
	}
}
