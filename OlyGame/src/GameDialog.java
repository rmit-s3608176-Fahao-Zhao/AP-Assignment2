import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;



public class GameDialog extends javax.swing.JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4287932734423574772L;
	
	private static final String ID_FILE = "id.txt";
	
	private JLabel labelId;
	private JTextField gameIdField;
	private JComboBox<String> athleteComboBox;
	private JLabel athLabel;
	private JLabel officialLabel;
	private JLabel athletesLabel;
	private JList<String> athleteList;
	private DefaultListModel<String> athleteListModel;
	private JButton btnCancel;
	private JButton btnOk;
	private JComboBox<String> officialComboBox;
	private JButton btnAddAth;
	private JComboBox<String> typeComboBox;
	private JLabel labelType;
	
	private Map<String, Official> officialMap;
	private Map<String, Athlete> athleteMap;
	private Driver driver;
	private Game game;


	/**
	* display this JDialog
	*/
		
	public GameDialog(JFrame frame, Driver driver) {
		super(frame, true);
		this.driver = driver;
		this.officialMap = driver.getOfficialMap();
		this.athleteMap = driver.getAthleteMap();
		initGUI();
		this.setLocationRelativeTo(null);
		readGameId();
		game = new Running(this.gameIdField.getText());
	}
	
	/**
	 * init GUI
	 */
	private void initGUI() {
		try {
				getContentPane().setLayout(null);

					labelId = new JLabel();
					getContentPane().add(labelId);
					labelId.setText("game id:");
					labelId.setBounds(27, 27, 63, 20);
			
					gameIdField = new JTextField();
					getContentPane().add(gameIdField);
					gameIdField.setBounds(120, 24, 140, 27);
					gameIdField.setEditable(false);
				
					labelType = new JLabel();
					getContentPane().add(labelType);
					labelType.setText("game type:");
					labelType.setBounds(26, 103, 82, 20);
				
					ComboBoxModel<String> typeComboBoxModel = 
							new DefaultComboBoxModel<String>(
									new String[] { "Running", "Cycling", "Swimming" });
					typeComboBox = new JComboBox<String>();
					getContentPane().add(typeComboBox);
					typeComboBox.setModel(typeComboBoxModel);
					typeComboBox.setBounds(120, 100, 145, 27);
					typeComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							gameTypeChanged();
						}
					});
				
					btnAddAth = new JButton();
					getContentPane().add(btnAddAth);
					btnAddAth.setText("add");
					btnAddAth.setBounds(269, 172, 63, 27);
					btnAddAth.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							try {
								addAthlete();
							} catch (WrongTypeException | GameFullException e) {
								driver.showMsg(e.getMessage());
							}
						}
					});
				
					officialLabel = new JLabel();
					getContentPane().add(officialLabel);
					officialLabel.setText("official:");
					officialLabel.setBounds(26, 246, 52, 20);
				
					String[] officials = new String[officialMap.size() + 1];
					officials[0] = "";
					int index = 1;
					for(Official off : officialMap.values()) {
						officials[index++] = off.getName(); 
					}
					ComboBoxModel<String> officialComboBoxModel = 
							new DefaultComboBoxModel<String>(officials);
					officialComboBox = new JComboBox<String>();
					getContentPane().add(officialComboBox);
					officialComboBox.setModel(officialComboBoxModel);
					officialComboBox.setBounds(120, 243, 145, 27);
				
					btnOk = new JButton();
					getContentPane().add(btnOk);
					btnOk.setText("ok");
					btnOk.setBounds(90, 302, 82, 27);
					btnOk.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							try {
								btnOkClick();
							} catch (TooFewAthleteException | NoRefereeException e) {
								driver.showMsg(e.getMessage());
							}
						}
					});
				
					btnCancel = new JButton();
					getContentPane().add(btnCancel);
					btnCancel.setText("cancel");
					btnCancel.setBounds(299, 302, 72, 27);
					btnCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							closeWindow();
						}
					});
				
					athleteListModel = 
							new DefaultListModel<String>();
					athleteList = new JList<String>();
					getContentPane().add(athleteList);
					athleteList.setModel(athleteListModel);
					athleteList.setBounds(343, 62, 122, 204);
				
					athletesLabel = new JLabel();
					getContentPane().add(athletesLabel);
					athletesLabel.setText("athletes:");
					athletesLabel.setBounds(343, 27, 62, 20);
				
					athLabel = new JLabel();
					getContentPane().add(athLabel);
					athLabel.setText("athlete:");
					athLabel.setBounds(26, 175, 55, 20);
				
					String[] athletes = new String[athleteMap.size()];
					index = 0;
					for(Athlete ath : athleteMap.values()) {
						athletes[index++] = ((Participant) ath).getName()
								+ "   " + driver.getPlayerType(ath); 
					}
					ComboBoxModel<String> athleteComboBoxModel = 
							new DefaultComboBoxModel<String>(athletes);
					athleteComboBox = new JComboBox<String>();
					getContentPane().add(athleteComboBox);
					athleteComboBox.setModel(athleteComboBoxModel);
					athleteComboBox.setBounds(120, 172, 143, 27);
				
		
			this.setSize(500, 400);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * read game id
	 */
	private void readGameId() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(ID_FILE)));
			gameIdField.setText(br.readLine());
			br.close();
		} catch (IOException e) {
			driver.showMsg("no id file found");
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					closeWindow();
				}
			
			}).start();
		}
	}
	
	/**
	 * close the window
	 */
	private void closeWindow() {
		this.dispose();
	}
	
	/**
	 * set the new game
	 * @throws TooFewAthleteException
	 * @throws NoRefereeException
	 */
	private void btnOkClick() throws TooFewAthleteException, NoRefereeException {
		if(game.getAthletes().size() < 4) {
			throw new TooFewAthleteException("too few athletes");
		}
		String offName = officialComboBox.getSelectedItem().toString();
		Official official = null;
		for(Official off : officialMap.values()) {
			if(off.getName().equals(offName)) {
				official = off;
				break;
			}
		}
		if(official == null) {
			throw new NoRefereeException("no official");
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(ID_FILE)));
			int num = Integer.valueOf(game.getId().substring(1));
			bw.write("R" + (num + 1));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		game.setOfficial(official);
		driver.setCurrentGame(game);
		this.closeWindow();
	}
	
	/**
	 * change the game type
	 */
	private void gameTypeChanged() {
		String strGame = (String)typeComboBox.getSelectedItem();
		if(strGame.equals("Running")) {
			game = new Running(this.gameIdField.getText());
		} else if (strGame.equals("Swimming")) {
			game = new Swimming(this.gameIdField.getText());
		} else {
			game = new Cycling(this.gameIdField.getText());
		}
		//clear the info
		athleteListModel.clear();
	}

	/**
	 * add athlete to the game
	 * @throws WrongTypeException
	 * @throws GameFullException
	 */
	private void addAthlete() throws WrongTypeException, GameFullException {
		String name = athleteComboBox.getSelectedItem().toString().split(" ")[0];
		Athlete athlete = null;
		for(Athlete ath : athleteMap.values()) {
			Participant p = (Participant) ath;
			if(p.getName().equals(name)) {
				athlete = ath;
				break;
			}
		}
		if(game instanceof Running && (athlete instanceof Cyclist || athlete instanceof Swimmer)) {
			throw new WrongTypeException("wrong type");
		} else if (game instanceof Swimming && (athlete instanceof Cyclist)) {
			throw new WrongTypeException("wrong type");
		} else if (game instanceof Cycling && (athlete instanceof Swimmer)) {
			throw new WrongTypeException("wrong type");
		}
		if(game.getAthletes().contains(athlete)) {
			return;
		}
		if(game.isFull()) {
			throw new GameFullException("game already has 8 athletes");
		}
		athlete.setType(game.getType());
		game.addAthlete(athlete);
		
		athleteListModel.addElement(name);
	}
	
}
