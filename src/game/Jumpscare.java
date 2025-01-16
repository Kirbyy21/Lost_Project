/**
 * Classe per implemetare un jumpscare
 * 
* @author Ruben Cortesi
* @version 16.01.2025
*/

package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;

public class Jumpscare {
    private Canvas canvas;
    private GraphicsContext gc;
    private String[] jumpscares = {"images/virus/jump1.png", "images/virus/jump2.png"};
    private int current_frame = 0;
    private Timeline jumpscare_animation;

    public Jumpscare(Stage stage) {
        Pane root = (Pane) stage.getScene().getRoot();
        canvas = new Canvas(stage.getWidth() + 60, stage.getHeight() + 250);
        //canvas.setTranslateX(-325);
        canvas.setTranslateY(-220);
        gc = canvas.getGraphicsContext2D();
        canvas.setVisible(false);
        root.getChildren().add(canvas);
    }

    /**
     * Mostra il jumpscare a schermo
     * @param game tutto il contenuto della finiestra
     */
    public void showJumpscare(Game game) {
        canvas.setVisible(true);
        jumpscare_animation = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            setImage(jumpscares[current_frame]);
            current_frame = 1 - current_frame;
        }));
        jumpscare_animation.setCycleCount(Timeline.INDEFINITE);
        jumpscare_animation.play();
        Timeline stop = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            jumpscare_animation.stop();
            canvas.setVisible(false);
            game.returnToMenu();
        }));
        stop.setCycleCount(1);
        stop.play();
    }

    /**
     * Imposta l'immagine del jumpscare
     * @param path path dell'immagine
     */
    private void setImage(String path) {
        try {
            InputStream imageStream = new FileInputStream(path);
            Image image = new Image(imageStream);
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.drawImage(image, (canvas.getWidth() - image.getWidth()) / 2, (canvas.getHeight() - image.getHeight()) / 2 - 10);
        } 
        catch (FileNotFoundException e) {
            System.err.println("Image file not found: " + e.getMessage());
        }
    }

    /**
     * Restituisce il canvas associato per mostrare il jumpscare
     * @return canvas restituisce il canvas usato
     */
    public Canvas getCanvas() {
        return canvas;
    }
}
