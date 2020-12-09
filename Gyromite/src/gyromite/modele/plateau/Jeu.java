/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyromite.modele.plateau;

import gyromite.modele.deplacements.Colonne;
import gyromite.modele.deplacements.Direction;
import gyromite.modele.deplacements.Controle4Directions;
import gyromite.modele.deplacements.RealisateurDeDeplacement;
import gyromite.modele.deplacements.Ordonnanceur;
import gyromite.modele.deplacements.IA;
import gyromite.modele.deplacements.Gravite;

import java.awt.Point;
import java.util.HashMap;

/** Actuellement, cette classe gère les postions
 */
public class Jeu {

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;

    public static int nb_bombes = 0;
    public static int nb_bombes_debut = 0;
    public static int points = 0;
    public static int current_level = 1;

    private Parse p;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    public Jeu() {
        //p = new Parse(this);
    }

    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }

    public Entite[][] getGrille() {
        return grilleEntites;
    }

    public void setHector(int posX, int posY)
    {
        hector = new Heros(this);
        addEntite(hector, posX, posY);
    }

    public Heros getHector() {
        return hector;
    }

    private void initialisationDesEntites()
    {
        //Parse p = new Parse("res/1level.txt",this);
        //p.readFile();
    }

    public void addEntite(Entite e, int x, int y)
    {
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }

    public void removeEntite(Entite e)
    {
        grilleEntites[map.get(e).x][map.get(e).y] = null;
        map.remove(e);
        ordonnanceur.remove((EntiteDynamique)e);
    }

    /** Permet par exemple a une entité  de percevoir sont environnement proche et de définir sa stratégie de déplacement
     *
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }

    /** Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = false;

        Point pCourant = map.get(e);

        Point pCible = calculerPointCible(pCourant, d);

        if (contenuDansGrille(pCible) && (objetALaPosition(pCible) == null || objetALaPosition(pCible).peutPermettreDeMonterDescendre())) { // a adapter (collisions murs, etc.)
            // compter le déplacement : 1 deplacement horizontal et vertical max par pas de temps par entité
            switch (d) {
                case bas:
                    if (cmptDeplV.get(e) == null) {
                        cmptDeplV.put(e, 1);

                        retour = true;
                    }
                    break;
                case haut:
                    if (cmptDeplV.get(e) == null) {
                        cmptDeplV.put(e, 1);

                        retour = true;
                    }
                    break;
                case gauche:
                    if (cmptDeplH.get(e) == null) {
                        cmptDeplH.put(e, 1);
                        retour = true;

                    }
                    break;
                case droite:
                    if (cmptDeplH.get(e) == null) {
                        cmptDeplH.put(e, 1);
                        retour = true;

                    }
                    break;
            }
        }

        if (retour) {
            deplacerEntite(pCourant, pCible, e);
        }

        return retour;
    }

    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;

        switch(d) {
            case haut: pCible = new Point(pCourant.x, pCourant.y - 1); break;
            case bas : pCible = new Point(pCourant.x, pCourant.y + 1); break;
            case gauche : pCible = new Point(pCourant.x - 1, pCourant.y); break;
            case droite : pCible = new Point(pCourant.x + 1, pCourant.y); break;

        }

        return pCible;
    }

    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        Entite bg = grilleEntites[pCible.x][pCible.y];
        grilleEntites[pCourant.x][pCourant.y] = e.background;
        e.background = bg;
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }

    /** Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    private Entite objetALaPosition(Point p) {
        Entite retour = null;

        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }

        return retour;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }

    public void ramasser()
    {
        int x = map.get(hector).x;
        int y = map.get(hector).y;

        Entite e;

        boolean aRamasseObj = false;

        for(int i=-1; i<2; i++)
            for(int j=-1; j<2; j++)
            {
                if(!aRamasseObj)
                {
                    e = objetALaPosition(new Point(x+i,y+j));

                    if(e!=null && (e instanceof Bombe || e instanceof Bonus))
                    {
                        removeEntite(e);
                        if(e instanceof Bombe)
                        {
                            points+=100;
                            nb_bombes--;
                        }
                        else
                        {
                            points+=200;
                        }
                        aRamasseObj = true;
                    }
                }
            }
    }

    public boolean finished()
    {
        return nb_bombes==0 || hector.estMort();
    }

    public boolean loadLevel(int level) {
      grilleEntites = new Entite[SIZE_X][SIZE_Y];
      // map = new HashMap<Entite, Point>();
      ordonnanceur.clear();
      p = new Parse(this,"./res/"+level+"level.txt");
      return p.readFile();
    }
}
