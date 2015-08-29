package ca.claytonrogers.Golf12.Client.GUIObjects;

import ca.claytonrogers.Golf12.Common.IntVector;

import java.awt.*;

/**
 * This is the generic GUI object which must be extended to use with the generic scene.
 * <p>
 * It handles disabling click when isClickable is false or when the item is not visible. It handles
 * only drawing the item when it is visible. The default on creation is visible but not clickable.
 * <p>
 * Created by clayton on 2015-07-05.
 */
public abstract class GUIObject {

    public enum Type {
        Hand,
        DrawPile,
        DiscardPile,
        ScoreScreenButton,
        NextRoundButton,
        None
    }

    protected IntVector location;
    protected IntVector size;
    private final Type type;
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

    public final void draw (Graphics g) {
        if (isVisible) {
            internalDraw(g);
        }
    }
    protected abstract void internalDraw (Graphics g);

    public boolean checkClicked (IntVector clickLocation) {
        if (!isClickable || !isVisible) {
            return false;
        }
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
