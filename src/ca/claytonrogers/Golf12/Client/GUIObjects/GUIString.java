package ca.claytonrogers.Golf12.Client.GUIObjects;

import ca.claytonrogers.Golf12.Common.Constants;
import ca.claytonrogers.Golf12.Common.IntVector;

import java.awt.*;

/**
 * This is a GUI string which is printed on the screen in a particular position and which can be
 * updated to give the player instructions.
 * <p>
 * Created by clayton on 2015-07-12.
 */
public class GUIString extends GUIObject {

    private String string = "";

    public GUIString(IntVector location) {
        super(
                location,
                Constants.NULL_SIZE,
                "NONE");
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
