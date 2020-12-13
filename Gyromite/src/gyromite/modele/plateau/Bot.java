/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyromite.modele.plateau;

import gyromite.modele.deplacements.Direction;

import java.util.Random;
import java.util.ArrayList;

/**
 * Ennemis (Smicks)
 */
public class Bot extends EntiteDynamique {
    private Random r = new Random();

    public Bot(Jeu _jeu) {
        super(_jeu);
    }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }

    public boolean seDeplacer()
    {
        boolean ret = false;

        r.setSeed((int)(java.lang.Math.random()*49195412));
        int i = r.nextInt()%3;

        Direction d = (i==0?Direction.gauche:(i==1?Direction.droite:Direction.haut));
        Entite cible = regarderDansLaDirection(d);

        // System.out.println(cible==null);
        // System.out.println();

        if(cible!=null && cible instanceof Heros)
        {
            System.out.println("mouahaha j'ai tué Hector !");
            ((Heros)cible).mourir();
        }

        switch(d)
        {
            case gauche:
            case droite:
            {
                Entite eBas = regarderDansLaDirection(Direction.bas);
                if (eBas != null && eBas.peutServirDeSupport()) {
                    if (avancerDirectionChoisie(d))
                        ret = true;
                }
                break;
            }
            case haut:
            {
                // on ne peut pas sauter sans prendre appui
                // (attention, test d'appui réalisé à partir de la position courante, si la gravité à été appliquée, il ne s'agit pas de la position affichée, amélioration possible)
                Entite eBas = regarderDansLaDirection(Direction.bas);
                if (eBas != null && eBas.peutServirDeSupport()) {
                    if (avancerDirectionChoisie(d))
                        ret = true;
                }
                break;
            }
        }

        return ret;
    }
}
