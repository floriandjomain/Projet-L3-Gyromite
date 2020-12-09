package gyromite.modele.plateau;

public class Colonne extends EntiteDynamique
{
    private boolean estRouge, top, verticale;

    public Colonne(Jeu _jeu, boolean _estRouge, boolean _verticale)
    {
        super(_jeu);
        estRouge = _estRouge;
        verticale = _verticale;
        top = false;
    }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }
    public boolean estRouge() { return estRouge; }
    public boolean top() { return top; }
    public boolean estVerticale() { return verticale; }
    public void topChange() { top=!top; }
}
