package gyromite.modele.deplacements;

import gyromite.modele.plateau.Entite;
import gyromite.modele.plateau.EntiteDynamique;
import gyromite.modele.plateau.Heros;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A la reception d'une commande, toutes les cases (EntitesDynamiques) des colonnes se déplacent dans la direction définie
 * (vérifier "collisions" avec le héros)
 */
public class Colonne extends RealisateurDeDeplacement
{
    private boolean moveR;
    private boolean moveB;

    // Design pattern singleton
    private static Colonne gCol;

    public static Colonne getInstance() {
        if (gCol == null) {
            gCol = new Colonne();
        }
        return gCol;
    }

    private Colonne()
    {
        moveR = false;
        moveB = false;
    }

    public boolean realiserDeplacement()
    {
        if(moveR || moveB)
        {
            boolean ret = false;

            ArrayList<gyromite.modele.plateau.Colonne> colonnesDep = new ArrayList<gyromite.modele.plateau.Colonne>();
            Direction d;

            boolean dep_sup;
            do
            {
                dep_sup = false;

                for (EntiteDynamique e : lstEntitesDynamiques)
                {
                    gyromite.modele.plateau.Colonne c = (gyromite.modele.plateau.Colonne) e;
                    d = (c.top()?Direction.bas:Direction.haut);

                    if(!colonnesDep.contains(c) && (moveR && c.estRouge() || moveB && !c.estRouge()))
                    {
                        if(c.avancerDirectionChoisie(d))
                        {
                            dep_sup = true;
                            colonnesDep.add(c);
                            c.topChange();
                        }
                    }
                }
            } while (dep_sup);

            moveR = moveB = false;

            return ret;
        }
        else
            return false;
    }

    public void moveR()
    {
        moveR = true;
    }

    public void moveB()
    {
        moveB = true;
    }
}
