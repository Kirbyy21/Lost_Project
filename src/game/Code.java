/**
 * Classe per gestire lo spargimento del codice nelle stanze
 * 
* @author Ruben Cortesi
* @version 16.01.2025
*/

package game;

import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.*;

public class Code {
    private static final String IMAGE_PATH = "images/numbers/%s.png";
    private ArrayList<Integer> code;
    private int[] rooms = {1,3,4,6,9,10,11};
    private int[] empty_rooms = {2,5,7,8,12};
    public static Map<Integer, ImageView> verifeir;
    public static Map<Image, Integer> map_used;
    private ImageView imageView;
    
    public Code(ArrayList<Integer> code, boolean newer_game) {
        this.code = code;
        verifeir = new HashMap<>();
        map_used = new HashMap<>();
        if (!newer_game) {
            setCode();
        }
        else {
            loadVerifeir();
        }
    }

    /**
     * Se è stato continuao una partita gia salvata prende i codici fal file txt
     */
    private void loadVerifeir() {
        File file = new File(Save.FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("VERIFEIR = ")) {
                        String verifeirStr = line.substring(11).trim();
                        if (!verifeirStr.isEmpty()) {
                            String[] parts = verifeirStr.split(",");
                            for (String part : parts) {
                                String[] roomData = part.split(":");
                                if (roomData.length == 2) {
                                    int roomNumber = Integer.parseInt(roomData[0].trim());
                                    int imageNumber = Integer.parseInt(roomData[1].trim());
                                    imageView = createImageView(imageNumber);
                                    verifeir.put(roomNumber, imageView);
                                    map_used.put(imageView.getImage(), imageNumber);
                                }
                            }
                        }
                    }
                }
            } 
            catch (IOException | NumberFormatException e) {
                System.out.println("Error loading verifeir: " + e.getMessage());
            }
        }
    }

    /**
     * Se è statto premuto new game genera uno nuova mappa dove mettere i numeri sparsi nelle stanze 
     * in modo casuale
     */
    public void setCode() {
        ArrayList<Integer> available_rooms = new ArrayList<>();
        for (int room : rooms) {
            available_rooms.add(room);
        }
        Collections.shuffle(available_rooms);
        for (int i = 0; i < code.size(); i++) {
            int number = code.get(i); 
            int room_number = available_rooms.get(i);
            imageView = createImageView(number);
            verifeir.put(room_number, imageView);
            map_used.put(imageView.getImage(), number);
        }
    }

    /**
     * Crea un oggetto ImageView per visualizzare l'immagine del codice
     * @param number in base al numero prende l'immagine del numero corrispodente
     * @return ritorna l'immagine
     */
    private ImageView createImageView(int number) {
        String imagePath = String.format(IMAGE_PATH, number);
        ImageView imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setTranslateX(-240);
        imageView.setTranslateY(380);
        try {
            InputStream imageStream = new FileInputStream(imagePath);
            Image image = new Image(imageStream);
            imageView.setImage(image);
        } 
        catch (Exception e) {
            System.err.println("Image file not found: " + imagePath);
        }
        return imageView;
    }

    /**
     * Prende e ritorna l'immagine in base al numero
     * @param number numero in base al quale si puo prendere l'immagine allegata
     * @return ritorna l'immagine
     */
    public ImageView getImageView(int number) {
        return verifeir.get(number);
    }

    /**
     * Ritorna il numero in base all'immagine assegnata
     * @param imageView immagine da cui estrapolare il numero
     * @return il numero del codice a cui è stato associato l'immagine
     */
    public static int getNumber(ImageView imageView) {
        return map_used.get(imageView.getImage());
    }
}