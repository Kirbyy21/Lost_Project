/**
 * Classe per gestire il nemico principale del gioco
 * 
* @author Ruben Cortesi
* @version 16.01.2025
*/

package game;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.Random;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Virus {
    public static int x_pos;
    private int y_pos = 590 / 2 - 130;
    private boolean is_moving = false;
    public static boolean is_chasing = false;
    private Timeline move_animation;
    private String[] walk_frames = {"images/virus/walk1.png", "images/virus/walk2.png"};
    private ImageView imageView;
    Random rand = new Random();
    int current_frame = 0;
    private Room enemy_room;
    private int spawn_room;
    public static boolean isleft = false;
    private double speed = 0;
    private int diff_room = -1;
    Game game;
    private boolean chase_left;
    public static boolean can_move = false;
    private int num_rand;
    private Game_Map game_map = new Game_Map();
    private boolean swap_move = true;
    private int change_time = 0;
    private Jumpscare jumpscare;
    private Sound sound;

    public Virus(Game game) {
        this.game = game;
        x_pos = (rand.nextInt(720/2) + 20) * 1;
        imageView  = new ImageView();
        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setTranslateX(x_pos);
        imageView.setTranslateY(y_pos);
        setImage("images/virus/losing-it.png");
        num_rand = rand.nextInt(3);
        if (num_rand == 2) {
            spawn_room = 3;
        }
        else {
            spawn_room = 11;
        }
        game_map.setCurrentRoom(spawn_room, false);
        enemy_room = Game_Map.getCurrentRoom(false);
        startVirus();
    }

    /**
     *  Ritorna l'immagine allegata
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Muove il nemico controllando se deve seguire il player o si muove a caso
    */
    private void startVirus() {
        move_animation = new Timeline(new KeyFrame(Duration.millis(300), event -> {
            if (can_move) {
                if (!is_chasing) {
                    moveRandom();
                    speed = 0;
                }
                else {
                    chasePlayer();
                }  
            }

        }));
        move_animation.setCycleCount(Timeline.INDEFINITE);
        move_animation.play();
    }

    /**
     *  Funzione chiamata quando il nemico si deve muovere a caso
     */
    private void moveRandom() {
        double direction = Math.random();
        current_frame = 1 - current_frame;
        if (swap_move) {
            x_pos += 7.5;
            isleft = false;
            changeRoom();
        }
        else {
            isleft = true;
            x_pos -= 7.5;
            changeRoom();
        }
        change_time += 10;
        if (change_time == 120) {
            if (direction < 0.5) {
                swap_move = true;
            }
            else {
                swap_move = false;
            }
        }
        imageView.setTranslateX(x_pos);
        if (Game.current_room.getNumber() == getEnemyNumber()) {
            setImage(walk_frames[current_frame]);
        }
        else {
            setImage("images/virus/losing-it.png");
        }
    }

    /**
     * Funzione chiamata quando il nemico deve inseguire il Player
     */
    private void chasePlayer() {
        if (!chase_left) {
            x_pos = x_pos + 40 - (int) speed;
            changeRoom();
        }
        else {
            x_pos = x_pos - 40 + (int) speed;
            changeRoom();
        }
        speed += 0.1;
        if (speed >= 5) {
            is_chasing = false;
        }
        current_frame = 1 - current_frame;
        imageView.setTranslateX(x_pos);
        if (Game.current_room.getNumber() == getEnemyNumber()) {
            setImage(walk_frames[current_frame]);
        }
        else {
            setImage("images/virus/losing-it.png");
        }
    }

    /**
     * Controlla dov'e il nemico e se deve cambiare stanza, esegue il controllo se ha preso il player e deve finire il gioco
     */
     private void changeRoom() {
        if (sameRoom() && sameDirection() && can_move) {
            startChase();
        }
        int different_room = enemy_room.getNumber();
        if (x_pos > 760/2) {
            enemy_room = Game_Map.moveRoom("right", false);
            if (different_room != enemy_room.getNumber()) {
                x_pos = -750/2;
            }
            else {
                swap_move = false;
                change_time = 0;
            }
        }
        if (x_pos < -760/2) {
            enemy_room = Game_Map.moveRoom("left", false);
            if (different_room != enemy_room.getNumber()) {
                x_pos = 750/2;
            }
            else {
                swap_move = true;
                change_time = 0;
            }
        }
        if (x_pos >= -64 && x_pos <= 64) {
            if (is_chasing) {
                Room forwardRoom = Game_Map.moveRoom("forward", false);
                if (forwardRoom.getNumber() == Game.current_room.getNumber()) {
                    enemy_room = forwardRoom;
                }
            }
            if (enemy_room.getNumber() != diff_room && !is_chasing) {
                enemy_room = Game_Map.moveRoom("forward", false);
                diff_room = enemy_room.getNumber();
            }
        }
        if (Game.x_pos <= x_pos  + 95 && Game.x_pos >= x_pos  - 95) {
            if (Game.current_room.getNumber() == getEnemyNumber()) {
                sound = new Sound();
                sound.play("sounds/jump.wav");
                jumpscare = new Jumpscare(Game.primaryStage);
                jumpscare.showJumpscare(game);
                is_chasing = false;
                can_move = false;
                x_pos = (rand.nextInt(720/2) + 20) * 1;
                num_rand = rand.nextInt(3);
                if (num_rand == 2) {
                    spawn_room = 3;
                }
                else {
                    spawn_room = 11;
                }
                game_map.setCurrentRoom(spawn_room, false);
                enemy_room = Game_Map.getCurrentRoom(false);
                speed = 0;
            }
        }
        if (Code.verifeir.containsKey(getEnemyNumber())) {
            imageView.setTranslateY(590 / 2 - 160);
        }
        else {
            imageView.setTranslateY(590 / 2 - 130);
        }
    }

    /**
     * Imposta l'immagine che deve assumere il nemico
     * @param path path dell'immagine
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
     *  Attiva l'inseguimento
     */
    public void startChase() {
        is_chasing = true;
    }

    /**
     * ritorna il numero della stanza in cui è il nemico
     * @return
     */
    public int getEnemyNumber() {
        return enemy_room.getNumber();
    }

    /**
     * Controlla se sono nelle stessa stanza
     * @return true se il player e il nemico sono nella stessa stanza
     * @return false se sono in stanze differenti
     */
    private boolean sameRoom() {
        if (Game.current_room.getNumber() == getEnemyNumber()) {
            return true;
        }
        return false;
    }

    /**
     * @return true se il nemico sta andando nella stessa direzione in cui si trova il player
     * @return false se il nemico sta andando nella direzione opposta al player
     */
    private boolean sameDirection() {
        if (!isleft && x_pos < Game.x_pos) {
            chase_left = false;
            return true;
        }
        if (isleft && x_pos > Game.x_pos) {
            chase_left = true;
            return true;
        }
        if (!isleft && x_pos > Game.x_pos - 100) {
            isleft = true;
            return true;
        }
        if (isleft && x_pos < Game.x_pos + 100) {
            isleft = false;
            return true;
        }
        return false;
    }
 }