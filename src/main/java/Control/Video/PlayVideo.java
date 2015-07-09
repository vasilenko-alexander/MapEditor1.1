package Control.Video;

import GUI.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Алекс on 07.07.2015.
 */
public class PlayVideo implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (MainWindow.getCurrentVideo().getVideoPath() == null) {
            MainWindow.openVideo();
        } else {
            if (!MainWindow.getMediaPlayer().isPlaying()) {
                MainWindow.getMediaPlayer().play();
            } else  {
                MainWindow.getMediaPlayer().pause();
            }
        }
    }
}
