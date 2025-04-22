/* Author: Ayah Alshami
 * CSI 2300
 * April 18 2025
 * initializes array for task storage
 * array stores a set amount of max tasks before overwriting old task
  */

package TasKitty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskHistory implements Serializable { // allows tasks to be saved and called across sessions
    private static final long serialVersionUID = 1L;    private final List<String> history;
    private final int maxSize; //cannot be changed when initialized

    // initializes empty array
    public TaskHistory(int maxSize) {
        this.history = new ArrayList<>();
        this.maxSize = maxSize;
    }
    
    // adds new task to list
    public void addTask(String taskDescription) {
        if (taskDescription == null || taskDescription.trim().isEmpty()) return;

        if (history.size() == maxSize) {
            history.remove(0); // remove oldest task once array is full
        }
        history.add(taskDescription);
    }
    // returns list of completed tasks
    public List<String> getTasks() {
        return new ArrayList<>(history); // return a copy to prevent issues with modification
    }

    public void clearHistory() {
        history.clear();
    }

    public int size() {
        return history.size();
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }
}