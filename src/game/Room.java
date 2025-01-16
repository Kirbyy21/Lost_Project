/**
 * Classe per implemetare e gestire le stanze
 * 
* @author Ruben Cortesi
* @version 16.01.2025
*/

package game;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private int number;
    private String bg_image;
    public Map<String, Room> connection;

    public Room(int number, String bg_image) {
        this.number = number;
        this.bg_image = bg_image;
        // Hasmap da stack overflow
        // https://stackoverflow.com/questions/68669468/java-adding-an-object-with-two-parameters-to-arraylist
        this.connection = new HashMap<>();
    }

    /**
     * Connette le direzioni con le stanze
     * @param direction direzione della stanza
     * @param room szanza
     */
    public void connect(String direction, Room room) {
        connection.put(direction, room);
    }

    /**
     * Ritorna la direzione 
     * @param direction direzione
     * @return ritorna la direzione
     */
    public Room getDirection(String direction) {
        return connection.get(direction);
    }

    /**
     * ritorna l'immagine di sfondo
     * @return l'immagine di sfondo
     */
    public String getBackgroundImage() {
        return bg_image;
    }

    /**
     * Imposta un nuovo sfondo
     * @param new_bg sfondoo nuovo da impostare
     */
    public void setBackgroundImage(String new_bg) {
        this.bg_image = new_bg;
    } 

    /**
     * Ritorna il numero della stanza
     * @return number ritorna il numero della stanza
     */
    public int getNumber() {
        return number;
    }
}