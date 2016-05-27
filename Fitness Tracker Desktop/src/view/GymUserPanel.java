package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class GymUserPanel extends Observable {
	private final JPanel myPanel;
	private final Map<String, JButton> buttons;
	private JPanel myWorkoutPanel;
	
	public GymUserPanel() {
		myPanel = new JPanel();
		buttons = new TreeMap<>();
		myWorkoutPanel = new JPanel();
		setUp();
	}
	
	private void setUp() {
		myPanel.setBackground(Color.WHITE);
//		myPanel.setLayout(new BorderLayout());\
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		final JButton viewSupplements = new JButton("View Supplements");
		final JLabel viewWorkouts = new JLabel("Daily Workouts");
		final JButton logout = new JButton("Logout");
		final JButton monday = new JButton("Monday");
		final JButton tuesday = new JButton("Tuesday");
		final JButton wednesday = new JButton("Wednesday");
		final JButton thursday = new JButton("Thursday");
		final JButton friday = new JButton("Friday");
		final JButton saturday = new JButton("Saturday");
		final JButton sunday = new JButton("Sunday");
		final JPanel northPanel = new JPanel();
		final JPanel centerPanel = new JPanel();
		
		buttons.put("sunday", sunday);
		buttons.put("monday", monday);
		buttons.put("tuesday", tuesday);
		buttons.put("wednesday", wednesday);
		buttons.put("thursday", thursday);
		buttons.put("friday", friday);
		buttons.put("saturday", saturday);
		
//		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		centerPanel.setBackground(Color.WHITE);
		northPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		northPanel.setBackground(Color.WHITE);
		myPanel.add(northPanel, BorderLayout.NORTH);
		centerPanel.add(viewWorkouts);
		for (JButton current : buttons.values()) {
			centerPanel.add(current);
		}
		final JPanel scrollPanel = getScrollPanel();
		northPanel.add(viewSupplements);
		northPanel.add(logout);
		northPanel.setMaximumSize(new Dimension(600, (int) northPanel.getMinimumSize().getHeight()));
		centerPanel.setMaximumSize(new Dimension(600, (int) centerPanel.getMinimumSize().getHeight()));
		myPanel.add(Box.createVerticalStrut(10));
		myPanel.add(centerPanel, BorderLayout.CENTER);
		myPanel.add(Box.createVerticalStrut(10));
		myPanel.add(scrollPanel);
		setListeners(logout, viewSupplements);
	}
	
	private JPanel getScrollPanel() {
		final JPanel innerPanel = new JPanel();
		final JScrollPane view = new JScrollPane(innerPanel);
		final JLabel text = new JLabel("This is my text for this JLabel");
		final JPanel panel = new JPanel();
		view.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		view.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		innerPanel.add(text);
//		view.add(innerPanel);
//		view.setSize(new Dimension(400, 200));
		innerPanel.setSize(new Dimension(400, 200));
		innerPanel.setPreferredSize(new Dimension(400, 200));
		view.setMinimumSize(new Dimension(400, 200));
		innerPanel.setMinimumSize(new Dimension(200, 200));
		innerPanel.setPreferredSize(new Dimension(400, 200));
		panel.setMinimumSize(new Dimension(200, 200));

		
		panel.setBackground(Color.WHITE);
		panel.add(view);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		return panel;
	}
	
	private void setListeners(final JButton logout, final JButton supplements) {
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
	public JPanel getPanel() {
		return myPanel;
	}
}
