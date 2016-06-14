package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main driver for the application. Hosts the JFrame and contains all main JPanels.
 * 
 * @author Jonah Howard
 */
public class GUI extends JFrame implements Observer {
	
	/** URL to connect to web service. */
	public static final String URL = 
			"http://cssgate.insttech.washington.edu/~jonahkh/445phpscript.php?";
	
	/** The currently logged in email. */
	protected static String EMAIL = null;
	/** Represents the login page card. */
	public static final String LOGIN = "LOGIN";
	
	/** Represents the admin page card. */
	public static final String ADMIN = "ADMIN";
	
	/** Represents the registration page card. */
	public static final String REGISTER  = "REGISTER";
	
	/** Represents the gym user page card. */
	public static final String USER = "USER";
	
	/** The user panel. */
	private GymUserPanel userPanel;
	
	/** The login page. */
	private final JPanel myLoginPanel;
	
	/** The admin page. */
	private final JPanel myAdminPanel;
	
	/** The registration panel. */
	private final JPanel myRegisterPanel;
	
	/** The gym user panel. */
	private final JPanel myGymUserPanel;
	
	/** The current layout for this frame. */
	private final CardLayout myLayout;
	
	/** The current card panel for the card layout. */
	private final JPanel myCardPanel;
	
	/** Initialize a new GUI. */
	public GUI() {
		final LoginPanel login = new LoginPanel();
		final AdminPanel admin = new AdminPanel();
		final RegisterPanel register = new RegisterPanel();
		userPanel = new GymUserPanel();
		
		login.addObserver(this);
		admin.addObserver(this);
		register.addObserver(this);
		userPanel.addObserver(this);
		
		myRegisterPanel = register.getPanel();
		myAdminPanel = admin.getPanel();
		myGymUserPanel = userPanel.getPanel();
		myLoginPanel = login.getPanel();
		myCardPanel = new JPanel();
		myLayout = new CardLayout(5, 5);
		
		setUp();
		setUpLayoutManager();
		setVisible(true);
	}
	
	/** Sets up the interface. */
	private void setUp() {
		final Toolkit kit = Toolkit.getDefaultToolkit();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.WHITE);
		setSize(600, 600);
		setTitle("Fitness Tracker");
		setLocation((int) (kit.getScreenSize().getWidth() / 2 - getWidth() / 2),
				(int) (kit.getScreenSize().getHeight() / 2 - getHeight() / 2));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		myCardPanel.setLayout(myLayout);
		myCardPanel.setBackground(Color.WHITE);
		myCardPanel.add(myLoginPanel);
		myCardPanel.add(myAdminPanel);
		myCardPanel.add(myGymUserPanel);
		myCardPanel.add(myRegisterPanel);
		add(myCardPanel, BorderLayout.CENTER);
	}
	
	/** Sets up the layout manager. */
	private void setUpLayoutManager() {
		myLayout.addLayoutComponent(myLoginPanel, LOGIN);
		myLayout.addLayoutComponent(myAdminPanel, ADMIN);
		myLayout.addLayoutComponent(myGymUserPanel, USER);
		myLayout.addLayoutComponent(myRegisterPanel, REGISTER);
		myLayout.show(myCardPanel, LOGIN);
		myCardPanel.revalidate();
	}
	
	/**
	 * Connect to the web service and retrieve the result. 
	 * 
	 * @param url the url to connect to the web service
	 * @return the results from the web service 
	 */
	public static String webConnect(String url) {
        URL urlObject;
		try {
			urlObject = new URL(url.toString());
	        HttpURLConnection urlConnection =  (HttpURLConnection) urlObject.openConnection();
	        InputStream content = urlConnection.getInputStream();
	        BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
	        return  buffer.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * Main driver for this application.
	 * 
	 * @param theArgs command line arguments, to be ignored
	 */
	public static void main(String... theArgs) {
		GUI gui = new GUI();
	}
	
	@Override
	public void update(final Observable arg0, final Object theObject) {
		if (theObject instanceof String) {
			if (((String) theObject).equals(USER) && userPanel.isFirst) {
				userPanel.setUp();
			}
			myLayout.show(myCardPanel, (String) theObject);
			myCardPanel.revalidate();
		}
	}	
}
