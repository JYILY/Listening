package userPane;

import ShowSongList.SongListPane;
import base.Playlist;
import base.Song;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import playlistPane.TopListPane;
import spider.WYY;
import myStyle.*;
import java.io.IOException;

public class UserPane extends Pane {

    private PlayingDetail playingDetail;
    private boolean tag = false;
    public ListPane listPane;
    public SongListPane songListPane;

    public UserPane() throws IOException {
//        if(STYLE.isTransparent)this.setStyle("-fx-background:transparent;");
        listPane = new ListPane();
        this.getChildren().add(listPane);
    }

    public void showSong(Song song){
        if(tag){
            playingDetail.changeSong(song);
        }
        else {
            playingDetail = new PlayingDetail(song,listPane);
            playingDetail.setLayoutX(0);
            playingDetail.setLayoutY(600 - 60);
            this.getChildren().add(playingDetail);
            tag = true;
        }
    }

    public Song[] getFavSong(){
        return listPane.getFavSongs();
    }

    public Playlist[] getFavPlaylist(){
        return listPane.getFavPlaylist();
    }

    public void setSongListPane(SongListPane songListPane){
        listPane.setSongListPane(songListPane);
        this.songListPane = songListPane;
    }

    public void setSpider(WYY wyy){
        listPane.setWyy(wyy);
    }

    public void exit() throws IOException {
        listPane.save();
    }

    public void setPP(Stage stage, Scene s1, Scene s2, Scene s3, TopListPane top){
        listPane.setPP(stage,s1,s2,s3,top);
    }

}
