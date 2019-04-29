package aaplication;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import son.Bruit;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.UIManager;

public class FenetreGameOver extends JFrame {

	private JPanel contentPane;
	private Bruit son = new Bruit() ;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetreGameOver frame = new FenetreGameOver();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FenetreGameOver() {
		setTitle("GAME OVER");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		Dimension ecranDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(ecranDimension.width/2-getSize().width/2, ecranDimension.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnRessayer = new JButton("R\u00E9essayer");
		btnRessayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				App10LaserDeLaJustice demarrage = new App10LaserDeLaJustice();
						demarrage.setVisible(true);
						setVisible(false);
			}
		});
		btnRessayer.setBounds(58, 106, 111, 55);
		contentPane.add(btnRessayer);
		
		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("leave");
				setVisible(false);
			}
		});
		btnQuitter.setBounds(266, 106, 105, 55);
		contentPane.add(btnQuitter);
		
		JLabel lblNewLabel = new JLabel("VOUS AVEZ PERDU");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		lblNewLabel.setBounds(135, 42, 167, 40);
		contentPane.add(lblNewLabel);
	}
}
