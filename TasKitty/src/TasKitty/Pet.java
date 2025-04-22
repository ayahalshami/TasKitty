/* Author: Ayah Alshami
 * CSI 2300
 * April 18 2025
 * This file defines the pet class, which sets up the behavior of pet object
 * pet levels up with point system
 * maintains task history
  */

package TasKitty;

import java.io.Serializable;
import java.util.List;

public class Pet implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int level;
    private int focusPoints;
    private TaskHistory taskHistory = new TaskHistory(10); // creates object to store history
    private int numStages; // store
    private int totalTasksCompleted = 0;

    public Pet(String name) {
        this(name, 3); // defaults to 3 stages
    }
    
    public void addFocusPoints(int minutes) {
        focusPoints += minutes;
        totalTasksCompleted++;  // add to total tasks
        checkLevelUp();
    }
    
    public int getTotalTasksCompleted() {
        return totalTasksCompleted;
    }

    // initializes new instance of pet
    public Pet(String name, int numStages) {
        this.name = name;
        this.level = 1;
        this.focusPoints = 0;
        this.numStages = numStages; 
    }


    // sets amount of levels needed per stage. default is 30 but i played with this a lot for ease when debugging
    protected void checkLevelUp() {
        if (focusPoints >= level * 30) {
            level++;
            focusPoints = 0;
            System.out.println(name + " leveled up! Now level " + level);
        }
    }
    // task information
    public void logTask(String taskDescription) {
        taskHistory.addTask(taskDescription);
    }
    
    // returns list of previous tasks
    public List<String> getTaskHistory() {
        return taskHistory.getTasks();
    }

    public String getName() {
        return name;
    }

    // getter and setter
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFocusPoints() {
        return focusPoints;
    }

    // get pet type for images
    public String getType() {
        return "pet"; 
    }

    // handles stage logic. this method is overriden in subclasses for different behaviors
    public String getStage() {
        if (level == 1) return "egg";
        else if (level <= numStages) return "baby"; 
        else return "teen"; 
    }
}