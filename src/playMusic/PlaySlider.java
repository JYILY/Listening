package playMusic;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class PlaySlider extends Pane {

    private VBox leftBox,rightBox;
    private ImageView thumb;
    private double value;
    private double w,h;
    private MediaPlayer mediaPlayer;
    private Duration duration;
    private boolean prepared =false;

    PlaySlider(double width,double height){
        leftBox = new VBox();
        leftBox.setId("leftBox");
        rightBox = new VBox();
        rightBox.setId("rightBox");
        thumb = new ImageView(new Image(getClass().getResource("image/bilibili.png").toExternalForm()));
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
                if(prepared) {
                    double mx = event.getSceneX();
                    double lx = leftBox.getLayoutX();
                    double pp = (mx - lx - 245) / (width - 2 * height / 3);
                    seek(pp);
                }
            }
        };
        leftBox.setOnMouseClicked(changeTime);
        rightBox.setOnMouseClicked(changeTime);
        thumb.setOnMouseDragged(changeTime);
        this.getChildren().addAll(rightBox,leftBox,thumb);
        w = width;
        h = height;
        this.setId("playSlider");
    }

    public void set(MediaPlayer mediaPlayer, Duration duration){
        prepared = true;
        this.mediaPlayer = mediaPlayer;
        this.duration = duration;
    }

    public void setValue(double v){
        v = Double.min(1.0,v);
        v = Double.max(0,v);
        value = v;
        leftBox.setPrefWidth((w-2*h/3)*v);
        thumb.setLayoutX((w-2*h/3)*v-h/6);
    }

    public void seek(double v){
        v = Double.min(1.0,v);
        v = Double.max(0,v);
        mediaPlayer.seek(new Duration(v*duration.toMillis()));
    }

    public double getValue(){
        return value;
    }

}
