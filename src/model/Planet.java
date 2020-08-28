package model;

import java.util.Random;

public class Planet {
	private String name;
	
	private String type;
	
	private int militiaBonus = 0;
	
	private int mineralBonus = 0;
	
	private int cropBonus = 0;

	private int size;
	
	private int population;
	
	private int unrest;
	
	private int fertility;
	
	private int farmLevel;
	
	private int industryLevel;
	
	private int mineralWealth;
	
	private int barrackLevel;
	
	private int extraFood = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public int getMilitiaBonus() {
		return militiaBonus;
	}

	public void setMilitiaBonus(int militiaBonus) {
		this.militiaBonus = militiaBonus;
	}

	public int getMineralBonus() {
		return mineralBonus;
	}

	public void setMineralBonus(int mineralBonus) {
		this.mineralBonus = mineralBonus;
	}

	public int getCropBonus() {
		return cropBonus;
	}

	public void setCropBonus(int cropBonus) {
		this.cropBonus = cropBonus;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getUnrest() {
		return unrest;
	}

	public void setUnrest(int unrest) {
		this.unrest = unrest;
	}

	public int getFertility() {
		return fertility;
	}

	public void setFertility(int fertility) {
		this.fertility = fertility;
	}

	public int getFarmLevel() {
		return farmLevel;
	}

	public void setFarmLevel(int farmLevel) {
		this.farmLevel = farmLevel;
	}

	public int getIndustryLevel() {
		return industryLevel;
	}

	public void setIndustryLevel(int industryLevel) {
		this.industryLevel = industryLevel;
	}

	public int getBarrackLevel() {
		return barrackLevel;
	}

	public void setMineralWealth(int mineralWealth) {
		this.mineralWealth = mineralWealth;
	}

	public int getMineralWealth() {
		return mineralWealth;
	}

	public void setBarrackLevel(int barrackLevel) {
		this.barrackLevel = barrackLevel;
	}
	
	public int getPopulationCapacity() {
		return size * 300;
	}
	
	public boolean isOverpopulated() {
		return population > getPopulationCapacity();
	}
	
	public int getFoodProd() {
		return (int)(((1 + cropBonus * 1) * farmLevel * fertility) * (1 - unrest * 0.2) + extraFood);
	}
	
	public int getFoodConsumption() {
		return (int)(2 * (population / 300) * (isOverpopulated() ? 1.25 : 1));
	}
	
	public boolean isPopulationStarving() {
		return getFoodProd() - getFoodConsumption() < 0;
	}
	
	public boolean isProsperous() {
		return mineralWealth == 0;
	}
	
	public int getMineralProd() {
		return (int)(((3 + mineralBonus * 3) * industryLevel) * (isProsperous() ? 0.5 : 1) * (1 - unrest * 0.2));
	}
	
	public int getMilitia() {
		return (int)((0.02 + militiaBonus * 0.06) * population);
	}
	
	public int getArmy() {
		return (int)((0.01 + militiaBonus * 0.02) * population * barrackLevel);
	}
	
	public int getExtraFood() {
		return extraFood;
	}

	public void setExtraFood(int extraFood) {
		this.extraFood = extraFood;
	}

	public int preStep() {
		Random rand = new Random();
		population += isOverpopulated() ? 50 + rand.nextInt(100) : 100 + rand.nextInt(200);
		
		return (getFoodProd() > getFoodConsumption()) ? getFoodProd() - getFoodConsumption() : 0;
	}
	
	public void postStep() {
		Random rand = new Random();
		if (mineralWealth > 0 && rand.nextInt(101) <= 20) {
			mineralWealth--;
		}
	}
	
	@Override
	public String toString() {
		String ret = "";
		
		ret += name + "(" + type + ")\n";
		ret += "===================\n";
		ret += "Taille : " + size + "\n";
		ret += "Population : " + population + "/" + getPopulationCapacity() + "\n";
		ret += "Niveau d'agitation : " + unrest + "\n";
		ret += "Production agricole : " + (getFoodProd() - getFoodConsumption()) + " (+" + getFoodProd() + "/-" + getFoodConsumption() + ")\n";
		ret += "Production minière : " + getMineralProd() + "\n";
		ret += "Milice : " + getMilitia() + "\n";
		ret += "Armée : " + getArmy() + "\n";
		ret += "-------------------\n";
		
		return ret;
	}
}
