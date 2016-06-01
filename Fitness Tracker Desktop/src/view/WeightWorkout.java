package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WeightWorkout {
	private String name;
	private Map<String, List<int[]>> exercises;
	
	public WeightWorkout(String name) {
		exercises = new TreeMap<>();
		this.name = name;
	}
	
	public void addSet(String name, int weight, int reps) {
		int[] val = new int[] {weight, reps};
		if (!exercises.containsKey(name)) {
			exercises.put(name, new ArrayList<int[]>());
		}
		exercises.get(name).add(val);
	}
	
	public Map<String, List<int[]>> getExercises() {
		return exercises;
	}
	
	
	public void clearExercises() {
		exercises.clear();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasWorkout() {
		return !exercises.isEmpty();
	}
}
