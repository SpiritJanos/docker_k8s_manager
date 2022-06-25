package sample.controller.docker;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.model.docker.DockerImage;
import sample.service.DockerCommandGenerator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DockerImageController implements Initializable {
    public Button nextButton;
    public Button returnToMenu;
    public Button searchImages;
    public Button deleteImageButton;

    public TableView<DockerImage> dockerImages;
    public TableColumn<DockerImage, String> repository;
    public TableColumn<DockerImage, String> tag;
    public TableColumn<DockerImage, String> id;
    public TableColumn<DockerImage, String> created;
    public TableColumn<DockerImage, String> size;

    private final DockerCommandGenerator dockerCommandGenerator = new DockerCommandGenerator();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nextButton.disableProperty().bind(dockerImages.getSelectionModel().selectedItemProperty().isNull());
        deleteImageButton.disableProperty().bind(dockerImages.getSelectionModel().selectedItemProperty().isNull());
        listImages();
    }

    public void goBack() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/docker/dockerContainerView.fxml"));
        Scene scene = returnToMenu.getScene();
        scene.setRoot(root);
    }

    public void containerOptions() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/docker/dockerContainerOptions.fxml"));
        Parent root = loader.load();

        DockerContainerOptionsController dockerContainerOptionsController = loader.getController();
        dockerContainerOptionsController.receiveData(selectImage());
        Scene scene = returnToMenu.getScene();
        scene.setRoot(root);
    }

    public void searchForImages() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/docker/dockerImagePullView.fxml"));
        Parent root = loader.load();
        Scene scene = returnToMenu.getScene();
        scene.setRoot(root);
    }

    public void deleteImages() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                dockerCommandGenerator.deleteImage(selectImage().getRepository());
                return null;
            }
        };
        task.setOnSucceeded(e -> listImages());

        Thread thread = new Thread(task);
        thread.start();
    }

    private void listImages() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                dockerImages.setItems(dockerCommandGenerator.listDockerImages());
                return null;
            }
        };
        setCellValues();
        Thread thread = new Thread(task);
        thread.start();
    }

    private void setCellValues() {
        repository.setCellValueFactory(new PropertyValueFactory<>("repository"));
        tag.setCellValueFactory(new PropertyValueFactory<>("tag"));
        id.setCellValueFactory(new PropertyValueFactory<>("imageId"));
        created.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
    }

    private DockerImage selectImage() {
        return dockerImages.getSelectionModel().getSelectedItem();
    }
}
