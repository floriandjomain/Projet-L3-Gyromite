package gyromite.VueControleur;

import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import gyromite.modele.deplacements.Controle4Directions;
import gyromite.modele.deplacements.Colonne;
import gyromite.modele.deplacements.Direction;
import gyromite.modele.plateau.*;

import java.awt.geom.AffineTransform;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 */
public class VueControleurGyromite extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)
    private int nb_bombes_debut;

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoSmick;
    private ImageIcon icoBombe;
    private ImageIcon icoBonus;
    private ImageIcon icoCorde;
    private ImageIcon[][][] icoColonne = new ImageIcon[2][3][2];

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public VueControleurGyromite(Jeu _jeu) {
        sizeX = jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : Controle4Directions.getInstance().setDirectionCourante(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : Controle4Directions.getInstance().setDirectionCourante(Direction.droite); break;
                    case KeyEvent.VK_DOWN : Controle4Directions.getInstance().setDirectionCourante(Direction.bas); break;
                    case KeyEvent.VK_UP : Controle4Directions.getInstance().setDirectionCourante(Direction.haut); break;
                    case KeyEvent.VK_R : Colonne.getInstance().moveR(); break;
                    case KeyEvent.VK_B : Colonne.getInstance().moveB(); break;
                    case KeyEvent.VK_SPACE : jeu.ramasser(); break;
                }
            }
        });
    }


    private void chargerLesIcones() {
        String path = "res/Images/";
        System.out.println((new java.io.File( "." )).getAbsolutePath());
        icoHero    = chargerIcone(path+"Heros.png");
        icoSmick   = chargerIcone(path+"Smick.png");
        icoVide    = chargerIcone(path+"Vide.png");

        icoColonne[0][0][0] = chargerIcone(path+"ColonneR_top_v.png");
        icoColonne[0][0][1] = chargerIcone(path+"ColonneR_top_h.png");
        icoColonne[0][1][0] = chargerIcone(path+"ColonneR_v.png");
        icoColonne[0][1][1] = chargerIcone(path+"ColonneR_h.png");
        icoColonne[0][2][0] = chargerIcone(path+"ColonneR_bot_v.png");
        icoColonne[0][2][1] = chargerIcone(path+"ColonneR_bot_h.png");
        icoColonne[1][0][0] = chargerIcone(path+"ColonneB_top_v.png");
        icoColonne[1][0][1] = chargerIcone(path+"ColonneB_top_h.png");
        icoColonne[1][1][0] = chargerIcone(path+"ColonneB_v.png");
        icoColonne[1][1][1] = chargerIcone(path+"ColonneB_h.png");
        icoColonne[1][2][0] = chargerIcone(path+"ColonneB_bot_v.png");
        icoColonne[1][2][1] = chargerIcone(path+"ColonneB_bot_h.png");

        icoBombe = chargerIcone(path+"Bombe.png");
        icoBonus = chargerIcone(path+"Bonus.png");
        icoCorde = chargerIcone(path+"Corde.png");
        icoMur   = chargerIcone(path+"Mur.png");
    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurGyromite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Gyromite - ("+jeu.nb_bombes+"/"+nb_bombes_debut+") bombes - "+jeu.points+" points");

        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }


    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage()
    {
        setTitle("Gyromite - ("+jeu.nb_bombes+"/"+jeu.nb_bombes_debut+") bombes - "+jeu.points+" points");

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (jeu.getGrille()[x][y] instanceof Heros) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue
                    // System.out.println("Héros !");
                    tabJLabel[x][y].setIcon(icoHero);
                } else if (jeu.getGrille()[x][y] instanceof Mur) {
                    tabJLabel[x][y].setIcon(icoMur);
                } else if (jeu.getGrille()[x][y] instanceof gyromite.modele.plateau.Colonne) {
                    boolean coul = ((gyromite.modele.plateau.Colonne) jeu.getGrille()[x][y]).estRouge();
                    int pos  = 1;
                    boolean vertical = ((gyromite.modele.plateau.Colonne) jeu.getGrille()[x][y]).estVerticale();

                    if(vertical)
                    {
                        if(y==0 || !(jeu.getGrille()[x][y-1] instanceof gyromite.modele.plateau.Colonne)
                            || ((gyromite.modele.plateau.Colonne) jeu.getGrille()[x][y-1]).estRouge()!=coul) //if top
                            pos--;

                        if(y==sizeY-1 || !(jeu.getGrille()[x][y+1] instanceof gyromite.modele.plateau.Colonne)
                        || ((gyromite.modele.plateau.Colonne) jeu.getGrille()[x][y+1]).estRouge()!=coul)//if bottom
                            pos++;
                    }
                    else
                    {
                        if(x==0 || !(jeu.getGrille()[x-1][y] instanceof gyromite.modele.plateau.Colonne)
                            || ((gyromite.modele.plateau.Colonne) jeu.getGrille()[x-1][y]).estRouge()!=coul) //if right
                            pos++;

                        if(x==sizeX-1 || !(jeu.getGrille()[x+1][y] instanceof gyromite.modele.plateau.Colonne)
                        || ((gyromite.modele.plateau.Colonne) jeu.getGrille()[x+1][y]).estRouge()!=coul)//if left
                            pos--;
                    }

                    tabJLabel[x][y].setIcon(icoColonne[(coul?0:1)][pos][(vertical?0:1)]);
                } else if (jeu.getGrille()[x][y] instanceof Bombe) {
                    tabJLabel[x][y].setIcon(icoBombe);
                } else if (jeu.getGrille()[x][y] instanceof Bonus) {
                    tabJLabel[x][y].setIcon(icoBonus);
                } else if (jeu.getGrille()[x][y] instanceof Bot) {
                    tabJLabel[x][y].setIcon(icoSmick);
                } else if (jeu.getGrille()[x][y] instanceof Corde) {
                    tabJLabel[x][y].setIcon(icoCorde);
                } else {
                    tabJLabel[x][y].setIcon(icoVide);
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(!jeu.finished())
            mettreAJourAffichage();
        else
            System.exit(0);

        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                });
        */

    }
}
