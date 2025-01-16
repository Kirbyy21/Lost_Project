/**
 * Classe primaria per gestire il menu principale
 * 
 * @author Ruben Cortesi
 * @version 16.01.2025
 *
 * Codice:
 * javafx con l'aiuto di chatGPT
 * https://chatgpt.com
 * 
 * Grafica:
 * Gli sprite sono una mia creazione a parte lo sfondo usato
 * background wallpaper preso e modificato da:
 * https://www.reddit.com/r/blender/comments/1gbf67y/made_a_simple_pbr_for_backrooms_level_0_wallpaper/?show=original
 */
package game;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.animation.PauseTransition;
import java.util.ArrayList;
import javafx.util.Duration;
import java.util.Random;

public class Program extends Application {
	private Stage primaryStage;
    private Room room;
    private ArrayList<Integer> code;
    private int[] collected_code = new int[7];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        gameMenu();
    }

    /**
     * Funzione per gestire il menu principale
     */
    public void gameMenu() {
        VBox txtbox = new VBox(50);
        txtbox.setPadding(new Insets(50));
        txtbox.setAlignment(Pos.CENTER);

        Label title = new Label("Lost");
        title.setTextFill(Color.web("#C49102"));
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(4.0f);
        innerShadow.setOffsetY(4.0f);
        innerShadow.setColor(Color.rgb(0, 0, 0, 0.5));
        title.setEffect(innerShadow);

        Text infoText = new Text("\t\t\tOBJECTIVE\nCollect the code pieces scattered around\n\n\t\t\tWARNING\n\t\tBeware of the creature\n\n\t\t    HOW TO PLAY\n Use AD to move and E to enter rooms");
        infoText.setFill(Color.WHITE);
        infoText.setStyle("-fx-font-size: 24px;");

        Button start_game = new Button("Continue Game");
        Button new_game = new Button("New Game");

        txtbox.getStyleClass().add("vbox");
        start_game.getStyleClass().add("button");
        new_game.getStyleClass().add("button");

        start_game.setOnAction(e -> {
            startGame(primaryStage, true);
        });

        txtbox.getChildren().addAll(title, new_game, start_game);
        Scene scene = new Scene(txtbox, 750, 591);
        scene.getStylesheets().add(getClass().getResource("text_changer.css").toExternalForm());

        new_game.setOnAction(e -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(12));
            pause.setOnFinished(event -> {
                txtbox.getChildren().remove(infoText);
                txtbox.setStyle("-fx-background-color: #ffffff;");
                startGame(primaryStage, false);
            });
            Save.resetData();
            txtbox.getChildren().clear();
            txtbox.setStyle("-fx-background-color: #000000;");
            txtbox.getChildren().add(infoText);
            pause.play();
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Lost");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Funzione per gestire l'inzio del gioco
     * @param primaryStage finestra per l'interfaccia grafica
     * @param newer_game controlla se è stato premuto new game
     */
	public void startGame(Stage primaryStage, boolean newer_game) {
        code = Save.loadCode(newer_game);
        Game_Map game_map = new Game_Map();
        int saved_room = Save.loadRoom();
        game_map.setCurrentRoom(saved_room, true);
        room = Game_Map.getCurrentRoom(true);
        Game game = new Game(primaryStage, this, room, code, newer_game);
    }
}