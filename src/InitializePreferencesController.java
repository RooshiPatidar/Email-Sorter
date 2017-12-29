import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.prefs.Preferences;

public class InitializePreferencesController {

    @FXML private CheckBox deleteCheckBox;
    @FXML private CheckBox readCheckBox;
    @FXML private CheckBox inCheckBox;
    @FXML private CheckBox outCheckBox;
    @FXML private CheckBox separateCheckBox;
    @FXML private ComboBox<String> saveDelayComboBox;
    @FXML private Button nextButton;


    @FXML
    public void initialize() {
        nextButton.setDisable(true);

        ObservableList<String> saveDelays =
                FXCollections.observableArrayList();
        saveDelays.add(0, "Any age");
        saveDelays.add(1, "1 Week");
        saveDelays.add(2, "2 Weeks");
        saveDelays.add(3, "1 Month");
        saveDelayComboBox.setItems(saveDelays);
        separateCheckBox.setDisable(true);

        inCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (inCheckBox.isSelected() && outCheckBox.isSelected()){
                separateCheckBox.setDisable(false);
            } else {
                separateCheckBox.setSelected(false);
                separateCheckBox.setDisable(true);
            }
        });
        outCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (inCheckBox.isSelected() && outCheckBox.isSelected()){
                separateCheckBox.setDisable(false);
            } else {
                separateCheckBox.setSelected(false);
                separateCheckBox.setDisable(true);
            }
        });
        saveDelayComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (saveDelayComboBox.getSelectionModel().getSelectedItem() != null){
                nextButton.setDisable(false);
            }
        });
    }

    @FXML protected void savePreferences() {
        Preferences preferences = DataStore.getPreferencesForCurrentUser();
        preferences.putBoolean(DataStore.getDeleteKey(), deleteCheckBox.isSelected());
        preferences.putBoolean(DataStore.getReadKey(), readCheckBox.isSelected());
        preferences.putBoolean(DataStore.getIncomingKey(), inCheckBox.isSelected());
        preferences.putBoolean(DataStore.getOutgoingKey(), outCheckBox.isSelected());
        preferences.putBoolean(DataStore.getSeparateKey(), separateCheckBox.isSelected());
        if (saveDelayComboBox.getValue() != null) {
            preferences.put(DataStore.getSaveDelayKey(), saveDelayComboBox.getValue());
        }
        Main.getInstance().getCurrentUser().finishedInitialSetup();
        switchToPrimaryScreen();
    }

    private void switchToPrimaryScreen() {
        Main.getInstance().goToPrimaryScreen();
    }

}

