package gyromite.modele.deplacements;

import gyromite.modele.plateau.Jeu;
import gyromite.modele.plateau.EntiteDynamique;

import java.util.ArrayList;
import java.util.Observable;

import static java.lang.Thread.*;

public class Ordonnanceur extends Observable implements Runnable {
    private Jeu jeu;
    private ArrayList<RealisateurDeDeplacement> lstDeplacements = new ArrayList<RealisateurDeDeplacement>();
    private long pause;
    public void add(RealisateurDeDeplacement deplacement) {
        lstDeplacements.add(deplacement);
    }

    public void remove(EntiteDynamique e)
    {
        for(RealisateurDeDeplacement rd : lstDeplacements)
            rd.remove(e);
    }

    public Ordonnanceur(Jeu _jeu) {
        jeu = _jeu;
    }

    public void start(long _pause) {
        pause = _pause;
        new Thread(this).start();
    }

    @Override
    public void run() {
        boolean update = false;

        while(!jeu.finished()) {
            jeu.resetCmptDepl();
            for (RealisateurDeDeplacement d : lstDeplacements) {
                if (d.realiserDeplacement())
                    update = true;
            }

            Controle4Directions.getInstance().resetDirection();

            if (update) {
                setChanged();
                notifyObservers();
            }

            if(jeu.points>0)
                jeu.points--;

            try {
                sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("partie terminée - "+jeu.points+"points");

        setChanged();
        notifyObservers();
    }
}
