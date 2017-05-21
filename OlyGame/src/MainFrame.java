import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


public class MainFrame extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1102724826899171641L;
	private JButton btnNewGame;
	private JLabel labelGameId;
	private JTextField gameIdField;
	private JButton btnPoints;
	private JPanel namePanel;
	private JPanel jPanel;
	private JScrollPane jScrollPane;
	private JLabel infoLabe;
	private JTextArea textArea;
	private JButton btnDisplayAll;
	private JButton btnDisplay;
	private JButton btnRun;

	private Driver driver;
	private JProgressBar[] bars;
	private JLabel[] labels;

	public MainFrame() {
		super();
		initGUI();
		this.setLocationRelativeTo(null);
		this.driver = new Driver();
	}

	/**
	 * init the GUI
	 */
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);

			btnNewGame = new JButton();
			getContentPane().add(btnNewGame);
			btnNewGame.setText("new game");
			btnNewGame.setBounds(24, 27, 117, 27);
			btnNewGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					showGameDialog();
				}
			});

			labelGameId = new JLabel();
			getContentPane().add(labelGameId);
			labelGameId.setText("game id:");
			labelGameId.setBounds(24, 73, 63, 20);

			gameIdField = new JTextField();
			getContentPane().add(gameIdField);
			gameIdField.setBounds(99, 70, 146, 27);
			gameIdField.setEditable(false);

			btnRun = new JButton();
			getContentPane().add(btnRun);
			btnRun.setText("run game");
			btnRun.setBounds(297, 70, 96, 27);
			btnRun.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textArea.setText("");
					if (driver.runGame()) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								showAnimation(driver.getCurrentGame());
								;
							}

						}).start();
					}
				}
			});

			btnDisplay = new JButton();
			getContentPane().add(btnDisplay);
			btnDisplay.setText("game result");
			btnDisplay.setBounds(152, 27, 117, 27);
			btnDisplay.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (driver.getCurrentGame() != null) {
						textArea.setText("game result:\n"
								+ driver.getCurrentGame().getGameResult());
					} else {
						driver.showMsg("no game runned");
						textArea.setText("");
					}
				}
			});

			btnDisplayAll = new JButton();
			getContentPane().add(btnDisplayAll);
			btnDisplayAll.setText("all results");
			btnDisplayAll.setBounds(442, 27, 118, 27);
			btnDisplayAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textArea.setText("all game results:\n"
							+ driver.allResults());
				}
			});

			btnPoints = new JButton();
			getContentPane().add(btnPoints);
			btnPoints.setText("athlete points");
			btnPoints.setBounds(292, 27, 129, 27);
			btnPoints.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textArea.setText("points of all athletes:\n"
							+ driver.allPoints());
				}
			});

			infoLabe = new JLabel();
			getContentPane().add(infoLabe);
			infoLabe.setText("infomation:");
			infoLabe.setBounds(24, 110, 128, 20);

			jScrollPane = new JScrollPane();
			getContentPane().add(jScrollPane);
			jScrollPane.setBounds(24, 143, 536, 238);
			textArea = new JTextArea();
			jScrollPane.setViewportView(textArea);
			textArea.setBounds(36, 306, 505, 202);

			jPanel = new JPanel();
			GridLayout jPanelLayout = new GridLayout(8, 1);
			jPanelLayout.setHgap(5);
			jPanelLayout.setVgap(5);
			jPanelLayout.setColumns(1);
			jPanel.setLayout(jPanelLayout);
			getContentPane().add(jPanel);
			jPanel.setBounds(158, 412, 402, 187);

			namePanel = new JPanel();
			GridLayout namePanelLayout = new GridLayout(8, 1);
			namePanelLayout.setHgap(5);
			namePanelLayout.setVgap(5);
			namePanelLayout.setColumns(1);
			namePanel.setLayout(namePanelLayout);
			getContentPane().add(namePanel);
			namePanel.setBounds(24, 412, 128, 187);

			pack();
			this.setSize(617, 656);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * show game dialog
	 */
	private void showGameDialog() {
		new GameDialog(this, driver).setVisible(true);
		if (driver.getCurrentGame() == null) {
			return;
		}
		this.gameIdField.setText(driver.getCurrentGame().getId());
	}
	
	/**
	 * show game animation
	 * @param game
	 */
	private void showAnimation(Game game) {
		
		int maxTime = game.getGameType().maxTime();
		int totalTime = Game.GAME_TIME;
		if(bars != null) {
			for(int i=0; i<bars.length; i++) {
				if(bars[i] != null) {
					jPanel.remove(bars[i]);
				}
			}
		}
		if(labels != null) {
			for(int i=0; i<labels.length; i++) {
				if(labels[i] != null) {
					namePanel.remove(labels[i]);
				}
			}
		}
		int num = game.getAthletes().size();
		int[] speeds = new int[num];
		bars = new JProgressBar[num];
		labels = new JLabel[num];
		for(int i=0; i<num; i++) {
			Participant p = (Participant)game.getAthletes().get(i);
			speeds[i] = 10 * maxTime / p.getTime();
			labels[i] = new JLabel(p.getName() + "(" + p.getId() + ")" + ":");
			namePanel.add(labels[i]);
			bars[i] = new JProgressBar();
			bars[i].setMaximum(1000);
			jPanel.add(bars[i]);
		}
		jPanel.updateUI();
		for(int i=0; i<totalTime*10; i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(int k=0; k<num; k++) {
				bars[k].setValue(speeds[k] * (i+1));
			}
		}

		textArea.setText(driver.getCurrentGame()
				.getGameResult().toString());
	}
}
