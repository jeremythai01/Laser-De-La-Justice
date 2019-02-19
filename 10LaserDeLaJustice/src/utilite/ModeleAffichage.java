package utilite;

import java.awt.geom.AffineTransform;

/**
 * Un objet ModeleAffichage permet de mémoriser un ensemble de valeurs pour passer du monde réel vers un composant de dessin dont les 
 * coordonnées sont en pixels. On peut interroger l'objet pour connaitre la matrice de transformation (qui contient un scale), le nombre de pixels par unité, 
 * les dimensions dans le monde réel, etc.
 * 
 * @author Caroline Houle
 */
 

public class ModeleAffichage {
	private double hautUnitesReelles = -1;
	private double largUnitesReelles;
	private double largPixels;
	private double hautPixels;
	private double pixelsParUniteX;
	private double pixelsParUniteY;
	private AffineTransform matMC;
	
	
	/**
	 * Permet de créer un objet ModeleAffichage, pouvant mémoriser la matrice (et autres valeurs) de transformation pour passer du monde vers le composant. La largeur du monde 
	 * réel est passée en paramètre. La hauteur sera  calculée de façon à n'introduire aucune distortion.
	 * 
	 * @param largPixels La largeur du composant, en pixels
	 * @param hautPixels La hauteur du composant, en pixels
	 * @param largeurDuMonde La largeur du monde, en unitées réelles
	 */
	public ModeleAffichage(double largPixels, double hautPixels, double largeurDuMonde) {
		
		this.largPixels = largPixels;
		this.hautPixels = hautPixels;
		this.largUnitesReelles = largeurDuMonde;
			
		//on calcule la hauteur correspondante pour éviter toute distortion
		this.hautUnitesReelles = largUnitesReelles * hautPixels/largPixels;
		        
		//calcul du nombre de pixels qu'on aura par unité réelle
		this.pixelsParUniteX = largPixels / largUnitesReelles;
		this.pixelsParUniteY = hautPixels / hautUnitesReelles ;

		//formation de la matrice monde-vers-composant
		AffineTransform mat = new AffineTransform();  //on part d'une matrice identite
		mat.scale( pixelsParUniteX, pixelsParUniteY ); 
		
		//on mémorise la matrice (qui pourra être retournée via le getter associé)
		this.matMC = mat; 
		
	}//fin constructeur

	

	
	/**
	 * Retourne une copie de la matrice monde-vers-composant qui a été calculée dans le constructeur
	 * @return La matrice monde-vers-composant
	 */
	public AffineTransform getMatMC() {
		//on décide de retourner une copie de celle qui est mémorisée, pour éviter qu'elle soit modifiée
		return new AffineTransform (matMC);
	}
	
	/**
	 * Retourne la hauteur du monde, en unités réelles
	 * @return La hauteur du monde, en unités réelles
	 */
	public double getHautUnitesReelles() {
		return hautUnitesReelles;
	}

	/**
	 * Retourne la largeur du monde, en unités réelles
	 * @return La largeur du monde, en unités réelles
	 */
	public double getLargUnitesReelles() {
		return largUnitesReelles;
	}

	/**
	 * Retourne le nombre de pixels contenus dans une unité du monde réel, en x
	 * @return Le nombre de pixels contenus dans une unité du monde réel, en x
	 */
	public double getPixelsParUniteX() {
		return pixelsParUniteX;
	}

	/**
	 * Retourne le nombre de pixels contenus dans une unité du monde réel, en y
	 * @return Le nombre de pixels contenus dans une unité du monde réel, en y
	 */
	public double getPixelsParUniteY() {
		return pixelsParUniteY;
	}

	/**
	 * Retourne la largeur en pixels du composant auquel s'appliquera la transformation 
	 * @return La largeur en pixels 
	 */
	public double getLargPixels() {
		return largPixels;
	}

	/**
	 * Retourne la hauteur en pixels du composant auquel s'appliquera la transformation 
	 * @return La hauteur en pixels 
	 */
	public double getHautPixels() {
		return hautPixels;
	}


}//fin classe
