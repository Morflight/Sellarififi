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
		
		if (input == "1") {
			declareEvent();
		}
		
		System.out.println("Le tour passe");
		
		for (Empire empire : empires) {
			empire.step();
		}
		
		displaySystem();
	}
	
	public void declareEvent() {
		
	}
	
	public String expectBoundedInteger(String message, int lowerBound, int higherBound) {
		System.out.println(message);
		String input = sc.nextLine();
		while (!isInteger(input) || (Integer.parseInt(input) < lowerBound || Integer.parseInt(input) > higherBound)) {
			input = sc.nextLine();
		}
		
		return input;
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
