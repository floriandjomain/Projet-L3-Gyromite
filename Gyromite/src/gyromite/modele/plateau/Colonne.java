package gyromite.modele.plateau;

public class Colonne extends EntiteDynamique
{
    private boolean estRouge;

    public Colonne(Jeu _jeu, boolean _estRouge)
    {
        super(_jeu);
        estRouge = _estRouge;
    }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }
    public boolean estRouge() { return estRouge; }
}
