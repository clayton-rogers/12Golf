package ca.claytonrogers.Client;

import ca.claytonrogers.Common.IntVector;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by clayton on 2015-07-05.
 */
public class InputHandler extends MouseInputAdapter {

    Application application;

    public InputHandler(Application application) {
        this.application = application;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        application.addClick(new IntVector(e.getX(), e.getY()));
    }
}
