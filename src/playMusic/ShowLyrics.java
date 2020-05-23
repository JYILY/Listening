package playMusic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.LightBase;
import javafx.scene.Scene;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;

public class ShowLyrics extends Stage {

    private Lyric[] lyrics = new Lyric[2000];
    private int len = 0;
    private int nowShowIndex = 0;
    private double interval;
    private double textLen;
    private MediaPlayer mediaPlayer;
    private Font font;


    ShowLyrics(String[] baseLyrics,MediaPlayer mediaPlayer,double w,double h)  {
        this.initStyle(StageStyle.TRANSPARENT);
        this.mediaPlayer = mediaPlayer;
        this.setWidth(w);
        this.setHeight(h);
        this.setAlwaysOnTop(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double sw = screenSize.getWidth();
        double sh = screenSize.getHeight();
        this.setX(sw/2-w/2);
        this.setY(17*sh/18-h/2);
        font = new Font("Microsoft YaHei",50);
//        InnerShadow is = new InnerShadow();
//        is.setOffsetX(4.0f);
//        is.setOffsetY(4.0f);
        Text text = new Text();
        text.setFont(font);
        text.setLayoutY(50);
        text.setFill(Paint.valueOf("#c73c3c"));
//        text.setFont(Font.font(null, FontWeight.BOLD, 50));
//        text.setEffect(is);
        Pane pane = new Pane();
        pane.getChildren().add(text);
        pane.setStyle("-fx-background:transparent;");
        pane.setLayoutY(10);
        pane.setLayoutX(0);
        Scene scene = new Scene(pane,w,h);
        scene.setFill(null);
        this.setScene(scene);
        for(String s: baseLyrics){
            try {
                Lyric lyric = Lyric.process(s);
                lyrics[len] =lyric;
                len ++;
            }catch (Exception e){
            }
        }
        if(len != 0) {
            text.setText(lyrics[nowShowIndex].text);
            textLen = text.getLayoutBounds().getWidth();
            if (nowShowIndex != len - 1) {
                interval = lyrics[nowShowIndex + 1].duration.toMillis() - lyrics[nowShowIndex].duration.toMillis();
            } else {
                interval = mediaPlayer.getMedia().getDuration().toMillis() - lyrics[nowShowIndex].duration.toMillis();
            }
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    double nm = newValue.toMillis();
                    nowShowIndex = len;
                    for(int i = 1 ; i<len;i++){
                        if(nm>lyrics[i-1].duration.toMillis()&&nm<lyrics[i].duration.toMillis()){
                            nowShowIndex = i-1;
                            break;
                        }
                    }
                    if(nowShowIndex == len ) return;
                    double pt = nm - lyrics[nowShowIndex].duration.toMillis();
                    text.setText(lyrics[nowShowIndex].text);
                    interval = lyrics[nowShowIndex + 1].duration.toMillis() - lyrics[nowShowIndex].duration.toMillis();
                    textLen = text.getLayoutBounds().getWidth();
                    double newX = Double.min((w-h)/2,0) ;
                    text.setLayoutX(newX);
                    if(textLen>w){
                        text.setLayoutX(-(textLen-w)*Math.pow(pt/interval,2));
                    }
                }
            });
        }
    }

}
