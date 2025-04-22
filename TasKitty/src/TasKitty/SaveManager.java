/* Author: Ayah Alshami
 * CSI 2300
 * April 18 2025
 * This file implements save system which saves files
 * then allows for user to load previous session on startup
 * information is stored locally in serialized file
  */

package TasKitty;

import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE = "petdata.ser"; //location of save data

    // saves pet object to created file using ObjectOutputStream
    public static void savePet(Pet pet) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(pet);
            System.out.println("Pet saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving pet: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // loads previous pet from save file using ObjectInputStream
    public static Pet loadPet() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("No save file found, creating new pet");
            return new TabbyCat("TaskCat"); // default to tabby if no previous save is found
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Pet loadedPet = (Pet) ois.readObject();
            System.out.println("Pet loaded successfully: " + loadedPet.getName());
            return loadedPet;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading pet: " + e.getMessage());
            e.printStackTrace();
            return new TabbyCat("TaskCat"); 
        }
    }
}