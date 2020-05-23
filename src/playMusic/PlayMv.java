package playMusic;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.util.TimerTask;


public class PlayMv extends Pane {
    //进度条
    private VBox le = new VBox();
    private VBox ri = new VBox();
//    private Label playTime = new Label();
    private ImageView thumb;
    private Double normal_h = 5.0,hided_h = 2.0,thumbSize = 15.0;
    private  final double np=0.9;
    private Boolean hided = false;
    private String sumTime;

    //控制播放
    private ImageView maxSize;
    private double maxSize_size = 30;

    private Font font1 = Font.font("FangSong",17);
    private Font font2 = Font.font("FangSong",17);
    private Font font3 = Font.font("FangSong",30);
    private Font font4 = Font.font("FangSong",25);

    private Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
    private double screenWidth = screensize.getWidth();
    private double screenHeight = screensize.getHeight();


    public int page=1;
    public Stage stage;
    public Scene scene1;
    public Duration duration;
    public MediaPlayer mediaPlayer;
    public MediaView mediaView;
    public Media media;
    public Stage watchStage;
    public Scene watchScene;
    public TimerTask timerTask;
    public Boolean changingTime=false;
    public double videoHeight;
    public double videoWidth;
    public double playerHeight;
    public double playerWidth;
    public double nowWidth;
    public double nowHeight;
    public double playPercentage;
    public double controlVolRate = 0.025;
    public double sTime;
    public double[] msxy = new double[2];
    public double[] ssxy = new double[2];
    public Label showVolume;
    public Boolean playing = true;
    public ImageView stop,close;
    public double bias = 5;
    public Boolean isMouseShow = true;

    private void initSlider(){
        le.setPrefSize(0,normal_h);
        ri.setPrefSize(mediaView.getFitWidth(),normal_h);
        le.setId("le");
        ri.setId("ri");
        le.setLayoutY(np*mediaView.getFitHeight());
        ri.setLayoutY(np*mediaView.getFitHeight());
        le.setLayoutX(0);
        ri.setLayoutX(0);
        thumb = new ImageView();
        thumb.setImage(new Image(getClass().getResource("image/bilibili.png").toExternalForm()));
        thumb.setFitWidth(thumbSize);
        thumb.setFitHeight(thumbSize);
        thumb.setLayoutX(0-thumbSize/2);
        thumb.setLayoutY(ri.getLayoutY()-(thumbSize-normal_h)/2);
        thumb.setId("thumb");
        EventHandler<MouseEvent> enterSlider = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                le.setStyle("-fx-background-color: #2560ce");
                ri.setStyle("-fx-background-color: #7e899c");
                changingTime =true;
            }
        };
        EventHandler<MouseEvent> exitSlider = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                le.setStyle("-fx-background-color: #79a1ec");
                ri.setStyle("-fx-background-color: #dfe8f8");
                changingTime =false;
            }
        };
        EventHandler<MouseEvent> changeTime =  new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double sx = event.getSceneX();
                Duration newDuration = new Duration(sx*duration.toMillis()/mediaView.getFitWidth());
                mediaPlayer.seek(newDuration);
            }
        };
        le.setOnMouseEntered(enterSlider);
        le.setOnMouseExited(exitSlider);
        le.setOnMouseClicked(changeTime);
        ri.setOnMouseExited(exitSlider);
        ri.setOnMouseEntered(enterSlider);
        ri.setOnMouseClicked(changeTime);
        thumb.setOnMouseEntered(enterSlider);
        thumb.setOnMouseExited(exitSlider);
        thumb.setOnMouseDragged(changeTime);
        int st = ((int)duration.toSeconds())%60;
        int mt = ((int)duration.toMinutes())%60;
        int ht = (int)duration.toHours();
        if(ht>0){
            sumTime = String.format("%d:%02d:%d",ht,mt,st);
        }
        else{
            sumTime = String.format("%02d:%d",mt,st);
        }
