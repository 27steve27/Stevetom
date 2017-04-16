package core;

import org.tbot.methods.Widgets;
import org.tbot.wrappers.Widget;
import org.tbot.wrappers.WidgetChild;
import org.tbot.wrappers.def.ItemDef;

/**
 * Project: Topbot
 * User:    Arno/Articron
 * Date:    26-5-2015
 */
public class TradeOffer {

    private TradePartner tp;
    private TradeScreen ts;

    /**
     * constructor, typically accessed by using Trading#getOffer(TradePartner);
     * @param ts Which TradeScreen (automatically grabbed if accessed by Trading#getOffer(TradePartner))
     * @param tp The partner scope (LOCAL_PLAYER / OTHER_PLAYER)
     */
    public TradeOffer(TradeScreen ts,TradePartner tp) {
        this.tp = tp;
        this.ts = ts;
    }


    /**
     * Gets the amount currently on offer
     * @param itemId itemID to look for
     * @return The quantity of specified item that is being offered
     */
   public int getCount(final int itemId) {
      WidgetChild c = determineFocus();
       int amount = 0;
       if (c == null || !c.isOnScreen()) {
           return 0;
       }
       switch (ts) {

           case FIRST:
               for(int i = 0; i < 27; i++) {
                   if (c.getChild(i).getItemID() == itemId && c.getChild(i).isVisible()) {
                       amount = amount + c.getChild(i).getItemStackSize();
                   }
               }
               return amount;

           case SECOND:
               int listLength = c.getChildren().length;
               boolean stack = ItemDef.get(itemId).isStackable();
               amount = ((stack) ? scanStacked(itemId, listLength, c) : scanNonStack(itemId, listLength, c));
               return amount;

           default:
               return 0;
       }
   }

    /**
     * Checks if Trade on offer contains item with specified item ID
     * @param itemId the item ID to check for
     * @return If specified item is currently being offered
     */
    public boolean contains(final int itemId) {
       return getCount(itemId) >= 1;
    }

    /**
     * Determines focus on offers depending on TradeScreen and TradePartner objects
     * @return The widgetChild that requires attention
     */
    private WidgetChild determineFocus() {
        Widget parent = Widgets.getWidget(ts.getWidgetID());
        if (parent == null) {
            return null;
        }
        else if (ts == TradeScreen.FIRST && tp == TradePartner.LOCAL_PLAYER) {
            return parent.getChild(24);
        }
        else if (ts == TradeScreen.FIRST && tp == TradePartner.OTHER_PLAYER) {
            return parent.getChild(27);
        }
        else if (ts == TradeScreen.SECOND && tp == TradePartner.LOCAL_PLAYER) {
            return parent.getChild(34);
        }
        else if (ts == TradeScreen.SECOND && tp == TradePartner.OTHER_PLAYER) {
            return parent.getChild(35);
        }
        else {
            return null;
        }
    }

    /**
     * Gets count of trade screen with non-stacked items
     * @param id The itemID
     * @param length the amount of all items offered
     * @param c our determined focus
     * @return The amount of unstacked items currently on offer with the specified item ID
     */
    private int scanNonStack(final int id, final int length, final WidgetChild c) {
        int amount = 0;
        String name = ItemDef.get(id).getName();
        for (int i = 0; i < length; i++) {
            if (c.getChild(i).getText().equalsIgnoreCase(name)) {
                amount++;
            }
        }
        return amount;
    }

    /**
     * Gets count of trade screen with stacked-items
     * @param id the itemID
     * @param length the amount of all items offered
     * @param c our determined focus
     * @return The amount of stacked items currently on offer with the specified item ID
     */
    private int scanStacked(final int id, final int length, final WidgetChild c) {
        int amount = 0;
        String name = ItemDef.get(id).getName();
        for (int i = 0; i < length; i++) {
            amount = amount + parseComponentAmount(name, c.getChild(i));
        }
        return amount;
    }

    /**
     * Grabs the amount of specified item in the second trading screen
     * @param name the item name
     * @param c determined focus
     * @return The amount of specified item offered
     */
    private int parseComponentAmount(final String name, final WidgetChild c) {
        return Integer.parseInt(c.getText().replace(name.concat("<col=ffffff> x <col=ffff00>"),"").replace(",",""));
    }

}