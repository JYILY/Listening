package ShowSongList;

import base.Playlist;
import base.Song;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import playMusic.Player;
import spider.WYY;
import userPane.ListPane;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SongListPane extends Pane {

    private ListPane listPane;
    public Song song;
    private Pane titlePane;
    private PlaylistPane playlistPane;
    private DesPane desPane;
    private Font font =new Font("Microsoft YaHei",20);
    private double width = 800 ;
    private double height = 600;
    private int length = 0;
    private double titleY = 250;
    private double desY;
    private Player player;
    private WYY wyy;
    private Stage stage;
    public boolean isLoved= false;
    private final static FileChooser fileChooser = new FileChooser();

    public SongListPane(Player player,ListPane listPane,WYY wyy,Stage stage){
        this.player = player;
        this.listPane = listPane;
        this.wyy = wyy ;
        this.stage = stage;
        titleY = 250;
        playlistPane = new PlaylistPane(player,listPane);
        playlistPane.setLayoutX(0);
        playlistPane.setLayoutY(0);
        this.getChildren().addAll(playlistPane);
        init();
    }

    private void initTitlePane(){
        titlePane = new Pane();
        titlePane.setPrefSize(width,50);
        titlePane.setLayoutX(0);
        titlePane.setLayoutY(titleY);
        titlePane.setStyle("-fx-background-color: #fafafa;");
        Label songName = new Label("歌曲");
        songName.setFont(font);
        songName.setStyle("-fx-text-fill: #a4a4a4;");
        songName.setLayoutX(width/8);
        songName.setLayoutY(10);
        Label singer = new Label("歌手");
        singer.setFont(font);
        singer.setStyle("-fx-text-fill: #a4a4a4;");
        singer.setLayoutX(5*width/8);
        singer.setLayoutY(10);
        Label duration = new Label("时长");
        duration.setFont(font);
        duration.setStyle("-fx-text-fill: #a4a4a4;");
        duration.setLayoutX(7*width/8);
        duration.setLayoutY(10);
        titlePane.getChildren().addAll(songName,singer,duration);
    }

    private void addSong(Song[] songs){
        for(int i = 0;i<songs.length;i++) {
            SongPane songPane = new SongPane(i+1, songs[i],this);
            songPane.setLayoutX(0);
            songPane.setLayoutY(titleY+desY+50 + i * 30);
            songPane.setPlayWay(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        player.addMusic(song);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            this.getChildren().add(i+1,songPane);
        }
        length = songs.length;
    }

    private void init(){
        this.setStyle("-fx-background-color: #fafafa;");
        initTitlePane();
        this.getChildren().add(0,titlePane);
        this.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double deltaY = event.getDeltaY();
                double newY = getLayoutY() + deltaY;
                newY = Double.max(height-titleY-length*30-50,newY);
                newY = Double.min(0,newY);
                setLayoutY(newY);
            }
        });
    }

    public void change(Song[] songs){
        setLayoutY(0);
        isLoved = false;
        titleY = 0;
        titlePane.setLayoutY(titleY);
        getChildren().clear();
        getChildren().addAll(titlePane);
        length = 0;
        addSong(songs);
    }

    public void change(Song[] songs,boolean isLoved){
        setLayoutY(0);
        this.isLoved = isLoved;
        titleY = 0;
        titlePane.setLayoutY(titleY);
        getChildren().clear();
        getChildren().addAll(titlePane);
        length = 0;
        addSong(songs);
    }

    public void change(Song[] songs,Playlist playlist){
        setLayoutY(0);
        isLoved = false;
        titleY = 250;
        titlePane.setLayoutY(titleY);
        playlistPane.update(playlist);
        getChildren().clear();
        getChildren().addAll(titlePane,playlistPane);
        length = 0;
        addSong(songs);
    }

    public boolean isLovedSong(Song song){
        return listPane.isLovedSong(song);
    }

    public void addFavSong(Song song){
        listPane.addSong(song);
    }

    public void delFavSong(Song song){
        listPane.delSong(song);
    }

    public void downloadSong(Song song) throws IOException {
        if(song.musicSrc.equals("")){
            song = wyy.getSongDetail(song);
            if(song.musicSrc.equals("")){
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.titleProperty().set("很抱歉");
                alert.setContentText(song.songName+"为相关平台收费歌曲，暂时不提供下载");
                alert.show();
                return;
            }
        }
        URL url = new URL(song.musicSrc);
        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setConnectTimeout(6000);
        urlCon.setReadTimeout(10000);
        int code = urlCon.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.titleProperty().set("很抱歉");
            alert.setContentText(song.songName+"下载失败"+" 作者也不知道发生了什么，@_@");
            alert.show();
            return;
        }
        fileChooser.setTitle("下载歌曲到");
        fileChooser.setInitialFileName(song.songName+".mp3");
        File file = fileChooser.showSaveDialog(stage);
        fileChooser.setInitialDirectory(FileSystemView.getFileSystemView() .getHomeDirectory());
        DataInputStream in = new DataInputStream(urlCon.getInputStream());
        DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
        byte[] buffer = new byte[2048];
        int count = 0;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        out.close();
        in.close();
        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
        _alert.setTitle(song.songName+" 下载成功");
        _alert.setHeaderText("^_^");
        _alert.setContentText("保存路径： "+file.getAbsolutePath());
        _alert.initOwner(stage);
        _alert.show();
    }
}
