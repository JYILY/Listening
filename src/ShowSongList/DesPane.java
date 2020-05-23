package ShowSongList;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;

public class DesPane extends Pane {
    DesPane(String[] description){
        Label des = new Label();
        String sum = "";
        for(String s:description){
            sum = sum + "\n"+s;
        }
        des.setText(sum);
        des.setMaxWidth(700);
        des.setWrapText(true);
        des.setFont(new Font("Microsoft YaHei",20));
        des.setTextFill(Paint.valueOf("#000000"));
        des.setLayoutX(50);
        this.setPrefWidth(800);
        this.getChildren().addAll(des);
    }
}
