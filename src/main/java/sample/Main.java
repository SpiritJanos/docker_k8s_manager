package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.sample.view.ConfirmExit;
import sample.service.ServiceStartOnBoot;


public class Main extends Application {

    private Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Scene scene = new Scene(root);
        window.setTitle("Docker and Kubernetes manager");
        window.setScene(scene);
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        ServiceStartOnBoot serviceStartOnBoot = new ServiceStartOnBoot();
        System.out.println("Checking prerequisites..");

        if (serviceStartOnBoot.preRequisitesCheck()) {
            System.out.println("Requirements met! Starting application..");
            window.show();
            window.sizeToScene();
        } else {
            System.out.println("Some requirements haven't been met at startup!");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void closeProgram() {
        boolean answer = ConfirmExit.display("Confirm", "Are you sure you want to exit?");
        if (answer) {
            window.close();
            System.exit(0);
        }
    }
}
