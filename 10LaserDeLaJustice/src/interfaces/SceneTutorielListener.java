package interfaces;

import java.util.EventListener;
/**
 * Interface qui contient les evenements pour le tutoriel
 * @author Arnaud
 *
 */
public interface SceneTutorielListener extends EventListener {

	/**
	 * Cette methode permet de changer les etapes du tutoriel
	 */
	public void changerEtapeTutoriel();
}
