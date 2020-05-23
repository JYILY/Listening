package playlistPane;

import ShowSongList.SongListPane;
import base.Playlist;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import spider.WYY;

import java.io.IOException;

public class TopListPane extends Pane {
    private WYY wyy;
    private double hs = 15,ws = 15;
    private double maxHeight = 0;
    private double opPaneWidth = 50;
    private Stage stage;
    private Scene scene;
    private SongListPane songListPane;
    private Playlist nowPlaylist = null;

    public TopListPane(WYY wyy) throws IOException {
        this.wyy = wyy;
        this.setWidth(800);
        this.setHeight(600);
        this.setStyle("-fx-background-color : #f3f3f3");
        Playlist[] nowPlayList = wyy.getTopList();

        for(int i=0;i<nowPlayList.length;i++){
            PlaylistPane playlistPane = new PlaylistPane(nowPlayList[i],wyy);
            int row = i%4;
            int col = i/4;
            playlistPane.setLayoutY((playlistPane.sumH+hs)*col+hs);
            playlistPane.setLayoutX((playlistPane.width+ws)*row+ws);
            maxHeight = Double.max(maxHeight,playlistPane.getLayoutY()+playlistPane.sumH);

            playlistPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    nowPlaylist = playlistPane.playlist;
                }
            });
            playlistPane.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    nowPlaylist = null;
                }
            });
            this.getChildren().add(playlistPane);
        }

        this.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double deltaY = event.getDeltaY();
                double newY = getLayoutY() + deltaY;
                newY = Double.max(600-maxHeight,newY);
                newY = Double.min(0,newY);
                setLayoutY(newY);
            }
        });

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(nowPlaylist != null) {
                    try {
                        songListPane.change(wyy.getPlaylistDetail(nowPlaylist.playlistId),nowPlaylist);
                        stage.setScene(scene);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setPP(Stage stage, Scene scene, SongListPane songListPane){
        this.songListPane =songListPane;
        this.stage = stage;
        this.scene = scene;
    }
}
