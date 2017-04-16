package core;

import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Bank;
import org.tbot.methods.GameObjects;
import org.tbot.methods.Players;
import org.tbot.methods.Random;
import org.tbot.methods.Time;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.methods.web.banks.WebBanks;
import org.tbot.util.Condition;
import org.tbot.wrappers.Area;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.Tile;

@Manifest(version = 1, name = "Ess Runnerer", description = "attempt 1", category = ScriptCategory.OTHER, openSource = false, authors = "Steve")
public class Main extends AbstractScript{

	public boolean onStart(){
		LogHandler.log("Let's run some ess");
			return true;
	}
	
	public boolean onStop(){
		LogHandler.log("Script stopped, thanks enjoy gains");
		return false;
	}
	
	public Tile goingin = new Tile(3051,3448, 0);
	public Area inside = new Area(2517, 4843, 2523, 4848);
	public Tile outside = new Tile(3050, 3442, 0);
	public Area altar = new Area(3049, 3444, 3054, 3449);
	public Path frombank = Walking.findPath(goingin);

	
	
	private enum State {
		TRADING, BANKING, WAIT;
	}
	
	
	private State getState(){
		
		if(Inventory.getCount("Pure essence") != 27){
			return State.BANKING;
		}
		
		if(Players.getLoaded("Range Mage") != null){
			return State.TRADING;
		}
		
		return State.WAIT;
	}
	
	
	@Override
	public int loop() {
		switch (getState()) {
		
		
		case TRADING:
			LogHandler.log("STATE = TRADING");
			GameObject portal = GameObjects.getNearest(14846);
			
			if(Players.getLoaded("Range Mage") != null)
			Players.getLoaded("Range Mage").interact("Trade with");
			
			Time.sleepUntil(new Condition(){
				public boolean check(){
					return Trading.isOpen();
				}
			});
			if(Trading.isOpen()){
			Trading.offerAll(7936);
			LogHandler.log("Offered all ess");
			Time.sleep(800, 1500);
			
			LogHandler.log("Trying to click accept");
			Trading.acceptTrade(TradeScreen.getCurrentlyOpen());
			LogHandler.log("Clicking Accept");
			
			
			Time.sleep(700, 950);
			
			Trading.acceptTrade(TradeScreen.getCurrentlyOpen());
			
			Time.sleep(4700, 4950);
			
			portal.interact("Use");
			
			break;
			
			}
			
		case BANKING:
			LogHandler.log("STATE = BANKING");
			
			if(Inventory.getCount("Pure essence") != 27){
		
			Bank.openBank(WebBanks.EDGEVILLE_BANK);
			
			Time.sleepUntil(new Condition(){
				public boolean check(){
					return Bank.isOpen();
				}}, Random.nextInt(600,1250));
			
			if(Bank.isOpen()){
				Bank.withdraw("Pure essence", 27);
			}
			
			
				
		else {
			
			if(!altar.contains(Players.getLocal())){
			frombank.traverse();
			
			Time.sleepUntil(new Condition(){
				public boolean check(){
					return altar.contains(Players.getLocal());
				}
			}, Random.nextInt(500,750));
			}
		
			else{
				GameObject bodyaltar = GameObjects.getNearest(14408);
				
			if(inside.contains(Players.getLocal()))
				Inventory.useItemOn(1446, bodyaltar);
			
				Time.sleepUntil(new Condition(){
					public boolean check(){
						return inside.contains(Players.getLocal());
					}
				}, Random.nextInt(600, 1700));
			}
			
			
			
			
			
			if(inside.contains(Players.getLocal())){
			
			break;
		}
		}	
			}	
			
			
		
		case WAIT:
			Time.sleep(850, 1650);
			break;
			
		}
		
		
		return 650;	
	}
}

