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

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class AdminPanel extends Observable {
	
	public static final String ADD_WORKOUT = "cmd=addworkout";
	private final JPanel myPanel;
	private JComboBox<String> myDays;
	private Map<String, JButton> buttons;
	private String index = "Select day to change";
	private JRadioButton weightButton;
	private JRadioButton cardioButton;

	
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
		myPanel.add(northPanel, BorderLayout.NORTH);
		northPanel.setMaximumSize(new Dimension(600, (int) northPanel.getMinimumSize().getHeight()));

		northPanel.add(adminView);
		northPanel.add(logout);
		myPanel.add(centerPanel);
		addComboBoxListener();
		addButtonListeners();
	}
	
	private void addButtonListeners() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("here");
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
							}
						}
					} else {
						JPanel panel = new JPanel();
						JPanel intensityPanel = new JPanel();
						JPanel durationPanel = new JPanel();
						JLabel intensity = new JLabel("Intensity: ");
						JLabel duration = new JLabel("Duration (minutes): "); 
						
						JComboBox<String> intensityOptions = new JComboBox<String>();
						JComboBox<Integer> durationOptions = new JComboBox<Integer>();
						
						panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
						for (int i = 1; i <= 120; i++) {
							durationOptions.addItem(i);
						}
						intensityOptions.addItem("easy");
						intensityOptions.addItem("medium");
						intensityOptions.addItem("hard");
						intensityPanel.add(intensity);
						intensityPanel.add(intensityOptions);
						durationPanel.add(duration);
						durationPanel.add(durationOptions);
						panel.add(intensityPanel);
						panel.add(durationPanel);
						if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(myPanel, panel, 
								"Set the workout for " + index, 
								JOptionPane.OK_CANCEL_OPTION)) {
							JOptionPane.showMessageDialog(
		            		        myPanel, "All Fields Required!",
		            		        "Failure", JOptionPane.ERROR_MESSAGE);	
							index = "";
							myDays.setSelectedIndex(myDays.getSelectedIndex());
						}
					}
				}
			}
			
		});
	}
}
