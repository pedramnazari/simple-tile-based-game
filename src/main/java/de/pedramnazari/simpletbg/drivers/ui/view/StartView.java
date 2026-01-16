// StartView.java
package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;
import de.pedramnazari.simpletbg.tilemap.config.GameMaps;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class StartView extends Application {

    private static final int SCENE_WIDTH = 1100;
    private static final int SCENE_HEIGHT = 575;

    // Pixel-style button CSS
    private static final String BUTTON_STYLE = 
        "-fx-font-family: 'Courier New', monospace; " +
        "-fx-font-size: 18px; " +
        "-fx-font-weight: bold; " +
        "-fx-background-color: #4a4a4a; " +
        "-fx-text-fill: #FFD700; " +
        "-fx-border-color: #8a8a8a; " +
        "-fx-border-width: 3px; " +
        "-fx-padding: 12px 24px; " +
        "-fx-cursor: hand;";

    private static final String BUTTON_DISABLED_STYLE = 
        "-fx-font-family: 'Courier New', monospace; " +
        "-fx-font-size: 18px; " +
        "-fx-font-weight: bold; " +
        "-fx-background-color: #2a2a2a; " +
        "-fx-text-fill: #666666; " +
        "-fx-border-color: #4a4a4a; " +
        "-fx-border-width: 3px; " +
        "-fx-padding: 12px 24px;";

    private static final String TITLE_STYLE = 
        "-fx-font-family: 'Courier New', monospace; " +
        "-fx-font-size: 48px; " +
        "-fx-font-weight: bold; " +
        "-fx-text-fill: #FFD700; " +
        "-fx-effect: dropshadow(gaussian, black, 10, 0.5, 2, 2);";

    @Override
    public void start(Stage primaryStage) {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/start_view_background.png")));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(SCENE_WIDTH);
        backgroundImageView.setFitHeight(SCENE_HEIGHT);

        VBox vbox = new VBox();
        vbox.setSpacing(25);
        vbox.setAlignment(Pos.CENTER);

        // Game title
        Label titleLabel = new Label("TILE BASED GAME");
        titleLabel.setStyle(TITLE_STYLE);

        // Map selection section (for advanced users / manual selection)
        Label mapSelectionLabel = new Label("Select a map:");
        mapSelectionLabel.setStyle("-fx-font-size: 16px; -fx-font-family: 'Courier New', monospace; -fx-text-fill: white;");

        final ListView<GameMapDefinition> mapListView = new ListView<>();
        mapListView.setItems(FXCollections.observableArrayList(GameMaps.availableMaps()));
        mapListView.setMaxWidth(300);
        mapListView.setPrefHeight(150);
        mapListView.setFocusTraversable(false);  // Prevent stealing focus
        if (!mapListView.getItems().isEmpty()) {
            mapListView.getSelectionModel().selectFirst();
        }

        // Button container
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        // New Game button - starts with a different map
        Button newGameButton = new Button("NEW GAME");
        newGameButton.setStyle(BUTTON_STYLE);
        newGameButton.setOnAction(event -> {
            // Reset GameContext for new game
            GameContext.resetInstance();
            
            // Clear any previous session and select a new map
            GameSessionManager sessionManager = GameSessionManager.getInstance();
            sessionManager.clearSession();
            GameMapDefinition newMap = sessionManager.selectNewGameMap();
            
            startGame(primaryStage, newMap);
        });

        // Resume Game button - only enabled if there's a resumable session
        Button resumeGameButton = new Button("RESUME GAME");
        GameSessionManager sessionManager = GameSessionManager.getInstance();
        boolean canResume = sessionManager.hasResumableSession();
        
        if (canResume) {
            resumeGameButton.setStyle(BUTTON_STYLE);
            resumeGameButton.setOnAction(event -> {
                // Reset GameContext for resumed game
                GameContext.resetInstance();
                
                GameMapDefinition resumeMap = sessionManager.getResumableMap();
                startGame(primaryStage, resumeMap);
            });
        } else {
            resumeGameButton.setStyle(BUTTON_DISABLED_STYLE);
            resumeGameButton.setDisable(true);
        }

        // Start Selected Map button (for manual map selection)
        Button startSelectedButton = new Button("START SELECTED MAP");
        startSelectedButton.setStyle(BUTTON_STYLE);
        startSelectedButton.setOnAction(event -> {
            // Reset GameContext for new game
            GameContext.resetInstance();
            
            GameMapDefinition selectedMap = mapListView.getSelectionModel().getSelectedItem();
            if (selectedMap == null) {
                selectedMap = GameMaps.defaultMap();
            }
            
            // Clear session since this is manual selection
            sessionManager.clearSession();
            startGame(primaryStage, selectedMap);
        });

        buttonBox.getChildren().addAll(newGameButton, resumeGameButton);

        vbox.getChildren().addAll(titleLabel, buttonBox, mapSelectionLabel, mapListView, startSelectedButton);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView, vbox);

        Scene scene = new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tile Based Game");
        
        // Handle window close to properly terminate application
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        
        primaryStage.show();
    }

    /**
     * Starts the game with the specified map.
     */
    private void startGame(Stage primaryStage, GameMapDefinition mapDefinition) {
        GameWorldVisualizer gameWorldVisualizer = new GameWorldVisualizer();
        gameWorldVisualizer.setMapDefinition(mapDefinition);
        try {
            gameWorldVisualizer.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
