/**
 * Classe principale per gestire il gioco, imposta la finestra, 
 * gestisce il player e il suo movimento
 * 
* @author Ruben Cortesi
* @version 16.01.2025
*/

package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Game {
    private boolean moving = false;
    private boolean endgame = false;
    public static boolean can_move = true;
    private ImageView imageView;
    private Timeline animation;
    private int current_frame = 0;
    private int scenex = 750;
    private int sceney = 590;
    public static int x_pos;
    public static boolean inventory_open = true;
	public static Stage primaryStage;
	private Inventory inventory;
	public static Room current_room;
    private Image image;
    private boolean can_change_inv = true;
    private boolean code_entered = false;
    private ArrayList<Integer> code = new ArrayList<>();
    private ArrayList<Integer> entered_code = new ArrayList<>();
    public static int code_pieces;
    private String[] frames = {"images/mc/idle/f1.png", "images/mc/idle/f2.png"};
    private String idleFrame = "images/mc/idle/f1.png";
    private String[] walk_frames = {"images/mc/walk/walk1.png", "images/mc/walk/walk2.png", "images/mc/walk/walk3.png"};
	private Timeline walk_animation;
    private int walk_frames_index = 0;
    public static boolean isleft = false;
    private Virus virus;
    private Program program;
    private Label save_label;
    private Label collect_label;
    private Code code_share;
    private int different_room;

	public Game(Stage primaryStage, Program program, Room current_room, ArrayList<Integer> code, boolean newer_game) {
        this.primaryStage = primaryStage;
        this.program = program;
        this.current_room = current_room;
        this.code = code;
        if (Save.isfirst) {
            x_pos = 0 - scenex / 2 + 150;
        }
        else {
            x_pos = 220;
        }
        virus = new Virus(this);
        Virus.can_move = true;
        code_share = new Code(code, newer_game);
        this.code_pieces = 7 - Code.verifeir.size();
		game_begin();
	}

    /**
     * Funzione per iniziare e strutturare il gioco
     */
    public void game_begin() {
        VBox txtbox = new VBox(20);
        txtbox.setAlignment(Pos.CENTER);

        save_label = new Label("Press E to Save");
        save_label.setVisible(false);
        save_label.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        save_label.setTranslateX(240);
        save_label.setTranslateY(-70);

        collect_label = new Label("Press E to Collect");
        collect_label.setVisible(false);
        collect_label.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        collect_label.setTranslateX(-240);
        collect_label.setTranslateY(-140);
        
        updateBackground(txtbox);

        imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setTranslateX(x_pos);
        imageView.setTranslateY(sceney / 2 + 70);

        txtbox.getChildren().add(imageView);
        txtbox.getChildren().add(virus.getImageView());
        txtbox.getChildren().add(save_label);
        txtbox.getChildren().add(collect_label);

        setImage(idleFrame);

        // Idle Animation
        animation = new Timeline(new KeyFrame(Duration.millis(1200), event -> {
            try {
                if (!moving || !inventory_open) {
                    InputStream imageStream = new FileInputStream(frames[current_frame]);
                    current_frame = 1 - current_frame;
                    Image image = new Image(imageStream);
                    imageView.setImage(image);
                }
            }
            catch (FileNotFoundException e) {
                System.err.println("Image file not found: " + e.getMessage());
            }
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();

        Scene scene = new Scene(txtbox, scenex, sceney);

        // Controlla quale pulsante viene premuto
        scene.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            if ((key == KeyCode.A || key == KeyCode.D) && can_move) {
                if (x_pos >= 220 && x_pos <= 270 && (current_room.getNumber() == 2 || current_room.getNumber() == 7) && !Virus.is_chasing) {
                    save_label.setVisible(true);
                }
                else {
                    save_label.setVisible(false);
                }
                if (x_pos <= -225 && x_pos >= -265 && Code.verifeir.containsKey(current_room.getNumber()) && !Virus.is_chasing) {
                    collect_label.setVisible(true);
                }
                else {
                    collect_label.setVisible(false);
                }
                moving = true;
                different_room = current_room.getNumber();
                if (key == KeyCode.A) {
                    if (x_pos < -764/2) {
                        current_room = Game_Map.moveRoom("left", true);
                        if (different_room != current_room.getNumber()) {
                            x_pos = 764/2;
                        }
                        can_move = false;
                        roomTrasition(txtbox);
                    }
                    if (x_pos < -710/2 && (current_room.getNumber() == 1 || current_room.getNumber() == 4)) {
                        x_pos += 0;
                    }
                    else {
                        x_pos -= 6.5;
                        isleft = true;
                    }
                }
                if (key == KeyCode.D) {
                    if (x_pos > 764/2) {
						current_room = Game_Map.moveRoom("right", true);
                        if (different_room != current_room.getNumber()) {
                            x_pos = -764/2;
                        }
                        can_move = false;
                        roomTrasition(txtbox);
                    }
                    if (x_pos > 710/2 && (current_room.getNumber() == 10 || current_room.getNumber() == 9)) {
                        x_pos -= 0;
                    }
                    else {
                        x_pos += 6.5;
                        isleft = false;
                    }
                }
                imageView.setTranslateX(x_pos);
                walkAnimation(isleft);
            }

			inventory = new Inventory(primaryStage, this);
			if (key == KeyCode.I) {
                if (inventory_open && can_change_inv) {
                    inventory_open = false;
                    inventory.openInventory();
                    can_move = false;
                    can_change_inv = false;
                }
            }

            if (x_pos >= 220 && x_pos <= 270) {
                if (key == KeyCode.E) {
                    if (current_room.getNumber() == 7 || current_room.getNumber() == 2 && !Virus.is_chasing) {
                        Save.saveData(current_room.getNumber(), code);
                    }
                }
            }

            if (x_pos >= -64 && x_pos <= 64) {
                if (key == KeyCode.E) {
                    if (current_room.getNumber() == 7 && !code_entered) {
                        if (code_pieces == 7) {
                            System.out.println("Insert code: ");
                            for (int i = 0; i < code.size(); i++) {
                                System.out.print(code.get(i));
                            }
                            codeVerify(txtbox);
                        }
                    }
                    else
                    {
                        current_room = Game_Map.moveRoom("forward", true);
                        roomTrasition(txtbox);
                    }
                }
            }

            if (x_pos <= -225 && x_pos >= -265) {
                if (key == KeyCode.E) {
                    if (Code.verifeir.containsKey(current_room.getNumber()) && !Virus.is_chasing) {
                        code_pieces += 1;
                        txtbox.getChildren().remove(code_share.getImageView(current_room.getNumber()));
                        Code.verifeir.remove(current_room.getNumber());
                        imageView.setTranslateY(sceney / 2 + 70);
                        collect_label.setVisible(false);
                    }
                }
            }
        });

        scene.setOnKeyReleased(e -> {
            KeyCode key = e.getCode();
            if (key == KeyCode.A || key == KeyCode.D) {
                moving = false;
                stopWalk();
            }

            if (key == KeyCode.I) {
                can_change_inv = true;
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Lost");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Funzione per gestire l'animazione della camminata
     */
    private void walkAnimation(boolean isleft) {
        if (animation != null && animation.getStatus() == Timeline.Status.RUNNING) {
            animation.pause();
        }
        if (walk_animation != null && walk_animation.getStatus() == Timeline.Status.RUNNING) {
            return;
        }
        walk_animation = new Timeline(new KeyFrame(Duration.millis(400), event -> {
            try {
                InputStream walk_imagestream = new FileInputStream(walk_frames[walk_frames_index]);
                Image walk_image = new Image(walk_imagestream);
                imageView.setImage(walk_image);
                walk_frames_index = (walk_frames_index + 1) % walk_frames.length;
                imageView.setScaleX(isleft ? -1 : 1);
            }
            catch (FileNotFoundException e) {
                System.out.println("Image not found: " + e.getMessage());
            }
        }));
        walk_animation.setCycleCount(Timeline.INDEFINITE);
        walk_animation.play();
    }

    /**
     * Ferma l'animazione di camminata
     */
    private void stopWalk() {
        if (walk_animation != null) {
            walk_animation.stop();
        }
        walk_frames_index = 0;
        setImage(idleFrame);
        if (animation != null) {
            animation.play();
        }
    }

    /**
     * Imposta l'immagine da mostrare
     * @param path path dell'immagine da inserire
     * 
     */
    private void setImage(String path) {
        try {
            InputStream imageStream = new FileInputStream(path);
            Image image = new Image(imageStream);
            imageView.setImage(image);
        }
        catch (FileNotFoundException e) {
            System.err.println("Image file not found: " + e.getMessage());
        }
    }

    /**
     * Funzione per cambiare l'immagine di sfondo
     * @param txtBox contenitore a cui cambiare lo sfondo
     * 
     */
    private void updateBackground(VBox txtBox) {
        try {
            InputStream bgStream = new FileInputStream(current_room.getBackgroundImage());
            BackgroundImage bg_image = new BackgroundImage(
                new Image(bgStream),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            Background bg = new Background(bg_image);
            txtBox.setBackground(bg);
    
        } 
        catch (FileNotFoundException e) {
            System.err.println("File image not found: " + e.getMessage());
        }
    }
    
    /**
     * Funzione per fare una trasizione tra il cambio di stanza
     * @param txtBox contenitore a cui applicare la transizione
     * 
     */
    private void roomTrasition(VBox txtBox) {
        if (current_room.getNumber() == 12) {
            returnToMenu();
        }
        if (txtBox.getChildren().contains(code_share.getImageView(different_room))) {
            txtBox.getChildren().remove(code_share.getImageView(different_room));
            imageView.setTranslateY(sceney / 2 + 70);
        }
        if (current_room.getNumber() != 7 && current_room.getNumber() != 12) {
            if (Code.verifeir.containsKey(current_room.getNumber())) {
                txtBox.getChildren().add(0, code_share.getImageView(current_room.getNumber()));
                imageView.setTranslateY(sceney / 2 + 35);
            }
        }
        FadeTransition fade_out = new FadeTransition(Duration.seconds(0.5), txtBox);
        fade_out.setFromValue(1.0);
        fade_out.setToValue(0.0); 
        fade_out.setOnFinished(event -> {
            updateBackground(txtBox);
            FadeTransition fade_in = new FadeTransition(Duration.seconds(0.5), txtBox);
            fade_in.setFromValue(0.0);
            fade_in.setToValue(1.0);
            fade_in.play();
            can_move = true;
        });
        fade_out.play();
    }

    /**
     * Funzione per creare una nuova finestra per gestire l'entrata del codice
     * @param txtBox contenitore
     * 
     */
    private void codeVerify(VBox txtBox) {

        Stage code_stage = new Stage();
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < 10; i++) {
            final int num = i;
            Button numberButton = new Button(String.valueOf(i));
            numberButton.setMinWidth(50);
            numberButton.setOnAction(event -> entered_code.add(num));
            grid.add(numberButton, i % 3, i / 3);
        }
        
        Button enter_button = new Button("Enter");
        enter_button.setOnAction(event -> {
            if (isCodeCorrect()) {
                code_entered = true;
                current_room.setBackgroundImage("images/unlocked.png");
                updateBackground(txtBox);
            }
            else {
                entered_code.clear();
            }
            code_stage.close();
        });
        grid.add(enter_button, 1, 4);
        VBox dialog_box = new VBox(10, grid);
        Scene code_scene = new Scene(dialog_box, 170, 165);
        code_stage.setTitle("");
        code_stage.setScene(code_scene);
        code_stage.show();
        code_stage.setResizable(false);
    }

    /**
     * Funzione per verificare se il codice inserito è quello generato
     * @return true se il codice inserito è corretto
     * @return false se il codice inserito non è corretto
     */
    private boolean isCodeCorrect() {
        if (entered_code.size() != code.size()) {
            return false;
        }
        for (int i = 0; i < entered_code.size(); i++) {
            if (!entered_code.get(i).equals(code.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Funzione per ritornare al menu
     */
    public void returnToMenu() {
        animation.stop();
        walk_animation.stop();
        
        program.gameMenu();
    }
}