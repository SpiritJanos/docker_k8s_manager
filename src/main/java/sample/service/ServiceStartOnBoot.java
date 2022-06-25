package sample.service;


import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import sample.service.K8sCommandGenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServiceStartOnBoot {

    private final K8sCommandGenerator k8sCommandGenerator = new K8sCommandGenerator();

    public boolean preRequisitesCheck() {
        if (!isDockerRunning()) {
            if (!startDocker()) {
                System.out.println("Docker Desktop is not installed!");
                return false;
            }
        }
        if (!isMinikubeRunning()) {
            if (!startMinikube()) {
                System.out.println("Minikube is not installed!");
                return false;
            }
        }
        return true;
    }

    private boolean isDockerRunning() {
        boolean isItRunning = false;
        String dockerDesktop = "Docker";
        try {

            Process send = Runtime.getRuntime().exec(new String[]{"tasklist"});
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("\\s+");
                if (splitedLine[0].equals(dockerDesktop)) {
                    isItRunning = true;
                    System.out.println("Docker Engine is running!");
                    break;
                }
            }
            send.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isItRunning;
    }

    private boolean startDocker() {
        String path = createPathForDockerDesktop();
        if (path == null) {
            System.out.println("Docker is not installed!");
            return false;
        }
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"\"" + path + "\""});
            System.out.println("Starting Docker Desktop...");
            send.isAlive();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean isMinikubeRunning() {
        boolean isItRunning = false;
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"minikube", "status"});
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = null;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("\\s+");
                if (splitedLine[1].equals("Stopped")) {
                    System.out.println("Minikube is not running!");
                    break;
                }
                if (splitedLine[1].equals("Running")) {
                    System.out.println("Minikube is running!");
                    isItRunning = true;
                    break;
                }
            }
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isItRunning;
    }

    private boolean startMinikube() {
        System.out.println("Starting minikube cluster...");
        boolean output = true;
        Task<Void> task = k8sCommandGenerator.installCluster("");

        Thread thread = new Thread(task);
        thread.start();

        while(!task.isDone()){
            if (task.isCancelled()){
                output = false;
            }
        }

        return output;
    }

    private String createPathForDockerDesktop() {
        String search = "\"Docker\"";
        StringBuilder output = new StringBuilder();
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"where", search});
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("\\\\");
                for (int i = 0; i < splitedLine.length; i++) {
                    if (splitedLine[i].equals("Docker")) {
                        output.append(splitedLine[i] + "\\" + splitedLine[i]);
                        break;
                    }
                    output.append(splitedLine[i] + "\\");
                }
                if (output.toString().contains("Docker")) {
                    output.append("\\" + "Docker Desktop.exe");
                    break;
                }
            }
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

}
