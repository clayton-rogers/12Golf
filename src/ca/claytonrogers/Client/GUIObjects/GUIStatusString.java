package ca.claytonrogers.Client.GUIObjects;

import ca.claytonrogers.Common.Constants;

import java.awt.*;

/**
 * Created by clayton on 2015-07-12.
 */
public class GUIStatusString extends GUIObject {

    String string = "";

    public GUIStatusString() {
        super(
                Constants.STATUS_STRING_LOCATION,
                Constants.NULL_SIZE,
                Type.None);
    }

    @Override
    protected void internalDraw(Graphics g) {
        g.setColor(Constants.BUTTON_FOREGROUND_COLOR);
        g.drawString(string, location.x, location.y);
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
