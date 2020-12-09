package gyromite.modele.plateau;

public class Echelle extends EntiteStatique
{
    public Echelle(Jeu _jeu) {
        super(_jeu);
    }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
}
