package sample.controller.k8s;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.model.k8s.Namespace;
import sample.model.k8s.Pods;
import sample.service.K8sCommandGenerator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class K8sNamespaceController implements Initializable {
    public Button returnToMenu;
    public Button podSelect;
    public Button namespaceSelect;
    public Button deleteButton;
    public Button createButton;
    public Button confirmButton;
    public Button podCreation;
    public TextField name;
    public HBox namespaceCreation;
    public Label podsLabel;
    public Label clusterName;
    public HBox podButtons;
    public Button deletePod;

    public TableView<Namespace> namespaces;
    public TableColumn<Namespace, String> namespaceName;
    public TableColumn<Namespace, String> namespaceStatus;
    public TableColumn<Namespace, String> namespaceAge;

    public TableView<Pods> pods;
    public TableColumn<Pods, String> podName;
    public TableColumn<Pods, String> podStatus;
    public TableColumn<Pods, String> podAge;
    public TableColumn<Pods, String> podReady;
    public TableColumn<Pods, Integer> podRestarts;
    public TableColumn<Pods, String> podIp;
    public TableColumn<Pods, String> podReadinessGate;
    public TableColumn<Pods, String> podNode;
    public TableColumn<Pods, String> podNominatedNode;

    private final K8sCommandGenerator k8sCommandGenerator = new K8sCommandGenerator();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        podSelect.disableProperty().bind(pods.getSelectionModel().selectedItemProperty().isNull());
        deletePod.disableProperty().bind(pods.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(namespaces.getSelectionModel().selectedItemProperty().isNull());
        namespaceSelect.disableProperty().bind(namespaces.getSelectionModel().selectedItemProperty().isNull());
        podCreation.disableProperty().bind(namespaces.getSelectionModel().selectedItemProperty().isNull());
        listNamespaces();
    }

    public void receiveCluster(String cluster) {
        clusterName.setText(cluster);
    }

    public void goBackToMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/k8s/k8sClusterView.fxml"));
        Scene scene = returnToMenu.getScene();
        scene.setRoot(root);
    }

    public void openNamespaceNameOption() {
        namespaceCreation.setVisible(true);
    }

    public void createPod() throws IOException {
        String selectedNamespace = namespaces.getSelectionModel().getSelectedItem().getName();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/k8s/k8sPodCreationView.fxml"));
        Scene scene = new Scene(loader.load());

        K8sPodCreationController k8sPodCreationController = loader.getController();
        k8sPodCreationController.receiveNamespace(selectedNamespace);
        k8sPodCreationController.setPreScene(createButton.getScene());

        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void deletePod() {
        String podName = pods.getSelectionModel().getSelectedItem().getName();
        String namespace = namespaces.getSelectionModel().getSelectedItem().getName();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                k8sCommandGenerator.deletePod(podName, namespace);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            listPods();
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void inspectPod() throws IOException {
        String podName = pods.getSelectionModel().getSelectedItem().getName();
        String namespace = namespaces.getSelectionModel().getSelectedItem().getName();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/k8s/k8sPodInspectionView.fxml"));
        Scene scene = new Scene(loader.load());

        K8sPodInspectionViewController k8sPodInspectionViewController = loader.getController();
        k8sPodInspectionViewController.receivePod(podName, namespace);
        k8sPodInspectionViewController.setPreScene(podSelect.getScene());

        Stage stage = (Stage) podSelect.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void installNamespace() {
        String namespaceName = name.getText();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                k8sCommandGenerator.createNamespace(namespaceName);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            namespaceCreation.setVisible(false);
            listNamespaces();
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    public void deleteNamespace() {
        String namespaceName = namespaces.getSelectionModel().getSelectedItem().getName();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                k8sCommandGenerator.deleteNamespace(namespaceName);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            listNamespaces();
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void listPods() {
        pods.setVisible(true);
        podsLabel.setVisible(true);
        podButtons.setVisible(true);
        String selectedNamespace = namespaces.getSelectionModel().getSelectedItem().getName();
        if (pods.getItems() != null) {
            pods.getItems().clear();
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                pods.setItems(k8sCommandGenerator.listPods(selectedNamespace));
                return null;
            }
        };
        setCellValuesPod();
        Thread thread = new Thread(task);
        thread.start();
    }

    private void listNamespaces() {
        if (namespaces.getItems() != null) {
            namespaces.getItems().clear();
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                namespaces.setItems(k8sCommandGenerator.listNamespaces());
                return null;
            }
        };
        setCellValuesNamespace();
        Thread thread = new Thread(task);
        thread.start();
    }

    private void setCellValuesNamespace() {
        namespaceName.setCellValueFactory(new PropertyValueFactory<>("name"));
        namespaceStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        namespaceAge.setCellValueFactory(new PropertyValueFactory<>("age"));
    }

    private void setCellValuesPod() {
        podName.setCellValueFactory(new PropertyValueFactory<>("name"));
        podStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        podAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        podReady.setCellValueFactory(new PropertyValueFactory<>("readiness"));
        podRestarts.setCellValueFactory(new PropertyValueFactory<>("restarts"));
        podIp.setCellValueFactory(new PropertyValueFactory<>("ip"));
        podNode.setCellValueFactory(new PropertyValueFactory<>("node"));
        podNominatedNode.setCellValueFactory(new PropertyValueFactory<>("nominatedNode"));
        podReadinessGate.setCellValueFactory(new PropertyValueFactory<>("readinessGates"));
    }
}
