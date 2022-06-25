package sample.controller.docker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.model.docker.DockerContainerGeneral;
import sample.service.DockerCommandGenerator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DockerContainerController implements Initializable {
    public Button returnToMenu;
    public Button createButton;
    public Button deleteButton;
    public Button startStopButton;
    public CheckBox listAll;
    public Button inspectContainer;

    public TableView<DockerContainerGeneral> dockerContainers;
    public TableColumn<DockerContainerGeneral, String> containerId;
    public TableColumn<DockerContainerGeneral, String> image;
    public TableColumn<DockerContainerGeneral, String> command;
    public TableColumn<DockerContainerGeneral, String> created;
    public TableColumn<DockerContainerGeneral, String> status;
    public TableColumn<DockerContainerGeneral, String> ports;
    public TableColumn<DockerContainerGeneral, String> names;

    private final DockerCommandGenerator dockerCommandGenerator = new DockerCommandGenerator();
    private static boolean checkBoxStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startStopButton.disableProperty().bind(dockerContainers.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(dockerContainers.getSelectionModel().selectedItemProperty().isNull());
        inspectContainer.disableProperty().bind(dockerContainers.getSelectionModel().selectedItemProperty().isNull());
        listContainers();
        listAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                checkBoxStatus = t1;
                listContainers();
            }
        });
    }

    public void goBackToMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Scene scene = returnToMenu.getScene();
        scene.setRoot(root);
    }

    private void listContainers() {
        if (dockerContainers.getItems() != null) {
            dockerContainers.getItems().clear();
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                dockerContainers.setItems(dockerCommandGenerator.listContainers(checkBoxStatus));
                return null;
            }
        };
        setCellValues();
        Thread thread = new Thread(task);
        thread.start();

    }

    private void setCellValues() {
        containerId.setCellValueFactory(new PropertyValueFactory<>("containerId"));
        image.setCellValueFactory(new PropertyValueFactory<>("imageId"));
        command.setCellValueFactory(new PropertyValueFactory<>("command"));
        created.setCellValueFactory(new PropertyValueFactory<>("created"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        ports.setCellValueFactory(new PropertyValueFactory<>("ports"));
        names.setCellValueFactory(new PropertyValueFactory<>("containerName"));
    }

    public void openContainerCreationWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/docker/dockerImageView.fxml"));
        Scene scene = createButton.getScene();
        scene.setRoot(root);
    }

    public void deleteContainers() {
        DockerContainerGeneral selectedContainer = dockerContainers.getSelectionModel().getSelectedItem();
        String selectedId = selectedContainer.getContainerName();
        String status = selectedContainer.getStatus();

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                dockerCommandGenerator.deleteContainer(selectedId, status);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            listContainers();
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void startStopContainers() {
        DockerContainerGeneral selectedContainer = dockerContainers.getSelectionModel().getSelectedItem();
        String selectedId = selectedContainer.getContainerName();
        String status = selectedContainer.getStatus();

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                dockerCommandGenerator.startAndStopGeneratedContainer(selectedId, status);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            listContainers();
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void inspectContainerLogs() throws IOException {
        String containerName = dockerContainers.getSelectionModel().getSelectedItem().getContainerName();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/docker/dockerContainerInspectionView.fxml"));
        Scene scene = new Scene(loader.load());

        DockerContainerInspectionViewController dockerContainerInspectionViewController = loader.getController();
        dockerContainerInspectionViewController.receiveContainer(containerName);
        dockerContainerInspectionViewController.setPreScene(inspectContainer.getScene());

        Stage stage = (Stage) inspectContainer.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
