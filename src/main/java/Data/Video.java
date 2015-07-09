package Data;

/**
 * Created by Алекс on 14.05.2015.
 */
public class Video {
    private String videoPath;

    public Video(String path) {
        videoPath = path;
    }

    public Video() {

    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String path) {
        videoPath = path;
    }
}
