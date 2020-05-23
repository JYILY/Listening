package playMusic;


import base.Playlist;
import base.Song;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
import org.omg.CORBA.PUBLIC_MEMBER;
import spider.KG;
import spider.Spider;
import spider.WYY;
import userPane.UserPane;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class Player {

//==============================
    @FXML
    private ImageView last,next,control,lyrics,playlist,mv,love,volume;


    @FXML
    private MediaView mediaview;

    @FXML
    private Label sumTime,nowTime;
//========================================


    private int nowPlayingIndex;
    private int nowLength = 0;
    private double nowVolume = 0.5;
    private double nowPlayTime = 0;
    private double playlistPaneWidth = 600;
    private double playlistPaneHeight = 500;
    private Duration duration;
    private Boolean isMuted = false;
    private Boolean isPlaying = false;
    private Boolean needToLoadTime = true;
    private Boolean isShowLyrics = false;
    private Boolean isShowPlaylist = false;
    private Boolean changingTime = false;
    private Boolean isPressed = false;
    private Song[] songList = new Song[100];
    private ArrayList<Song> _playlist = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private PlaySlider playSlider;
    private VolumeSlider volumeSlider;
    private ShowLyrics showLyrics;
    private PlayListPane playListPane;
    private Stage playlistStage;
    private double mx,my,sx,sy;
    public UserPane userPane ;
    public ReentrantLock lock = new ReentrantLock();
    public WYY wyy;
    public Parent root;
    public Stage primaryStage;
    private long passKey;

    private void initPlaylistPane(){
        playListPane = new PlayListPane(playlistPaneWidth,playlistPaneHeight,this);
        playlistStage = new Stage();
        playlistStage.setScene(new Scene(playListPane,playlistPaneWidth,playlistPaneHeight));
        playlistStage.initStyle(StageStyle.TRANSPARENT);
        playlistStage.initOwner(primaryStage);
        playlistStage.hide();
    }

    public void initMediaPlayer(){
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>(){
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                if(!needToLoadTime||!Double.isNaN(mediaPlayer.getMedia().getDuration().toMillis())) {
                    if (needToLoadTime) {
                        needToLoadTime = false;
                        duration = mediaPlayer.getMedia().getDuration();
                        sumTime.setText(String.format("%02d:%02d", (int) duration.toMinutes(), (int) duration.toSeconds() % 60));
                        playSlider.set(mediaPlayer, duration);
                        volumeSlider.set(mediaPlayer);
                        isPlaying = true;
                        control.setImage((new Image(getClass().getResource("image/pause.png").toExternalForm())));
                        showLyrics = new ShowLyrics(_playlist.get(nowPlayingIndex).lyrics, mediaPlayer, 500, 300);
                        showLyrics.initOwner(primaryStage);
                        if (isShowLyrics) showLyrics.show();
                        else showLyrics.hide();
                        mediaPlayer.setVolume(volumeSlider.value);
                        lock.unlock();
                    } else {
                        playSlider.setValue(newValue.toMillis() / duration.toMillis());
                        String newTime = String.format("%02d:%02d", (int) newValue.toMinutes(), (int) newValue.toSeconds() % 60);
                        nowTime.setText(newTime);
                    }
                }
            }
        });
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                try {
                    playNextSong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void init() throws IOException {
        playSlider = new PlaySlider(385,15);
        playSlider.setLayoutX(245);
        playSlider.setLayoutY(17.5);
        volumeSlider = new VolumeSlider(140,15,0.5);
        volumeSlider.setLayoutX(720);
        volumeSlider.setLayoutY(17.5);
        Pane pane = new Pane();
        pane.getChildren().addAll(root,playSlider,volumeSlider);
        Scene scene = new Scene(pane,1000,50);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setY(800);
        primaryStage.setX(400);
        primaryStage.setScene(scene);

        last.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    playLastSong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        next.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    playNextSong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        initPlaylistPane();
    }

    public void exit() throws IOException {

    }

    public void addPlaylist(Song[] songs) throws IOException {
        nowLength = 0;
        for(Song song:songs){
            if(_playlist.contains(song))continue;
            playListPane.addSong(song);
            _playlist.add(song);
            nowLength = nowLength + 1;
        }
        nowPlayingIndex = 0;
        addMusic(songs[0]);
    }

    public void addPlaylist(Playlist playlist) throws IOException {
        _playlist.clear();
        playListPane.delAllSong();
        addPlaylist(wyy.getPlaylistDetail(playlist.playlistId));
    }

    public void addMusic(Song song) throws IOException {
        song = wyy.getSongDetail(song);
        if(song.musicSrc.equals("")){
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.titleProperty().set("很抱歉");
            alert.setContentText(song.songName+"为相关平台收费歌曲，暂时不提供播放");
            alert.show();
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    Thread.sleep(3000);
                    alert.close();
                    return "";
                }
            };
            new Thread(task).start();
            playNextSong();
            return;
        }
        if(!_playlist.contains(song)){
            _playlist.add(song);
            playListPane.addSong(song);
            nowPlayingIndex = nowLength;
            nowLength = nowLength +1;
        }
        else{
            nowPlayingIndex = _playlist.indexOf(song);
        }
        long playKey = System.currentTimeMillis();
        passKey = playKey;
        replay(playKey);
    }

    @FXML
    public void playLastSong() throws IOException {
        if(nowLength>0){
            nowPlayingIndex = (nowPlayingIndex-1+nowLength)%nowLength;
            addMusic(_playlist.get(nowPlayingIndex));
        }
    }

    @FXML
    public void playNextSong() throws IOException {
        if(nowLength>0){
            nowPlayingIndex = (nowPlayingIndex+1+nowLength)%nowLength;
            addMusic(_playlist.get(nowPlayingIndex));
        }
    }

    @FXML
    public void replay(long key){
        if(!needToLoadTime){
            mediaPlayer.stop();
            mediaPlayer.dispose();
            showLyrics.close();
        }
        needToLoadTime = true;
//        mediaPlayer = new MediaPlayer(new Media(songList[nowPlayingIndex].musicSrc));
        mediaPlayer = new MediaPlayer(new Media(_playlist.get(nowPlayingIndex).musicSrc));
        initMediaPlayer();
        mediaPlayer.play();
        userPane.showSong(_playlist.get(nowPlayingIndex));
    }

    @FXML
    public void play(){
        if(isPlaying){
            control.setImage(new Image(getClass().getResource("image/play.png").toExternalForm()));
            mediaPlayer.pause();
            isPlaying = false;
        }
        else{
            control.setImage((new Image(getClass().getResource("image/pause.png").toExternalForm())));
            mediaPlayer.play();
            isPlaying = true;
        }

    }

    @FXML
    public void playMv() throws IOException {
        if(isPlaying){
            play();
        }
        if(!_playlist.get(nowPlayingIndex).mvSrc.equals("0")){
            new PlayMv(_playlist.get(nowPlayingIndex).mvSrc);
        }
    }

    @FXML
    public void showLyrics(){
        if(isShowLyrics){
            lyrics.setImage(new Image(getClass().getResource("image/closeLyrics.png").toExternalForm()));
            isShowLyrics = false;
            showLyrics.hide();
        }
        else{
            lyrics.setImage(new Image(getClass().getResource("image/openLyrics.png").toExternalForm()));
            isShowLyrics = true;
            showLyrics.show();
        }
    }

    @FXML
    public void showLove(){

    }

    @FXML
    public void showSongList(){
        if(isShowPlaylist){
            playlistStage.hide();
            isShowPlaylist = false;
        }
        else{

            playlistStage.setX(primaryStage.getX()-playlistPaneWidth+primaryStage.getWidth());
            playlistStage.setY(primaryStage.getY()-playlistPaneHeight);
            playlistStage.show();
            isShowPlaylist = true;
        }
    }

    @FXML
    public void mute() {
        if (isMuted) {
            volume.setImage(new Image(getClass().getResource("image/volume.png").toExternalForm()));
            isMuted = false;
            volumeSlider.setValue(nowVolume);
            mediaPlayer.setVolume(nowVolume);
        } else {
            nowVolume = mediaPlayer.getVolume();
            volumeSlider.setValue(0);
            mediaPlayer.setVolume(0);
            volume.setImage(new Image(getClass().getResource("image/mute.png").toExternalForm()));
            isMuted = true;
        }
    }

}

//==================================================================

