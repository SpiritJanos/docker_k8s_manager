package main.java.sample.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmExit {

    private static boolean answer;

    public static boolean display(String title, String message) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(125);

        Label label = new Label();
        label.setText(message);
        GridPane.setConstraints(label, 0, 0);

        Button yesButton = new Button("Yes");
        yesButton.setMinWidth(50);
        Button noButton = new Button("No");
        noButton.setMinWidth(50);

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        HBox layout = new HBox(10);
        layout.getChildren().addAll(yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        GridPane.setConstraints(layout, 0, 1);

        grid.getChildren().addAll(label, layout);

        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
