package Control.Video;

import GUI.MainWindow;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static Data.Time.formatTime;

/**
 * Created by Алекс on 07.07.2015.
 */
public class PositionChangeControl implements ChangeListener {
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        /*if (MainWindow.isClicked()) {
            changeMediaPosition(source);
            MainWindow.setClicked(false);
        }*/
        if (source.getValueIsAdjusting()) {
            changeMediaPosition(source);
        }
    }

    private void changeMediaPosition(JSlider source) {
        MainWindow.getMediaPlayer().setPosition(source.getValue() / 1000.0f);
        MainWindow.getCurrentTime().setText(formatTime(MainWindow.getMediaPlayer().getTime()));
    }
}
