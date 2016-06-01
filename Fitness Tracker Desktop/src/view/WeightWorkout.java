package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class to represent a weight workout.
 * 
 * @author jonah Howard
 */
public class WeightWorkout {
	/** The name of the workout. */
	private String name;
	/** Mapping of all exercises to their sets. */
	private Map<String, List<int[]>> exercises;
	
	/** @param name initialize new WeightWorkout with name. */
	public WeightWorkout(String name) {
		exercises = new TreeMap<>();
		this.name = name;
	}
	
	/**
	 * Add a set to this workout. 
	 * 
	 * @param name exercise name
	 * @param weight weight used
	 * @param reps reps performed
	 */
	public void addSet(String name, int weight, int reps) {
		int[] val = new int[] {weight, reps};
		if (!exercises.containsKey(name)) {
			exercises.put(name, new ArrayList<int[]>());
		}
		exercises.get(name).add(val);
	}
	
	/** @return all of the exercises for this workout. */
	public Map<String, List<int[]>> getExercises() {
		return exercises;
	}

	/**
	 * Remove all exercises for this workout.
	 */
	public void clearExercises() {
		exercises.clear();
	}
	
	/** @return the name of this workout. */
	public String getName() {
		return name;
	}
	
	/** @return wether this workout has any exercises. */
	public boolean hasWorkout() {
		return !exercises.isEmpty();
	}
}
