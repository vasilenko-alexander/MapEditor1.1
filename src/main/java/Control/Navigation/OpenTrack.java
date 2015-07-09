package Control.Navigation;

import GUI.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Алекс on 02.07.2015.
 */
public class OpenTrack implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e) {
        MainWindow.openTrack();
    }
}
