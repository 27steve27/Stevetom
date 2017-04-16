package core;
/**
 * Project: Topbot
 * User:    Arno/Articron
 * Date:    26-5-2015
 */
public enum TradeScreen {


    FIRST(335,10,13,30,29),
    SECOND(334,16,17,36,29);


    private TradeScreen(int widgetID, int acceptID, int refuseID, int nameID, int acceptmsgID) {
        this.widgetID = widgetID;
        this.acceptID = acceptID;
        this.refuseID = refuseID;
        this.nameID = nameID;
        this.acceptmsgID = acceptmsgID;
    }


    private int widgetID;
    private int acceptmsgID;
    private int refuseID;
    private int nameID;
    private int acceptID;


    public int getAcceptMsgID() {
        return acceptmsgID;
    }

    public int getWidgetID() {
        return widgetID;
    }


    public int getAcceptButtonID() {
        return acceptID;
    }


    public int getRefuseButtonID() {
        return refuseID;
    }


    public int getNameID() {
        return nameID;
    }


    public static TradeScreen getCurrentlyOpen() {
        for (TradeScreen ts : TradeScreen.values()) {
            if (Trading.isOpen(ts)) {
                return ts;
            }
        }
        return null;
    }
}