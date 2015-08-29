package ca.claytonrogers.Golf12.Common;

/**
 * This is a two dimensional vector with int internal representation. It is generally used to
 * position and size items on the screen in units of pixels.
 * <p>
 * Created by clayton on 2015-07-05.
 */
public class IntVector {
    public int x;
    public int y;

    public IntVector (int x, int y) {
        this.x = x;
        this.y = y;
    }
    public IntVector(IntVector otherVector) {
        this.x = otherVector.x;
        this.y = otherVector.y;
    }

    public IntVector add (IntVector otherVector) {
        return new IntVector(x + otherVector.x, y + otherVector.y);
    }

    public IntVector sub (IntVector otherVector) {
        return new IntVector(x - otherVector.x, y - otherVector.y);
    }
}
