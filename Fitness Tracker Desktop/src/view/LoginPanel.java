package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends Observable {

	/** The current panel. */
	private final JPanel myPanel;
	
	/** Textfield for the email. */
	private JTextField myEmail;

	/** Textfield for the password. */
	private JPasswordField myPassword;
	
	/** Initialize a new LoginPanel. */
	public LoginPanel() {
		myPanel = new JPanel();
		setUp();
	}
	
	/**@return the current panel. */
	public JPanel getPanel() {
		return myPanel;
	}
	
	
	/**
	 * Creates the dialog that prompts the user to enter administrator credentials.
	 * 
	 * @return true if correct login information was entered
	 */
	private void  setUp() {
		final JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		final JPanel passPanel = new JPanel(new FlowLayout());
		final JPanel userPanel = new JPanel(new FlowLayout());
		final JLabel email  = new JLabel("Email: ");
		final JLabel password = new JLabel("Passowrd: ");
		myEmail = new JTextField(10);
		myPassword = new JPasswordField(10);
		final JPanel buttonPanel = new JPanel();
		final JButton login = new JButton("Login");
		final JButton register = new JButton("Register");
		final JButton adminLogin = new JButton("Admin Login");
		attachListeners(login, register, adminLogin);
		passPanel.add(password);
		passPanel.add(myPassword);
		userPanel.add(email);
		userPanel.add(myEmail);
		
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.add(userPanel);
		panel.add(passPanel);
		
		buttonPanel.add(login);
		buttonPanel.add(register);
		buttonPanel.add(adminLogin);
		mainPanel.add(panel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel);
		myPanel.add(mainPanel);
		myPanel.setBackground(Color.WHITE);
		panel.setBackground(Color.WHITE);
	}
	
	/**
	 * Attach the action listeners to the buttons for this panel.
	 * 
	 * @param login the login button
	 * @param register the register button
	 * @param adminLogin the admin login button
	 */
	private void attachListeners(final JButton login, final JButton register, final JButton adminLogin) {
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				StringBuilder url = new StringBuilder(GUI.URL);

                url.append("cmd=login&email=");
                try {
					url.append(URLEncoder.encode(myEmail.getText(), "UTF-8"));
				
	                url.append("&pwd=");
	                url.append(URLEncoder.encode(myPassword.getText(), "UTF-8"));
	                // Establish network access
	                String s = GUI.webConnect(url.toString());
	            	if (s.contains("success")) {
	            		setChanged();
	            		GUI.EMAIL = myEmail.getText();
						notifyObservers(GUI.USER);
						clearChanged();
	            	} else if (s.contains("email")){
	            		JOptionPane.showMessageDialog(
	            		        myPanel, "User does not exist or invalid", "Failure", JOptionPane.ERROR_MESSAGE);
	            	} else if (s.contains("Incorrect password")) {
	            		JOptionPane.showMessageDialog(
	            		        null, "Incorrect password", "Failure", JOptionPane.ERROR_MESSAGE);
	            	}
                } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				setChanged();
				notifyObservers(GUI.REGISTER);
				clearChanged();
			}
		});
		
		adminLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				//TODO validate
				setChanged();
				notifyObservers(GUI.ADMIN);
				clearChanged();
			}
		});
	}
}
