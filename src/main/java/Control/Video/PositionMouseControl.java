package Control.Video;

import GUI.MainWindow;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static Data.Time.formatTime;

/**
 * Created by Алекс on 07.07.2015.
 */
public class PositionMouseControl implements MouseListener{
    @Override
    public void mouseClicked(MouseEvent e) {
        MainWindow.setClicked(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
