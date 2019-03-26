package util;

import domain.Music;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import util.FileUtil;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MusicCrol implements Initializable {

    @FXML
    private TableView<Music> musicList;
    @FXML
    private TableColumn name;
    @FXML
    private TableColumn singer;
    @FXML

    private TableColumn path;

    private MediaPlayer mp;
    private final boolean repeat = false;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Duration duration;
    @FXML
    private Slider timeSlider;
    @FXML
    private Label playTime;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Button Play;
    @FXML
    private Button Previous;
    @FXML
    private Button Next;

    private String url;

    @FXML
    private ImageView imageCover;


    public void TableList() {
        ObservableList<Music> list = FXCollections.observableArrayList();

        name.setCellValueFactory(new PropertyValueFactory("name"));
        path.setCellValueFactory(new PropertyValueFactory("path"));
        singer.setCellValueFactory(new PropertyValueFactory("singer"));
        List<Music> musics = FileUtil.FileLIst(new File("G:/CloudMusic"));
        list.addAll(musics);
        musicList.setItems(list);
    }

    private boolean flag = false;

    public void ActionTable() {

        musicList.setRowFactory(tv -> {
            TableRow<Music> row = new TableRow<Music>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Music item = row.getItem();
                    player(item.getPath());
                }
            });
            return row;
        });

    }

    private void player(String file) {
        mp = musicPlay.MediaPlayer(file);
        MediaPlayer.Status status = mp.getStatus();
        routerImage();
        if (flag) {

            mp.play();
            flag = false;
            Play.setStyle("-fx-background-image:url('/stop.png') ");

        } else {
            mp.stop();
            mp.dispose();
            musicPlay.desc();
            flag = true;
            Play.setStyle("-fx-background-image:url('/play.png') ");
        }
        mp.setOnReady(new Runnable() {
            public void run() {
                duration = mp.getMedia().getDuration();
                updateValues();
            }
        });
        mp.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                updateValues();
            }
        });

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                mp.setVolume(newValue.doubleValue() / 100);
            }
        });

        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                    mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
                }
            }
        });

        Play.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (flag) {
                    mp.play();
                    flag = false;
                    Play.setStyle("-fx-background-image:url('/stop.png') ");
                } else {
                    mp.pause();
                    Play.setStyle("-fx-background-image:url('/play.png') ");

                    flag = true;
                }
            }
        });
    }

    public RotateTransition routerImage() {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(10000), imageCover);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setRate(360);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);
        rotateTransition.setAutoReverse(false);
        rotateTransition.play();
        return rotateTransition;
    }

    protected void updateValues() {
        if (playTime != null && timeSlider != null && volumeSlider != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mp.getCurrentTime();
                    playTime.setText(formatTime(currentTime, duration));
                    timeSlider.setDisable(duration.isUnknown());
                    if (!timeSlider.isDisabled()
                            && duration.greaterThan(Duration.ZERO)
                            && !timeSlider.isValueChanging()) {
                        timeSlider.setValue(currentTime.divide(duration).toMillis()
                                * 100.0);
                    }
                    if (!volumeSlider.isValueChanging()) {
                        volumeSlider.setValue((int) Math.round(mp.getVolume()
                                * 100));
                    }
                }
            });
        }
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        TableList();
        ActionTable();

    }
}
