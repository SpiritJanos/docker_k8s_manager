package sample.controller.docker;

import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import sample.service.DockerCommandGenerator;

import java.net.URL;
import java.util.ResourceBundle;

public class DockerContainerTerminalController implements Initializable {
    public Label containerName;
    public TextArea terminal;

    private String selectedContainerName;
    private final DockerCommandGenerator dockerCommandGenerator = new DockerCommandGenerator();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTerminal();
    }

    public void receiveContainerForTerminal(String selectedContainer) {
        selectedContainerName = selectedContainer;
        containerName.setText(selectedContainerName);
    }

    private void initializeTerminal() {
        terminal.appendText("/# ");
        terminal.setOnKeyPressed((t) -> {
            try {
                if (t.getCode() == KeyCode.ENTER) {
                    String[] splitedLine = terminal.getText().split("\\/\\#\\s|\\n");
                    Task<Void> task = dockerCommandGenerator.execCommandInTerminal(selectedContainerName, splitedLine[splitedLine.length - 1]);
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
