// StartView.java
package de.pedramnazari.simpletbg.drivers.ui.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class StartView extends Application {

    private static final int SCENE_WIDTH = 1100;
    private static final int SCENE_HEIGHT = 575;

    @Override
    public void start(Stage primaryStage) {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/start_view_background.png")));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(SCENE_WIDTH);
        backgroundImageView.setFitHeight(SCENE_HEIGHT);

        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);

        Button playButton = new Button("Start Game");
        playButton.setStyle("-fx-font-size: 24px; -fx-padding: 10px 50px;");
        playButton.setOnAction(event -> {
            GameWorldVisualizer gameWorldVisualizer = new GameWorldVisualizer();
            try {
                gameWorldVisualizer.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        vbox.getChildren().add(playButton);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView, vbox);

        Scene scene = new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Start Game");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}