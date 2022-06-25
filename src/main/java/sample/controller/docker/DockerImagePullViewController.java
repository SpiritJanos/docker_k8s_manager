package sample.controller.docker;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import sample.model.docker.DockerRepositoryImage;
import sample.service.DockerCommandGenerator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DockerImagePullViewController implements Initializable {
    public TextField imageName;
    public Button searchButton;
    public Button cancelButton;
    public Label statusLabel;
    public ProgressIndicator progress;
    public Button pullButton;
    public HBox mainHbox;
    public HBox statusHbox;
    public Button returnButton;

    public TableView<DockerRepositoryImage> foundImages;
    public TableColumn<DockerRepositoryImage, String> foundImageName;
    public TableColumn<DockerRepositoryImage, String> foundImageDescription;
    public TableColumn<DockerRepositoryImage, Integer> foundImageStars;
    public TableColumn<DockerRepositoryImage, Boolean> foundImageOfficial;

    private final DockerCommandGenerator dockerCommandGenerator = new DockerCommandGenerator();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchButton.disableProperty().bind(imageName.textProperty().isNull());
        pullButton.disableProperty().bind(foundImages.getSelectionModel().selectedItemProperty().isNull());
    }

    public void searchForImages() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                foundImages.setItems(dockerCommandGenerator.searchForImagesInHub(imageName.getText()));
                return null;
            }
        };
        setCellValues();
        Thread thread = new Thread(task);
        thread.start();
    }

    public void pullImages() {
        mainHbox.setVisible(false);
        statusHbox.setVisible(true);
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                dockerCommandGenerator.pullImagesFromHub(selectImageName());
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            progress.setProgress(1);
            statusLabel.setText("Image successfully pulled!");
            returnButton.setVisible(true);
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void goBackToImages() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/docker/dockerImageView.fxml"));
        Scene scene = cancelButton.getScene();
        scene.setRoot(root);
    }

    private void setCellValues() {
        foundImageName.setCellValueFactory(new PropertyValueFactory<>("repositoryImageName"));
        foundImageDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        foundImageStars.setCellValueFactory(new PropertyValueFactory<>("stars"));
        foundImageOfficial.setCellValueFactory(new PropertyValueFactory<>("official"));
        foundImageOfficial.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "+" : "");
            }
        });
    }

    private String selectImageName() {
        return foundImages.getSelectionModel().getSelectedItem().getRepositoryImageName();
    }


}
