package ca.claytonrogers.Client.GUIObjects;

import ca.claytonrogers.Common.IntVector;

import java.awt.*;

/**
 * Created by clayton on 2015-07-05.
 */
public abstract class GUIObject {

    public enum Type {
        Hand,
        DrawPile,
        DiscardPile,
        Other
    }

    IntVector location;
    private IntVector size;
    private Type type;
    private boolean isVisible;
    private boolean isClickable;


    public GUIObject (IntVector location, IntVector size, Type type) {
        this.location = new IntVector(location);
        this.size = new IntVector(size);
        this.type = type;
        isVisible = true;
        isClickable = false;
    }

    public void setVisibility (boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setClickability (boolean isClickable) {
        this.isClickable = isClickable;
    }

    public abstract void draw (Graphics g);

    public boolean checkClicked (IntVector clickLocation) {
        if (clickLocation.x > location.x && clickLocation.x < location.x + size.x) {
            // Within x bounds
            if (clickLocation.y > location.y && clickLocation.y < location.y + size.y) {
                // Withing y bounds as well
                return true;
            }
        }

        return false;
    }

    public Type getType() {
        return type;
    }
}
