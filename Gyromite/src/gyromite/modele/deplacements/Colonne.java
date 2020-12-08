package gyromite.modele.deplacements;

import gyromite.modele.plateau.EntiteDynamique;
import gyromite.modele.plateau.Entite;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A la reception d'une commande, toutes les cases (EntitesDynamiques) des colonnes se déplacent dans la direction définie
 * (vérifier "collisions" avec le héros)
 */
public class Colonne extends RealisateurDeDeplacement
{
    private boolean top;
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
        top  = false;
        moveR = false;
        moveB = false;
    }

    public boolean realiserDeplacement()
    {
        if(moveR || moveB)
        {
            boolean ret = false;

            ArrayList<gyromite.modele.plateau.Colonne> colonnesADep = new ArrayList<gyromite.modele.plateau.Colonne>();
            Direction d = (top?Direction.bas:Direction.haut);

            for (EntiteDynamique e : lstEntitesDynamiques)
            {
                gyromite.modele.plateau.Colonne c = (gyromite.modele.plateau.Colonne) e;

                if(moveR && c.estRouge() || moveB && !c.estRouge())
                {
                    if(!c.avancerDirectionChoisie(d))
                    {
                        Entite ed = c.regarderDansLaDirection(d);
                        if((ed instanceof gyromite.modele.plateau.Colonne) || ed.peutEtreEcrase())
                        {
                            colonnesADep.add(c);
                            ret = true;
                        }
                    }
                    else
                        ret = true;
                }
            }

            int i=0;

            while(!colonnesADep.isEmpty())
            {
                if(colonnesADep.get(i).avancerDirectionChoisie(d))
                    colonnesADep.remove(i);
                else
                    i=(i+1)%(colonnesADep.size()-1);
            }

            moveR = moveB = false;
            top  = !top;

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
