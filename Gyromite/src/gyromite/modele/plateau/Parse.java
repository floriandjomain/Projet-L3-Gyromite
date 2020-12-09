package gyromite.modele.plateau;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

import gyromite.modele.deplacements.Colonne;
import gyromite.modele.deplacements.Direction;
import gyromite.modele.deplacements.Controle4Directions;
import gyromite.modele.deplacements.RealisateurDeDeplacement;
import gyromite.modele.deplacements.Ordonnanceur;
import gyromite.modele.deplacements.IA;
import gyromite.modele.deplacements.Gravite;

public class Parse {
  protected File _file;
  protected Jeu _jeu;

  public Parse(String filepath, Jeu jeu) {
    _file = new File(filepath);
    _jeu = jeu;
  }

  public void readFile() {
    try {
      Scanner scan = new Scanner(_file);
      Gravite g = new Gravite();
      while (scan.hasNextLine()) {
        String line = scan.nextLine();
        String [] params = line.split(",");

        try {
          int character = Integer.parseInt(params[0]);
          int posX = Integer.parseInt(params[1]);
          int posY = Integer.parseInt(params[2]);

          switch (character) {
            case 0 : //heros
              Heros h = new Heros(_jeu);
              _jeu.addEntite(h, posX, posY);
              Controle4Directions.getInstance().addEntiteDynamique(h);
              g.addEntiteDynamique(h);
              break;

            case 1 : //mur
              _jeu.addEntite(new Mur(_jeu), posX, posY);
              break;

            case 2 : //corde
              _jeu.addEntite(new Corde(_jeu), posX, posY);
              break;

            case 3 : //Colonne bleue
              gyromite.modele.plateau.Colonne cb = new gyromite.modele.plateau.Colonne(_jeu, false);
              _jeu.addEntite(cb, posX, posY);
              Colonne.getInstance().addEntiteDynamique(cb);
              break;

            case 4 : //Colonne rouge
              gyromite.modele.plateau.Colonne cr = new gyromite.modele.plateau.Colonne(_jeu, true);
              _jeu.addEntite(cr, posX, posY);
              Colonne.getInstance().addEntiteDynamique(cr);
              break;

            case 5 : //Bot ; IA
              Bot bot = new Bot(_jeu);
              _jeu.addEntite(bot, posX, posY);
              IA.getInstance().addEntiteDynamique(bot);
              break;

            case 6 : //Bombe : gravité
              Bombe b = new Bombe(_jeu);
              _jeu.addEntite(b, posX, posY);
              g.addEntiteDynamique(b);
              break;
          }
        }
        catch (NumberFormatException e) {
          System.out.println("an error occured.");
          e.printStackTrace();
        }
      }

      //ordonnanceur
      _jeu.getOrdonnanceur().add(Controle4Directions.getInstance());
      _jeu.getOrdonnanceur().add(Colonne.getInstance());
      _jeu.getOrdonnanceur().add(IA.getInstance());
      _jeu.getOrdonnanceur().add(g);

      scan.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}
