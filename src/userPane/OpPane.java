package userPane;

import base.Song;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class OpPane extends Pane {

    private ListPane listPane;

    OpPane(Image image, String title){

        ImageView imageView = new ImageView(image);
        Label label = new Label(title);

        this.setPrefSize(200,30);

        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        imageView.setLayoutX(15);
        imageView.setLayoutY(7.5);

        label.setFont(new Font("Microsoft YaHei",12));
        label.setPrefSize(150,30);
        label.setLayoutX(50);
        label.setLayoutY(0);

        this.getChildren().addAll(imageView,label);

        setStyle("-fx-background-color: null;");

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStyle("-fx-background-color: rgb(214, 69, 65)");
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStyle("-fx-background-color: null;");
            }
        });

    }


}
