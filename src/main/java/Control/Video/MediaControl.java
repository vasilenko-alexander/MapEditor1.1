package Control.Video;

import GUI.MainWindow;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

import static Data.Time.formatTime;

/**
 * Created by Алекс on 07.07.2015.
 */
public class MediaControl implements MediaPlayerEventListener {
    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t libvlc_media_t, String s) {

    }

    @Override
    public void opening(MediaPlayer mediaPlayer) {
        MainWindow.getPlayPauseButton().setIcon(MainWindow.getPauseIcon());
    }

    @Override
    public void buffering(MediaPlayer mediaPlayer, float v) {

    }

    @Override
    public void playing(MediaPlayer mediaPlayer) {
        if (MainWindow.getSynch().isSelected()) {
            MainWindow.getDrawPointThread().start();
        }
        MainWindow.getPlayPauseButton().setIcon(MainWindow.getPauseIcon());
    }

    @Override
    public void paused(MediaPlayer mediaPlayer) {
        MainWindow.getPlayPauseButton().setIcon(MainWindow.getPlayIcon());
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        MainWindow.getPlayPauseButton().setIcon(MainWindow.getPlayIcon());
        MainWindow.setPrevValue(0);
    }

    @Override
    public void forward(MediaPlayer mediaPlayer) {

    }

    @Override
    public void backward(MediaPlayer mediaPlayer) {

    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {

    }

    @Override
    public void timeChanged(MediaPlayer mediaPlayer, long l) {
        MainWindow.getCurrentTime().setText(formatTime(l));
    }

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, float v) {
        MainWindow.getPositionSlider().setValue(Math.round(v * 1000));
    }

    @Override
    public void seekableChanged(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void pausableChanged(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void titleChanged(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void snapshotTaken(MediaPlayer mediaPlayer, String s) {

    }

    @Override
    public void lengthChanged(MediaPlayer mediaPlayer, long l) {

    }

    @Override
    public void videoOutput(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void scrambledChanged(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void elementaryStreamAdded(MediaPlayer mediaPlayer, int i, int i2) {

    }

    @Override
    public void elementaryStreamDeleted(MediaPlayer mediaPlayer, int i, int i2) {

    }

    @Override
    public void elementaryStreamSelected(MediaPlayer mediaPlayer, int i, int i2) {

    }

    @Override
    public void error(MediaPlayer mediaPlayer) {

    }

    @Override
    public void mediaMetaChanged(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t libvlc_media_t) {

    }

    @Override
    public void mediaDurationChanged(MediaPlayer mediaPlayer, long l) {

    }

    @Override
    public void mediaParsedChanged(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void mediaFreed(MediaPlayer mediaPlayer) {

    }

    @Override
    public void mediaStateChanged(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void mediaSubItemTreeAdded(MediaPlayer mediaPlayer, libvlc_media_t libvlc_media_t) {

    }

    @Override
    public void newMedia(MediaPlayer mediaPlayer) {

    }

    @Override
    public void subItemPlayed(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void subItemFinished(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void endOfSubItems(MediaPlayer mediaPlayer) {

    }
}
