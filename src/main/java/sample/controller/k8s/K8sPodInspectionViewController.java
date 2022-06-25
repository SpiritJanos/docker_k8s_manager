package sample.controller.k8s;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.k8s.PodDescription;
import sample.service.K8sCommandGenerator;

import java.io.IOException;

public class K8sPodInspectionViewController {
    public Label podName;
    public Label name;
    public Label namespace;
    public Label status;
    public Label node;
    public Label startTime;
    public Label createdAt;
    public Label image;
    public Label ipAddress;
    public Label port;
    public Label hostPort;
    public TextArea podLogs;
    public Button cancelButton;

    private String selectedPodName;
    private String namespaceName;
    private Scene previousScene;
    private final K8sCommandGenerator k8sCommandGenerator = new K8sCommandGenerator();

    public void receivePod(String selectedPod, String selectedNamespace) {
        selectedPodName = selectedPod;
        namespaceName = selectedNamespace;
        podName.setText(selectedPodName);
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

    public void execPod() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/k8s/k8sPodTerminal.fxml"));
        Parent root = loader.load();

        K8sPodTerminalController k8sPodTerminalController = loader.getController();
        k8sPodTerminalController.receivePodForTerminal(selectedPodName, namespaceName);

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Terminal");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void inspectPod() {
        Task<Void> task = k8sCommandGenerator.showPodLogs(selectedPodName, namespaceName);
        task.messageProperty().addListener((obs, oldText, newText) -> {
            podLogs.appendText(newText + "\n");
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    private void setLabelsForDescribe() {
        Task<PodDescription> task = new Task() {
            @Override
            protected PodDescription call() throws Exception {
                return k8sCommandGenerator.describePod(selectedPodName, namespaceName);
            }
        };
        task.setOnSucceeded(e -> {
            name.setText(task.getValue().getName());
            namespace.setText(task.getValue().getNamespace());
            status.setText(task.getValue().getStatus());
            node.setText(task.getValue().getNode());
            startTime.setText(task.getValue().getStartTime());
            createdAt.setText(task.getValue().getCreatedAt());
            image.setText(task.getValue().getImage());
            ipAddress.setText(task.getValue().getIp());
            port.setText(task.getValue().getPort());
            hostPort.setText(task.getValue().getHostPort());
            inspectPod();
        });


        Thread thread = new Thread(task);
        thread.start();
    }

}
