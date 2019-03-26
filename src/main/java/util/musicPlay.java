package util;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class musicPlay {

    private static Media media;
    private static MediaPlayer mediaPlayer;


    private musicPlay() {
    }


    private static Media setMedia(String file) {
        if (media == null) {
            synchronized (Media.class) {
                if (media == null) {
                    media = new Media(file);
                }
            }

        }
        return media;
    }

    public static MediaPlayer MediaPlayer(String file) {
        if (mediaPlayer == null) {
            synchronized (MediaPlayer.class) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer(setMedia(file));
                }
            }

        }
       return mediaPlayer;
    }

    public static void desc(){
        media=null;
        mediaPlayer=null;
    }
}
