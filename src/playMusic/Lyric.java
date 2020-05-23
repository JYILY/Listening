package playMusic;


import javafx.util.Duration;


public class Lyric {

    public Duration duration;
    public String text ;


    private Lyric(Duration duration1, String lyric1){
        this.duration = duration1;
        this.text = lyric1;
    }

    public static Lyric process(String lyric) throws Exception{
        String[] dl = lyric.split("]");
        String[] time = dl[0].split(":");
        double minute = Double.parseDouble(time[0].substring(1,3));
        double seconds = Double.parseDouble(time[1]);
        Duration duration = new Duration(1000*seconds+60000*minute);
        StringBuilder sumLyric = new StringBuilder();
        for(int i = 1;i<dl.length;i++){
            sumLyric.append(dl[i]);
        }
        String ly = sumLyric.toString();
        return new Lyric(duration,ly);
    }


}
