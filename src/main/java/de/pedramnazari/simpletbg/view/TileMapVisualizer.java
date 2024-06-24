package de.pedramnazari.simpletbg.view;

import de.pedramnazari.simpletbg.controller.TileMapController;
import de.pedramnazari.simpletbg.model.Hero;
import de.pedramnazari.simpletbg.repository.AllTileMapConfigData;
import de.pedramnazari.simpletbg.model.DefaultTileFactory;
import de.pedramnazari.simpletbg.service.TileMapConfig;
import de.pedramnazari.simpletbg.service.TileMapService;
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
        TileMapConfig mapConfig = AllTileMapConfigData.getMapConfig("2");

        final Hero hero = new Hero(1, 0);
        TileMapService tileMapService = new TileMapService(new DefaultTileFactory(), hero);
        TileMapController controller = new TileMapController(tileMapService);

        final TileMap tileMap = controller.startGameUsingMap(mapConfig);

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
                    case 11:
                        rectangle.setFill(Color.RED);
                        break;
                    default:
                        rectangle.setFill(Color.BLACK);
                        break;
                }

                grid.add(rectangle, x, y);
            }
        }

        // add hero to grid
        final Rectangle heroRectangle = new Rectangle(20, 20, Color.YELLOW);
        grid.add(heroRectangle, hero.getX(), hero.getY());

        Scene scene = new Scene(grid, 800, 600);
        scene.setOnKeyPressed(event -> {
            boolean hasNewPosition = false;
            switch (event.getCode()) {
                case RIGHT:
                    controller.moveHeroToRight();
                    heroRectangle.setX(hero.getX());
                    hasNewPosition = true;

                    break;
                case LEFT:
                    controller.moveHeroToLeft();
                    heroRectangle.setX(hero.getX());
                    hasNewPosition = true;

                    break;
                case DOWN:
                   controller.moveHeroDown();
                    heroRectangle.setY(hero.getY());
                    hasNewPosition = true;

                    break;
                case UP:
                    controller.moveHeroUp();
                    heroRectangle.setY(hero.getY());
                    hasNewPosition = true;

                    break;
            }

            if(hasNewPosition) {
                grid.getChildren().remove(heroRectangle);
                grid.add(heroRectangle, (int) heroRectangle.getX(), (int) heroRectangle.getY());
            }

        });


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}