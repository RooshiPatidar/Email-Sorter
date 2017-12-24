import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InitializeHardDriveController {

    @FXML private Label warningText;
    @FXML private TextField directoryField;
    @FXML private Button nextButton;

    private File emailDirectory;


    @FXML
    public void initialize() {
    nextButton.setDisable(true);
    directoryField.textProperty().addListener((observable, oldValue, newValue) -> {
        if (directoryField.getCharacters() != null){
            nextButton.setDisable(false);
        }
    });
    }


    @FXML
    protected void chooseRootFolder() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select your root email directory (create a folder if you want)");
        //directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "Documents"));
        emailDirectory = directoryChooser.showDialog(Main.getInstance().getStage().getOwner());
        directoryField.setText(emailDirectory.getAbsolutePath());

        Path path = Paths.get(emailDirectory.getAbsolutePath());
        Path drivePath = path.getRoot();
        String drive = drivePath.toString().substring(0,1);


        String systemDisplayName = FileSystemView.getFileSystemView().getSystemDisplayName(drivePath.toFile());

        String[] separated = systemDisplayName.split(" ");
        String driveName = "";
        for (int i = 0; i < separated.length; i++) {
            if (separated[i].equals("(" + drive + ":)")){
            } else {
                if (i == 0){
                    driveName += separated[i];
                } else {
                    driveName += (" " + separated[i]);
                }
            }
        }
        Main.getInstance().setHardDriveName(driveName);

        String folderPath = emailDirectory.getAbsolutePath().substring(2);
        Main.getInstance().setRootFolder(folderPath);

    }

    @FXML protected void toInitializePreferences() {
        Main.getInstance().goToInitializePreferences();
    }

}

