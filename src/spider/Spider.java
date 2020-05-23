package spider;

import base.Playlist;
import base.Song;

import java.io.IOException;

public interface Spider {
    public Song[] search(String searchText)throws IOException;
    //获取一页歌单
    public Playlist[] getOnePagePlaylist(int page) throws IOException;
    //获取排行榜信息
    public Playlist[] getTopList() throws IOException;
    //获取歌单里的所有歌曲信息
    public Song[] getPlaylistDetail(String playlistId)throws IOException;
    //获取歌曲的所有信息
    public Song getSongDetail(Song song)throws IOException;
    //结束爬虫程序
    public void exit() throws IOException;
}
