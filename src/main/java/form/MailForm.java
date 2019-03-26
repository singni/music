package form;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MailForm extends Application {


    public void start(Stage primaryStage) throws Exception {

        // "http://m10.music.126.net/20190323225902/0b6793d858bfd310f25332e8f2de021c/ymusic/4ba6/a619/1ebc/17e7d4fc2ff4cea0f933b3389f4de318.mp3";



        Parent root = FXMLLoader.load(getClass().getResource("/mainForm.fxml"));
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("天音播放器 | Sky Music");
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image("/icon.jpeg"));
        primaryStage.show();

    }
}
