package Control.Video;

import GUI.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Алекс on 02.07.2015.
 */
public class OpenVideo implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        MainWindow.openVideo();
    }
}
