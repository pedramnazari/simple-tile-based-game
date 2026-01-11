// StartView.java
package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;
import de.pedramnazari.simpletbg.tilemap.config.GameMaps;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

        Label mapSelectionLabel = new Label("Select a map:");
        mapSelectionLabel.setStyle("-fx-font-size: 20px;");

        final ListView<GameMapDefinition> mapListView = new ListView<>();
        mapListView.setItems(FXCollections.observableArrayList(GameMaps.availableMaps()));
        mapListView.setMaxWidth(300);
        mapListView.setPrefHeight(200);
        if (!mapListView.getItems().isEmpty()) {
            mapListView.getSelectionModel().selectFirst();
        }

        Button playButton = new Button("Start Game");
        playButton.setStyle("-fx-font-size: 24px; -fx-padding: 10px 50px;");
        playButton.setOnAction(event -> {
            GameWorldVisualizer gameWorldVisualizer = new GameWorldVisualizer();
            GameMapDefinition selectedMap = mapListView.getSelectionModel().getSelectedItem();
            if (selectedMap == null) {
                selectedMap = GameMaps.defaultMap();
            }
            gameWorldVisualizer.setMapDefinition(selectedMap);
            try {
                gameWorldVisualizer.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Label helpHint = new Label("💡 Press 'H' in-game for controls and help");
        helpHint.setStyle("-fx-font-size: 14px; -fx-text-fill: #3498db; -fx-font-weight: bold;");

        vbox.getChildren().addAll(mapSelectionLabel, mapListView, playButton, helpHint);

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
