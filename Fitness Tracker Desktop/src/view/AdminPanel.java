package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminPanel extends Observable {
	
	public static final String ADD_WORKOUT = GUI.URL + "cmd=addworkout";
	public static final String GET_SUPPLEMENTS = GUI.URL + "cmd=getsupplementnames";
	public static final String DELETE_SUPPLEMENT = GUI.URL + "cmd=deletesupplement";
	public static final String ADD_SUPPLEMENT = GUI.URL + "cmd=addsupplement";
	private final JPanel myPanel;
	private JComboBox<String> myDays;
	private Map<String, JButton> buttons;
	private String index = "Select day to change";
	private JRadioButton weightButton;
	private JRadioButton cardioButton;
	private String suppIndex = "";

	
	public AdminPanel() {
		myDays = new JComboBox<String>();
		buttons = new HashMap<String, JButton>();
		myPanel = new JPanel();
		setUp();
	}
	
	public JPanel getPanel() {
		return myPanel;
	}
	
	private void setUp() {
		myPanel.setBackground(Color.WHITE);
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		final JButton logout = new JButton("Logout");
		final JButton monday = new JButton("Monday");
		final JButton tuesday = new JButton("Tuesday");
		final JButton wednesday = new JButton("Wednesday");
		final JButton thursday = new JButton("Thursday");
		final JButton friday = new JButton("Friday");
		final JButton saturday = new JButton("Saturday");
		final JButton sunday = new JButton("Sunday");
		final JLabel adminView = new JLabel("Administrator View");
		final JLabel changeWorkout = new JLabel("Change Workout");
		final JPanel northPanel = new JPanel();
		final JPanel centerPanel = new JPanel();
		JLabel cardioLabel = new JLabel("Cadio");
		JLabel weightLabel = new JLabel("Weight");
		cardioButton = new JRadioButton();
		weightButton = new JRadioButton();
		weightButton.setSelected(true);
		cardioButton.setBackground(Color.WHITE);
		weightButton.setBackground(Color.WHITE);
		final ButtonGroup group = new ButtonGroup();
		group.add(weightButton);
		group.add(cardioButton);
		JPanel selectType = new JPanel();
		selectType.setBackground(Color.WHITE);
		selectType.add(weightLabel);
		selectType.add(weightButton);		
		selectType.add(cardioLabel);
		selectType.add(cardioButton);
		myDays.addItem("Select day to change");
		myDays.addItem("sunday");
		myDays.addItem("monday");
		myDays.addItem("tuesday");
		myDays.addItem("wednesday");
		myDays.addItem("thursday");
		myDays.addItem("friday");
		myDays.addItem("saturday");
		myDays.setBackground(Color.WHITE);
		final JComboBox<String> comboBox = getSupps();
		final JLabel delete = new JLabel("Delete Supplement: ");
		final JButton addSupplementButton = addSuppButton();
		buttons.put("sunday", sunday);
		buttons.put("monday", monday);
		buttons.put("tuesday", tuesday);
		buttons.put("wednesday", wednesday);
		buttons.put("thursday", thursday);
		buttons.put("friday", friday);
		buttons.put("saturday", saturday);
		centerPanel.add(selectType);
		centerPanel.add(changeWorkout);
		centerPanel.add(myDays);
		centerPanel.setBackground(Color.WHITE);
		northPanel.setBackground(Color.WHITE);
//		northPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		myPanel.add(northPanel, BorderLayout.NORTH);
		northPanel.setMaximumSize(new Dimension(600, (int) logout.getMinimumSize().getHeight() + 20));
		JPanel comboPanel = new JPanel();
		comboPanel.setBackground(Color.WHITE);
//		comboPanel.setMaximumSize(new Dimension(600, (int) comboBox.getMinimumSize().getHeight()));
		centerPanel.setMaximumSize(new Dimension(600, (int) centerPanel.getPreferredSize().getHeight()));
//		centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		comboPanel.add(delete);
		comboPanel.add(comboBox);
		comboPanel.setMaximumSize(new Dimension(600, (int) comboPanel.getPreferredSize().getHeight()));
		northPanel.add(adminView);
		northPanel.add(logout);
		addLogoutListener(logout);
		myPanel.add(Box.createVerticalStrut(10));
		myPanel.add(centerPanel);
		myPanel.add(comboPanel);
		myPanel.add(addSupplementButton);
		addComboBoxListener();
		addButtonListeners();
	}
	
	private JButton addSuppButton() {
		JButton button = new JButton("Add Supplement");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				JPanel pricePanel = new JPanel();
				JPanel carbsPanel = new JPanel();
				JPanel proPanel = new JPanel();
				JPanel namePanel = new JPanel();
				JPanel fatPanel = new JPanel();
				JTextField nameField = new JTextField(10);
				
				JLabel priceLabel = new JLabel("Price: ");
				JLabel carbsLabel = new JLabel("Carbs: ");
				JLabel proLabel = new JLabel("Protein: ");
				JLabel fatLabel = new JLabel("Fat: ");
				JLabel nameLabel = new JLabel("Name: ");
				
				JComboBox<String> priceBox = new JComboBox<String>();
				JComboBox<String> carbsBox = new JComboBox<String>();
				JComboBox<String> fatBox = new JComboBox<String>();
				JComboBox<String> proBox = new JComboBox<String>();
				
				namePanel.add(nameLabel);
				namePanel.add(nameField);
				
				pricePanel.add(priceLabel);
				pricePanel.add(priceBox);
				
				carbsPanel.add(carbsLabel);
				carbsPanel.add(carbsBox);
				
				proPanel.add(proLabel);
				proPanel.add(proBox);
				
				fatPanel.add(fatLabel);
				fatPanel.add(fatBox);
				panel.add(namePanel);
				panel.add(pricePanel);
				panel.add(fatPanel);
				panel.add(carbsPanel);
				panel.add(proPanel);
				if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(myPanel, panel, 
						"Enter in new supplement information", 
						JOptionPane.OK_CANCEL_OPTION)) {
				}
			}
			
		});
		return button;
	}
	
	private JComboBox<String> getSupps() {
		try {
			final JComboBox<String> supps = new JComboBox<String>();
			supps.setBackground(Color.WHITE);
			String result = GUI.webConnect(GET_SUPPLEMENTS);
			System.out.println(result);
			JSONArray arr = new JSONArray(result);
			supps.addItem("Choose to delete");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				supps.addItem(obj.getString("SupplementName"));
			}
			supps.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg) {
					if (supps.getSelectedIndex() != 0 
							&& !suppIndex.equals(supps.getSelectedItem())) {
						suppIndex = supps.getSelectedItem().toString();
						if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(myPanel, 
								"Delete " + supps.getSelectedItem().toString(),
								"Delete this supplement?", 
								JOptionPane.OK_CANCEL_OPTION)) {
							String newR = GUI.webConnect(DELETE_SUPPLEMENT + "&supp=" + suppIndex);
							if (newR.contains("success")) {
								JOptionPane.showMessageDialog(
				        		        myPanel, "Successfully deleted!",
				        		        "Success", JOptionPane.INFORMATION_MESSAGE);	
							} else {
								JOptionPane.showMessageDialog(
				        		        myPanel, "Deletion Failed!",
				        		        "Failure", JOptionPane.ERROR_MESSAGE);	
							}
						}
					}
				}
				
			});
			return supps;		
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Set up the listener for the logout button.
	 * 
	 * @param logout the button that the listener is being attached to
	 */
	private void addLogoutListener(final JButton logout) {
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				setChanged();
				notifyObservers(GUI.LOGIN);
				GUI.EMAIL = null;
				clearChanged();
			}
		});
	}
	
	private void addButtonListeners() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				index = "";
				myDays.setSelectedIndex(myDays.getSelectedIndex());

			}
		};
		cardioButton.addActionListener(listener);
		weightButton.addActionListener(listener);
	}
	
	private void addComboBoxListener() {
		myDays.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (myDays.getSelectedIndex() != 0 && !myDays.getSelectedItem().equals(index)) {
					index = myDays.getSelectedItem().toString();
					if (weightButton.isSelected()) {
						JPanel panel = new JPanel();
						panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
						panel.setBackground(Color.WHITE);
						JLabel workoutName = new JLabel("Workout name: ");
						JPanel panel1 = new JPanel();
						JPanel panel2 = new JPanel();
						JPanel panel3 = new JPanel();
						JPanel panel4 = new JPanel();
						JPanel panel5 = new JPanel();
						JLabel e1 = new JLabel("Exercise one: ");
						JLabel e2 = new JLabel("Exercise two: ");
						JLabel e3 = new JLabel("Exercise three: ");
						JLabel e4 = new JLabel("Exercise four: ");
						
						JTextField wName = new JTextField(10);
						JTextField name1 = new JTextField(10);
						JTextField name2 = new JTextField(10);
						JTextField name3 = new JTextField(10);
						JTextField name4 = new JTextField(10);
						panel1.add(workoutName);
						panel1.add(wName);
						panel2.add(e1);
						panel2.add(name1);
						panel3.add(e2);
						panel3.add(name2);
						panel4.add(e3);
						panel4.add(name3);
						panel5.add(e4);
						panel5.add(name4);
						
						panel.add(panel1);
						panel.add(panel2);
						panel.add(panel3);
						panel.add(panel4);
						panel.add(panel5);
						if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(myPanel, panel, 
								"Set the workout for " + index, 
								JOptionPane.OK_CANCEL_OPTION)) {
							if (wName.getText().equals("") || e1.getText().equals("") 
									|| e2.getText().equals("") || e3.getText().equals("") 
									|| e4.getText().equals("")) {
								JOptionPane.showMessageDialog(
			            		        myPanel, "All Fields Required!",
			            		        "Failure", JOptionPane.ERROR_MESSAGE);	
								index = "";
								myDays.setSelectedIndex(myDays.getSelectedIndex());
							} else {
								String url = ADD_WORKOUT + "&name=" + wName.getText() + "&ex1=" 
										+ name1.getText() + "&ex2=" + name2.getText() + "&ex3="
										+ name3.getText() + "&ex4="  + name4.getText() 
										+ "&type=weight&day=" + myDays.getSelectedItem();
								GUI.webConnect(url);
							}
						}
					} else {
						JPanel panel = new JPanel();
						JPanel descriptionPanel = new JPanel();
						JPanel namePanel = new JPanel();
						JLabel nameLabel = new JLabel("Workout name: ");
						JTextField nameField = new JTextField(10);
						JTextField descriptionField = new JTextField(100);
						JLabel description = new JLabel("Please Enter Description: ");
						
						panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
						
						namePanel.add(nameLabel);
						namePanel.add(nameField);
						descriptionPanel.add(description);
						descriptionPanel.add(descriptionField);
						panel.add(namePanel);
						panel.add(descriptionPanel);
						if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(myPanel, panel, 
								"Set the workout for " + index, 
								JOptionPane.OK_CANCEL_OPTION)) {
							if (nameField.getText().equals("") || descriptionField.getText().equals("")) {
								JOptionPane.showMessageDialog(
			            		        myPanel, "All Fields Required!",
			            		        "Failure", JOptionPane.ERROR_MESSAGE);	
								index = "";
								myDays.setSelectedIndex(myDays.getSelectedIndex());
							} else {
								String url = ADD_WORKOUT + "&name=" + nameField.getText() 
											+ "&type=cardio&description=" + descriptionField.getText()
											+ "&day=" + myDays.getSelectedItem();
								GUI.webConnect(url);
							}
						}
					}
				}
			}
			
		});
	}
}
