package gyromite.modele.deplacements;

public class IA extends RealisateurDeDeplacement
{
    // Design pattern singleton
    private static IA ia;

    public static IA getInstance()
    {
        if (ia == null) {
            ia = new IA();
        }
        return ia;
    }

    protected boolean realiserDeplacement()
    {
        boolean ret = false;

        for(EntiteDynamique e : lstEntitesDynamiques)
            if((Bot)e.seDeplacer())
                ret = true;

        return ret;
    }
}
