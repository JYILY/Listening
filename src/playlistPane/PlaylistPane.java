package playlistPane;

import ShowSongList.SongListPane;
import base.Playlist;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import spider.WYY;
import userPane.ListPane;

import javax.security.auth.callback.LanguageCallback;
import java.io.IOException;

public class PlaylistPane extends Pane {
    private ImageView img;
    private Label label,playCount;
    private SongListPane songListPane;
    public double height = 180;
    public double width =180;
    public double textHeight = 30;
    public double playCountHeight = 20;
    public double sumH = height+textHeight+playCountHeight;
    private Stage stage;
    private WYY wyy;
    public Playlist playlist;

    PlaylistPane(Playlist playlist,  WYY wyy){
        this.playlist = playlist;
        this.wyy = wyy;
        this.setPrefSize(width,sumH);

        img = new ImageView(new Image(playlist.imgSrc));
        img.setFitHeight(height);
        img.setFitWidth(width);
        img.setLayoutX(0);
        img.setLayoutY(0);

        img.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setCursor(Cursor.HAND);
            }
        });
        img.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setCursor(Cursor.DEFAULT);
            }
        });

        label = new Label(playlist.title);
        label.setFont(new Font("Microsoft YaHei",20));
        label.setPrefSize(width,textHeight);
        label.setLayoutX(0);
        label.setLayoutY(height);

        playCount = new Label("播放量："+playlist.normalPlayCount());
        playCount.setFont(new Font("Microsoft YaHei",15));
        playCount.setTextFill(Paint.valueOf("#08ad53"));
        playCount.setPrefSize(width,playCountHeight);
        playCount.setLayoutX(0);
        playCount.setLayoutY(height+textHeight);

        this.getChildren().addAll(img,label,playCount);
    }

}
