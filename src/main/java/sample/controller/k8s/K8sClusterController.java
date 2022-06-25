package sample.controller.k8s;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import sample.service.K8sCommandGenerator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class K8sClusterController implements Initializable {
    public Button returnToMenu;
    public Button installButton;
    public Button clusterSelect;
    public Button deleteButton;
    public Button confirmButton;
    public Button stopClusterButton;
    public HBox clusterCreation;
    public TextField clusterName;
    public TextArea cmdOutput;

    public TableView<String> k8sClusters;
    public TableColumn<String, String> clusters;

    private final K8sCommandGenerator k8sCommandGenerator = new K8sCommandGenerator();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clusterSelect.disableProperty().bind(k8sClusters.getSelectionModel().selectedItemProperty().isNull());
        stopClusterButton.disableProperty().bind(k8sClusters.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(k8sClusters.getSelectionModel().selectedItemProperty().isNull());
        listClusters();
    }

    public void goBackToMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Scene scene = returnToMenu.getScene();
        scene.setRoot(root);
    }

    public void openSelectedCluster() throws IOException {
        String selectedCluster = k8sClusters.getSelectionModel().getSelectedItem();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                k8sCommandGenerator.selectCluster(selectedCluster);
                return null;
            }
        };

        switchScene(selectedCluster);

        Thread thread = new Thread(task);
        thread.start();
    }

    private void switchScene(String selectedCluster) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/k8s/k8sNamespaceView.fxml"));
        Parent root = loader.load();

        K8sNamespaceController k8sNamespaceController = loader.getController();
        k8sNamespaceController.receiveCluster(selectedCluster);
        Scene scene = returnToMenu.getScene();
        scene.setRoot(root);
    }

    private void listClusters() {
        if (k8sClusters.getItems() != null) {
            k8sClusters.getItems().clear();
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                k8sClusters.setItems(k8sCommandGenerator.listClusters());
                return null;
            }
        };
        setCellValues();
        Thread thread = new Thread(task);
        thread.start();
    }

    public void openClusterNameOption() {
        clusterCreation.setVisible(true);
    }

    public void installCluster() {
        clusterCreation.setVisible(false);
        String name = clusterName.getText();
        cmdOutput.clear();
        Task<Void> task = k8sCommandGenerator.installCluster(name);

        task.messageProperty().addListener((obs, oldText, newText) -> {
            cmdOutput.appendText(newText + "\n");
        });
        task.setOnSucceeded(e -> {
            listClusters();
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    public void stopCluster() {
        String selectedCluster = k8sClusters.getSelectionModel().getSelectedItem();
        cmdOutput.clear();
        Task<Void> task = k8sCommandGenerator.stopCluster(selectedCluster);

        task.messageProperty().addListener((obs, oldText, newText) -> {
            cmdOutput.appendText(newText + "\n");
        });
        task.setOnSucceeded(e -> {
            listClusters();
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    public void deleteClusters() {
        String selectedCluster = k8sClusters.getSelectionModel().getSelectedItem();
        cmdOutput.clear();
        Task<Void> task = k8sCommandGenerator.deleteCluster(selectedCluster);

        task.messageProperty().addListener((obs, oldText, newText) -> {
            cmdOutput.appendText(newText + "\n");
        });
        task.setOnSucceeded(e -> {
            listClusters();
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    private void setCellValues() {
        clusters.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    }


}
