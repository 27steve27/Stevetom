package core;


import java.awt.Graphics2D;

import org.tbot.client.Player;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Bank;
import org.tbot.methods.Game;
import org.tbot.methods.GameObjects;
import org.tbot.methods.Npcs;
import org.tbot.methods.Players;
import org.tbot.methods.Random;
import org.tbot.methods.Time;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.methods.web.banks.WebBanks;
import org.tbot.util.Condition;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.Tile;

@Manifest(version = 1, name = "Gout Tuber Farmer", description = "attempt 1", category = ScriptCategory.OTHER, openSource = false, authors = "Steve")
public class Main extends AbstractScript {

	public boolean onStart() {
		LogHandler.log("exit!");
		return true;
	}
	
	private enum State {
		CHOP, COMBAT, DROP, WAIT, BANK, BROODOO, WORLDHOP;
	};

	
	NPC broodoo = Npcs.getNearest("Broodoo victim");
	Tile safespot = new Tile(2787, 3102, 0);
	Tile village = new Tile(2807, 3081, 0);
	Path tovillage = Walking.findPath(village);

	private State getState() {
		
		
		
		if(!Inventory.contains("Shark") || Inventory.getCount("Gout tuber") > 4){
			return State.BANK;}
		
		if(Players.getLocal().getLocation() == safespot){
			return State.WORLDHOP;
		}
		
		if(broodoo != null){
			return State.BROODOO;
		}
		
		
		if (Players.getLocal().inCombat()) {
			return State.COMBAT;
		}
		
		if (Inventory.isFull()) {
			return State.DROP;
		}

		if ((Players.getLocal().getAnimation()) ==  -1) {
			return State.CHOP;
		}
		return State.WAIT;
	}


	public void onStop() {
		LogHandler.log("exit!");
	}

	public void onPaint(Graphics2D g) {

	}

