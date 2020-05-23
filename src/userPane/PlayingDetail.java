package userPane;

import ShowSongList.SongListPane;
import base.Song;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class PlayingDetail extends Pane {
    EventHandler<MouseEvent> addFav;
    private Song nowSong;
    private ImageView imageView,favTag;
    private Image fav,fav_;
    private Label songName,singer;
    private ListPane listPane;

    PlayingDetail(Song song, ListPane listPane){
        this.listPane = listPane;
        this.setLayoutX(0);
        this.setWidth(200);
        this.setHeight(60);
        nowSong = song;

        imageView = new ImageView(new Image(song.imgSrc));
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        imageView.setLayoutY(5);
        imageView.setLayoutX(5);

        songName = new Label(song.songName);
        songName.setFont(new Font("Microsoft YaHei",13));
        songName.setStyle("-fx-text-fill: #000000");
        songName.setPrefSize(120,25);
        songName.setLayoutX(65);
        songName.setLayoutY(5);

        singer = new Label(song.singer);
        singer.setFont(new Font("Microsoft YaHei",12));
        singer.setStyle("-fx-text-fill: #545454");
        singer.setPrefSize(120,25);
        singer.setLayoutX(65);
        singer.setLayoutY(30);

        fav = new Image(getClass().getResource("image/fav.png").toExternalForm());
        fav_ = new Image(getClass().getResource("image/fav_.png").toExternalForm());

        favTag = new ImageView();
        song.isLoved = listPane.isLovedSong(song);
        if(song.isLoved){ favTag.setImage(fav); }
        else { favTag.setImage(fav_); }
        favTag.setFitWidth(15);
        favTag.setFitHeight(15);
        favTag.setLayoutY(40);
        favTag.setLayoutX(180);
        favTag.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                favTag.setCursor(Cursor.HAND);
            }
        });
        favTag.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                favTag.setCursor(Cursor.DEFAULT);
            }
        });
        favTag.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(song.isLoved){
                    favTag.setImage(fav_);
                    song.isLoved = false;
                    listPane.delSong(nowSong);
                }
                else{
                    favTag.setImage(fav);
                    song.isLoved = true;
                    listPane.addSong(nowSong);
                }
            }
        });

        this.getChildren().addAll(imageView,songName,singer,favTag);
        this.setPrefSize(200,60);
        this.setStyle("-fx-background-color: #f3f3f3;");
    }

    public void changeSong(Song newSong){
        nowSong = newSong;
        newSong.isLoved = listPane.isLovedSong(newSong);
        imageView.setImage(new Image(newSong.imgSrc+"?50y50"));
        if(newSong.isLoved)favTag.setImage(fav);
        else favTag.setImage(fav_);
        singer.setText(newSong.singer);
        songName.setText(newSong.songName);
    }

}
