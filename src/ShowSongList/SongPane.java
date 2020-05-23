package ShowSongList;

import base.Song;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import userPane.ListPane;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SongPane extends Pane {

    private  ImageView favTag,download;
    private Label num,songName,singer,duration;
    private double itemHeight = 30;
    private double width = 800;
    private Song song;
    private Font font =new Font("Microsoft YaHei",20);
    private SongListPane songListPane;
    private Image love = new Image(getClass().getResource("image/love.png").toExternalForm());
    private Image loved = new Image(getClass().getResource("image/loved.png").toExternalForm());

    boolean isLoved = false;

    SongPane(int index,Song song,SongListPane songListPane){
        this.song = song;
        this.songListPane = songListPane;
        isLoved = songListPane.isLovedSong(song);
        init(index);
        initNum(index);
        initSongName();
        initSinger();
        initDuration();
        initFavTag();
        initDownload();

        this.getChildren().addAll(num,songName,singer,duration,favTag,download);
        this.setPrefSize(width,itemHeight);

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                favTag.setVisible(true);
                download.setVisible(true);
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                favTag.setVisible(false);
                download.setVisible(false);
            }
        });
    }

    private void init(int index){
        this.setPrefSize(width,itemHeight);
        if(index%2 == 0){
            setStyle("-fx-background-color: #fafafa;");
        }
        else{
            setStyle("-fx-background-color: #ffffff;");
        }
    }

    private void initFavTag(){
        favTag = new ImageView(love);
        if(isLoved)favTag.setImage(loved);
        favTag.setFitHeight(23);
        favTag.setFitWidth(24);

        favTag.setLayoutX(5*width/8-57);
        favTag.setLayoutY(3.5);

        favTag.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setCursor(Cursor.HAND);
            }
        });

        favTag.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setCursor(Cursor.DEFAULT);
            }
        });

        favTag.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isLoved){
                    isLoved = false;
                    favTag.setImage(love);
                    songListPane.delFavSong(song);
                }else{
                    isLoved = true;
                    favTag.setImage(loved);
                    songListPane.addFavSong(song);
                }
            }
        });

        favTag.setVisible(false);

    }

    private void initDownload(){
        download = new ImageView(getClass().getResource("image/download.png").toExternalForm());
        download.setFitHeight(25);
        download.setFitWidth(25);
        download.setLayoutX(5*width/8-27.5);
        download.setLayoutY(2.5);

        download.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setCursor(Cursor.HAND);
            }
        });

        download.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setCursor(Cursor.DEFAULT);
            }
        });

        download.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    songListPane.downloadSong(song);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        download.setVisible(false);
    }

    private void initNum(int index){
        num = new Label(index+"");
        num.setFont(font);
        num.setLayoutX(width/64);
        num.setLayoutY(2.5);
        num.setTextFill(Paint.valueOf("#a4a4a4"));
    }

    private void initSongName(){
        songName = new Label(song.songName);
        songName.setFont(font);
        songName.setLayoutX(width/8);
        songName.setLayoutY(2.5);
        songName.setStyle("-fx-text-fill: #000000;");
        songName.setPrefWidth(3*width/8);

        songName.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                songName.setStyle("-fx-text-fill: #08ad53;");
                setCursor(Cursor.HAND);
                songListPane.song = song;
            }
        });

        songName.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                songName.setStyle("-fx-text-fill: #000000;");
                setCursor(Cursor.DEFAULT);
            }
        });
    }

    private void initSinger(){
        singer = new Label(song.singer);
        singer.setFont(font);
        singer.setLayoutX(5*width/8);
        singer.setLayoutY(2.5);
        singer.setPrefWidth(width/4);
        singer.setStyle("-fx-text-fill: #000000;");
    }

    private void initDuration(){
        duration  = new Label(song.normalDuration());
        duration.setFont(font);
        duration.setLayoutX(7*width/8);
        duration.setLayoutY(2.5);
        duration.setTextFill(Paint.valueOf("#a4a4a4"));
    }

    public void setPlayWay(EventHandler<MouseEvent> eventEventHandler){
        songName.setOnMouseClicked(eventEventHandler);
    }

}
