import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;


public class EditFolderController {

    @FXML private ComboBox<String> folderDropdown;
    @FXML private ScrollPane folderListScrollPane;
    @FXML private Button addSubfolderButton;
    private ArrayList<Folder> retrievedFolders;

    @FXML
    public void initialize() {
        System.setProperty("prism.lcdtext", "true");


        //If anything is changed ie. something is added or removed it will relist the folders on the scrollpane
        folderDropdown.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                listFoldersOnScrollPane();
            }
        });


        folderListScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        fillUpComboBox();
        addSubfolderButton.setVisible(false);

    }

    /**
     * This method adds all of the available folders to the dropdown combobox
     */
    private void fillUpComboBox() {
        retrievedFolders = Main.getInstance().getPrimaryScreenController().getFolders();
        ObservableList<String> folderNameList =
                FXCollections.observableArrayList();
        for (Folder folder : retrievedFolders){
            folderNameList.add(folder.getName());
        }
        folderDropdown.setItems(folderNameList);
    }

    /**
     * Since I am adding the folders programmatically to the drop down using anchorpanes I can add clickListeners but
     *  since the tabs don't have the folder info, I have to take the name of the tab and then search for the info
     *  afterwards with this method
     * @return The folder that corresponds to the tab clicked based on the tab text
     */
    private Folder getChosenFolderFromDropdown() {
        retrievedFolders = Main.getInstance().getPrimaryScreenController().getFolders();
        for (Folder folder : retrievedFolders){
            if (folderDropdown.getValue().equals(folder.getName())){
                addSubfolderButton.setVisible(true);
                return folder;
            }
        }
        addSubfolderButton.setVisible(false);
        return null;
    }

    @FXML protected void addNewSubfolder() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Subfolder");
        dialog.setHeaderText("Add the name of the new Subfolder");
        dialog.setContentText("Name:");

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            getChosenFolderFromDropdown().addNewSubfolder(result.get());
            listFoldersOnScrollPane();
        }
    }


    private void listFoldersOnScrollPane() {
        final Random rng = new Random();
        VBox content = new VBox(5);
        ScrollPane scroller = folderListScrollPane;

        Folder folderChosen = getChosenFolderFromDropdown();
        if (folderChosen != null) {
            ArrayList<Subfolder> subfolders = getChosenFolderFromDropdown().getSubfolders();

            for (Subfolder folder : subfolders) {
                AnchorPane anchorPane = new AnchorPane();
                String style = String.format("-fx-background: rgb(%d, %d, %d);" +
                                "-fx-background-color: -fx-background;",
                        rng.nextInt(128),
                        rng.nextInt(128),
                        rng.nextInt(128));
                anchorPane.setStyle(style);
                Label label = new Label(folder.getName());
                AnchorPane.setLeftAnchor(label, 5.0);
                AnchorPane.setTopAnchor(label, 5.0);

                ImageView trash = new ImageView("/res/images/trash.png");
                trash.setPreserveRatio(true);
                AnchorPane.setRightAnchor(trash, 5.0);
                AnchorPane.setTopAnchor(trash, 5.0);
                AnchorPane.setBottomAnchor(trash, 5.0);
                anchorPane.getChildren().add(label);
                anchorPane.getChildren().add(trash);
                content.getChildren().add(anchorPane);
            }
        }
        scroller.setContent(content);
    }
}

