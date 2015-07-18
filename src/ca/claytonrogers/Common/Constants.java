package ca.claytonrogers.Common;

import java.awt.*;

/**
 * Created by clayton on 2015-07-05.
 */
public class Constants {

    public static final int VERSION = 6;
    public static final int PORT_NUMBER = 60093;
    public static final String ADDRESS = "claytonrogers53.ca";
    public static final int NUMBER_OF_CARDS = 12;
    public static final boolean DEBUG = false;
    public static final boolean NET_DEBUG = false;

    public static final IntVector CARD_SIZE = new IntVector(30,60);
    public static final int       CARD_CORNER_RADIUS = 3;
    public static final IntVector DECK_SIZE = CARD_SIZE.add(new IntVector(3,3));
    public static final IntVector DECK_OFFSET = new IntVector(-2, 2);
    public static final int       SPACE_BETWEEN_CARDS = 10;
    public static final IntVector HAND_SIZE =
            new IntVector(
                    6*CARD_SIZE.x + 5*SPACE_BETWEEN_CARDS,
                    2*CARD_SIZE.y +   SPACE_BETWEEN_CARDS);

    public static final IntVector NULL_LOCATION         = new IntVector(0,0);
    public static final IntVector NULL_SIZE             = NULL_LOCATION;
    public static final IntVector FIELD_OFFSET          = new IntVector(50,50);
    public static final IntVector WAITING_FOR_PLAYERS   = FIELD_OFFSET.add(new IntVector(100, 100));
    public static final IntVector DISCARD_PILE_LOCATION = FIELD_OFFSET.add(new IntVector(100, 180));
    public static final IntVector DRAW_PILE_LOCATION    = FIELD_OFFSET.add(new IntVector(150, 180));
    public static final IntVector STATUS_STRING_LOCATION= FIELD_OFFSET.add(new IntVector(  0, 280));
    public static final IntVector[] HAND_LOCATIONS      = new IntVector[4];

    static {
        HAND_LOCATIONS[0] = FIELD_OFFSET.add(new IntVector(  0,300));  // Bottom
        HAND_LOCATIONS[1] = FIELD_OFFSET.add(new IntVector(100,100));  // Left
        HAND_LOCATIONS[2] = FIELD_OFFSET.add(new IntVector(  0,  0));  // Top
        HAND_LOCATIONS[3] = FIELD_OFFSET.add(new IntVector(600,100));  // Right
    }

    public static final Color BACKGROUND_COLOR = Color.WHITE;
    public static final Color CARD_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    public static final Color CARD_FOREGROUND_COLOR = Color.BLACK;
}
