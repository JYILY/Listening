package playMusic;

import base.Song;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;
import java.util.List;

public class PlayListPane extends Pane {

    public int length = 0;
    private double itemWidth,itemHeight;
    private double baseWidth,baseHeight;
    public double nowHeight;
    private Player player;
    private static String STYLE_ODD,STYLE_EVEN,STYLE_CHOSEN,STYLE_MOUSE_ON;
    private Pane title,songList;
    private ArrayList<String> inListId = new ArrayList<>();

    static {
        STYLE_ODD = "-fx-background-color: #ffffff;";
        STYLE_EVEN = "-fx-background-color: #f5f5f5;";
        STYLE_CHOSEN = "-fx-background-color: #cacaca;";
        STYLE_MOUSE_ON = "-fx-background-color: #eeeeee";
    }

    public PlayListPane(double width,double height,Player player){
        this.setWidth(width);
        this.setHeight(height);
        this.baseHeight = height;
        this.baseWidth = width;
        this.player = player;
        itemHeight = 25;
        itemWidth = width;
        this.setLayoutY(0);

        songList = new Pane();
        songList.setLayoutY(40);
        songList.setLayoutX(0);

        songList.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double deltaY  = event.getDeltaY();
                double p = 1.0;
                double newY = songList.getLayoutY()+deltaY*p;
                newY = Double.max(baseHeight-length*itemHeight,newY);
                newY = Double.min(40,newY);
                songList.setLayoutY(newY);
            }
        });
        this.getChildren().add(songList);

        title = new Pane();
        title.setLayoutY(0);
        title.setLayoutX(0);
        title.setPrefSize(width,40);
        Label info = new Label("播放列表");
        info.setFont(new Font("Microsoft YaHei",20));
        info.setTextFill(Paint.valueOf("#ffffff"));
        info.setPrefSize(100,30);
        info.setStyle("-fx-text-fill: white;");
        info.setStyle("-fx-background-radius: 8px;");
        info.setLayoutX(width/2-40);
        info.setLayoutY(5);
        info.setCenterShape(true);
        title.getChildren().add(info);
        title.setStyle("-fx-background-color: #c73c3c");
        this.getChildren().add(title);
    }

    public void addSong(Song song){
        if(inListId.contains(song.songId))return;
        SongPane songPane = new SongPane(song,itemWidth,itemHeight,songList);
        songPane.setPlayer(player);
        songPane.setLayoutX(0);
        songPane.setLayoutY(length*itemHeight);
        if(length%2==0){
            songPane.setStyle_base(PlayListPane.STYLE_ODD);
        }else{
            songPane.setStyle_base(PlayListPane.STYLE_EVEN);
        }
        songPane.setStyle_be_choose(PlayListPane.STYLE_CHOSEN);
        songPane.setStyle_mouse_on(PlayListPane.STYLE_MOUSE_ON);
        songPane.normal();
        songList.getChildren().add(songPane);
        length = length + 1;

        inListId.add(song.songId);
    }

    public void delAllSong(){
        this.getChildren().clear();
        this.getChildren().addAll(songList,title);
    }
    public void deleteSong(int index){
        songList.getChildren().remove(index);
        length = 0;
        for(Node song:songList.getChildren()){
            song.setLayoutY(length*itemHeight);
            length = length + 1;
        }
    }

    public void normal(){

    }
}
