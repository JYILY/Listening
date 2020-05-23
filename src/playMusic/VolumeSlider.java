package playMusic;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;


public class VolumeSlider extends Pane {

    private VBox leftBox,rightBox;
    private ImageView thumb;
    public double value;
    private double w,h;
    private MediaPlayer mediaPlayer;

    VolumeSlider(double width,double height,double initValue){
        leftBox = new VBox();
        leftBox.setId("volume-left-box");
        rightBox = new VBox();
        rightBox.setId("volume-right-box");
        thumb = new ImageView(new Image(getClass().getResource("image/bili.png").toExternalForm()));
        thumb.setId("thumb");
        thumb.setFitWidth(height);
        thumb.setFitHeight(height);
        leftBox.setPrefSize(0,height/3);
        rightBox.setPrefSize(width-2*height/3,height/3);
        leftBox.setLayoutX(height/3);
        leftBox.setLayoutY(height/3);
        rightBox.setLayoutX(height/3);
        rightBox.setLayoutY(height/3);
        thumb.setLayoutY(0);
        EventHandler<MouseEvent> changeTime =  new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double mx = event.getSceneX();
                double lx = leftBox.getLayoutX();
                double pp = (mx-lx-720)/(width-2*height/3);
                setVolume(pp);
                setValue(pp);
            }
        };
        leftBox.setOnMouseClicked(changeTime);
        rightBox.setOnMouseClicked(changeTime);
        thumb.setOnMouseDragged(changeTime);
        this.getChildren().addAll(rightBox,leftBox,thumb);
        w = width;
        h = height;
        setValue(initValue);
        thumb.setVisible(false);
        this.setId("volumeSlider");
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                thumb.setVisible(true);
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                thumb.setVisible(false);
            }
        });
    }

    public void set(MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
    }

    public void setValue(double v){
        v = Double.min(1.0,v);
        v = Double.max(0,v);
        value = v;
        leftBox.setPrefWidth((w-2*h/3)*v);
        thumb.setLayoutX((w-2*h/3)*v-h/6);
    }

    private void setVolume(double v){
        v = Double.min(1.0,v);
        v = Double.max(0,v);
        mediaPlayer.setVolume(v);
    }

    public double getValue(){
        return value;
    }

    public void hideThumb(){
        thumb.setVisible(false);
    }

}
