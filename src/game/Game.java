package game;

import java.util.ArrayList;
import java.util.Scanner;

import model.Empire;
import model.Planet;

public class Game {
	public static ArrayList<Empire>empires = new ArrayList<Empire>();
	
	private Scanner sc;
	
	public Game() {
		this.sc = new Scanner(System.in);
	}
	
	public void displayMainMenu() {
		System.out.println("\t1/ Créer un empire");
		System.out.println("\t2/ Créer une planète");
		System.out.println("\t3/ Voir le système");
		System.out.println("\t4/ Passer un tour");
	}

	public void mainMenuLoop() {
		displayMainMenu();
		
		String userInput = sc.nextLine();
		while (!userInput.equals("9")) {
			switch (userInput) {
			case "1":
				createEmpire();
				break;
			case "2":
				createPlanet();
				break;
			case "3":
				displaySystem();
				break;
			case "4":
				step();
				break;
			}
			
			displayMainMenu();
			userInput = sc.nextLine();
		}
	}
	
	public void createEmpire() {
		Empire em = new Empire();
		
		System.out.println("\n\tEntrez le nom du joueur :");
		String player = sc.nextLine();
		
		em.setPlayerName(player);
		
		System.out.println("\n\tEntrez le nom de l'empire :");
		String empireName = sc.nextLine();
		
		em.setName(empireName);
		
		empires.add(em);
		System.out.println("\n\nL'empire " + empireName + " du joueur " + player + " a été crée !\n\n");
	}
	
	public void createPlanet() {
		if (empires.isEmpty()) {
			System.out.println("\nIl n'y a pas d'empire, impossible de créer une planète.\n");
			return;
		}
		
		Planet planet = new Planet();
		
		System.out.println("A quel empire appartient cette planète ?\n");
		String message = "\t";
		for (Empire empire : empires) {
			message += (empires.indexOf(empire) + 1) + "/ " + empire.getName() + " (" + empire.getPlayerName() + ")\t";
		}
		
		String input;
		
		input = expectBoundedInteger(message, 1, empires.size());
		Empire empire = empires.get(Integer.parseInt(input) - 1);
		
		System.out.println("\nEntrez le nom de cette planète :");
		String planetName = sc.nextLine();
		
		planet.setName(planetName);
		
		System.out.println("\nQuel est le type de cette planète ?");		
		input = expectBoundedInteger("\t1/ Forteresse\t2/ Forge\t3/ Agri-monde\t4/ Classique", 1, 4);
		
		switch (input) {
		case "1":
			planet.setType("Forteresse");
			planet.setMilitiaBonus(1);
			break;
		case "2":
			planet.setType("Forge");
			planet.setMineralBonus(1);
			break;
		case "3":
			planet.setType("Agri-monde");
			planet.setCropBonus(1);
			break;
		case "4":
			planet.setType("Classique");
			break;
		}
		
		planet.setUnrest(0);
		
		input = expectBoundedInteger("Entrez la taille de la planète (2 - 20) :", 2, 20);
		planet.setSize(Integer.parseInt(input));
		
		input = expectBoundedInteger("Entrez la population de la planète :", 0, 100000);
		planet.setPopulation(Integer.parseInt(input));
		
		input = expectBoundedInteger("Entrez l'indice de fertilité de la planète (1 - 5) :", 1, 5);
		planet.setFertility(Integer.parseInt(input));
		
		input = expectBoundedInteger("Entrez le niveau de ferme de la planète (1 - 5) :", 1, 5);
		planet.setFarmLevel(Integer.parseInt(input));
		
		input = expectBoundedInteger("Entrez la richesse minière de la planète (1 - 5) :", 1, 5);
		planet.setMineralWealth(Integer.parseInt(input));
		
		input = expectBoundedInteger("Entrez le niveau industriel de la planète (1 - 5) :", 1, 5);
		planet.setIndustryLevel(Integer.parseInt(input));
		
		input = expectBoundedInteger("Entrez le niveau de caserne de la planète (1 - 5) :", 1, 5);
		planet.setBarrackLevel(Integer.parseInt(input));
		
		empire.getPlanets().add(planet);
	}
	
	public void displaySystem() {
		for (Empire empire : empires) {
			System.out.println(empire);
		}
	}
	
	public void step() {
		System.out.println("\nVoulez-vous déclarer un alea ?");
		String input = expectBoundedInteger("\t1/ Oui\t2/ Non\n", 0, 2);

		if (input.equals("1")) {
			declareEvent();
		}
		
		System.out.println("Le tour passe");
		
		for (Empire empire : empires) {
			empire.step();
		}
		
		displaySystem();
	}
	
