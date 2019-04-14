package interfaces;

import java.util.EventListener;
/**
 * Interface qui contient les evenements
 * @author Jeremy Thai 
 */
public interface SceneListener extends EventListener{
	
	public void couleurLaserListener();
	public void changementTempsListener(double temps);
}
