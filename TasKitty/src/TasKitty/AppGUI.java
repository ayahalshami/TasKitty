/* Author: Ayah Alshami
 * CSI 2300
 * April 18 2025
 * This file defines the AppGUi class, which sets up the ui for my app
 * It creates scenes with vboxes and stackplanes for layout
 * Handles user interaction
 * it also utilizes the other classes  in order to increase functionality of app
 * */
package TasKitty;


import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AppGUI extends Application {

    private Pet pet = null; //creates pet object to be initialized
    private Timeline timeline;
    private int secondsRemaining;
    private String currentTask = "";

    // buttons for pet choices
    private ToggleGroup petTypeToggle = new ToggleGroup(); 
    private RadioButton siameseButton;
    private RadioButton tabbyButton;

    private Label timerLabel = new Label("Time: 00:00");  //timer
    private ImageView petImageView; //image of pet
    private Label petStatsLabel = new Label();

    // sets up vboxes for different layouts
    private VBox inputLayout;
    private VBox timerLayout;
    private VBox completeLayout;
    private VBox welcomeLayout;
    private VBox catSelectionLayout;
    

    // method defines ui for app
    @Override
    public void start(Stage primaryStage) {
       // sets up stackplane for ui elements
        StackPane rootLayout = new StackPane();
        rootLayout.setAlignment(Pos.CENTER); 

        // add background image and accounts for if image doesnt load
        try {
            Image backgroundImage = new Image(getClass().getResourceAsStream("/images/background.png"));
            BackgroundImage background = new BackgroundImage(
                backgroundImage, 
                BackgroundRepeat.NO_REPEAT, // x direction
                BackgroundRepeat.NO_REPEAT, // y direction
                BackgroundPosition.CENTER, 
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            rootLayout.setBackground(new Background(background));
        } catch (Exception e) {
            rootLayout.setStyle("-fx-background-color: #fdf6ec;");
            System.err.println("Error loading background image: " + e.getMessage());
        }
        
        // vbox for main layout
        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(15));
        mainLayout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);"); // Semi-transparent background
        
        // petheader which is always visible
        VBox petHeader = new VBox(5);
        petHeader.setAlignment(Pos.CENTER);
        
        // pet image
        petImageView = new ImageView();
        petImageView.setFitWidth(150);
        petImageView.setPreserveRatio(true);
        
        // pet stats
        petStatsLabel.setText("Choose your cat!");
        
        // add pet image and stats to the header
        petHeader.getChildren().addAll(petImageView, petStatsLabel);
        
        // content changes based on state
        StackPane contentArea = new StackPane();
        contentArea.setAlignment(Pos.CENTER);
        
        // add both sections to the main layout
        mainLayout.getChildren().addAll(petHeader, contentArea);
        
        // creates welcome layout
        welcomeLayout = new VBox(10);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setPadding(new Insets(20));
        
        Label welcomeLabel = new Label("🐾 Welcome to TasKitty!");
        welcomeLabel.setStyle("-fx-font-size: 20;");
        
        TextField nameField = new TextField();
        nameField.setPromptText("Name your cat");
        
        // sets up cat selection
        catSelectionLayout = new VBox(10);
        catSelectionLayout.setAlignment(Pos.CENTER);
        setupCatSelection();
        
        // buttons for game type
        Button loadGameButton = new Button("Continue Game");
        Button newGameButton = new Button("New Game");
        
        // adds all sections to welcome layout and sets visibility true
        
        welcomeLayout.getChildren().addAll(welcomeLabel, nameField, catSelectionLayout, newGameButton, loadGameButton);
        welcomeLayout.setVisible(true);
        
        // input layout
        Label taskLabel = new Label("What task are you getting done?");
        TextField focusTaskField = new TextField();
        focusTaskField.setPromptText("Enter task");
        
        Label timeLabel = new Label("For how long?");
        TextField minutesField = new TextField();
        minutesField.setPromptText("Enter minutes");
        
        Button startButton = new Button("Start Task");
        
        // layout for input
        inputLayout = new VBox(10);
        inputLayout.setAlignment(Pos.CENTER);
        inputLayout.getChildren().addAll(taskLabel, focusTaskField, timeLabel, minutesField, startButton);
        inputLayout.setVisible(false);
        
        // layout for timer
        timerLabel.setStyle("-fx-font-size: 22; -fx-text-fill: #333;");
        timerLayout = new VBox(15);
        timerLayout.setAlignment(Pos.CENTER);
        timerLayout.getChildren().add(timerLabel);
        timerLayout.setVisible(false); //hidden until needed
        
        // end screen layout
        completeLayout = new VBox(15);  
        completeLayout.setAlignment(Pos.CENTER); 
        Label completeLabel = new Label("Task Complete!");
        completeLabel.setStyle("-fx-font-size: 20;");
        
        Button restartButton = new Button("Start Another Task?");
        restartButton.setOnAction(e -> resetToInput());
        
        Button showHistoryButton = new Button("Show Task History");
        showHistoryButton.setOnAction(e -> showTaskHistory());
        
        completeLayout.getChildren().addAll(completeLabel, restartButton, showHistoryButton);
        completeLayout.setVisible(false); // hides  until needed
        
        
        // adds all content layouts to the content area
        contentArea.getChildren().addAll(welcomeLayout, inputLayout, timerLayout, completeLayout);
        
        // adds  the main layout to root
        rootLayout.getChildren().add(mainLayout);
        
        // updates the preview image initially
        updatePreviewImage();
        
        // button logic
        newGameButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showAlert("Please enter a name for your cat!"); //sends alert if pet name is blank
                return;
            }
            
            // utilize pet class based on selection
            if (siameseButton.isSelected()) {
                pet = new SiameseCat(name);
            } else {
                pet = new TabbyCat(name);
            }
            
            SaveManager.savePet(pet);  //save choice
            petStatsLabel.setText(getPetStats());
            updatePetImage();
            
            welcomeLayout.setVisible(false);
            inputLayout.setVisible(true); //handles visibility again
        });
        
        loadGameButton.setOnAction(e -> {
            try {
                pet = SaveManager.loadPet();
                if (pet == null) {
                    showAlert("No save file found. Please start a new game."); //gives an error if user attempts to load nonexistent file
                } else {
                    petStatsLabel.setText(getPetStats());
                    updatePetImage();
                    
                    welcomeLayout.setVisible(false);
                    inputLayout.setVisible(true); //visibility
                }
            } catch (Exception ex) {
                showAlert("Error loading game: " + ex.getMessage());
            }
        });        
        // start button logic
        startButton.setOnAction(e -> {
            String task = focusTaskField.getText();
            currentTask = task;
            String minutesText = minutesField.getText();
            
            if (task.isEmpty() || minutesText.isEmpty()) {
                showAlert("Please fill in both fields."); //sends error if field is empty
                return;
            }
            
            int minutes;
            try {
                minutes = Integer.parseInt(minutesText);
            } catch (NumberFormatException ex) {
                showAlert("Please enter a valid number of minutes."); //sends error if input isnt an int
                return;
            }
            
            startFocusTimer(minutes);  // start the  timer
            
            inputLayout.setVisible(false);
            timerLayout.setVisible(true);
        });
        
        // create  the scene and show the stage
        Scene scene = new Scene(rootLayout, 450, 550);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Taskitty 🐾");
        primaryStage.setScene(scene);
        primaryStage.show();
    }   	
    	
    private void setupCatSelection() {
        Label catTypeLabel = new Label("Choose your cat type:");
        
        siameseButton = new RadioButton("Siamese Cat");
        tabbyButton = new RadioButton("Tabby Cat");
        
        // Add buttons to toggle group
        siameseButton.setToggleGroup(petTypeToggle);
        tabbyButton.setToggleGroup(petTypeToggle);
        
        // Set default selection
        tabbyButton.setSelected(true);
        
        // Add listeners to update preview image when selection changes
        siameseButton.setOnAction(e -> updatePreviewImage());
        tabbyButton.setOnAction(e -> updatePreviewImage());
        
        // Create an HBox for the radio buttons
        HBox catTypeOptions = new HBox(20);
        catTypeOptions.setAlignment(Pos.CENTER);
        catTypeOptions.getChildren().addAll(siameseButton, tabbyButton);
        
        // Add everything to the cat selection layout
        catSelectionLayout.getChildren().addAll(catTypeLabel, catTypeOptions);
    }
    
    // changes view based on which pet is selected
    private void updatePreviewImage() {
        String type = siameseButton.isSelected() ? "siamese" : "tabby";
        try {
            String imagePath = "/images/" + type + "/egg.png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            petImageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading preview image: " + e.getMessage());
        }
    }
        
    //displays task history using array
    private void showTaskHistory() {
        if (pet == null || pet.getTaskHistory().isEmpty()) {
            showAlert("No task history available.");
            return;
        }
        
        List<String> history = pet.getTaskHistory();
        StringBuilder historyText = new StringBuilder("Recent Tasks:\n");
        for (String task : history) {
            historyText.append("- ").append(task).append("\n");
        }
        showAlert(historyText.toString());
    }
    // starts a timer for given time
    private void startFocusTimer(int minutes) {
        secondsRemaining = minutes * 60;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsRemaining--;
            updateTimerLabel();

            if (secondsRemaining <= 0) {
                timeline.stop();
                focusComplete(minutes);
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    // updates the timer second by second
    private void updateTimerLabel() {
        int mins = secondsRemaining / 60;
        int secs = secondsRemaining % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", mins, secs));
    }

    // gives pet points when timer ends
    private void focusComplete(int minutesFocused) {
        pet.addFocusPoints(minutesFocused);
        
        // log the task that was  completed
        pet.logTask(currentTask + " (" + minutesFocused + " min)"); 

        SaveManager.savePet(pet);
        petStatsLabel.setText(getPetStats());
        updatePetImage();
        
        // show complete screen visibility 
        timerLayout.setVisible(false);
        completeLayout.setVisible(true);
    }
    // resets screen and updates pet image
    private void resetToInput() {
        updatePetImage();
        petStatsLabel.setText(getPetStats());

        completeLayout.setVisible(false);
        inputLayout.setVisible(true);
    }
    
    // loads pet image depending on type/level
    private void updatePetImage() {
        if (pet == null) return;
        
        String type = pet.getType();
        String stage = pet.getStage();
        try {
            String imagePath = "/images/" + type + "/" + stage + ".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            petImageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image for type: " + type + ", stage: " + stage);
            System.err.println(e.getMessage());
        }
    }

    // returns string showing pet name level and task count for header
    private String getPetStats() {
        if (pet == null) return "No pet loaded";
        return "Pet: " + pet.getName() + " | Level: " + pet.getLevel() + 
               " | Tasks Completed: " + pet.getTotalTasksCompleted();
    }
    // displays popup
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("TasKitty");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // saves pet progress when app is closing
    @Override
    public void stop() {
        if (pet != null) {
            try {
                SaveManager.savePet(pet);
            } catch (Exception e) {
                System.err.println("Error saving pet on exit: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}