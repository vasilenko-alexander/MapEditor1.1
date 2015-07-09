package Data;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алекс on 02.07.2015.
 */
public class Track {

    private String path;
    private List<String> allTime;
    private List<Pair<Double, Double>> allCoordinates;

    public Track() {
        allTime = new ArrayList<String>();
        allCoordinates = new ArrayList<Pair<Double, Double>>();
    }

    public void loadTrack(String path) {

        this.path = path;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.path));
            String line;
            String[] tokens;
            boolean isFirstLine = true;
            while ((line = bufferedReader.readLine()) != null) {
                if (!isFirstLine) {
                    String delims = "[ ]+";
                    tokens = line.split(delims);
                    setData(tokens);
                }
                isFirstLine = false;
            }

        } catch (FileNotFoundException e) {
            errMessage(e);
        } catch (IOException e) {
            errMessage(e);
        }

    }

    private void setData(String[] tokens) {
        CoordinateConversion conversion = new CoordinateConversion();
        String utm = "35 N " + tokens[1] + " " + tokens[0];
        double[] XY = conversion.utm2LatLon(utm);
        Pair<Double, Double> coordinate = new Pair<Double, Double>(XY[0], XY[1]);
        allCoordinates.add(coordinate);
        allTime.add(tokens[2]);
    }

    private void errMessage(Exception e) {
        JFrame err = new JFrame();
        JLabel errMessage = new JLabel();

        err.setLayout(new BorderLayout());
        err.add(errMessage, BorderLayout.CENTER);

        errMessage.setText(String.valueOf(e));

        err.setSize(err.getPreferredSize());
        err.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        err.setVisible(true);
    }

    public String getPath() {
        return path;
    }

    public List<String> getAllTime() {
        return allTime;
    }

    public List<Pair<Double, Double>> getAllCoordinates() {
        return allCoordinates;
    }
}
