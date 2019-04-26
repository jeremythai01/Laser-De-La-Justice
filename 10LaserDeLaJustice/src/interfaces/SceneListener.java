package interfaces;

import java.util.ArrayList;
import java.util.EventListener;

import physique.Balle;
/**
 * Interface qui contient les evenements
 * @author Jeremy Thai 
 */
public interface SceneListener extends EventListener{
	
	public void couleurLaserListener();
	public void changementTempsListener(int temps);
	public void modeScientifiqueListener(ArrayList<Balle> listeBalles, double hauteurMonde);
}
