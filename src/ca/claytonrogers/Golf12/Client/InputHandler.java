package ca.claytonrogers.Golf12.Client;

import ca.claytonrogers.Golf12.Common.IntVector;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

/**
 * This is a short class to handle input into the program so that we can use the mouse adaptor.
 * <p>
 * Created by clayton on 2015-07-05.
 */
class InputHandler extends MouseInputAdapter {

    private final Application application;

    public InputHandler(Application application) {
        this.application = application;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        application.addClick(new IntVector(e.getX(), e.getY()));
    }
}
