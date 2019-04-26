package interfaces;

import java.util.EventListener;
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
}
