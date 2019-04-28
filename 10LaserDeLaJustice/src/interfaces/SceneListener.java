package interfaces;

import java.util.ArrayList;
import java.util.EventListener;

import physique.Balle;
/**
 * Interface qui contient les evenements
 * @author Miora
 */
public interface SceneListener extends EventListener{
	/**
	 * Cet ecouteur modifie le temps si celui-ci a ete change
	 * @param temps : le nouveau temps
	 */
	public void changementTempsListener(int temps);
	public void modeScientifiqueListener(ArrayList<Balle> listeBalles, double hauteurMonde);
	public void vitesesMoyenneBalle(ArrayList<Balle> listeBalles);
	
}
