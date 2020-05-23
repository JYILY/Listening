package userPane;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class InfoPane extends Pane {

    InfoPane(String text){
        this.setPrefSize(200,40);

        Label info = new Label(text);

        info.setFont(new Font("Microsoft YaHei",12));
        info.setLayoutX(5);
        info.setLayoutY(10);
        info.setPrefSize(195,10);
        info.setStyle("-fx-text-fill: #000000");
        this.getChildren().add(info);
        this.setLayoutX(0);
        setStyle("-fx-background-color: null;");
    }
}
