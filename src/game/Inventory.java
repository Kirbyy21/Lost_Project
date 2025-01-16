package game;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

public class Inventory {
	public static ArrayList<String> items = new ArrayList<>();
	private ImageView imageView;
    private Game game;
    private Scene scene;
    private  Scene previousScene;
    private Stage primarystage;
    private boolean can_close;
	
	public Inventory(Stage primarystage, Game game) {
		this.scene = scene;
        this.game = game;
        this.primarystage = primarystage;
        this.previousScene = primarystage.getScene();
	}
	
    public void openInventory() {
		imageView = new ImageView();
		imageView.setFitWidth(750);
        imageView.setFitHeight(590);
        BorderPane layout = new BorderPane();
		try {
            InputStream imageStream = new FileInputStream("images/pixil-frame-1.png");
            Image image = new Image(imageStream);
            imageView.setImage(image);
            layout.setCenter(imageView);
        }
        catch (FileNotFoundException e) {
            System.err.println("Image file not found: " + e.getMessage());
        }

        Scene inventory_scene = new Scene(layout, 750, 590);
        primarystage.setScene(inventory_scene);
        primarystage.show();

        inventory_scene.setOnKeyReleased(e -> {
           if (e.getCode().toString().equalsIgnoreCase("I")) {
               can_close = true;
           }
        });
        inventory_scene.setOnKeyPressed(event -> {
            if (event.getCode().toString().equalsIgnoreCase("I")) {
                if (can_close) {
                    closeInventory();
                }
            }
        });
    }

    public void closeInventory() {
        Game.can_move = true;
        Game.inventory_open = true;
        primarystage.setScene(previousScene);
    }
}