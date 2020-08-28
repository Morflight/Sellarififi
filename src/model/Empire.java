package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Empire {
	
	private String name;
	
	private String playerName;
	
	private List<Planet> planets;
	
	private int foodSurplus;
	
	public Empire() {
		planets = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	public void setPlanets(List<Planet> planets) {
		this.planets = planets;
	}
	
	public int getFoodSurplus() {
		return foodSurplus;
	}

	public void setFoodSurplus(int foodSurplus) {
		this.foodSurplus = foodSurplus;
	}

	public void step() {
		Map<Planet, Integer> starvingPlanets = new HashMap<Planet, Integer>();
		for (Planet planet : planets) {
			foodSurplus += planet.preStep();
			
			if (planet.getFoodProd() < planet.getFoodConsumption()) {
				starvingPlanets.put(planet, planet.getFoodConsumption() - planet.getFoodProd());
			}
		}
		
		for (Entry<Planet, Integer> entry : starvingPlanets.entrySet()) {
			if (foodSurplus < entry.getValue()) {
				entry.getKey().setUnrest(entry.getKey().getUnrest() + 1);
				break;
			}
			
			int carePackage = foodSurplus - entry.getValue();
			foodSurplus -= carePackage;
			
			entry.getKey().setExtraFood(carePackage);
			starvingPlanets.remove(entry.getKey());
		}
		
		if (foodSurplus > 0 && starvingPlanets.isEmpty()) {
			for (Planet planet : planets) {
				planet.setPopulation(planet.getPopulation() + 100 * foodSurplus);
			}
		}
		
		for (Planet planet : planets) {
			planet.postStep();
		}
	}
	
	@Override
	public String toString() {
		String ret = "";
		
		ret +=  "(" + playerName + ") " + name;
		ret += "\n//////////////////////\n\n";
		if (!planets.isEmpty()) {
			for (Planet planet : planets) {
				ret += planet;
			}
		}
		
		return ret;
	}
	
}
