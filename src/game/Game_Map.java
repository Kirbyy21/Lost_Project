/**
 * Classe per inizializzare la mappa delle stanze
 * 
* @author Ruben Cortesi
* @version 13.01.2025
*/

package game;

import java.util.HashMap;
import java.util.Map;

public class Game_Map {
    private static Map<Integer, Room> rooms;
    //private int number_room = Save.loadRoom();
    private static Room current_room;
    private static Room enemy_room;

    public Game_Map() {
        // Hasmap da stack overflow
        // https://stackoverflow.com/questions/68669468/java-adding-an-object-with-two-parameters-to-arraylist
        rooms = new HashMap<>();
        initializeRooms();
    }

    /**
     * Inizializza le stanze nella mappa
     */
    private void initializeRooms() {
        Room room1 = new Room(1, "images/def-back.png");
        Room room2 = new Room(2, "images/door-back-ws.png");
        Room room3 = new Room(3, "images/def-back.png");
        Room room4 = new Room(4, "images/door-back.png");
        Room room5 = new Room(5, "images/def-back.png");
        Room room6 = new Room(6, "images/def-back.png");
        Room room7 = new Room(7, "images/locked.png");
        Room room8 = new Room(8, "images/def-back.png");
        Room room9 = new Room(9, "images/door-back.png");
        Room room10 = new Room(10, "images/door-back.png");
        Room room11 = new Room(11, "images/def-back.png");
        Room room12 = new Room(12, "images/unlocked.png");

        room1.connect("right", room2);
        room2.connect("left", room1);
        room2.connect("right", room3);
        room2.connect("forward", room4);
        room3.connect("left", room2);
        room3.connect("right", room11);
        room4.connect("forward", room2);
        room4.connect("right", room5);
        room5.connect("left", room4);
        room5.connect("right", room6);
        room6.connect("left", room5);
        room6.connect("right", room7);
        room7.connect("left", room6);
        room7.connect("forward", room12);
        room7.connect("right", room8);
        room8.connect("left", room7);
        room8.connect("right", room9);
        room9.connect("left", room8);
        room9.connect("forward", room10);
        room10.connect("forward", room9);
        room10.connect("left", room11);
        room11.connect("right", room10);
        room11.connect("left", room3);
        room12.connect("forward", room7);

        rooms.put(1, room1);
        rooms.put(2, room2);
        rooms.put(3, room3);
        rooms.put(4, room4);
        rooms.put(5, room5);
        rooms.put(6, room6);
        rooms.put(7, room7);
        rooms.put(8, room8);
        rooms.put(9, room9);
        rooms.put(10, room10);
        rooms.put(11, room11);
        rooms.put(12, room12);
    }

    /**
     * Ritorna il numero della stanza
     * @param player controlla se deve prendere la stanza del giocatore del nemico
     * @return il numero della stanza
     */
    public static Room getCurrentRoom(boolean player) {
        if (player) {
            return current_room;
        }
        return enemy_room;
    }

    /**
     * Imposta la nuova stanza
     * @param number_room numero della stanza nuova
     * @param player controlla se deve cambiare la tanza del player o del nemico
     */
    public void setCurrentRoom(int number_room, boolean player) {
        if (player) {
            current_room = rooms.get(number_room);
        }
        else {
            enemy_room = rooms.get(number_room);
        }
  
    }

    /**
     * Muove il player o il nemcio nella nuova stanza
     * @param direction direzione in cui si è mosso
     * @param player controlla se si è mosso il player o il nemico
     * @return il numero attuale della stanza
     */
    public static Room moveRoom(String direction, boolean player) {
        Room next_room;
        if (player) {
            next_room = current_room.getDirection(direction);
            if (next_room != null) {
                current_room = next_room;
            }
            return current_room;
        }
        next_room = enemy_room.getDirection(direction);
        if (next_room != null && next_room.getNumber() != 12) {
            enemy_room = next_room;
        }
        return enemy_room;
    }
}