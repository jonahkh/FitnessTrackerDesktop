package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Class representing the Gym User interface. Allows gym users to view the daily workouts and 
 * log them, view their logged workouts, view all of the supplements, and rate supplements.
 * 
 * @author jonah Howard
 *
 */
public class GymUserPanel extends Observable {
	
	// Constants to store and retrieve data from database
	/** Viewing all workouts for a day. */
	public static final String VIEW_WORKOUTS_URL = "cmd=viewworkouts&day=";
	
	/** Adding a new set. */
	public static final String ADD_SET = GUI.URL + "cmd=addset";
	
	/** Adding a weight workout. */
	public static final String ADD_WORKOUT = GUI.URL + "cmd=logworkout";
	
	/** Adding a cardio workout. */
	public static final String ADD_CARDIO = GUI.URL + "cmd=cardiosession";
	
	/** Fetching all of the supplements. */
	public static final String VIEW_SUPPS = GUI.URL + "cmd=viewsupplements";
	
	/** Rating a supplement. */
	public static final String RATE_SUPP = GUI.URL + "cmd=ratesupplements";
	
	/** Rating a workout. */
	public static final String RATE_WORKOUT = GUI.URL + "cmd=rateworkout";
	
	/** Fetch logged workouts. */
	public static final String VIEW_LOGGED_WORKOUTS = GUI.URL + "cmd=viewloggedworkout" 
												+ "&email=";

	public boolean isFirst = true;
	
	/**  The main JPanel for this class. */
	private final JPanel myPanel;
	
	/** A mapping of exercise names to their buttons. */
	private final Map<String, JButton> buttons;
	
	/** The main panel for viewing and logging workouts. */
	private JPanel myWorkoutPanel;
	
	/** Combo box displaying each day of the week. */
	private JComboBox<String> myDays;
	
	// Exercises for current workout
	/** Exercise one. */
	private JButton exercise1;
	
	/** Exercise two. */
	private JButton exercise2;
	
	/** Exercise three. */
	private JButton exercise3;
	
	/** Exercise four. */
	private JButton exercise4;
	
	/** The current weight workout being performed. */
	private WeightWorkout workout = null;
	
	/** Label displaying the name of the current workout. */
	private JLabel currentWorkout;
	
	/** Button for adding a cardio workout. */
	private JButton addCardioWorkout;
	
	/** Button for finishing a weight workout. */
	private JButton finish;
	
	/** The name of the cardio exercise completed. Default empty. */
	private String cardioName = "";
	
	/** Maps the JButtons for logged workouts to their respective workout numbers. */
	private Map<JButton, String> loggedWorkouts;
	
	private JPanel loggedWorkoutsPanel;

	/** Initialize a new GymUserPanel. */
	public GymUserPanel() {
		myPanel = new JPanel();
		buttons = new HashMap<>();
		myWorkoutPanel = new JPanel();
		currentWorkout = new JLabel("Current Workout: ");
		loggedWorkouts = new HashMap<JButton, String>();
		myDays = new JComboBox<String>();
	}
	
