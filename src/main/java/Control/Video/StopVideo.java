package Control.Video;

import GUI.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Алекс on 07.07.2015.
 */
public class StopVideo implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (MainWindow.getMediaPlayer().isPlaying()) {
            MainWindow.getMediaPlayer().stop();
        }
    }
}
