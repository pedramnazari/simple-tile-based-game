package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.drivers.GameApplication;
import de.pedramnazari.simpletbg.drivers.GameSession;
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

    private GameApplication gameApplication;

    public StartView() {
    }

    public StartView(GameApplication gameApplication) {
        this.gameApplication = Objects.requireNonNull(gameApplication, "gameApplication");
    }

    @Override
    public void start(Stage primaryStage) {
        if (gameApplication == null) {
            gameApplication = new GameApplication();
        }

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
            GameMapDefinition selectedMap = mapListView.getSelectionModel().getSelectedItem();
            if (selectedMap == null) {
                selectedMap = GameMaps.defaultMap();
            }
            GameSession session = gameApplication.startNewSession(selectedMap);
            launchGame(primaryStage, session);
        });

        Button continueButton = new Button("Continue");
        continueButton.setStyle("-fx-font-size: 18px; -fx-padding: 8px 30px;");
        continueButton.setDisable(!gameApplication.hasSavedGame());
        continueButton.setOnAction(event -> gameApplication.loadMostRecentSession()
                .ifPresent(session -> launchGame(primaryStage, session)));

        vbox.getChildren().addAll(mapSelectionLabel, mapListView, playButton, continueButton);

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

    private void launchGame(Stage primaryStage, GameSession session) {
        GameWorldVisualizer gameWorldVisualizer = new GameWorldVisualizer(gameApplication);
        gameWorldVisualizer.setSession(session);
        try {
            gameWorldVisualizer.start(primaryStage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to launch game", e);
        }
    }
}
