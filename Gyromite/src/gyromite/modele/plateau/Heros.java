/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyromite.modele.plateau;

/**
 * Héros du jeu
 */
public class Heros extends EntiteDynamique {
    private boolean estMort = false;

    public Heros(Jeu _jeu) {
        super(_jeu);
    }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; };

    public boolean estMort() { return estMort; }
    public void mourir() { estMort = true; }
}
