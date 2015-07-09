package Control.Video;

import GUI.MainWindow;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static Data.Time.getSeconds;

/**
 * Created by Алекс on 07.07.2015.
 */
public class SynchControl implements ChangeListener {
    @Override
    public void stateChanged(ChangeEvent e) {
        if (MainWindow.getMediaPlayer().isPlaying() && MainWindow.getSynch().isSelected())
            MainWindow.getDrawPointThread().start();
    }
}
