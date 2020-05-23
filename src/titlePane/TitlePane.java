package titlePane;

import ShowSongList.PlaylistPane;
import ShowSongList.SongListPane;
import base.Song;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import spider.WYY;

import java.io.IOException;

public class TitlePane extends Pane {

        private ImageView logo, close, minimize, search;
        private TextField searchTextField;
        private EventHandler<MouseEvent> enter = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                        setCursor(Cursor.HAND);
                }
        };
        private EventHandler<MouseEvent> exit = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                        setCursor(Cursor.DEFAULT);
                }
        };
        private WYY wyy;
        private SongListPane songListPane;
        private Stage stage,mainStage;
        Scene scene2;

        public TitlePane(WYY wyy, SongListPane songListPane, Stage stage) {

                this.wyy = wyy;
                this.songListPane = songListPane;
                this.stage = stage;
                this.setPrefSize(1000, 50);
                this.setStyle("-fx-background-color: #c73c3c");

                initLogo();
                initClose();
                initMinimize();
                initSearch();
                initSearchTextField();

                this.getChildren().addAll(logo,close,minimize,searchTextField,search);
        }

        private void initLogo(){
                logo = new ImageView(new Image(getClass().getResource("image/logo.png").toExternalForm()));
                logo.setFitWidth(130);
                logo.setFitHeight(40);
                logo.setLayoutX(40);
                logo.setLayoutY(5);

                logo.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {

                        }
                });
                logo.setOnMouseEntered(enter);
                logo.setOnMouseExited(exit);
        }

        private void initClose(){
                close = new ImageView(new Image(getClass().getResource("image/close.png").toExternalForm()));
                close.setFitWidth(20);
                close.setFitHeight(20);
                close.setLayoutX(950);
                close.setLayoutY(15);

                close.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                                stage.close();
                        }
                });
                close.setOnMouseEntered(enter);
                close.setOnMouseExited(exit);
        }

        private void initMinimize(){
                minimize = new ImageView(new Image(getClass().getResource("image/minimize.png").toExternalForm()));
                minimize.setFitWidth(30);
                minimize.setFitHeight(20);
                minimize.setLayoutX(900);
                minimize.setLayoutY(15);

                minimize.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                                stage.setIconified(true);
                        }
                });
                minimize.setOnMouseEntered(enter);
                minimize.setOnMouseExited(exit);
        }

        private void initSearch(){
                search = new ImageView(new Image(getClass().getResource("image/search.png").toExternalForm()));
                search.setFitWidth(18);
                search.setFitHeight(18);
                search.setLayoutX(455);
                search.setLayoutY(15);

                search.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                                try {
                                        doSearch();
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
                });
                search.setOnMouseEntered(enter);
                search.setOnMouseExited(exit);
        }

        private void initSearchTextField(){
                searchTextField = new TextField();
                searchTextField.setPromptText("快来搜索音乐鸭");
//                searchTextField.setStyle(" -fx-background-color: #a9a9a9 , white , white;");
                searchTextField.setStyle("-fx-background-radius: 32px;-fx-text-inner-color: #000000;-fx-background-color: #a82828;-fx-text-fill:#ffffff");
                searchTextField.setPrefSize(200,20);
                searchTextField.setLayoutX(280);
                searchTextField.setLayoutY(12.5);

                searchTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                                if(event.getCode()== KeyCode.ENTER){
                                        try {
                                                doSearch();
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        }
                                }
                        }
                });
        }

        private void doSearch() throws IOException {
                Song[] songs = wyy.search(searchTextField.getText());
                songListPane.change(songs);
                mainStage.setScene(scene2);
        }

        public void setPP(Stage mainStage, Scene s2){
                this.mainStage =mainStage;
                this.scene2 = s2;
        }

        public void setOnClose(EventHandler<MouseEvent> closeRequest){
                close.setOnMouseClicked(closeRequest);
        }
}
