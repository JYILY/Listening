package playlistPane;

import ShowSongList.SongListPane;
import base.Playlist;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import spider.WYY;
import userPane.OpPane;
import java.io.IOException;

public class PlaylistListPane extends Pane {

    private SongListPane songListPane;
    private Playlist[] nowPlaylist = null;
    private WYY wyy;
    private int page =1;
    private double hs = 15,ws = 15;
    private double maxHeight = 0;
    private double opPaneWidth = 50;
    private double ops = 20;
    private double os = 40;
    private int num = 8;
    private Pane opPane;
    private Button[] buttons;
    EventHandler<MouseEvent> sp;

    public Playlist playlist;
    private  Stage stage;
    private Scene scene;

    public PlaylistListPane(SongListPane songListPane, WYY wyy, Scene scene , Stage stage) throws IOException {
        this.songListPane = songListPane;
        this.wyy = wyy;
        this.scene = scene;
        this.stage =stage;


        updatePage();

        opPane = new Pane();
        opPane.setPrefSize(800,50);
        opPane.setStyle("-fx-background-color : #f3f3f3");

        ImageView lastPage = new ImageView(new Image(getClass().getResource("image/lastPage.png").toExternalForm()));
        ImageView nextPage = new ImageView(new Image(getClass().getResource("image/nextPage.png").toExternalForm()));
        lastPage.setFitWidth(os);
        lastPage.setFitHeight(os);
        nextPage.setFitHeight(os);
        nextPage.setFitWidth(os);
        lastPage.setLayoutY(5);
        nextPage.setLayoutY(5);
        EventHandler<MouseEvent> enter = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setCursor(Cursor.HAND);
            }
        };
        EventHandler<MouseEvent> exit = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setCursor(Cursor.DEFAULT);
            }
        };

        lastPage.setOnMouseEntered(enter);
        lastPage.setOnMouseExited(exit);
        nextPage.setOnMouseEntered(enter);
        nextPage.setOnMouseExited(exit);

        double baseX = (800-(num+2)*(os+ops)+ops)/2;
        lastPage.setLayoutX(baseX);
        nextPage.setLayoutX(baseX+(num+1)*(os+ops));
        lastPage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(page>=1){
                    page = page - 1;
                    try {
                        updatePage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        nextPage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                page = page + 1;
                try {
                    updatePage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        opPane.getChildren().addAll(lastPage,nextPage);

        buttons = new Button[num+1];
        for(int i = 1;i<=num;i++){
            buttons[i] = new Button(i+"");
            buttons[i].setStyle("-fx-background-color : #ffffff");
            buttons[i].setPrefSize(os,os);
            buttons[i].setLayoutY(5);
            buttons[i].setLayoutX(baseX+i*(os+ops));
            int finalI = i;
            buttons[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    page = Integer.parseInt(buttons[finalI].getText());
                    try {
                        updatePage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            buttons[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    buttons[finalI].setStyle("-fx-background-color : #b61717");
                    setCursor(Cursor.HAND);
                }
            });
            buttons[i].setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    buttons[finalI].setStyle("-fx-background-color : #ffffff");
                    setCursor(Cursor.DEFAULT);
                }
            });
            opPane.getChildren().add(buttons[i]);
        }

        opPane.setLayoutX(0);
        opPane.setLayoutY(maxHeight);
        opPane.setStyle("-fx-background-color : #ffffff");
        maxHeight = opPaneWidth + maxHeight;
        this.getChildren().add(opPane);

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

    }

    public void updatePlaylist(Playlist[] playlists) throws IOException {
        if(nowPlaylist!=null){
            this.getChildren().clear();
            this.getChildren().add(opPane);
        }
        nowPlaylist = playlists;
        for(int i=0;i<nowPlaylist.length;i++){
            PlaylistPane playlistPane = new PlaylistPane(playlists[i],wyy);
            int row = i%4;
            int col = i/4;
            playlistPane.setLayoutY((playlistPane.sumH+hs)*col+hs);
            playlistPane.setLayoutX((playlistPane.width+ws)*row+ws);
            maxHeight = Double.max(maxHeight,playlistPane.getLayoutY()+playlistPane.sumH);
            playlistPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        songListPane.change(wyy.getPlaylistDetail(playlistPane.playlist.playlistId),playlistPane.playlist);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setScene(scene);
                }
            });
            this.getChildren().add(playlistPane);
        }
    }

    public void updatePage() throws IOException {
        if(nowPlaylist!=null) {
            int bias = Integer.min(4, page);
            for (int j = 1; j <= num; j++) {
                int finalP = page + j - bias;
                buttons[j].setText(finalP + "");
            }
        }
        updatePlaylist(wyy.getOnePagePlaylist(page));
    }

    public void setPP(Scene scene,Stage stage){
        this.stage = stage;
        this.scene = scene;
    }


}
