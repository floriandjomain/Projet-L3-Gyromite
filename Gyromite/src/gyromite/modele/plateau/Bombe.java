package gyromite.modele.plateau;

public class Bombe extends EntiteDynamique
{
    public Bombe(Jeu _jeu) {
        super(_jeu);
        jeu.nb_bombes++;
    }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return true; };
}
