package interfaces;

public interface MiroirListener {
	/**
	 * Interface qui contient les evenements des miroirs a surveiller
	 * @author Miora
	 */
	
	/**
	 * Ce listener signale si l'angle d'un miroir a été changé
	 * @param angle l'angle du miroir
	 */
	public void changementAngleMiroirListener(int angle);
	
	/**
	 * Ce listener signale si la longueur d'un miroir a été modifiée
	 * @param longueur la longueur
	 */
	public void changementLongueurMiroirListener(int longueur);
	
	/**
	 * Cette methode signale si on desire dessiner un miroir
	 * @param nomMiroir le type de miroir
	 */
	public void dessinerLeMiroirListener(String nomMiroir);

}
