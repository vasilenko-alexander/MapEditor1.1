package Data;

import GUI.MainWindow;

/**
 * Created by Алекс on 07.07.2015.
 */
public class VideoThread implements Runnable {
    @Override
    public void run() {
        MainWindow.getMediaPlayer().playMedia(MainWindow.getCurrentVideo().getVideoPath());
    }
}