	@Override
	public int loop() {

		switch (getState()) {
		case WAIT:
			Time.sleep(500,700);
			break;
		case WORLDHOP:
			LogHandler.log("STATE = WORLDHOP");
			Time.sleep(6000, 7500);
			Game.instaHopNextP2P();
			Time.sleep(2500, 3500);

			if(Players.getLocal().getLocation() != village){
				tovillage.traverse();
				
				
				Time.sleepUntil(new Condition(){
					public boolean check(){
						return Players.getLocal().getLocation() == village;
					}
				}, Random.nextInt(900, 1100));
					}
			if(Players.getLocal().getLocation() == village){
			break;
			}
			
		case BROODOO:
			LogHandler.log("STATE = BROODOO");
			Path tosafespot = Walking.findPath(safespot);
				LogHandler.log("detected Broodoo");
				tosafespot.traverse();
				
				Time.sleepUntil(new Condition(){
					public boolean check(){
						return Players.getLocal().getLocation() == safespot;
					}
				}, Random.nextInt(900, 1100));
				
		
				
			
			
		if(Players.getLocal().getLocation() == safespot){
			break;
		}
		case DROP:
			LogHandler.log("STATE = DROP");
			LogHandler.log("dropping shit");
			Inventory.dropAll(6281);
			break;
		case COMBAT:
			
			NPC junglespider = Npcs.getNearest(6267);
			NPC tribesman = Npcs.getNearest(2888);
			NPC snake = Npcs.getNearest(6102);
			NPC mosquito1 = Npcs.getNearest(6273);
			//NPC mosquito2 = Npcs.getNearest();
			//NPC mosquito3 = Npcs.getNearest();
			LogHandler.log("detected combat");

			
			
//IF WE'RE POISONED
			
			
			//EAT IF LOW
			if(Players.getLocal().getHealthPercent() < 35) {
				Inventory.getFirst("Shark").interact("Eat");
				LogHandler.log("Ate Shark");
			}			
			

			if(Inventory.contains("Adamant scimitar")){
				Inventory.getFirst("Adamant scimitar").interact("Wield");
				
				Time.sleepUntil(new Condition(){
					public boolean check(){
						return Inventory.getCount("Adamant scimitar") == 0;
					}
				}, Random.nextInt(900, 1100));
					}
		
			
			
	/*		
		//JUNGLE SPIDER
			if (junglespider.interactsLocalPlayer() && junglespider != null)
			{
				LogHandler.log("the jungle spider is attacking me");
				junglespider.interact("Attack");
			}
		//TRIBESMAN
		if (tribesman.isInteractingWithLocalPlayer() && tribesman != null)
			{
				LogHandler.log("the tribesman is attacking me");
				tribesman.interact("Attack");
			}
		//MOSQUITO	
			 if(mosquito1.isInteractingWithLocalPlayer() && mosquito1 != null)
			{
				LogHandler.log("the mosquito is attacking me");
				mosquito1.interact("Attack");
			}
			/*if (mosquito2..getInteractingEntity() == Players.getLocal() && mosquito2 != null && !(Players.getLocal().getInteracting() == mosquito2))
			{
				LogHandler.log("the tribesman is attacking me");
				mosquito2.interact("Attack");
			}
			if (mosquito3..getInteractingEntity() == Players.getLocal() && mosquito3 != null && !(Players.getLocal().getInteracting() == mosquito3))
			{
				LogHandler.log("the tribesman is attacking me");
				mosquito3.interact("Attack");
			}
			
		//SNAKE
			if (snake.isInteractingWithLocalPlayer() && snake != null)
			{
				LogHandler.log("the snake is attacking me");
				snake.interact("attack");
			}
*/
				break;
		case CHOP:
			LogHandler.log("STATE = CHOP");
			GameObject jungle = GameObjects.getNearest("Light Jungle");
			GameObject gouttuber =  GameObjects.getNearest(9033);
			
		
			
			if(gouttuber != null){
				Inventory.useItemOn("Spade", gouttuber);
				Time.sleep(1200, 2600);
			LogHandler.log("im sensing a tuber");
			}
			else{
				
			if(Inventory.contains("Red topaz machete"))	{
				Inventory.getFirst("Red topaz machete").interact("Wield");
				
				Time.sleepUntil(new Condition(){
					public boolean check(){
						return Inventory.getCount("Red topaz machete") == 0;
					}
				}, Random.nextInt(900, 1100));
					}
				
			}
				
			if (jungle != null) {
				jungle.interact("Hack");
				
				Time.sleep(400, 798);
			}		
			
			break;
			
			
		case BANK:
			
			LogHandler.log("BANK STATE");
			
			if(Inventory.getCount("Shark") < 10 || Inventory.getCount("Coins") < 1000 || Inventory.getCount("Adamant scimitar") < 1 || Inventory.getCount("Camelot teleport") < 1){
			Bank.openBank(WebBanks.CATHERBY_BANK);
			
			LogHandler.log("Going to catherby");
			
			Time.sleep(600, 1866);
				


			Bank.depositAll();
			
			if(Inventory.getCount("Coins") < 1000){
				Bank.withdraw("Coins", 1000);
				
				Time.sleepUntil(new Condition(){
					public boolean check(){
						return Inventory.getCount("Coins") == 1000;
					}
				}, Random.nextInt(3500, 6000));
					}

			if(Inventory.getCount("Adamant scimitar") < 1){
				Bank.withdraw("Adamant scimitar", 1);
				
				Time.sleepUntil(new Condition(){
					public boolean check(){
						return Inventory.getCount("Adamant scimitar") == 1;
					}
				}, Random.nextInt(3500, 6000));
					}
			
			if(Inventory.getCount("Camelot teleport") < 1){
				Bank.withdraw("Camelot teleport", 1);
				
				Time.sleepUntil(new Condition(){
					public boolean check(){
						return Inventory.getCount("Camelot teleport") == 1;
					}
				}, Random.nextInt(3500, 6000));
					}
			
			
			if(Inventory.getCount("Shark") < 10){
				Bank.withdraw("Shark", 10);
				
				Time.sleepUntil(new Condition(){
					public boolean check(){
						return Inventory.getCount("Shark") == 10;
					}
				}, Random.nextInt(3500, 6000));
					}
		
//WITHDRAW ANTIDOTES NEEDS TO BE ADDED
			
			if(Bank.isOpen()){
				Bank.close();
				Time.sleepUntil(new Condition(){
					public boolean check(){
						return !Bank.isOpen();
					}
				}, Random.nextInt(900, 1100));
					}
			}
//START WALKING
		if(Inventory.getCount("Shark") == 10 && Inventory.getCount("Coins") == 1000 && Inventory.getCount("Adamant scimitar") == 1 || Inventory.getCount("Camelot teleport") == 1) {
			
		if(Players.getLocal().getLocation() != village){
			tovillage.traverse();
			
			
			Time.sleepUntil(new Condition(){
				public boolean check(){
					return Players.getLocal().getLocation() == village;
				}
			}, Random.nextInt(900, 1100));
				}
			
		
		if(Players.getLocal().getLocation() == village){
			LogHandler.log("IN VILLAGE");
			break;
		}
		}
			}
		
		return 600;
	}
}
			
	 
					

		
