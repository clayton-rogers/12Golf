package ca.claytonrogers.Common;

import java.awt.*;

/**
 * Created by clayton on 2015-07-05.
 */
public abstract class GUIObject {

    private IntVector location;
    private IntVector size;
    private boolean isVisible;
    private boolean isClickable;

    public GUIObject (IntVector location, IntVector size) {
        this.location = new IntVector(location);
        this.size = new IntVector(size);
        isVisible = false;
        isClickable = false;
    }

    public void setVisibility (boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setClickability (boolean isClickable) {
        this.isClickable = isClickable;
    }

    public abstract void draw (Graphics2D g);

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
}
