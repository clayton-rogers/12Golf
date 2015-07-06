package ca.claytonrogers.Common;

/**
 * Created by clayton on 2015-07-05.
 */
public class Constants {

    public static final int VERSION = 1;

    public static final IntVector CARD_SIZE = new IntVector(30,60);
    public static final IntVector DECK_SIZE = CARD_SIZE.add(new IntVector(3,3));

    public static final IntVector NULL_LOCATION         = new IntVector(0,0);
    public static final IntVector FIELD_OFFSET          = new IntVector(15,45);
    public static final IntVector DISCARD_PILE_LOCATION = FIELD_OFFSET.add(new IntVector(300, 300));
    public static final IntVector DRAW_PILE_LOCATION    = FIELD_OFFSET.add(new IntVector(350, 300));
    public static final IntVector[] HAND_LOCATIONS      = new IntVector[4];

    static {
        HAND_LOCATIONS[0] = new IntVector(400,500);  // Bottom
        HAND_LOCATIONS[1] = new IntVector(100,100);  // Left
        HAND_LOCATIONS[2] = new IntVector(400,100);  // Top
        HAND_LOCATIONS[3] = new IntVector(600,100);  // Right
    }
}
