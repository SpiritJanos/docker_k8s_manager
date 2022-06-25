package sample.controller.k8s;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.model.docker.DockerImage;
import sample.service.DockerCommandGenerator;
import sample.service.K8sCommandGenerator;

import java.net.URL;
import java.util.ResourceBundle;

public class K8sPodCreationController implements Initializable {
    public Button cancelButton;
    public Button confirmButton;
    public Button returnButton;
    public Label namespaceName;
    public Label imageName;
    public Label tag;
    public Label size;
    public Label done;
    public TextField podName;
    public ProgressBar progressBar;
    public GridPane status;

    public TableView<DockerImage> imageTable;
    public TableColumn<DockerImage, String> imageNames;

    private Scene previousScene;
    private final DockerCommandGenerator dockerCommandGenerator = new DockerCommandGenerator();
    private final K8sCommandGenerator k8sCommandGenerator = new K8sCommandGenerator();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmButton.disableProperty().bind(imageTable.getSelectionModel().selectedItemProperty().isNull());
        listImages();
    }

    public void receiveNamespace(String selectedNamespace) {
        namespaceName.setText(selectedNamespace);
    }

    public void goBackToPods() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(previousScene);
        stage.show();
    }

    public void setPreScene(Scene preScene) {
        this.previousScene = preScene;
    }

    public void createPods() {
        String imageName = imageTable.getSelectionModel().getSelectedItem().getRepository();
        setLabels();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                k8sCommandGenerator.createPods(podName.getText(), imageName, namespaceName.getText());
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            progressBar.setProgress(1);
            done.setVisible(true);
            returnButton.setVisible(true);

        });
        status.setVisible(true);
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

        Thread thread = new Thread(task);
        thread.start();
    }

    private void setLabels() {
        imageName.textProperty().bind(new SimpleStringProperty(imageTable.getSelectionModel().getSelectedItem().getRepository()));
        tag.textProperty().bind(new SimpleStringProperty(imageTable.getSelectionModel().getSelectedItem().getTag()));
        size.textProperty().bind(new SimpleStringProperty(imageTable.getSelectionModel().getSelectedItem().getSize()));
    }

    private void listImages() {
        if (imageTable.getItems() != null) {
            imageTable.getItems().clear();
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                imageTable.setItems(dockerCommandGenerator.listDockerImages());
                return null;
            }
        };
        setCellValues();
        Thread thread = new Thread(task);
        thread.start();
    }

    private void setCellValues() {
        imageNames.setCellValueFactory(new PropertyValueFactory<>("repository"));
    }
}
