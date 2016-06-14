package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Class representing the registration panel. 
 * 
 * @author Jonah Howard
 *
 */
public class RegisterPanel extends Observable {
	
	/** Text field for the first name. */
	private final JTextField myFirstName;
	
	/** The text field for the last name. */
	private final JTextField myLastName;
	
	/** The combo box for the weight. */
	private final JComboBox<Integer> myWeight;
	
	/** The combo box for the gender. */
	private final JComboBox<String> myGender;
	
	/** The text field for the email. */
	private final JTextField myEmail;
	
	/** The combo box for the number of days you workout. */
	private final JComboBox<Integer> myDaysWorkout;
	
	/** The current panel. */
	private final JPanel myPanel;
	
	/** password text field. */
	private final JPasswordField passField;
	
	/** Initialize a new RegisterPanel. */
	public RegisterPanel() {
		myPanel = new JPanel();
		myFirstName = new JTextField(30);
		myLastName = new JTextField(30);
		myWeight = new JComboBox<Integer>();
		myGender = new JComboBox<String>();
		myEmail = new JTextField(30);
		myDaysWorkout = new JComboBox<Integer>();
		passField = new JPasswordField(10);
		fillComboBoxes();
		setUp();
	}
	
	/** @return the current panel. */
	public JPanel getPanel() {
		return myPanel;
	}
	
	/** Fill the combo boxes of their components. */
	private void fillComboBoxes() {
		for (int i = 1; i < 300; i++) {
			myWeight.addItem(i);
		}
		myGender.addItem("Male");
		myGender.addItem("Female");
		for (int i = 1; i < 8; i++) {
			myDaysWorkout.addItem(i);
		}
	
	}
	
	/** Set up components for this panel. */
	private void setUp() {
		myPanel.setBackground(Color.WHITE);
		final JPanel panel = new JPanel();
		myPanel.add(textFields());
		
	}
	
	/** @return a JPanel with all of the text fields for registering. */
	private JPanel textFields() {
		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		final JPanel firstPanel = new JPanel();
		final JPanel lastPanel = new JPanel();
		final JPanel emailPanel = new JPanel();
		final JPanel weightPanel = new JPanel();
		final JPanel genderPanel = new JPanel();
		final JPanel daysPanel = new JPanel();
		final JPanel buttonsPanel = new JPanel();
		final JPanel passPanel = new JPanel();
		final JLabel passwordLabel = new JLabel("Password");
		
		
		final JLabel weight = new JLabel("Weight: ");
		final JLabel gender = new JLabel("Gender: ");
		final JLabel days = new JLabel("Days Committed to Workouts: ");
		final JLabel first = new JLabel("First Name: ");
		final JLabel last = new JLabel(" Last Name: ");
		final JLabel email = new JLabel("     Email: ");
		
		final JButton register = new JButton("Register");
		final JButton cancel = new JButton("Cancel");
		addListeners(register, cancel);
		
		firstPanel.add(first);
		firstPanel.add(myFirstName);
		lastPanel.add(last);
		lastPanel.add(myLastName);
		emailPanel.add(email);
		emailPanel.add(myEmail);
		weightPanel.add(weight);
		weightPanel.add(myWeight);
		genderPanel.add(gender);
		genderPanel.add(myGender);
		daysPanel.add(days);
		daysPanel.add(myDaysWorkout);
		buttonsPanel.add(register);
		buttonsPanel.add(cancel);
		
		passPanel.setBackground(Color.WHITE);
		buttonsPanel.setBackground(Color.WHITE);
		weightPanel.setBackground(Color.WHITE);
		genderPanel.setBackground(Color.WHITE);
		daysPanel.setBackground(Color.WHITE);
		firstPanel.setBackground(Color.WHITE);
		lastPanel.setBackground(Color.WHITE);
		emailPanel.setBackground(Color.WHITE);
		panel.setBackground(Color.WHITE);
		myWeight.setBackground(Color.WHITE);
		myGender.setBackground(Color.WHITE);
		myDaysWorkout.setBackground(Color.WHITE);
		passPanel.add(passwordLabel);
		passPanel.add(passField);
		
		panel.add(firstPanel);
		panel.add(lastPanel);
		panel.add(emailPanel);
		panel.add(passPanel);
		
		panel.add(Box.createVerticalStrut(10));
		panel.add(weightPanel);
		panel.add(genderPanel);
		panel.add(daysPanel);
		panel.add(buttonsPanel);
		
		
		return panel;
	}
	
	/**
	 * Add listeners for the register and cancel buttons.
	 * 
	 * @param register button
	 * @param cancel button
	 */
	private void addListeners(final JButton register, final JButton cancel) {
		register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				String first = myFirstName.getText();
				String last = myLastName.getText();
				String email = myEmail.getText();
				String gender = (String) myGender.getSelectedItem();
				int days = (int) myDaysWorkout.getSelectedItem();
				int weight = (int) myWeight.getSelectedItem();
				
				String url = GUI.URL;
				boolean error = true;
				String password = passField.getText();
				if (password.contains(" ")) {
					error = false;
					JOptionPane.showMessageDialog(
            		        myPanel, "Password cannot contain spaces!", "Failure", JOptionPane.ERROR_MESSAGE);
				}
				if (first.length() < 1 || last.length() < 1 || email.length() < 1 || password.length() < 1) {
					JOptionPane.showMessageDialog(
            		        myPanel, "All fields required", "Failure", JOptionPane.ERROR_MESSAGE);
					error = false;
				}
				if (first.contains(" ") || last.contains(" ") || email.contains(" ")) {
					JOptionPane.showMessageDialog(
            		        myPanel, "No spaces allowed!", "Failure", JOptionPane.ERROR_MESSAGE);
					error = false;
				}
				if (error) {
					url += "cmd=register&email=" + email + "&first=" + first.replaceAll(" ", "_") 
					+ "&last=" + last.replaceAll(" ", "_") 
							+ "&gender=" + gender + "&days=" + days + "&weight=" + weight
							+ "&pwd=" + password;
					String result = GUI.webConnect(url);
					if (result.contains("success")) {
						GUI.EMAIL = email;
						setChanged();
						notifyObservers(GUI.USER);
						clearChanged();
					} else {
						JOptionPane.showMessageDialog(
	            		        myPanel, "Email already exists!", "Failure", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				myFirstName.setText("");
				myLastName.setText("");
				myEmail.setText("");
				setChanged();
				notifyObservers(GUI.LOGIN);
				clearChanged();
			}
		});
	}
}
