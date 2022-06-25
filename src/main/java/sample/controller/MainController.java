package sample.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.java.sample.view.ConfirmExit;

public class MainController {

    private Scene scene;

    public Button dockerScene;
    public Button k8sScene;
    public Button exitButton;

    public void openDockerImageWindow() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/docker/dockerContainerView.fxml"));
        scene = dockerScene.getScene();
        scene.setRoot(root);

    }

    public void openKubernetesWindow() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/k8s/k8sClusterView.fxml"));
        scene = k8sScene.getScene();
        scene.setRoot(root);
    }

    public void closeTheApp() {
        boolean answer = ConfirmExit.display("Confirm", "Are you sure you want to exit?");
        if (answer) {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        }
    }
}
