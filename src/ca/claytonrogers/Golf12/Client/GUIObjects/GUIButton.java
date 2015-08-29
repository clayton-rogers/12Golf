package ca.claytonrogers.Golf12.Client.GUIObjects;

import ca.claytonrogers.Golf12.Common.Constants;
import ca.claytonrogers.Golf12.Common.IntVector;

import java.awt.*;

/**
 * This is a simple button that can be used to do stuff. To use it you will need to create a type
 * in the {@link ca.claytonrogers.Golf12.Client.GUIObjects.GUIObject.Type} enum. Then create a button of
 * that type, then listen for hits of that type.
 * <p>
 * Created by clayton on 2015-07-18.
 */
public class GUIButton extends GUIObject {

    private static final int BUTTON_CORNER_RADIUS = 3;

    private String buttonText;

    public GUIButton(IntVector location, IntVector size, String buttonText, Type type) {
        super(location, size, type);
        this.buttonText = buttonText;
        setClickability(true); // We will generally always want buttons to be clickable
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    @Override
    protected void internalDraw(Graphics g) {
        // Draw the background.
        g.setColor(Constants.BUTTON_BACKGROUND_COLOR);
        g.fillRoundRect(
                location.x,
                location.y,
                size.x,
                size.y,
                BUTTON_CORNER_RADIUS,
                BUTTON_CORNER_RADIUS);

        // Draw the outline
        g.setColor(Constants.BUTTON_FOREGROUND_COLOR);
        g.drawRoundRect(
                location.x,
                location.y,
                size.x,
                size.y,
                BUTTON_CORNER_RADIUS,
                BUTTON_CORNER_RADIUS);

        // Draw the button text
        g.drawString(
                buttonText,
                location.x+4,
                location.y+12);
    }
}
