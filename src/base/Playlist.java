package base;

public class Playlist {
    public String imgSrc;
    public String title;
    public String playlistId;
    public boolean isLoved = false;
    public long playCount = 0;
    public String description;

    public String normalPlayCount(){
        if(playCount<100000){
            return ""+playCount;
        }
        else{
            double w = playCount/10000.0;
            if(w>10000){
                double y = w/10000.0;
                return String.format("%.2f亿",y);
            }
            else{
                return String.format("%.2f万",w);
            }
        }
    }
}
