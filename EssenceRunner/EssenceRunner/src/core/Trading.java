package core;

import org.tbot.methods.Mouse;
import org.tbot.methods.Random;
import org.tbot.methods.Time;
import org.tbot.methods.Widgets;
import org.tbot.methods.input.keyboard.Keyboard;
import org.tbot.methods.tabs.Inventory;
import org.tbot.wrappers.Item;
import org.tbot.wrappers.Widget;
import org.tbot.wrappers.WidgetChild;

/**
 * Project: Topbot
 * User:    Arno/Articron
 * Date:    26-5-2015
 */
public class Trading {

    /**
     * @return true if first or second trade screen is open
     */
    public static boolean isOpen() {
        TradeScreen screen = TradeScreen.getCurrentlyOpen();
        if (screen == null) {
            return false;
        }
        WidgetChild c = Widgets.getWidget(screen.getWidgetID()).getChild(0);
        return (c != null) && c.isOnScreen();
    }

    /**
     * @param screen type of screen
     * @return true if specified trade screen is open
     */
    public static boolean isOpen(final TradeScreen screen) {
        WidgetChild c = Widgets.getWidget(screen.getWidgetID()).getChild(0);
        return (c != null) && c.isOnScreen();
    }

    /**
     * Gets current state of offers
     * @param tp partner to get offers off
     * @return TradeOffer object
     */
    public static TradeOffer getTradeOffer(final TradePartner tp) {
        return new TradeOffer(TradeScreen.getCurrentlyOpen(),tp);
    }

     /**
     * Accepts a trade request from specified player username
     * @param username username to accept request from
     * @return if we accepted correctly
     */
    public static boolean acceptTradeRequestFrom(final String username) {
        Widget parent = Widgets.getWidget(137);
        if (parent == null) {
            return false;
        }
        WidgetChild c =  parent.getChild(2).getChildWithText("<col=7f007f>" + username + " wishes to trade with you.");
        return (c != null) && Mouse.move(c.getRandomPoint()) && Mouse.click(true);
    }

    /**
     * Gets the person's username you are trading with
     * @return Partner's username as String
     */
    public static String getCurrentTradingPartnerName() {
        TradeScreen ts = TradeScreen.getCurrentlyOpen();
        if (ts == null) {
            return null;
        }
        WidgetChild c = Widgets.getWidget(ts.getWidgetID()).getChild(ts.getNameID());
        if (c == null) {
            return null;
        }
        return c.getText().replace("Trading With: ", "")
                .replace("<br>", "").replace("Trading with: ","")
                .replace("Trading with:", "").trim();
    }

    /**
     * Offers all of a specified item to the trade screen
     * @param itemID the item which has to be offered
     * @return if we offered the item
     */
    public static boolean offerAll(final int itemID) {
        Item toOffer = Inventory.getItemClosestToMouse(itemID);
        if(!isOpen(TradeScreen.FIRST) || toOffer == null) {
            return false;
        }
        if(toOffer.interact("Offer-All")) {
            Time.sleepUntil(() -> !Inventory.contains(itemID),Random.nextInt(3400,4500));
        }
        Time.sleep(600);
        return getTradeOffer(TradePartner.LOCAL_PLAYER).contains(itemID);
    }

    /**
     * Offers an Item to the trade screen
     * @param itemID the item which has to be offered
     * @param amount quantity of offered item
     * @return if we offered the item
     */
    public static boolean offerItem(final int itemID, final int amount) {
        Item toOffer = Inventory.getItemClosestToMouse(itemID);
        if(!isOpen(TradeScreen.FIRST) || toOffer == null) {
            return false;
        }
        if (toOffer.interact("Offer-X")) {
            Time.sleepUntil(() -> Widgets.getWidget(548).getChild(119).isVisible(),Random.nextInt(3400,4500));
        }
        Keyboard.sendText(Integer.toString(amount));
        Time.sleep(600);

        if (!getTradeOffer(TradePartner.LOCAL_PLAYER).contains(itemID)) {
            return false;
        }
        return getTradeOffer(TradePartner.LOCAL_PLAYER).getCount(itemID) == amount;
    }



    /**
     * Clicks accept on specified trade screen
     * @param screen screen to click accept on
     * @return if we clicked on accept correctly
     */
    public static boolean acceptTrade(final TradeScreen screen) {
        WidgetChild c = Widgets.getWidget(screen.getWidgetID()).getChild(screen.getAcceptButtonID());
        if (c == null) {
            return false;
        }
        if(Mouse.move(c.getRandomPoint())) {
            Time.sleepUntil(() -> c.getBounds().contains(Mouse.getLocation()), Random.nextInt(900, 1200));
        }
        return c.getBounds().contains(Mouse.getLocation()) && Mouse.click(true);
    }

    /**
     * Clicks deny on specified trade screen
     * @param screen screen to click deny on
     * @return if we clicked on deny correctly
     */
    public static boolean denyTrade(final TradeScreen screen) {
        WidgetChild c = Widgets.getWidget(screen.getWidgetID()).getChild(screen.getRefuseButtonID());
        if (c == null) {
            return false;
        }
        if(Mouse.move(c.getRandomPoint())) {
            Time.sleepUntil(() -> c.getBounds().contains(Mouse.getLocation()), Random.nextInt(900, 1200));
        }
        return c.getBounds().contains(Mouse.getLocation()) && Mouse.click(true);
    }

    /**
     * Returns true if specified {@Object TradePartner} has accepted the trade
     * @param tp the person you want to check with if they accepted or not
     * @return true if specified partner has accepted the current trade screen
     */
    public static boolean hasAccepted(final TradePartner tp) {
        TradeScreen ts = TradeScreen.getCurrentlyOpen();
        if (ts == null) {
            return false;
        }
        WidgetChild c = Widgets.getWidget(ts.getWidgetID()).getChild(ts.getAcceptMsgID());
        if (c == null) {
            return false;
        }
        switch(tp) {
            case LOCAL_PLAYER:
                return c.getText().contains("Waiting for other player");
            case OTHER_PLAYER:
                return c.getText().contains("Other player has accepted");
            default:
                return false;
        }
    }

}