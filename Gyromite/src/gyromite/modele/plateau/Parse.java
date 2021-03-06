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

  public Parse(Jeu jeu, String filepath) {
    _jeu = jeu;
    _file = new File(filepath);
  }

  public boolean readFile() {
    try {
      Scanner scan = new Scanner(_file);
      Gravite g = new Gravite();
      while (scan.hasNextLine()) {
        String line = scan.nextLine();
        String [] params = line.split(",");

        try {
          char character = params[0].charAt(0);
          int posX = Integer.parseInt(params[1]);
          int posY = Integer.parseInt(params[2]);

          switch (character) {
            case 'h' : //heros
              System.out.println("un héros est arrivé");
              _jeu.setHector(posX,posY);
              Controle4Directions.getInstance().addEntiteDynamique(_jeu.getHector());
              g.addEntiteDynamique(_jeu.getHector());
              break;

            case 'm' : //mur
              _jeu.addEntite(new Mur(_jeu), posX, posY);
              break;

            case 'c' : //corde OU Colonne
              if(params[0].length()==1)
              {
                  _jeu.addEntite(new Corde(_jeu), posX, posY);
              }
              else
              {
                  if(params[0].charAt(1)=='b')
                  {
                      //Colonne bleue
                      System.out.println(params[0]);
                      gyromite.modele.plateau.Colonne cb = new gyromite.modele.plateau.Colonne(_jeu, false, params[0].charAt(2)=='v');
                      _jeu.addEntite(cb, posX, posY);
                      Colonne.getInstance().addEntiteDynamique(cb);
                  }
                  else
                  {
                      //Colonne rouge
                      System.out.println(params[0]);
                      gyromite.modele.plateau.Colonne cr = new gyromite.modele.plateau.Colonne(_jeu, true, params[0].charAt(2)=='v');
                      _jeu.addEntite(cr, posX, posY);
                      Colonne.getInstance().addEntiteDynamique(cr);
                  }
              }
              break;

            case 's' : //Bot ; IA
              Bot bot = new Bot(_jeu);
              _jeu.addEntite(bot, posX, posY);
              IA.getInstance().addEntiteDynamique(bot);
              g.addEntiteDynamique(bot);
              break;

            case 'b' : //Bombe : gravité
              Bombe bombe = new Bombe(_jeu);
              _jeu.addEntite(bombe, posX, posY);
              g.addEntiteDynamique(bombe);
              break;
            case '+' : //Bonus : gravité
              Bonus bonus = new Bonus(_jeu);
              _jeu.addEntite(bonus, posX, posY);
              g.addEntiteDynamique(bonus);
              break;
            default:
              System.out.println("commentaire croisé");
          }
        }
        catch (NumberFormatException e) {
          System.out.println("an error occured.");
          e.printStackTrace();
        }
      }

      //borders
      for (int x = 0; x < _jeu.SIZE_X; x++) {
        _jeu.addEntite(new Mur(_jeu), x, 0);
        _jeu.addEntite(new Mur(_jeu), x, _jeu.SIZE_Y-1);
      }

      for (int y = 1; y < _jeu.SIZE_Y; y++) {
        _jeu.addEntite(new Mur(_jeu), 0, y);
        _jeu.addEntite(new Mur(_jeu), _jeu.SIZE_X-1, y);
      }

      //ordonnanceur
      _jeu.getOrdonnanceur().add(Controle4Directions.getInstance());
      _jeu.getOrdonnanceur().add(Colonne.getInstance());
      _jeu.getOrdonnanceur().add(IA.getInstance());
      _jeu.getOrdonnanceur().add(g);

      _jeu.nb_bombes_debut = _jeu.nb_bombes;
      System.out.println(_jeu.nb_bombes + "/" + _jeu.nb_bombes_debut);
      scan.close();
      return true;
    }
    catch (FileNotFoundException e) {
      System.out.println("Aucune ressource n'a été trouvée pour le niveau suivant.");
      return false;
    }
  }
}
