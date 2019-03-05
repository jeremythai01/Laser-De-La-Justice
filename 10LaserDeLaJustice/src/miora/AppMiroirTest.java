package miora;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AppMiroirTest extends JFrame {

	private JPanel contentPane;
	private SceneTestMiroirs sceneTestMiroirs;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppMiroirTest frame = new AppMiroirTest();
					frame.setVisible(true);
					frame.sceneTestMiroirs.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AppMiroirTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 629, 567);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		sceneTestMiroirs = new SceneTestMiroirs();
		sceneTestMiroirs.setBounds(10, 11, 461, 481);
		contentPane.add(sceneTestMiroirs);
		donneFocusALaScene();
		
		JButton btnStart = new JButton("start");
		btnStart.setBounds(495, 87, 108, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("stop");
		btnStop.setBounds(495, 197, 108, 23);
		contentPane.add(btnStop);
		
		JButton btnMiroirPlan = new JButton("Miroir plan");
		btnMiroirPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneTestMiroirs.setMiroirPlan(true);
				donneFocusALaScene();
			}
		});
		btnMiroirPlan.setBounds(495, 307, 108, 23);
		contentPane.add(btnMiroirPlan);
		
		JButton btnMiroirConvexe = new JButton("Miroir convexe");
		btnMiroirConvexe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneTestMiroirs.setMiroirPlan(false);
				donneFocusALaScene();
			}
		});
		btnMiroirConvexe.setBounds(495, 417, 108, 23);
		contentPane.add(btnMiroirConvexe);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneTestMiroirs.arreter();
				donneFocusALaScene();
			}
		});
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneTestMiroirs.demarrer();
				donneFocusALaScene();
			}
		});
	}
	public void donneFocusALaScene() {
		sceneTestMiroirs.requestFocusInWindow();
	}
}
