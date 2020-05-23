package ShowSongList;

import base.Playlist;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import playMusic.Player;
import userPane.ListPane;
import java.io.IOException;


public class PlaylistPane extends Pane {

    private double width = 800 ,height = 250;
    private Player player;
    private ListPane listPane;
    private Playlist playlist;
    private ImageView img;
    private Label label,des,playCount;
    private Text desText;
    private ImageView fav;
    public boolean watchingDes = false;
    private ImageView playAll;
    private boolean loved = false;

    private EventHandler<MouseEvent> enter = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            setCursor(Cursor.HAND);
        }
    };
    private EventHandler<MouseEvent> exit = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            setCursor(Cursor.DEFAULT);
        }
    };

    PlaylistPane(Player player,userPane.ListPane listPane){
        this.listPane = listPane;
        this.player = player;
        this.setPrefSize(width,height);
        img = new ImageView();
        img.setFitWidth(200);
        img.setFitHeight(200);
        img.setLayoutX(width/64);
        img.setLayoutY(25);

        playAll = new ImageView(new Image(getClass().getResource("image/playAll.png").toExternalForm()));
        playAll.setFitHeight(55);
        playAll.setFitWidth(180);
        playAll.setLayoutY(175);
        playAll.setLayoutX(250);
        playAll.setOnMouseEntered(enter);
        playAll.setOnMouseExited(exit);
        playAll.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    player.addPlaylist(playlist);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fav = new ImageView(new Image(getClass().getResource("image/fav.png").toExternalForm()));
//        if(playlist.isLoved)fav.setImage(new Image(getClass().getResource("image/faved.png").toExternalForm()));

        fav.setFitWidth(180);
        fav.setFitHeight(55);
        fav.setLayoutX(550);
        fav.setLayoutY(175);
        fav.setOnMouseEntered(enter);
        fav.setOnMouseExited(exit);
        fav.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(playlist.isLoved){
                    playlist.isLoved = false;
                    listPane.delPlaylist(playlist);
                    fav.setImage(new Image(getClass().getResource("image/fav.png").toExternalForm()));
                }
                else{
                    listPane.addPlaylist(playlist);
                    playlist.isLoved = true;
                    fav.setImage(new Image(getClass().getResource("image/faved.png").toExternalForm()));
                }
            }
        });

//        label = new Label(playlist.title);
        label = new Label("");
        label.setPrefWidth(500);
        label.setLayoutX(250);
        label.setLayoutY(25);
        label.setFont(new Font("Microsoft YaHei",30));

        Label help = new Label("《==长按封面可查看简历");
        help.setFont(new Font("Microsoft YaHei",20));
        help.setTextFill(Paint.valueOf("#545454"));
        help.setLayoutX(250);
        help.setLayoutY(120);

//        playCount = new Label("播放量："+playlist.normalPlayCount());
        playCount = new Label("");
        playCount.setFont(new Font("Microsoft YaHei",20));
        playCount.setTextFill(Paint.valueOf("#08ad53"));
        playCount.setLayoutX(250);
        playCount.setLayoutY(80);

        des = new Label("");
        des.setPrefWidth(500);
        des.setWrapText(true);
        des.setLayoutX(20);
        des.setLayoutY(20);
        des.setFont(new Font("Microsoft YaHei",20));
        des.setTextFill(Paint.valueOf("#ffffff"));

        desText = new Text();
        desText.setText("");
        desText.setWrappingWidth(500);
        desText.setLayoutX(20);
        desText.setLayoutY(20);
        desText.setFont(new Font("Microsoft YaHei",20));
        desText.setFill(Paint.valueOf("#ffffff"));

        Pane desPane = new Pane();
        desPane.setLayoutY(230);
        desPane.setLayoutX(width/64);
        desPane.setPrefWidth(520);
        desPane.getChildren().add(desText);
        desPane.setStyle("-fx-background-color: rgb(37, 116, 169);");

        desPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                watchingDes = true;
            }
        });

        desPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                watchingDes = false;
                desPane.setVisible(false);
            }
        });

        desPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double  deltaY = event.getDeltaY();
                double desW = des.getLayoutBounds().getHeight();
                double newY = des.getLayoutY()+deltaY;
                newY = Double.min(desW-200,newY);
                newY = Double.max(20,newY);
                des.setLayoutY(newY);
            }
        });

        desPane.setVisible(false);

        this.getChildren().addAll(img,playAll,fav,label,playCount,desPane,help);

        img.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                desPane.setVisible(true);
            }
        });

        img.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        Thread.sleep(300);if(!watchingDes)desPane.setVisible(false);
                        return "";
                    }
                };
                new Thread(task).start();
            }
        });
    }

    public void update(Playlist playlist){
        this.playlist = playlist;
        img.setImage(new Image(playlist.imgSrc));
        label.setText(playlist.title);
//        des.setText(playlist.description+"\n ");
//        des.impl_updatePeer();
        desText.setText(playlist.description+"\n ");
        playCount.setText("播放量："+playlist.normalPlayCount());
        playlist.isLoved = listPane.isLovePlaylist(playlist);
        if(!playlist.isLoved)fav.setImage(new Image(getClass().getResource("image/fav.png").toExternalForm()));
        else{
            fav.setImage(new Image(getClass().getResource("image/faved.png").toExternalForm()));
        }
    }

}