	public void declareEvent() {
		System.out.println("\nQui sera impacté ?");
		String message = "\t0/ Tous\t\t";
		
		int count = 1;
		for (Empire empire : empires) {
			String entry = "\n(" + empire.getName() + "|" + empire.getPlayerName() +")\n";
			for (Planet planet : empire.getPlanets()) {
				message += count + "/ " + planet.getName() + "\t";
				count++;
			}
			message = entry + message + "\n";
		}
		String targetInput = expectBoundedInteger(message, 0, count);
		
		String statInput = "";
		while (!statInput.equals("0")) {
			System.out.println("\nChoisir la statistique à modifier : ");
			message = "\t0/ Fin\t\t";
			message += "1/ Population\t";
			message += "2/ Indice de révolte\t";
			message += "3/ Indice de fertilité\t";
			message += "4/ Niveau de ferme\t\t\n\t";
			message += "5/ Niveau d'industrie\t";
			message += "6/ Richesse minérale\t";
			message += "7/ Niveau de caserne\t";
			message += "8/ Nourriture bonus\t\n";
			statInput = expectBoundedInteger(message, 0, 8);
			
			String operandInput = "";
			String diffInput = "0";
			if (!statInput.equals("0")) {
				System.out.println("\nSouhaitez-vous augmenter ou diminuer la valeur ?");
				message = "\t1/ Augmenter \t2 Diminuer";
				operandInput = expectBoundedInteger(message, 1, 2);
				diffInput = expectBoundedInteger("\nDe combien ?", 0, 100000);
			}
			
			evaluateUserInputs(targetInput, statInput, operandInput, diffInput, getPlanetFromIndex(Integer.parseInt(targetInput)));
		}
	}
	
	public String expectBoundedInteger(String message, int lowerBound, int higherBound) {
		System.out.println(message);
		String input = sc.nextLine();
		while (!isInteger(input) || (Integer.parseInt(input) < lowerBound || Integer.parseInt(input) > higherBound)) {
			input = sc.nextLine();
		}
		
		return input;
	}
	
	public void evaluateUserInputs(String target, String stat, String operand, String diff, Planet currentPlanet) {
		if (target.equals("0")) {
			for (Empire empire : empires) {
				for (Planet planet : empire.getPlanets()) {
					switch (stat) {
					case "1":
						int newPop = evaluateAddition(planet.getPopulation(), operand, diff);
						planet.setPopulation(newPop);
						break;
					case "2":
						int newUnrest = evaluateAddition(planet.getUnrest(), operand, diff);
						planet.setUnrest(newUnrest);
						break;
					case "3":
						int newFertility = evaluateAddition(planet.getFertility(), operand, diff);
						planet.setFertility(newFertility);
						break;
					case "4":
						int newFarm = evaluateAddition(planet.getFarmLevel(), operand, diff);
						planet.setFarmLevel(newFarm);
						break;
					case "5":
						int newIndustry = evaluateAddition(planet.getIndustryLevel(), operand, diff);
						planet.setIndustryLevel(newIndustry);
						break;
					case "6":
						int newMineral = evaluateAddition(planet.getMineralWealth(), operand, diff);
						planet.setMineralWealth(newMineral);
						break;
					case "7":
						int newBarrack = evaluateAddition(planet.getBarrackLevel(), operand, diff);
						planet.setBarrackLevel(newBarrack);
						break;
					case "8":
						int newFoodBonus = evaluateAddition(planet.getExtraFood(), operand, diff);
						planet.setExtraFood(newFoodBonus);
						break;
					default:
						break;
					}
				}
			}
		} else {
			Planet planet = currentPlanet;
			switch (stat) {
			case "1":
				int newPop = evaluateAddition(planet.getPopulation(), operand, diff);
				planet.setPopulation(newPop);
				break;
			case "2":
				int newUnrest = evaluateAddition(planet.getUnrest(), operand, diff);
				planet.setUnrest(newUnrest);
				break;
			case "3":
				int newFertility = evaluateAddition(planet.getFertility(), operand, diff);
				planet.setFertility(newFertility);
				break;
			case "4":
				int newFarm = evaluateAddition(planet.getFarmLevel(), operand, diff);
				planet.setFarmLevel(newFarm);
				break;
			case "5":
				int newIndustry = evaluateAddition(planet.getIndustryLevel(), operand, diff);
				planet.setIndustryLevel(newIndustry);
				break;
			case "6":
				int newMineral = evaluateAddition(planet.getMineralWealth(), operand, diff);
				planet.setMineralWealth(newMineral);
				break;
			case "7":
				int newBarrack = evaluateAddition(planet.getBarrackLevel(), operand, diff);
				planet.setBarrackLevel(newBarrack);
				break;
			case "8":
				int newFoodBonus = evaluateAddition(planet.getExtraFood(), operand, diff);
				planet.setExtraFood(newFoodBonus);
				break;
			default:
				break;
			}
		}
	}
	
	public int evaluateAddition(int stat, String operand, String diff) {
		if (operand.equals("1")) {
			return stat + Integer.parseInt(diff);
		} else if (operand.equals("2")) {
			return stat - Integer.parseInt(diff);
		} else {
			return stat;
		}
	}
	
	public Planet getPlanetFromIndex(int index) {
		int count = 0;
		Planet needle = null;
		
		for (Empire empire : empires) {
			for (Planet planet : empire.getPlanets()) {
				count++;
				needle = planet;
				
				if (count == index) {
					return needle;
				}
			}
		}
		
		return needle;
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
}
