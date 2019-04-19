package son;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Bruit {

	private Clip musique;
	
	private boolean enCours = false;

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

	public void joueSonLorsqueTouche() {


		int nb = 0 + (int)(Math.random() * ((3 - 0) + 1));

		switch(nb) {

		case 1: 
			joue("scream");
			break;

		case 2: 
			joue("chosenone");
			break;

		case 3: 
			joue("chosenone");
			break;

		}
	}



	public boolean isEnCours() {
		return enCours;
	}



	public void setEnCours(boolean enCours) {
		this.enCours = enCours;
	}





}
