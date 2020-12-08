package gyromite.modele.plateau;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Parse {
  protected File _file;

  public Parse(String filepath) {
    _file = new File(filepath);
  }

  public void readFile() {
    try {
      Scanner scan = new Scanner(_file);
      while (scan.hasNextLine()) {
        String line = scan.nextLine();
        String [] params = line.split(",");

        try {
          int character = Integer.parseInt(params[0]);
          int posX = Integer.parseInt(params[1]);
          int posY = Integer.parseInt(params[2]);

          switch (character) {
            case 0 : //heros
              break;

            case 1 : //mur
              break;

            case 2 : //corde
              break;

            case 3 : //Colonne
              break;

            case 4 : //Bot
              break;

            case 5 : //Bombe
              break;
          }
        }
        catch (NumberFormatException e) {
          System.out.println("an error occured.");
          e.printStackTrace();
        }
      }

      scan.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}
