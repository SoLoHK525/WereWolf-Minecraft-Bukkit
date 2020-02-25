package com.hkhbc.mc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WereWolfRoles extends WereWolfManager {
	private List<WereWolfRole> TeamWolf;
	private List<WereWolfRole> TeamHuman;
	
	public WereWolfRoles() {
		/*
		TeamWolf.add(new WereWolfRoleWolf());
		TeamHuman.add(new WereWolfRoleVillager());
		TeamHuman.add(new WereWolfRoleChemist());
		TeamHuman.add(new WereWolfRoleCupid());
		TeamHuman.add(new WereWolfRoleTanner());
		TeamHuman.add(new WereWolfRolePrince());
		TeamHuman.add(new WereWolfRoleForeseer());
		TeamHuman.add(new WereWolfRoleDumb());
		TeamHuman.add(new WereWolfRoleFakeWolf());
		TeamHuman.add(new WereWolfRoleWitch());
		TeamHuman.add(new WereWolfRoleTheCursed());
		TeamHuman.add(new WereWolfRoleMoreMore());
		*/
		//TeamHuman.add(new WereWolfRoleSleeper());
		TeamWolf = new ArrayList<WereWolfRole>();
		TeamHuman = new ArrayList<WereWolfRole>();
	}

	public List<WereWolfRole> getRandomRoles(int count) {
		initializeRoles();
		Collections.shuffle(TeamHuman);
		
		List<WereWolfRole> roles = new ArrayList<WereWolfRole>();
		if(count <= 10) {
			roles.add(getRandomTeamWolf());
			//Debug
			//roles.add(TeamHuman[2]);
			for(int i = 0; i < count - 1; i++) {
				roles.add(TeamHuman.get(i));
			}
		}
		
		Collections.shuffle(roles);
		TeamWolf.clear();
		TeamHuman.clear();
		return roles;
	}
	
	public void initializeRoles(){
		TeamWolf.add(new WereWolfRoleWolf());
		//TeamHuman.add(new WereWolfRoleVillager());
		/*
		TeamHuman.add(new WereWolfRoleChemist());
		TeamHuman.add(new WereWolfRoleCupid());
		TeamHuman.add(new WereWolfRoleForeseer());
		TeamHuman.add(new WereWolfRoleDumb());
		TeamHuman.add(new WereWolfRoleFakeWolf());
		TeamHuman.add(new WereWolfRoleWitch());
		*/
		TeamHuman.add(new WereWolfRoleTheCursed());
		TeamHuman.add(new WereWolfRoleMoreMore());
		TeamHuman.add(new WereWolfRoleTanner());
		//TeamHuman.add(new WereWolfRolePrince());
		//TeamHuman.add(new WereWolfRoleMoreMore());
	}
	
	public List<WereWolfRole> getAllRoles(){
		List<WereWolfRole> roles = new ArrayList<WereWolfRole>();
		roles.addAll(TeamWolf);
		roles.addAll(TeamHuman);
		return roles;
	}
	
	private WereWolfRole getRandomTeamWolf() {
		Collections.shuffle(TeamWolf);
		return TeamWolf.get(0);
	}
	
}
