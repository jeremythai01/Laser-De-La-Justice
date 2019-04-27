package son;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Classe qui s'occupe d'enregistrer tous les types de sons dans le jeu et de les jouer.
 * @author Jérémy Thai
 */

public class Bruit {

	private Clip musique;


	/**
	 * Méthode qui cherche et fait jouer un son dont le nom du fichier de son est passée en paramètre 
	 * @param str chaine de caractere qui represente le nom du fichier de son
	 */
	public  void joue(String str) {

		try {

			URL url = this.getClass().getClassLoader().getResource(str+".wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Méthode qui cherche et fait jouer une musique dont le nom du fichier de musique est passée en paramètre 
	 * @param str chaine de caractere qui represente le nom du fichier de musique
	 */
	public  void joueMusique(String str) {

		if(musique == null || !musique.isRunning()) {
			try {
				URL url = this.getClass().getClassLoader().getResource(str+".wav");
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
				musique = AudioSystem.getClip();
				musique.open(audioIn);
				musique.start();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}	
		}
	}

	/**
	 * Arrete la musique en cours
	 */
	public void arreter() {
		if( musique.isRunning()) 
			musique.stop();
	}




	/**
	 * Méthode qui fait jouer un son au hasard lorsque le joueur est touché par un obstacle
	 */
	public void joueSonLorsqueTouche() {

		int nb = 0 + (int)(Math.random() * ((3 - 2) + 1));

		switch(nb) {

		case 1: 
			joue("scream");
			break;

		case 2: 
			joue("comeon");
			break;
		}
	}


	/**
	 * Méthode qui fait jouer un son au hasard lorsque le personnage perd toutes ses vies ou le temps est ecoule
	 */
	public void joueSonLorsqueFini() {

		int nb = 0 + (int)(Math.random() * ((4 - 1) + 1));

		switch(nb) {

		case 1: 
			joue("gg");
			break;

		case 2: 
			joue("careless");
			break;

		case 3: 
			joue("chosenone");
			break;


		case 4:
			joue("gameover");
			break;
		}
	}
}