	/** Set up the main panel and all of its components. */
	public void setUp() {
		isFirst = false;
		myPanel.setBackground(Color.WHITE);
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		final JPanel supplementPanel = setUpSupplementPanel();
		final JButton viewWorkoutsButton = new JButton("View Workouts");
		final JButton scrollPaneSupplements = new JButton("View Supplements");
		final JLabel scrollPaneWorkouts = new JLabel("View Daily Workouts");
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
		addComboBoxListener();
		centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		centerPanel.setBackground(Color.WHITE);
		northPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		northPanel.setBackground(Color.WHITE);
		myPanel.add(northPanel, BorderLayout.NORTH);
		
		centerPanel.add(scrollPaneWorkouts);
		centerPanel.add(myDays);
		centerPanel.add(currentWorkout);
		myWorkoutPanel = getScrollPanel();
		northPanel.add(viewWorkoutsButton);
		northPanel.add(scrollPaneSupplements);
		northPanel.add(logout);
		northPanel.setMaximumSize(new Dimension(600, (int) northPanel.getMinimumSize().getHeight()));
		centerPanel.setMaximumSize(new Dimension(600, (int) centerPanel.getMinimumSize().getHeight()));
		myPanel.add(Box.createVerticalStrut(10));
		myPanel.add(centerPanel, BorderLayout.CENTER);
		myPanel.add(Box.createVerticalStrut(10));
		myPanel.add(myWorkoutPanel);
		setListeners(logout, scrollPaneSupplements);
		viewWorkoutsButton.setEnabled(false);
		viewWorkoutsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				myPanel.remove(supplementPanel);
				myPanel.add(myWorkoutPanel);
				viewWorkoutsButton.setEnabled(false);
				scrollPaneSupplements.setEnabled(true);
				myPanel.revalidate();
				myPanel.repaint();
			}
		});
		scrollPaneSupplements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				myPanel.remove(myWorkoutPanel);
				myPanel.add(supplementPanel);
				viewWorkoutsButton.setEnabled(true);
				scrollPaneSupplements.setEnabled(false);
				myPanel.repaint();
				myPanel.revalidate();
			}
		});
		myDays.setSelectedIndex(1);
	}
	
	/**
	 * Set up the supplements panel.
	 * 
	 * @return the supplements panel
	 */
	private JPanel setUpSupplementPanel() {
		final JPanel panel = new JPanel();

		panel.setBackground(Color.WHITE);
		try {
			String[] header = new String[] {"Name", "Price", "Fat", "Carbs", "Protein", 
					"Calories", "Rating"};
			String result = GUI.webConnect(VIEW_SUPPS);
			JSONArray arr = new JSONArray(result);
			Object[][] data = new Object[arr.length()][7];
			JComboBox<String> ratings = new JComboBox<String>();
			ratings.addItem("Choose to Rate");
			ratings.setBackground(Color.WHITE);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				data[i][0] = obj.getString("SupplementName");
				data[i][1] = obj.getString("Price");
				data[i][2] = obj.getString("Fat");
				data[i][3] = obj.getString("Carbs");
				data[i][4] = obj.getString("Protein");
				data[i][5] = obj.getString("Calories");
				ratings.addItem(obj.getString("SupplementName"));
				if (!obj.get("Rating").equals(null)) {
					data[i][6] = obj.getString("Rating");
				} else {
					data[i][6] = 0;
				}
			}
			final JTable table = new JTable(new DefaultTableModel(data, header)) {
				/** A generated Serial Version UID. */
				private static final long serialVersionUID = -4651154286147356726L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			final JScrollPane pane = new JScrollPane(table);
			
			pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			table.getTableHeader().setReorderingAllowed(false);
			table.setPreferredSize(new Dimension(580, 400 - table.getRowHeight() - 10));
			table.getColumnModel().getColumn(0).setPreferredWidth(200);
			pane.setBackground(Color.WHITE);
			pane.setPreferredSize(new Dimension(580, 400));
			ratings.addItemListener(new ItemListener() {
				private String index = "";
				@Override
				public void itemStateChanged(ItemEvent arg0) {
					if (ratings.getSelectedIndex() > 0 
							&& !index.equals(ratings.getSelectedItem())) {
						index = ratings.getSelectedItem().toString();
						JPanel panel = new JPanel();
						JLabel label = new JLabel("Rate " 
						+ ratings.getSelectedItem().toString());
						JComboBox<Integer> rate = new JComboBox<Integer>();
						for (int i = 1; i < 6; i++) {
							rate.addItem(i);
						}
						panel.setBackground(Color.WHITE);
						panel.add(label);
						panel.add(rate);
						if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(myPanel, 
								panel, "Rate a supplement", JOptionPane.OK_CANCEL_OPTION)) {
							String url = RATE_SUPP + "&email=" + GUI.EMAIL + "&name=" 
								+ index.replaceAll(" ", "_") + "&rate=" 
									+ rate.getSelectedItem();
							if (GUI.webConnect(url).contains("failure")) {
								JOptionPane.showMessageDialog(
			            		        myPanel, "You already rated this supplement!",
			            		        "Failure", JOptionPane.ERROR_MESSAGE);	
							} else {
								JOptionPane.showMessageDialog(
			            		        myPanel, "Rating successful! new rating for supplement"
			            		        		+ " will be shown next time you log in.",
			            		        "Failure", JOptionPane.INFORMATION_MESSAGE);	
							}
						}
						
					}
				}
				
			});
			panel.add(pane);
			pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			panel.add(ratings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return panel;
		
	}
	
	/**
	 * For logging a cardio workout. 
	 */
	private void cardioWorkout() {
		enableButtons(false);
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
				"Enter your cardio workout information", 
				JOptionPane.OK_CANCEL_OPTION)) {
			int num = getWorkoutNumber();
			String url = ADD_CARDIO + "&email=" + GUI.EMAIL + "&name=" +  cardioName
					+ "&num=" + num 
					+ "&dur=" + durationOptions.getSelectedItem()
					+ "&int=" + intensityOptions.getSelectedItem();
			GUI.webConnect(url);
			rateWorkout(cardioName);
			JButton button = new JButton(cardioName);
			loggedWorkouts.put(button, (loggedWorkouts.size() + 1) + "");
			loggedWorkoutsPanel.setPreferredSize(new Dimension(200, loggedWorkouts.size() * 30));
			addLoggedWorkoutActionListener(button);
		}
	}
	
	/** @param bool enable or disable buttons according to this value. */
	private void enableButtons(boolean bool) {
		// Exercise buttons only enabled when on the workouts panel
		exercise1.setEnabled(bool);
		exercise2.setEnabled(bool);
		exercise3.setEnabled(bool);
		exercise4.setEnabled(bool);
		// Only enabled when the current day selected is a cardio workout
		addCardioWorkout.setEnabled(!bool);
		// Only enabled for a weight workout
		finish.setEnabled(bool);
	}	
	
	/**
	 * Set up the JPanel for viewing logged workouts.
	 * 
	 * @param panel the panel for viewing logged workouts
	 */
	private void addLoggedWorkouts(JPanel panel) {
		try {
			JSONArray arr = new JSONArray(GUI.webConnect(VIEW_LOGGED_WORKOUTS + GUI.EMAIL));
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				JButton button = new JButton(obj.getString("WorkoutName"));
				addLoggedWorkoutActionListener(button);
				loggedWorkouts.put(button, obj.getString("WorkoutNumber"));
			}
			for (JButton b : loggedWorkouts.keySet()) {
				panel.add(b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		panel.setPreferredSize(new Dimension(100, 200));
		panel.setBackground(Color.WHITE);
	}	
	
	/** 
	 * Add listener for the logged workout buttons.
	 * 
	 * @param button current button
	 */
	private void addLoggedWorkoutActionListener(JButton button) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
				for (JButton b : loggedWorkouts.keySet()) {
					if (event.getActionCommand().equals(b.getText())) {
						JPanel p = new JPanel();
						JScrollPane pane = new JScrollPane(p);
						pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
						String url = GUI.URL + "cmd=getallexercises&email=" + GUI.EMAIL 
								+ "&num=" + loggedWorkouts.get(b);
						String result = GUI.webConnect(url);
						JSONArray arr = new JSONArray(result);
						if (result.contains("Intensity")) {
							JSONObject obj = arr.getJSONObject(0);
							JLabel intensity = new JLabel("Intensity: " + obj.getString("Intensity"));
							JLabel duration = new JLabel("Duration: " + obj.getString("Duration"));
							p.add(intensity);
							p.add(duration);
							JOptionPane.showMessageDialog(myPanel, pane, 
									"Workout Information for " + obj.getString("WorkoutName"),
									JOptionPane.OK_CANCEL_OPTION);
						} else {
							Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
							for (int i = 0; i < arr.length(); i++) {
								JSONObject obj = arr.getJSONObject(i);
								String exName = obj.getString("ExerciseName");
								if (!map.containsKey(exName)) {
									map.put(exName, new ArrayList<String[]>());
								}
								map.get(exName).add(new String[]{obj.getString("Weight"),
										obj.getString("Repetitions")});
							}
							for (String s : map.keySet()) {
								JLabel name = new JLabel("Exercise: " + s);
								p.add(name);
								for (String[] i : map.get(s)) {
									JLabel weight = new JLabel("        Weight: " + i[0]);
									JLabel reps = new JLabel("        Reps: " + i[1]);
									p.add(weight);
									p.add(reps);
								}
							}
							JOptionPane.showConfirmDialog(myPanel, pane, 
									"Workout Information for " + b.getText(),
									JOptionPane.OK_CANCEL_OPTION);
						}
					}
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	/**
	 * Set up the listener for the combobox representing the days.
	 */
	private void addComboBoxListener() {
		myDays.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent event) {
				try {
					String item = event.getItem().toString();
					String result = GUI.webConnect(GUI.URL + VIEW_WORKOUTS_URL + item);
					JSONArray arr = new JSONArray(result);
					JSONObject obj0 = arr.getJSONObject(0);
					String name = obj0.getString("WorkoutName");
					currentWorkout.setText("Current Workout: " + name);
					if (result.contains("WorkoutDescription")) {
						cardioName = name;
						enableButtons(false);
					} else {
							enableButtons(true);
		                    JSONObject obj1 = arr.getJSONObject(1);
		                    JSONObject obj2 = arr.getJSONObject(2);
		                    JSONObject obj3 = arr.getJSONObject(3);
		
			                workout = new WeightWorkout(name);
			                exercise1.setText(obj0.getString("Exercise"));
			                exercise2.setText(obj1.getString("Exercise"));
			                exercise3.setText(obj2.getString("Exercise"));
			                exercise4.setText(obj3.getString("Exercise"));
					}
	            } catch (JSONException e) {
	            	e.printStackTrace();
	            }
			}
			
		});
	}
	
	/** @return the workout number fetched from the database. */
	private int getWorkoutNumber() {
		String workoutNumber = GUI.webConnect(GUI.URL 
				+ "&cmd=getworkoutnumber&email=" + GUI.EMAIL);
		JSONArray arr;
		try {
			arr = new JSONArray(workoutNumber);
			JSONObject obj = arr.getJSONObject(0);
			int num = Integer.parseInt(obj.getString("count")) + 1;
			return num;
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Set up listener for adding an exercise.
	 * 
	 * @param button for the exercise
	 */
	private void getExerciseListneers(JButton button) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				JPanel repsPanel = new JPanel();
				JPanel weightPanel = new JPanel();
				JLabel repsLabel = new JLabel("Reps: ");
				JLabel weightLabel = new JLabel("Weight: ");
				JTextField weight = new JTextField(5);
				JTextField reps = new JTextField(5);
				repsPanel.add(repsLabel);
				repsPanel.add(reps);
				weightPanel.add(weightLabel);
				weightPanel.add(weight);
				panel.add(weightPanel);
				panel.add(repsPanel);
				if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(myPanel, panel, 
						"Enter your set information", JOptionPane.OK_CANCEL_OPTION)) {
					try {
						if (weight.getText().equals("") || reps.getText().equals("")) {
							throw new IllegalArgumentException();
						}
						int w = Integer.parseInt(weight.getText());
						int r = Integer.parseInt(reps.getText());
						workout.addSet(button.getText(), w, r);
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(
	            		        myPanel, "Must enter numbers only!", "Failure", 
	            		        JOptionPane.ERROR_MESSAGE);
					
					} catch (IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(
	            		        myPanel, "Both fields required", "Failure", 
	            		        JOptionPane.ERROR_MESSAGE);
					
					}
					
				}
					
			}
			
		});
	}
	
	/** @return the main JPanel for the GymUSerPanel for the user to log workouts. */
	private JPanel getScrollPanel() {
		final JPanel innerPanel = new JPanel();
		loggedWorkoutsPanel = new JPanel();
		loggedWorkoutsPanel.setLayout(new BoxLayout(loggedWorkoutsPanel, BoxLayout.Y_AXIS));
//		loggedWorkoutsPanel.add(new JLabel("Logged Workouts"));
		addLoggedWorkouts(loggedWorkoutsPanel);
		JScrollPane pane = new JScrollPane(loggedWorkoutsPanel);
		pane.setMaximumSize(new Dimension(200, 200));
		loggedWorkoutsPanel.setPreferredSize(new Dimension(100, loggedWorkouts.size() * 30));
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		final JLabel text = new JLabel("<html>To log a workout, press one of the exercise "
				+ "buttons to add a set.<br>Workouts are only saved if finish is "
				+ "pressed</html>");
		final JPanel panel = new JPanel();
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		innerPanel.add(text);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		panel.setMinimumSize(new Dimension(200, 200));
		exercise1 = new JButton();
		exercise2 = new JButton();
		exercise3 = new JButton();
		exercise4 = new JButton();
		getExerciseListneers(exercise1);
		getExerciseListneers(exercise2);
		getExerciseListneers(exercise3);
		getExerciseListneers(exercise4);
		innerPanel.add(exercise1);
		innerPanel.add(Box.createVerticalStrut(5));
		innerPanel.add(exercise2);
		innerPanel.add(Box.createVerticalStrut(5));
		innerPanel.add(exercise3);
		innerPanel.add(Box.createVerticalStrut(5));
		innerPanel.add(exercise4);
		innerPanel.add(Box.createVerticalStrut(5));
		
		
		
		panel.setBackground(Color.WHITE);
		innerPanel.setBackground(Color.WHITE);
//		panel.add(scrollPane);
		panel.add(innerPanel);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel bottom = new JPanel();
		bottom.setBackground(Color.WHITE);
		finish = new JButton("Finish");
		addCardioWorkout = new JButton("Log Cardio Workout");
		finish.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (workout != null && workout.hasWorkout()) {
					int num = getWorkoutNumber();
					Map<String, List<int[]>> exercises = workout.getExercises();
					GUI.webConnect(ADD_WORKOUT + "&num=" + num 
							 	+ "&name=" + workout.getName() + "&email=" + GUI.EMAIL);
					int set = 1;
					for (String s : exercises.keySet()) {
						List<int[]> currentList = exercises.get(s);
						for (int[] a : currentList) {
							String url = ADD_SET + "&email=" 
									+ GUI.EMAIL +  "&weight=" + a[0] + "&reps=" + a[1] + "&name=" + s.replaceAll(" ", "_") 
									+ "&num=" + num + "&set=" + set;
							GUI.webConnect(url);
							set++;
						}
					}	
					JButton button = new JButton(workout.getName());
					loggedWorkouts.put(button, (loggedWorkouts.size() + 1) + "");
					loggedWorkoutsPanel.add(button);
					loggedWorkoutsPanel.setPreferredSize(new Dimension(200, loggedWorkouts.size() * 30));
					addLoggedWorkoutActionListener(button);
					myPanel.revalidate();
					myPanel.repaint();
					rateWorkout(workout.getName());
					workout.clearExercises();
				}
				
			}
			
		});
		addCardioWorkout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cardioWorkout();
			}
			
		});
		bottom.add(finish);
		bottom.add(addCardioWorkout);
		panel.add(bottom);
		panel.add(new JLabel("Logged Workouts"));
		panel.add(pane);
		return panel;
	}
	
	/** @param name rate the workout with the passed name. */
	private void rateWorkout(String name) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("How would you rate this workout? " );
		JComboBox<Integer> rate = new JComboBox<Integer>();
		for (int i = 1; i < 6; i++) {
			rate.addItem(i);
		}
		panel.setBackground(Color.WHITE);
		panel.add(label);
		panel.add(rate);
		if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(myPanel, panel, 
				"Please rate this workout", 
				JOptionPane.OK_CANCEL_OPTION)) {
			String url = (RATE_WORKOUT + "&email=" + GUI.EMAIL + "&name=" + name + "&rate=" 
					+ rate.getSelectedItem());
			url = url.replaceAll(" ", "_");
			if (GUI.webConnect(url).contains("failure")) {
				JOptionPane.showMessageDialog(
        		        myPanel, "You already rated this workout! Workouts can only be rated once",
        		        "Failure", JOptionPane.ERROR_MESSAGE);	
			} else {
				JOptionPane.showMessageDialog(
        		        myPanel, "Rating successful! Thank you for completing this workout",
        		        "Failure", JOptionPane.INFORMATION_MESSAGE);	
			}
		}
				
	}
	
	/**
	 * Set listeners for the logout and view supplements buttons. 
	 * @param logout button to logout
	 * @param supplements button to view supplements
	 */
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
	
	/** @return this JPanel. */
	public JPanel getPanel() {
		return myPanel;
	}
}
