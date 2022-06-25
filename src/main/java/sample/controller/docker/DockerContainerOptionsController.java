package sample.controller.docker;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import sample.model.docker.DockerImage;
import sample.service.DockerCommandGenerator;

import java.io.IOException;

public class DockerContainerOptionsController {
    public Label imageName;
    public Label tag;
    public Label size;
    public Label done;
    public Button cancelButton;
    public Button confirmButton;
    public Button returnButton;
    public TextField containerName;
    public TextField externalPort;
    public TextField internalPort;
    public GridPane status;
    public ProgressBar progressBar;

    private DockerImage dockerImage;
    private final DockerCommandGenerator commandGenerator = new DockerCommandGenerator();

    public void receiveData(DockerImage selectedImage) {
        dockerImage = selectedImage;
        setLabels();
    }

    private void setLabels() {
        imageName.setText(dockerImage.getRepository());
        tag.setText(dockerImage.getTag());
        size.setText(dockerImage.getSize());
    }

    public void createContainers() {
        String selectedId = dockerImage.getRepository();
        String name = containerName.getText();
        String ePort = externalPort.getText();
        String iPort = internalPort.getText();
        status.setVisible(true);
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                commandGenerator.generateContainerFromImage(selectedId, name, ePort, iPort);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            progressBar.setProgress(1);
            done.setVisible(true);
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

    public void goBackToContainers() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/docker/dockerContainerView.fxml"));
        Scene scene = cancelButton.getScene();
        scene.setRoot(root);
    }
}
