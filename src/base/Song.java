package base;

public class Song {
    public String songName = "";
    public String singer = "";
    public double duration ;   //毫秒级
    public String[] lyrics;   //歌词
    public String imgSrc= "";     //歌曲的图片资源
    public String songId= "";     //歌曲的身份标识
    public String mvId= "";       //如果等于0说明没有mv
    public String musicSrc= "";   //音乐资源链接
    public String mvSrc= "";      //MV资源链接
    public Boolean isLoved = false;   //是否喜欢

    @Override
    public String toString(){
        return String.format("songName: %s\nsinger: %s\n", this.songName, this.singer);
    }

    public String normalDuration(){
        return String.format("%02d:%02d",(int)this.duration/60000,((int)this.duration/1000)%60);
    }
}
