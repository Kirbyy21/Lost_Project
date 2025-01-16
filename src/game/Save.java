/**
 * Classe per gestire il salvataggio dei dati su file txt,
 * oltre a salvare carica i dati salvati prendendoli dallo stesso file
 * 
* @author Ruben Cortesi
* @version 16.01.2025
*/

package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.ImageView;
import java.util.HashMap;
import java.util.Map;

public class Save {
    public static final String FILE_NAME = "game_data.txt";
    public static boolean isfirst;

    /**
     * Funzione per salvare i dati sul file txt
     * @param room stanza da salvare
     * @param code codice da salvare
     */
    public static void saveData(int room, ArrayList<Integer> code) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write("ROOM = " + room + "\n");
            writer.write("CODE = ");
            for (int i = 0; i < code.size(); i++) {
                writer.write(code.get(i).toString());
                if (i < code.size() - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");
            writer.write("VERIFEIR = ");
            for (Map.Entry<Integer, ImageView> entry : Code.verifeir.entrySet()) {
                int room_2 = entry.getKey();
                ImageView imageView = entry.getValue();
                int imageNumber = Code.getNumber(imageView);
                writer.write(room_2 + ":" + imageNumber);
                if (entry.getKey() != Code.verifeir.size()) {
                    writer.write(",");
                }
            }
            writer.write("\n");
            
        } 
        catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Funzione per prendere la stanza salvata dal file txt
     * @return room ritorna il numero di stanza che era stato salvato
     */
    public static int loadRoom() {
        int room = 7;
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null && line.startsWith("ROOM = ")) {
                    room = Integer.parseInt(line.substring(7).trim());
                }
            } 
            catch (IOException | NumberFormatException e) {
                System.out.println("Error loading room: " + e.getMessage());
            }
        }
        return room;
    }

    /**
     * Funzione per prendere il codice salvato dal file txt, 
     * se è stato premuto new game ne crea uno nuovo
     * @param newer_game controlla se deve creare un nuovo codice
     * @return code ritorna il codice salvato
     */
    public static ArrayList<Integer> loadCode(boolean newer_game) {
        ArrayList<Integer> code = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("CODE = ")) {
                        String codeStr = line.substring(7).trim();
                        if (!codeStr.isEmpty() && newer_game) {
                            String[] parts = codeStr.split(",");
                            for (String part : parts) {
                                part = part.trim();
                                if (!part.isEmpty()) { 
                                    code.add(Integer.parseInt(part));
                                }
                            }
                            isfirst = false;
                            return code;
                        }
                    }
                }
            } 
            catch (IOException | NumberFormatException e) {
                System.out.println("Error loading code: " + e.getMessage());
            }
        }
    
        Random rand = new Random();
        for (int i = 0; i < 7; i++) {
            code.add(rand.nextInt(10));
        }
        isfirst = true;
        return code;
    }

    /**
     * Resetta i dati salvati
     */
    public static void resetData() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write("ROOM = 7\n");
            writer.write("CODE = \n");
            writer.write("VERIFEIR = ");
        } 
        catch (IOException e) {
            System.out.println("Error resetting data: " + e.getMessage());
        }
        Game.code_pieces = 0;
    }
}
