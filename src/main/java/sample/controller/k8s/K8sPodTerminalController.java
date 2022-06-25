package sample.controller.k8s;

import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import sample.service.K8sCommandGenerator;
import java.net.URL;
import java.util.ResourceBundle;


public class K8sPodTerminalController implements Initializable {
    public Label podName;
    public TextArea terminal;

    private String selectedPodName;
    private String namespaceName;
    private final K8sCommandGenerator k8sCommandGenerator = new K8sCommandGenerator();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTerminal();
    }

    public void receivePodForTerminal(String selectedPod, String selectedNamespace) {
        selectedPodName = selectedPod;
        namespaceName = selectedNamespace;
        podName.setText(selectedPodName);
    }

    private void initializeTerminal() {
        terminal.appendText("/# ");
        terminal.setOnKeyPressed((t) -> {
            try {
                if (t.getCode() == KeyCode.ENTER) {
                    String[] splitedLine = terminal.getText().split("\\/\\#\\s|\\n");
                    Task<Void> task = k8sCommandGenerator.runCommandInPodTerminal(selectedPodName, splitedLine[splitedLine.length - 1], namespaceName);
                    task.messageProperty().addListener((obs, oldText, newText) -> {
                        terminal.appendText(newText + "\n");
                    });
                    task.setOnSucceeded(e -> {
                        terminal.appendText("/# ");
                    });

                    Thread thread = new Thread(task);
                    thread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
