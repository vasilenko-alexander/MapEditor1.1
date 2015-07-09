package GUI;

import Control.Map.OpenMap;
import Control.Navigation.OpenTrack;
import Control.Video.*;
import Data.DrawThread;
import Data.Track;
import Data.Video;
import Data.VideoThread;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.Point;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.*;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.NoToolAction;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.geotools.swing.control.JMapStatusBar;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static Data.Time.formatTime;
import static Data.Time.getSeconds;

/**
 * Created by Алекс on 18.06.2015.
 */
public class MainWindow extends JFrame {

    private JMenuBar menuBar;
    private JMenuItem trackOpen;
    private JMapPane mapPane;
    private static MapContent mapContent;
    private JToolBar toolBar;
    private JPanel mapPanel;
    private EmbeddedMediaPlayerComponent mediaComponent;
    private static EmbeddedMediaPlayer mediaPlayer;
    private Canvas videoScreen;
    private Dimension screenSize;
    private Dimension mainCompSize;

    private JPanel videoPanel;
    private JPanel videoBottomPanel;
    private JPanel videoPositionPanel;
    private JPanel videoControlPanel;

    private static JSlider positionSlider;
    private static JLabel currentTime;
    private static JLabel allTime;

    private static JButton playPauseButton;
    private JButton stopButton;
    private static JRadioButton synchButton;

    private static Icon playIcon;
    private static Icon pauseIcon;
    private Icon stopIcon;

    private static JFileChooser fileChooser;

    private final String TOOLBAR_ZOOMIN_BUTTON_NAME = "ToolbarZoomInButton";
    private final String TOOLBAR_ZOOMOUT_BUTTON_NAME = "ToolbarZoomOutButton";
    private final String TOOLBAR_POINTER_BUTTON_NAME = "ToolbarPointerButton";
    private final String TOOLBAR_PAN_BUTTON_NAME = "ToolbarPanButton";

    private enum Tool {

        POINTER,

        PAN,

        ZOOM
    }

    private Set<Tool> toolSet;
    private static JRadioButton mapOpened;
    private static JRadioButton trackOpened;

    private static Track track;
    private static Video currentVideo;
    private static Boolean isClick;

    private static Runnable videoRun;
    private static Runnable drawPoint;
    private static Thread videoThread;
    private static Thread drawPointThread;

    private static int prevValue;

    public MainWindow(String title){
        super(title);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        screenSize = toolkit.getScreenSize();
        setSize((int) screenSize.getWidth(), (int)(screenSize.getHeight() - 40));
        mainCompSize = new Dimension((int) (screenSize.getWidth() - 20) / 2, (int)(screenSize.getHeight() - 100));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new MigLayout("insets 0", "[grow][grow]", "[grow]"));

        initData();

