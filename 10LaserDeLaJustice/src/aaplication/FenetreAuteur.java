package aaplication;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class FenetreAuteur extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetreAuteur frame = new FenetreAuteur();
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
	public FenetreAuteur() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 847, 362);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblArezkiIssaadi = new JLabel("Arezki Issaadi");
		lblArezkiIssaadi.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblArezkiIssaadi.setBounds(10, 181, 121, 14);
		contentPane.add(lblArezkiIssaadi);
		
		JLabel lblArnaud = new JLabel("Arnaud Lefevfre");
		lblArnaud.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblArnaud.setBounds(175, 181, 121, 14);
		contentPane.add(lblArnaud);
		
		JLabel lblMiora = new JLabel("Miora Ratsirahoanana");
		lblMiora.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblMiora.setBounds(366, 181, 180, 14);
		contentPane.add(lblMiora);
		
		JLabel lblThai = new JLabel("Jeremy Thai");
		lblThai.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblThai.setBounds(641, 181, 180, 14);
		contentPane.add(lblThai);
		
		JLabel lblDescriptif = new JLabel("Cette application est dans le cadre du cours SCD");
		lblDescriptif.setFont(new Font("Yu Gothic UI Semilight", Font.BOLD, 15));
		lblDescriptif.setBounds(65, 239, 450, 29);
		contentPane.add(lblDescriptif);
		
		JButton btnRetour = new JButton("Retour");
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnRetour.setBounds(10, 6, 89, 23);
		contentPane.add(btnRetour);
	
		
		
		
	}
}
