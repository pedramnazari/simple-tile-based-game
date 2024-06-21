package de.pedramnazari.simpletbg.view;

import de.pedramnazari.simpletbg.repository.AllTileMapConfigData;
import de.pedramnazari.simpletbg.service.TileMapConfig;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import de.pedramnazari.simpletbg.model.TileMap;
import de.pedramnazari.simpletbg.model.Tile;

public class TileMapVisualizer extends Application {

    @Override
    public void start(Stage primaryStage) {
        TileMapConfig mapConfig = AllTileMapConfigData.getMapConfig("1");
        final TileMap tileMap = new TileMap(mapConfig.getMapId(), mapConfig.getMap());


        GridPane grid = new GridPane();

        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                Tile tile = tileMap.getTile(x, y);
                Rectangle rectangle = new Rectangle(20, 20);

                // Set color based on tile type
                switch (tile.getType()) {
                    case 0:
                        rectangle.setFill(Color.BLUE);
                        break;
                    case 1:
                        rectangle.setFill(Color.GRAY);
                        break;
                    // Add more cases as needed
                    default:
                        rectangle.setFill(Color.BLACK);
                        break;
                }

                grid.add(rectangle, x, y);
            }
        }

        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}