        trackOpened.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                synchButton.setEnabled(true);
            }
        });

        mapOpened.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enableToolBar(toolBar, true);
                trackOpen.setEnabled(true);
            }
        });

        initMenu();
        initMapEditor();
        initVideoPlayer();

        addAllCompenent();

        setVisible(true);
    }

    private void initData() {
        menuBar = new JMenuBar();
        mapOpened = new JRadioButton();
        mapOpened.setSelected(false);

        trackOpened = new JRadioButton();
        trackOpened.setSelected(false);

        fileChooser = new JFileChooser();
        track = new Track();
        currentVideo = new Video();
        isClick = false;

        videoRun = new VideoThread();
        videoThread = new Thread(videoRun);

        drawPoint = new DrawThread();
        drawPointThread = new Thread(drawPoint);
    }

    private void addAllCompenent() {
        setJMenuBar(menuBar);
        add(mapPanel, "grow");
        add(videoPanel, "grow");
    }

    private void initVideoPlayer() {
        initVideoScreen();
        initVideoBottomPanel();

        videoPanel.add(videoBottomPanel, "grow");
        videoPanel.setMaximumSize(mainCompSize);
        videoPanel.setMinimumSize(mainCompSize);
    }

    private void initVideoBottomPanel() {
        videoBottomPanel = new JPanel();

        initPositionPanel();
        initControlPanel();

        videoBottomPanel.setLayout(new MigLayout("wrap 1, insets 0", "[grow, center]", "[][]"));

        videoBottomPanel.add(videoPositionPanel, "grow");
        videoBottomPanel.add(videoControlPanel);
    }

    private void initControlPanel() {
        videoControlPanel = new JPanel();
        playPauseButton = new JButton();
        stopButton = new JButton();
        synchButton = new JRadioButton("Синхронизировать");
        synchButton.setEnabled(false);
        synchButton.addChangeListener(new SynchControl());

        playIcon = newIcon("play");
        stopIcon = newIcon("stop");
        pauseIcon = newIcon("pause");

        playPauseButton.setIcon(playIcon);
        stopButton.setIcon(stopIcon);

        playPauseButton.addActionListener(new PlayVideo());
        stopButton.addActionListener(new StopVideo());

        videoControlPanel.setLayout(new MigLayout("insets 0", "[][][]", "[center]"));

        videoControlPanel.add(playPauseButton);
        videoControlPanel.add(stopButton);
        videoControlPanel.add(synchButton);
    }

    private Icon newIcon(String name) {
        return new ImageIcon("C:\\java\\Map Editor\\icon\\buttons\\" + name + ".png");
    }

    private void initPositionPanel() {
        videoPositionPanel = new JPanel();
        positionSlider = new JSlider();
        currentTime = new JLabel("-:--:--");
        allTime = new JLabel("-:--:--");

        positionSlider.setMinimum(0);
        positionSlider.setMaximum(1000);
        positionSlider.setValue(0);
        //positionSlider.addMouseListener(new PositionMouseControl());
        positionSlider.addChangeListener(new PositionChangeControl());

        videoPositionPanel.setLayout(new MigLayout("fill, insets 0", "[][grow][]", "[]"));
        videoPositionPanel.add(currentTime, "shrink");
        videoPositionPanel.add(positionSlider, "grow");
        videoPositionPanel.add(allTime, "shrink");
    }

    private void initVideoScreen() {
        mediaComponent = new EmbeddedMediaPlayerComponent();
        videoScreen = mediaComponent.getVideoSurface();
        videoScreen.setBackground(Color.BLACK);
        mediaPlayer = mediaComponent.getMediaPlayer();
        mediaPlayer.addMediaPlayerEventListener(new MediaControl());

        videoPanel = new JPanel();
        videoPanel.setLayout(new MigLayout("wrap 1, insets 0", "[grow]", "[grow][]"));
        videoPanel.add(videoScreen, "grow");
    }

    private void initMapEditor() {
        initMapPane();
        initMapToolBar();
        initMapPanel();
    }

    private void initMapPane() {
        mapContent = new MapContent();
        mapPane = new JMapPane(mapContent);
        mapPane.setBackground(Color.WHITE);
        mapPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        mapPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                mapPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }

            @Override
            public void focusLost(FocusEvent e) {
                mapPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
        });

        mapPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mapPane.requestFocusInWindow();
            }
        });
    }

    private void initMapPanel() {
        mapPanel = new JPanel(new MigLayout("wrap 1, insets 0", "[grow]", "[][grow][]"));
        mapPanel.add(toolBar, "grow");
        enableToolBar(toolBar, false);
        mapPanel.add(mapPane, "grow");
        mapPanel.add(JMapStatusBar.createDefaultStatusBar(mapPane), "grow");
        mapPanel.setMinimumSize(mainCompSize);
        mapPanel.setMaximumSize(mainCompSize);
    }

    private void initMapToolBar() {
        toolSet = EnumSet.allOf(Tool.class);

        toolBar = new JToolBar();
        toolBar.setOrientation(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);

        JButton toolButton;

        if (toolSet.contains(Tool.POINTER)) {
            toolButton = new JButton(new NoToolAction(mapPane));
            toolButton.setName(TOOLBAR_POINTER_BUTTON_NAME);
            toolBar.add(toolButton);
            toolBar.addSeparator();
        }

        if (toolSet.contains(Tool.ZOOM)) {
            toolButton = new JButton(new ZoomInAction(mapPane));
            toolButton.setName(TOOLBAR_ZOOMIN_BUTTON_NAME);
            toolBar.add(toolButton);

            toolButton = new JButton(new ZoomOutAction(mapPane));
            toolButton.setName(TOOLBAR_ZOOMOUT_BUTTON_NAME);
            toolBar.add(toolButton);

            toolBar.addSeparator();
        }

        if (toolSet.contains(Tool.PAN)) {
            toolButton = new JButton(new PanAction(mapPane));
            toolButton.setName(TOOLBAR_PAN_BUTTON_NAME);
            toolBar.add(toolButton);
        }
    }

    private void initMenu() {

        JMenu mapMenu = new JMenu("Карта");
        JMenu naviMenu = new JMenu("Навигация");
        JMenu videoMenu = new JMenu("Видео");
        JMenuItem mapOpen = new JMenuItem("Открыть карту");

        trackOpen = new JMenuItem("Открыть маршрут");
        trackOpen.setEnabled(false);

        JMenuItem videoOpen = new JMenuItem("Открыть видео");
        JMenuItem exitItem = new JMenuItem("Выход");

        videoOpen.addActionListener(new OpenVideo());
        mapOpen.addActionListener(new OpenMap());
        trackOpen.addActionListener(new OpenTrack());
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });

        mapMenu.add(mapOpen);
        mapMenu.add(new JSeparator());
        mapMenu.add(exitItem);

        naviMenu.add(trackOpen);

        videoMenu.add(videoOpen);

        menuBar.add(mapMenu);
        menuBar.add(naviMenu);
        menuBar.add(videoMenu);
    }

    public static void openMap() {
        FileDataStoreFactorySpi format = new ShapefileDataStoreFactory();
        File mapFile = JFileDataStoreChooser.showOpenFile(format, new File("C:\\WorkData\\Карты"), null);

        if (mapFile == null) {
            return;
        }

        try {
            FileDataStore fileDataStore = FileDataStoreFinder.getDataStore(mapFile);
            SimpleFeatureSource featureSource = fileDataStore.getFeatureSource();
            Style style = SLD.createSimpleStyle(featureSource.getSchema());
            Layer mapLayer = new FeatureLayer(featureSource, style);
            mapContent.addLayer(mapLayer);
            mapOpened.setSelected(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void openTrack() {
        fileChooser.setCurrentDirectory(new File("C:\\WorkData\\Маршруты"));
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            track.loadTrack(fileChooser.getCurrentDirectory() + "\\" + fileChooser.getSelectedFile().getName());
        }
        drawTrack(track);
        trackOpened.setSelected(true);
    }

    public static void openVideo() {
        fileChooser.setCurrentDirectory(new File("C:\\WorkData\\Видео"));
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            currentVideo.setVideoPath(fileChooser.getCurrentDirectory() + "\\" + fileChooser.getSelectedFile().getName());
            videoThread.start();
            try {
                Thread.sleep(1000);
                currentTime.setText(formatTime(mediaPlayer.getTime()));
                allTime.setText(formatTime(mediaPlayer.getLength()));
                prevValue = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void drawTrack(Track track) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        java.util.List<Pair<Double, Double>> coordinatesList = track.getAllCoordinates();

        Coordinate[] trackCoordinates = new Coordinate[coordinatesList.size()];
        Coordinate coordinateBegin = null;
        Coordinate coordinateEnd = null;

        int count = 0;

        for(Pair<Double, Double> coord : coordinatesList) {
            trackCoordinates[count] = new Coordinate(coord.getKey(), coord.getValue());
            if (count == 0)
                coordinateBegin = new Coordinate(coord.getKey(), coord.getValue());
            if(count == coordinatesList.size() - 1)
                coordinateEnd = new Coordinate(coord.getKey(), coord.getValue());
            count++;
        }

        LineString line = geometryFactory.createLineString(trackCoordinates);

        com.vividsolutions.jts.geom.Point beginPoint = geometryFactory.createPoint(coordinateBegin);
        com.vividsolutions.jts.geom.Point endPoint = geometryFactory.createPoint(coordinateEnd);

        SimpleFeatureType lineType = null;
        SimpleFeatureType pointType = null;
        try {
            lineType = DataUtilities.createType("LINE", "geom:LineString,name:String");
            pointType = DataUtilities.createType("POINT", "geom:Point,name:String");
        } catch (SchemaException e1) {
            e1.printStackTrace();
        }

        SimpleFeatureBuilder lineBuilder = new SimpleFeatureBuilder(lineType);
        SimpleFeatureBuilder pointBuilder = new SimpleFeatureBuilder(pointType);

        lineBuilder.add(line);
        SimpleFeature lineFeature = lineBuilder.buildFeature(null);

        pointBuilder.add(beginPoint);
        SimpleFeature pointBFeature = pointBuilder.buildFeature(null);

        pointBuilder.add(endPoint);
        SimpleFeature pointEFeature = pointBuilder.buildFeature(null);

        final DefaultFeatureCollection collectionLine = new DefaultFeatureCollection();
        final DefaultFeatureCollection collectionBPoint = new DefaultFeatureCollection();
        final DefaultFeatureCollection collectionEPoint = new DefaultFeatureCollection();

        collectionLine.add(lineFeature);
        collectionBPoint.add(pointBFeature);
        collectionEPoint.add(pointEFeature);

        Style pointBStyle = SLD.createPointStyle("Circle", Color.BLACK, Color.BLUE, 1.0f, 10.0f);
        Style pointEStyle = SLD.createPointStyle("Circle", Color.BLACK, Color.RED, 1.0f, 10.0f);
        Style lineStyle = SLD.createLineStyle(Color.RED, 2.0f);
        final Layer layerLine = new FeatureLayer(collectionLine, lineStyle);
        final Layer layerBPoint = new FeatureLayer(collectionBPoint, pointBStyle);
        final Layer layerEPoint = new FeatureLayer(collectionEPoint, pointEStyle);
        mapContent.addLayer(layerLine);
        mapContent.addLayer(layerBPoint);
        mapContent.addLayer(layerEPoint);
    }

    public static void startDrawPoint() {
        int value = getSeconds(mediaPlayer.getTime());
            if (value != prevValue) {
                drawCurrentPoint(getTrack().getAllCoordinates().get(value));
                prevValue = value;
            }
    }

    private static void drawCurrentPoint(Pair<Double, Double> coord) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        Coordinate currentCoordinate = new Coordinate(coord.getKey(), coord.getValue());

        Point currentPoint = geometryFactory.createPoint(currentCoordinate);

        SimpleFeatureType pointType = null;
        try {
            pointType = DataUtilities.createType("POINT", "geom:Point,name:String");
        } catch (SchemaException e1) {
            e1.printStackTrace();
        }

        SimpleFeatureBuilder pointBuilder = new SimpleFeatureBuilder(pointType);

        pointBuilder.add(currentPoint);
        SimpleFeature pointFeature = pointBuilder.buildFeature(null);

        final DefaultFeatureCollection collectionPoint = new DefaultFeatureCollection();
        collectionPoint.add(pointFeature);

        Style pointStyle = SLD.createPointStyle("Circle", Color.GREEN, Color.GREEN, 1.0f, 10.0f);
        final Layer layerPoint = new FeatureLayer(collectionPoint, pointStyle);
        mapContent.addLayer(layerPoint);
    }

    private void enableToolBar(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
        }
    }

    public static Track getTrack() {
        return track;
    }

    public static Video getCurrentVideo() {
        return currentVideo;
    }

    public static EmbeddedMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setClicked(Boolean clicked) {
        isClick = clicked;
    }

    public static Boolean isClicked() {
        return isClick;
    }

    public static JLabel getCurrentTime() {
        return currentTime;
    }

    public static JButton getPlayPauseButton() {
        return playPauseButton;
    }

    public static Icon getPlayIcon() {
        return playIcon;
    }

    public static Icon getPauseIcon() {
        return pauseIcon;
    }

    public static JSlider getPositionSlider() {
        return positionSlider;
    }

    public static JRadioButton getSynch() {
        return synchButton;
    }

    public static Thread getDrawPointThread() {
        return drawPointThread;
    }

    public static void setPrevValue(int value) {
        prevValue = value;
    }
}
