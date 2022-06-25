package sample.controller.docker;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.docker.DockerContainerDescription;
import sample.service.DockerCommandGenerator;

import java.io.IOException;

public class DockerContainerInspectionViewController {
    public Label containerName;
    public Label id;
    public Label status;
    public Label startTime;
    public Label createdAt;
    public Label image;
    public Label ipAddress;
    public Label internalPort;
    public Label externalPort;
    public TextArea containerLogs;
    public Button cancelButton;
    public Button openTerminal;

    private String selectedContainer;
    private Scene previousScene;
    private final DockerCommandGenerator dockerCommandGenerator = new DockerCommandGenerator();

    public void receiveContainer(String selectedContainer) {
        this.selectedContainer = selectedContainer;
        containerName.setText(this.selectedContainer);
        setLabelsForDescribe();
    }

    public void goBack() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(previousScene);
        stage.show();
    }

    public void setPreScene(Scene preScene) {
        this.previousScene = preScene;
    }

    public void execContainer() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/docker/dockerContainerTerminal.fxml"));
        Parent root = loader.load();

        DockerContainerTerminalController dockerContainerTerminalController = loader.getController();
        dockerContainerTerminalController.receiveContainerForTerminal(selectedContainer);

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Terminal");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void inspectContainer() {
        Task<Void> task = dockerCommandGenerator.showContainerLogs(selectedContainer);
        task.messageProperty().addListener((obs, oldText, newText) -> {
            containerLogs.appendText(newText + "\n");
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    private void setLabelsForDescribe() {
        Task<DockerContainerDescription> task = new Task() {
            @Override
            protected DockerContainerDescription call() throws Exception {
                return dockerCommandGenerator.findContainerDescription(selectedContainer);
            }
        };
        task.setOnSucceeded(e -> {
            id.setText(task.getValue().getContainerId());
            status.setText(task.getValue().getStatus());
            startTime.setText(task.getValue().getStartedAt());
            createdAt.setText(task.getValue().getCreated());
            image.setText(task.getValue().getImageId());
            ipAddress.setText(task.getValue().getIpAddress());
            internalPort.setText(task.getValue().getInternalPort());
            externalPort.setText(task.getValue().getExternalPort());
            inspectContainer();
        });


        Thread thread = new Thread(task);
        thread.start();
    }

}
