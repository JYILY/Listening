package playMusic;

import base.Song;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import playMusic.Player;

import java.io.IOException;

public class SongPane extends Pane {
    private Label sequenceNum,songName,singer,duration;
    private static ImageView  loved = new ImageView(new Image("https://i.loli.net/2020/05/09/PQgwELU26R5TYGK.png"));
    private static ImageView unloved = new ImageView(new Image("https://i.loli.net/2020/05/09/yqzp19xcGVsQMmJ.png"));
    private Font font;
    private double paneHeight,paneWidth;
    private Song ownSong;
    private Player player;
    private String style_base,style_mouse_on,style_be_choose;
    private boolean beingChoose = false;
    private Pane songList;

    static {
        loved.setFitHeight(15);
        loved.setFitWidth(15);
        loved.setLayoutX(55);
        loved.setLayoutY(5);
        unloved.setFitWidth(15);
        unloved.setFitHeight(15);
        unloved.setLayoutX(55);
        unloved.setLayoutY(5);
    }

    SongPane(Song song,double width,double height,Pane songList){

        this.songList = songList;
        this.setPrefHeight(height);
        this.setPrefWidth(width);
        this.paneHeight = height;
        this.paneWidth = width;
        this.ownSong = song;

        songName = new Label(song.songName);
        singer = new Label(song.singer);
        duration = new Label(song.normalDuration());

        font = new Font("FangSong",15);
        songName.setFont(font);
        singer.setFont(font);
        duration.setFont(font);
        double fontHeight = font.getSize();

        songName.setLayoutY((height-fontHeight)/2);
        songName.setLayoutX(width/16);
        songName.setPrefWidth(5*width/8);

        singer.setLayoutY((height-fontHeight)/2);
        singer.setLayoutX(11*width/16);
        singer.setPrefWidth(3*width/16);

        duration.setLayoutY((height-fontHeight)/2);
        duration.setLayoutX(14*width/16);


        this.getChildren().addAll(songName,singer,duration);

        initMouseAction();
//        initPaneStyle();
    }

    public void setFont(Font ft){
        font = ft;
        songName.setFont(font);
        singer.setFont(font);
        duration.setFont(font);
        double fontHeight = font.getSize();
        songName.setLayoutY((paneHeight-fontHeight)/2);
        singer.setLayoutY((paneHeight-fontHeight)/2);
        duration.setLayoutY((paneHeight-fontHeight)/2);
    }

    private void initMouseAction(){
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for(Node songPane:songList.getChildren()){
                    ((SongPane) songPane).normal();
                }
                beingChoose = true;
                setStyle(style_be_choose);
                if(event.getClickCount()>1){
                    try {
                        player.addMusic(ownSong);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!beingChoose) setStyle(style_mouse_on);
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!beingChoose)setStyle(style_base);
            }
        });
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public void showNum(int num){
        sequenceNum = new Label(String.valueOf(num));
        sequenceNum.setFont(font);
        sequenceNum.setLayoutY((paneHeight-font.getSize())/2);
        sequenceNum.setLayoutX(paneWidth/32);
        this.getChildren().add(this.sequenceNum);
    }

    public void setStyle_base(String style){
        style_base = style;
    }

    public void setStyle_mouse_on(String style){
        style_mouse_on = style;
    }

    public void setStyle_be_choose(String style){
        style_be_choose = style;
    }

    public void normal(){
        beingChoose = false;
        setStyle(style_base);
    }
}
