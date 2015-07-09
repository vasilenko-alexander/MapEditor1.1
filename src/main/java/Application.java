import GUI.MainWindow;

import javax.swing.*;

/**
 * Created by Алекс on 18.06.2015.
 */
public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow("Map Editor");
            }
        });
    }
}
