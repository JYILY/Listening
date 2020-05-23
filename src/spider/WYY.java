package spider;

import base.Playlist;
import base.Song;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WYY implements Spider {

    private BufferedOutputStream out;
    private BufferedReader in;

    public WYY(){
        try {
            Process process = Runtime.getRuntime().exec("exe/wyy.exe");
            out = new BufferedOutputStream(process.getOutputStream());
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Song[] search(String searchText)throws IOException{
        String s = "7\n"+searchText+"\n";
        out.write(s.getBytes());
        out.flush();
        int songCount = Integer.parseInt(in.readLine());
        Song[] songs = new Song[songCount];
        for(int i = 0;i<songCount;i++){
            songs[i] = new Song();
            songs[i].songName = in.readLine();
            songs[i].imgSrc = in.readLine();
            songs[i].duration = Double.parseDouble(in.readLine());
            songs[i].singer = in.readLine();
            songs[i].songId = in.readLine();
            songs[i].mvId = in.readLine();
        }
        return songs;
    }

    public Playlist[] getOnePagePlaylist(int page) throws IOException {
        String s = "1\n"+page+"\n";
        out.write(s.getBytes());
        out.flush();
        int count = Integer.parseInt(in.readLine());
        Playlist[] playlists = new Playlist[count];
        for(int i = 0;i<count;i++){
            playlists[i] = new Playlist();
            playlists[i].title = in.readLine();
            playlists[i].playlistId = in.readLine();
            playlists[i].imgSrc = in.readLine()+"?param=200y200";
            playlists[i].playCount = Integer.parseInt(in.readLine());
            int len_description = Integer.parseInt(in.readLine());
            for(int j = 0;j<len_description;j++){
                if(playlists[i].description==null)playlists[i].description = in.readLine();
                else playlists[i].description = playlists[i].description + "\n"+ in.readLine();
            }
        }
        return playlists;
    }

    public Playlist[] getTopList() throws IOException {
        out.write("6\n".getBytes());
        out.flush();
        int count = Integer.parseInt(in.readLine());
        Playlist[] playlists = new Playlist[count];
        for(int i = 0;i<count;i++){
            playlists[i] = new Playlist();
            playlists[i].title = in.readLine();
            playlists[i].playlistId = in.readLine();
            playlists[i].imgSrc = in.readLine()+"?param=200y200";
            playlists[i].playCount = Long.parseLong(in.readLine());
            int len_description = Integer.parseInt(in.readLine());
            for(int j = 0;j<len_description;j++){
                if(playlists[i].description==null)playlists[i].description = in.readLine();
                else playlists[i].description = playlists[i].description + "\n"+ in.readLine();
            }
        }
        return playlists;
    }

    public Song[] getPlaylistDetail(String playlistId)throws IOException{
        String s = "2\n"+ playlistId +"\n";
        out.write(s.getBytes());
        out.flush();
        int songCount = Integer.parseInt(in.readLine());
        Song[] songs = new Song[songCount];
        for(int i = 0;i<songCount;i++){
            songs[i] = new Song();
            songs[i].songName = in.readLine();
            songs[i].imgSrc = in.readLine();
            songs[i].duration = Double.parseDouble(in.readLine());
            songs[i].singer = in.readLine();
            songs[i].songId = in.readLine();
            songs[i].mvId = in.readLine();
        }
        return songs;
    }

    public Song getSongDetail(Song song)throws IOException{
        String s = "5\n" + song.songId + "\n";
        out.write(s.getBytes());
        out.flush();
        int lineCount = Integer.parseInt(in.readLine());
        String[] lyrics = new String[lineCount];
        for(int i=0;i<lineCount;i++){
            lyrics[i] = in.readLine();
        }
        song.lyrics = lyrics;
        s = "3\n" + song.songId +"\n";
        out.write(s.getBytes());
        out.flush();
        song.musicSrc = in.readLine();
        if(!song.mvId.equals("0")){
            s = "4\n"+song.mvId+"\n";
            out.write(s.getBytes());
            out.flush();
            song.mvSrc = in.readLine();
        }
        return song;
    }

    public String getName(){ return "网易云音乐"; }

    public void exit() throws IOException {
        out.write("99\n".getBytes());
        out.flush();
    }
}
