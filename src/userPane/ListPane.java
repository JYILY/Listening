package userPane;

import ShowSongList.SongListPane;
import base.Playlist;
import base.Song;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import playMusic.PlayListPane;
import playlistPane.TopListPane;
import spider.WYY;

import java.io.*;
import java.util.ArrayList;

public class ListPane extends Pane {

    private EventHandler<MouseEvent> openRank;
    private EventHandler<MouseEvent> openList;
    private EventHandler<MouseEvent> openLove;
    private ArrayList<Song> favSongs = new ArrayList<>();
    private ArrayList<Playlist> favPlaylist = new ArrayList<>();
    public String nowPlaylistId;
    private ArrayList<String> favSongIds = new ArrayList<>();
    private ArrayList<String> favPlaylistIds = new ArrayList<>();
    private double nowHeight;
    private SongListPane songListPane;
    private WYY wyy;
    private Stage mainStage;
    private Scene scene1,scene2,scene3;
    private InfoPane find,myMusic,myFav;
    private OpPane reconList,rank,favSong;
    private TopListPane top;
    private int listNum = 0;

    public ListPane() throws IOException {
        this.setWidth(200);
        this.setHeight(600);

        nowHeight = 0;

        find = new InfoPane("发现");
        find.setLayoutY(nowHeight);
        nowHeight = nowHeight + 40 ;

        reconList = new OpPane(new Image(getClass().getResource("image/music.png").toExternalForm()),"推荐歌单");
        reconList.setOnMouseClicked(openList);
        reconList.setLayoutY(nowHeight);
        nowHeight = nowHeight +30;
        reconList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainStage.setScene(scene1);
            }
        });

        rank = new OpPane(new Image(getClass().getResource("image/music.png").toExternalForm()),"排行榜");
        rank.setOnMouseClicked(openRank);
        rank.setLayoutY(nowHeight);
        nowHeight = nowHeight +30;
        rank.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                top.setPP(mainStage,scene2,songListPane);
                mainStage.setScene(scene3);
            }
        });

        myMusic = new InfoPane("我的音乐");
        myMusic.setLayoutY(nowHeight);
        nowHeight = nowHeight + 40 ;

        favSong = new OpPane(new Image(getClass().getResource("image/love.png").toExternalForm()),"我喜欢的音乐");
        favSong.setOnMouseClicked(openLove);
        favSong.setLayoutY(nowHeight);
        favSong.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int num = favSongs.size();
                Song[] nowSongs = new Song[num];
                int index = 0;
                for(Song song:favSongs){
                    try {
                        nowSongs[index] = wyy.getSongDetail(song);
                        System.out.println(song.mvSrc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    index = index + 1;
                }
                songListPane.change(nowSongs,true);
                mainStage.setScene(scene2);
            }
        });
        nowHeight = nowHeight +30;

        myFav = new InfoPane("我的收藏");
        myFav.setLayoutY(nowHeight);
        nowHeight = nowHeight + 40 ;

        this.getChildren().addAll(find,reconList,rank,myMusic,myFav,favSong);

        loadFavSongs();
        loadFavList();
        int len = favPlaylist.size();
        listNum = len;
        for(int i = 0 ;i<len;i++){
            Playlist playlist = favPlaylist.get(i);
            OpPane onePlaylist = new OpPane(new Image(getClass().getResource("image/playlist.png").toExternalForm()),playlist.title);
            onePlaylist.setLayoutY(nowHeight+i*30);
            onePlaylist.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    nowPlaylistId = playlist.playlistId;
                    try {
                        songListPane.change(wyy.getPlaylistDetail(playlist.playlistId),playlist);
                        mainStage.setScene(scene2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            this.getChildren().add(onePlaylist);
        }

        this.setPrefSize(200,nowHeight+listNum*30);

        this.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double deltaY  = event.getDeltaY();
                double p = 1.0;
                double newY = getLayoutY()+deltaY*p;
                newY = Double.max(600 - nowHeight-listNum*30-60,newY);
                newY = Double.min(0,newY);
                setLayoutY(newY);
            }
        });

    }

    public String getPlaylistId(){
        return nowPlaylistId;
    }

    public void setOpenRank(EventHandler<MouseEvent> openRank){
        this.openRank = openRank;
    }

    public void setOpenList(EventHandler<MouseEvent> openList){
        this.openList = openList;
    }

    private void loadFavList() throws IOException {
        FileReader reader = new FileReader("data/FavLists");
        BufferedReader br = new BufferedReader(reader);
        while (true){
            String imgSrc = br.readLine();
            if(imgSrc== null)break;
            else {
                Playlist playlist = new Playlist();
                playlist.imgSrc = imgSrc;
                playlist.title = br.readLine();
                playlist.playlistId = br.readLine();
                playlist.playCount = Long.parseLong(br.readLine());
                playlist.description = br.readLine();
                String des = br.readLine();
                while (des!=null){
                    playlist.description = playlist.description +"\n"+des;
                    des = br.readLine();
                }
                favPlaylist.add(playlist);
                favPlaylistIds.add(playlist.playlistId);
            }
        }
        reader.close();
        br.close();
    }

    private void loadFavSongs() throws IOException {
        FileReader reader = new FileReader("data/FavSongs");
        BufferedReader br = new BufferedReader(reader);
        while (true){
            String songId = br.readLine();
            if(songId == null)break;
            else {
                Song newSong = new Song();
                newSong.songId = songId;
                newSong.songName = br.readLine();
                newSong.imgSrc = br.readLine();
                newSong.duration = Double.parseDouble(br.readLine());
                newSong.isLoved = true;
                favSongs.add(newSong);
                favSongIds.add(songId);
            }
        }
        br.close();
        reader.close();
    }

    private void saveFavSongs() throws IOException {
        File file = new File("data/FavSongs");
        OutputStreamWriter osw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            osw = new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        };
        for(Song song:favSongs){
            osw.write(song.songId+"\n");
            osw.write(song.songName+"\n");
            osw.write(song.imgSrc+"\n");
            osw.write(song.duration+"\n");
        }
        osw.close();
    }

    private void saveFavPlaylist() throws IOException {
        File file = new File("data/FavLists");
        OutputStreamWriter osw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            osw = new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        };
        for(Playlist playlist:favPlaylist){
            osw.write(playlist.imgSrc+"\n");
            osw.write(playlist.title+"\n");
            osw.write(playlist.playlistId+"\n");
            osw.write(playlist.playCount+"\n");
            osw.write(playlist.description+"\n");
        }
        osw.close();
    }

    public void save() throws IOException {
        saveFavPlaylist();
        saveFavSongs();
    }

    public void addSong(Song song){
        favSongs.add(song);
        favSongIds.add(song.songId);
        if(songListPane.isLoved){
            int num = favSongs.size();
            Song[] nowSongs = new Song[num];
            int index = 0;
            for(Song _song:favSongs){
                nowSongs[index] = _song;
                index = index + 1;
            }
            songListPane.change(nowSongs,true);
            mainStage.setScene(scene2);
        }
    }

    public void delSong(Song song){
//        favSongs.remove(song);
        song.isLoved = true;
        favSongs.remove(song);
        song.isLoved = false;
        favSongIds.remove(song.songId);
        if(songListPane.isLoved) {
            int num = favSongs.size();
            Song[] nowSongs = new Song[num];
            int index = 0;
            for (Song _song : favSongs) {
                nowSongs[index] = _song;
                index = index + 1;
            }
            songListPane.change(nowSongs, true);
            mainStage.setScene(scene2);
        }
    }

    public void addPlaylist(Playlist playlist){
        favPlaylist.add(playlist);
        favPlaylistIds.add(playlist.playlistId);
        OpPane onePlaylist = new OpPane(new Image(getClass().getResource("image/playlist.png").toExternalForm()),playlist.title);
        onePlaylist.setLayoutY(nowHeight+listNum*30);
        listNum = listNum + 1;
        onePlaylist.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                nowPlaylistId = playlist.playlistId;
                try {
                    songListPane.change(wyy.getPlaylistDetail(playlist.playlistId),playlist);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        this.getChildren().add(onePlaylist);
    }

    public void delPlaylist(Playlist playlist){
        favPlaylist.remove(playlist);
        favPlaylistIds.remove(playlist.playlistId);
        getChildren().remove(playlist);
        getChildren().clear();
        refreshLists();
    }

    public boolean isLovedSong(Song song){
        for(String id :favSongIds){
            if(id.equals(song.songId))return true;
        }
        return false;
    }

    public boolean isLovePlaylist(Playlist playlist){
        for(String id :favPlaylistIds){
            if(id.equals(playlist.playlistId))return true;
        }
        return false;
    }

    public Song[] getFavSongs(){
        return (Song[])favSongs.toArray();
    }

    public Playlist[] getFavPlaylist(){
        return (Playlist[])favPlaylist.toArray();
    }

    public void setSongListPane(SongListPane songListPane){
        this.songListPane = songListPane;
    }

    public void setWyy(WYY wyy){
        this.wyy =wyy;
    }

    public void setPP(Stage stage,Scene s1,Scene s2,Scene s3,TopListPane top){
        scene1 = s1;
        scene2 = s2;
        scene3 =s3;
        mainStage = stage;
        this.top = top;
    }

    private void refreshLists(){
        this.getChildren().clear();
        this.getChildren().addAll(find,reconList,rank,myMusic,favSong,myFav);
        int len = favPlaylist.size();
        listNum = len;
        for(int i = 0 ;i<len;i++){
            Playlist playlist = favPlaylist.get(i);
            OpPane onePlaylist = new OpPane(new Image(getClass().getResource("image/playlist.png").toExternalForm()),playlist.title);
            onePlaylist.setLayoutY(nowHeight+i*30);
            onePlaylist.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    nowPlaylistId = playlist.playlistId;
                    try {
                        songListPane.change(wyy.getPlaylistDetail(playlist.playlistId),playlist);
                        mainStage.setScene(scene2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            this.getChildren().add(onePlaylist);
        }
        this.setPrefSize(200,nowHeight+listNum*30);
    }

    private void refreshSongs(){

    }

}
