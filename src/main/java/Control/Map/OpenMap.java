package Control.Map;

import GUI.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Алекс on 29.06.2015.
 */
public class OpenMap implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        MainWindow.openMap();
    }
}
