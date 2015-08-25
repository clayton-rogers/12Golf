package ca.claytonrogers.Common;

import java.awt.*;
import java.util.Random;

/**
 * A collection of constants used by the program. Can be used to easily change the appearance of
 * the program and change other things.
 * <p>
 * Created by clayton on 2015-07-05.
 */
public class Constants {

    // Global configuration settings
    public static final int VERSION = 11;
    public static final int PORT_NUMBER = 60093;
    public static final String ADDRESS = "claytonrogers53.ca";
    public static final int NUMBER_OF_CARDS = 12;
    public static final int NUMBER_OF_ROUNDS = 10;
    public static final boolean NET_DEBUG = false;
    public static Random random;

    // GUI constants
    public static final IntVector NULL_SIZE    = new IntVector(0,0);
    public static final IntVector FIELD_OFFSET = new IntVector(20,50);

    // GUI Color constants
    public static final Color BACKGROUND_COLOR = Color.WHITE;
    public static final Color CARD_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    public static final Color CARD_FOREGROUND_COLOR = Color.BLACK;
    public static final Color BUTTON_BACKGROUND_COLOR = Color.WHITE;
    public static final Color BUTTON_FOREGROUND_COLOR = Color.BLACK;
}
