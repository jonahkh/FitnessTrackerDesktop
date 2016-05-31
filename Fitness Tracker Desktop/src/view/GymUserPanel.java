package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class GymUserPanel extends Observable {
	public static final String VIEW_WORKOUTS_URL = "cmd=viewworkouts&day=";
	public static final String ADD_SET = GUI.URL + "cmd=addset";
	public static final String ADD_WORKOUT = GUI.URL + "cmd=logworkout";
	public static final String ADD_CARDIO = GUI.URL + "cmd=cardio";
	private final JPanel myPanel;
	private final Map<String, JButton> buttons;
	private JPanel myWorkoutPanel;
	private JComboBox<String> myDays;
	private JScrollPane scrollPane;
	private JButton exercise1;
	private JButton exercise2;
	private JButton exercise3;
	private JButton exercise4;
	private WeightWorkout workout = null;
	private JLabel currentWorkout;
	private JButton addCardioWorkout;
	private JButton finish;

	
	public GymUserPanel() {
		myPanel = new JPanel();
		buttons = new HashMap<>();
		myWorkoutPanel = new JPanel();
		currentWorkout = new JLabel("Current Workout: ");
		myDays = new JComboBox<String>();
		setUp();
		myDays.setSelectedIndex(1);
	}
	
	private void setUp() {
		myPanel.setBackground(Color.WHITE);
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
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
		northPanel.add(scrollPaneSupplements);
		northPanel.add(logout);
		northPanel.setMaximumSize(new Dimension(600, (int) northPanel.getMinimumSize().getHeight()));
		centerPanel.setMaximumSize(new Dimension(600, (int) centerPanel.getMinimumSize().getHeight()));
		myPanel.add(Box.createVerticalStrut(10));
		myPanel.add(centerPanel, BorderLayout.CENTER);
		myPanel.add(Box.createVerticalStrut(10));
		myPanel.add(myWorkoutPanel);
//		myPanel.remove(scrollPanel);
		setListeners(logout, scrollPaneSupplements);
	}
	
	
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
			String url = ADD_CARDIO + "&email=" + GUI.EMAIL + "&num=" + num 
					+ "&dur=" + durationOptions.getSelectedItem()
					+ "&int=" + intensityOptions.getSelectedItem();
			System.out.println(url);
//			System.out.println(GUI.webConnect(url));
		}
	}
	
	private void enableButtons(boolean bool) {
		exercise1.setEnabled(bool);
		exercise2.setEnabled(bool);
		exercise3.setEnabled(bool);
		exercise4.setEnabled(bool);
		addCardioWorkout.setEnabled(!bool);
		finish.setEnabled(bool);
	}	
		
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
	            		        myPanel, "Must enter numbers only!", "Failure", JOptionPane.ERROR_MESSAGE);
					
					} catch (IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(
	            		        myPanel, "Both fields required", "Failure", JOptionPane.ERROR_MESSAGE);
					
					}
					
				}
					
			}
			
		});
	}
	
	private JPanel getScrollPanel() {
		final JPanel innerPanel = new JPanel();
//		scrollPane = new JScrollPane(innerPanel);
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
					System.out.println(GUI.webConnect(ADD_WORKOUT + "&num=" + num 
							+ "&name=" + workout.getName() + "&email=" + GUI.EMAIL));
					Map<String, List<int[]>> exercises = workout.getExercises();
					int set = 1;
					for (String s : exercises.keySet()) {
						List<int[]> currentList = exercises.get(s);
						for (int[] a : currentList) {
							System.out.println(GUI.webConnect(ADD_SET + "&email=" 
						+ GUI.EMAIL +  "&weight=" + a[0] + "&reps=" + a[1] + "&name=" + s 
						+ "&num=" + num + "&set=" + set));
							set++;
						}
					}					
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
