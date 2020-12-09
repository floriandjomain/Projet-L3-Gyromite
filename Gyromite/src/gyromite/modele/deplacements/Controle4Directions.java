package gyromite.modele.deplacements;

import gyromite.modele.plateau.Entite;
import gyromite.modele.plateau.EntiteDynamique;
import gyromite.modele.plateau.Heros;
import gyromite.modele.plateau.Bot;

import java.awt.Point;
import gyromite.modele.plateau.Jeu;
/**
 * Controle4Directions permet d'appliquer une direction (connexion avec le clavier) à un ensemble d'entités dynamiques
 */
public class Controle4Directions extends RealisateurDeDeplacement {
    private Direction directionCourante;
    // Design pattern singleton
    private static Controle4Directions c3d;

    public static Controle4Directions getInstance() {
        if (c3d == null) {
            c3d = new Controle4Directions();
        }
        return c3d;
    }

    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (directionCourante != null)
            {
                if(e instanceof Heros && e.regarderDansLaDirection(directionCourante) instanceof Bot)
                {
                    ((Heros) e).mourir();
                }

                switch (directionCourante) {
                    case gauche:
                    case droite:
                        if (e.avancerDirectionChoisie(directionCourante))
                            ret = true;
                        break;

                    case haut:
                        // on ne peut pas sauter sans prendre appui
                        // (attention, test d'appui réalisé à partir de la position courante, si la gravité à été appliquée, il ne s'agit pas de la position affichée, amélioration possible)
                        Entite eBas = e.regarderDansLaDirection(Direction.bas);
                        if (eBas != null && eBas.peutServirDeSupport()) {
                            if (e.avancerDirectionChoisie(Direction.haut))
                                ret = true;
                        }
                        break;
                }
            }
        }

        return ret;

    }

    public void resetDirection() {
        directionCourante = null;
    }
}