//        playTime.setTextFill("");
    }

    private void updateSlider(){
        if(hided){
            thumb.setVisible(false);
            le.setPrefHeight(hided_h);
            ri.setPrefHeight(hided_h);
            le.setLayoutY(mediaView.getFitHeight()-hided_h);
            ri.setLayoutY(mediaView.getFitHeight()-hided_h);
        }
        else{
            thumb.setVisible(true);
            le.setPrefHeight(normal_h);
            ri.setPrefHeight(normal_h);
            le.setLayoutY(np*mediaView.getFitHeight());
            ri.setLayoutY(np*mediaView.getFitHeight());
        }
        le.setPrefWidth(mediaView.getFitWidth()*playPercentage);
        ri.setPrefWidth(mediaView.getFitWidth()*(1-playPercentage));
        ri.setLayoutX(mediaView.getFitWidth()*playPercentage);
        thumb.setLayoutX(ri.getLayoutX()-thumbSize/2);
        thumb.setLayoutY(ri.getLayoutY()-(thumbSize-normal_h)/2);
    }

    private void watch(String src) throws IOException {

        final Boolean[] needFit = {true};
        media = new Media(src);
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);
        Pane watchPane = new Pane(mediaView);
        Scene loadScene = new Scene(watchPane,100,100);
        watchStage = new Stage();
        watchStage.setScene(loadScene);
        mediaPlayer.setVolume(0);
        mediaPlayer.play();
        mediaPlayer.setVolume(0.5);
        ImageView loading = new ImageView(getClass().getResource("image/loading.gif").toExternalForm());
        watchStage.getIcons().add(new Image(getClass().getResource("image/scau.jpg").toExternalForm()));
        watchStage.setScene(new Scene(new Pane(loading),400,300));
        close = new ImageView(new Image(getClass().getResource("image/close.png").toExternalForm()));
        watchStage.initStyle(StageStyle.TRANSPARENT);
        watchStage.show();
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                if(needFit[0]){
                    watchStage.hide();
                    videoHeight = media.getHeight();
                    videoWidth = media.getWidth();
                    playerHeight = videoHeight;
                    playerWidth = videoWidth;
                    nowHeight = playerHeight;
                    nowWidth = playerWidth;
                    duration = media.getDuration();
                    mediaView.setFitHeight(videoHeight);
                    mediaView.setFitWidth(videoWidth);
                    showVolume = new Label();
                    showVolume.setFont(font1);
                    showVolume.setStyle("-fx-background:transparent;");
                    showVolume.setStyle("-fx-background-color: #cceb5f;");
                    showVolume.setVisible(false);
                    showVolume.setLayoutX(5);
                    showVolume.setLayoutY(5);
                    Pane watchPane = new Pane();
                    playPercentage = 0.0;
                    initSlider();
                    stop = new ImageView(getClass().getResource("image/stopTags.png").toExternalForm());
                    stop.setFitWidth(50);
                    stop.setFitHeight(50);
                    stop.setStyle("-fx-opacity: 0.4;");
                    stop.setVisible(false);
                    close.setFitWidth(30);
                    close.setFitHeight(30);
                    close.setLayoutX(nowWidth-30);
                    close.setLayoutY(0);
                    close.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            watchPane.setCursor(Cursor.HAND);
                        }
                    });
                    close.setOnMouseExited(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            watchPane.setCursor(Cursor.DEFAULT);
                        }
                    });
                    close.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            watchStage.close();
                        }
                    });
                    watchPane.getChildren().addAll(mediaView,le,ri,thumb,showVolume,stop,close);
                    watchScene = new Scene(watchPane,videoWidth,videoHeight);
                    watchScene.getStylesheets().add(getClass().getResource("style/watch.css").toExternalForm());
                    watchStage.setScene(watchScene);
                    needFit[0] = false;
                    mediaPlayer.setVolume(50);
                    watchStage.show();
                }
                else{
                    playPercentage = mediaPlayer.getCurrentTime().toSeconds()/duration.toSeconds();
                    updateSlider();
                    if(System.currentTimeMillis()-sTime>3000){
                        hided = true;
                        if(watchStage.isMaximized()){
                            watchScene.setCursor(Cursor.NONE);
                        }
                    }
                }
            }
        });

        mediaView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                hided = false;
                sTime = System.currentTimeMillis();
                close.setVisible(true);
            }
        });

        mediaView.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(hided) {
                    hided = false;
                    sTime = System.currentTimeMillis();
                    watchScene.setCursor(Cursor.DEFAULT);
                }
                if(event.getSceneX()>0-bias&&event.getSceneX()<0+bias){
                    if(event.getSceneY()>0-bias&&event.getSceneY()<0+bias){
                        watchScene.setCursor(Cursor.NW_RESIZE);
                    }
                    else if(event.getSceneY()>watchStage.getHeight()-bias&&event.getSceneY()<watchStage.getHeight()+bias){
                        watchScene.setCursor(Cursor.SW_RESIZE);
                    }
                    else{
                        watchScene.setCursor(Cursor.W_RESIZE);
                    }
                }
                else if(event.getSceneX()>watchStage.getWidth()-bias&&event.getSceneX()<watchStage.getWidth()+bias){
                    if(event.getSceneY()>0-bias&&event.getSceneY()<0+bias){
                        watchScene.setCursor(Cursor.NE_RESIZE);
                    }
                    else if(event.getSceneY()>watchStage.getHeight()-bias&&event.getSceneY()<watchStage.getHeight()+bias){
                        watchScene.setCursor(Cursor.SE_RESIZE);
                    }
                    else{
                        watchScene.setCursor(Cursor.E_RESIZE);
                    }
                }
                else if(event.getSceneY()>0-bias&&event.getSceneY()<0+bias){
                    watchScene.setCursor(Cursor.N_RESIZE);
                }
                else if(event.getSceneY()>watchStage.getHeight()-bias&&event.getSceneY()<watchStage.getHeight()+bias){
                    watchScene.setCursor(Cursor.S_RESIZE);
                }
                else {
                    watchScene.setCursor(Cursor.DEFAULT);
                }
            }
        });

        mediaView.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getSceneY()<0||event.getSceneY()>mediaView.getFitHeight()){
                    hided = true;
                    close.setVisible(false);
                }
                if(event.getSceneX()<0||event.getSceneX()>mediaView.getFitWidth()){
                    close.setVisible(false);
                    hided = true;
                }
            }
        });

        mediaView.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double deltaY = event.getDeltaY();
                double volume = mediaPlayer.getVolume()*100.0;
                volume = volume + deltaY*controlVolRate;
                volume = Double.max(0,volume);
                volume = Double.min(100,volume);
                mediaPlayer.setVolume(volume/100.0);
                String volumeInfo = "音量："+Math.round(mediaPlayer.getVolume()*100);
                showVolume.setText(volumeInfo);
                showVolume.setStyle("-fx-opacity: 0.8;");
                showVolume.setVisible(true);
                Task t = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        Thread.sleep(2000);
                        showVolume.setVisible(false);
                        return "";
                    }
                };
                new Thread(t).start();
            }
        });

        mediaView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()== 1){
                    if(playing) {
                        mediaPlayer.pause();
                        playing =false;
                        stop.setLayoutX(mediaView.getFitWidth()-50);
                        stop.setLayoutY(mediaView.getFitHeight()*np-50);
                        stop.setVisible(true);
                    }
                    else {
                        mediaPlayer.play();
                        stop.setVisible(false);
                        playing = true;
                    }
                }
                else {
                    if(!watchStage.isMaximized()) {
                        watchStage.setMaximized(true);
                        mediaView.setFitWidth(screenWidth);
                        mediaView.setFitHeight(screenHeight);
                        stop.setLayoutX(mediaView.getFitWidth()-50);
                        stop.setLayoutY(mediaView.getFitHeight()*np-50);
                        updateSlider();
                    }
                    else{
                        watchStage.setMaximized(false);
                        watchStage.setHeight(playerHeight);
                        watchStage.setWidth(playerWidth);
                        mediaView.setFitHeight(playerHeight);
                        mediaView.setFitWidth(playerWidth);
                        stop.setLayoutX(mediaView.getFitWidth()-50);
                        stop.setLayoutY(mediaView.getFitHeight()*np-50);
                        updateSlider();
                    }
                }
            }
        });

        mediaView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                msxy[0] = event.getScreenX();
                msxy[1] = event.getScreenY();
                ssxy[0] = watchStage.getX();
                ssxy[1] = watchStage.getY();
            }
        });

        mediaView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Cursor cursor = watchScene.getCursor();
                if(cursor==Cursor.DEFAULT) {
                    watchStage.setX(ssxy[0] + event.getScreenX() - msxy[0]);
                    watchStage.setY(ssxy[1] + event.getScreenY() - msxy[1]);
                }
                else{
                    //向右
                    if(cursor==Cursor.E_RESIZE){
                        watchStage.setWidth(event.getScreenX()-ssxy[0]);
                        watchStage.setHeight(watchStage.getWidth()*videoHeight/videoWidth);
                        mediaView.setFitWidth(watchStage.getWidth());
                        mediaView.setFitHeight(watchStage.getHeight());
                        updateSlider();
                        stop.setLayoutX(mediaView.getFitWidth()-50);
                        stop.setLayoutY(mediaView.getFitHeight()*np-50);
                    }
                    //向上
                    if(cursor==Cursor.N_RESIZE){

                    }
                    //向下
                    if(cursor==Cursor.S_RESIZE){

                    }
                    //向左
                    if(cursor==Cursor.W_RESIZE){

                    }
                    if(cursor==Cursor.NE_RESIZE){

                    }
                    if(cursor==Cursor.NW_RESIZE){

                    }
                    if(cursor==Cursor.SE_RESIZE){

                    }
                    if(cursor==Cursor.SW_RESIZE){

                    }
                }
            }
        });

        watchStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent arg0) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                stage.show();
            }
        });

        watchStage.setResizable(true);

    }

    PlayMv(String url) throws IOException {
        watch(url);
        System.out.println("playing mv");
    }
}
