package sample.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import sample.model.docker.DockerContainerDescription;
import sample.model.docker.DockerContainerGeneral;
import sample.model.docker.DockerImage;
import sample.model.docker.DockerRepositoryImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class DockerCommandGenerator {

    public ObservableList<DockerImage> listDockerImages() {
        String repository;
        String tag;
        String id;
        String created;
        String size;
        ObservableList<DockerImage> images = FXCollections.observableArrayList();

        try {
            Process send = Runtime.getRuntime().exec(new String[]{"docker", "images"});

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = null;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("\\s+ | \"");
                repository = splitedLine[0];
                tag = splitedLine[1];
                id = splitedLine[2];
                created = splitedLine[3];
                size = splitedLine[4];
                images.add(new DockerImage(repository, tag, id, created, size));
            }
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    public void deleteImage(String imageName) {
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"docker", "image", "rm", imageName});

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = bufferedReader.readLine();
            if (line.contains("Error")) {
                send = Runtime.getRuntime().exec(new String[]{"docker", "image", "rm", "-f", imageName});
            }
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateContainerFromImage(String repositoryName, String containerName, String externalPort, String internalPort) {
        String portPair = externalPort.isEmpty() || internalPort.isEmpty() ? "" : externalPort + ":" + internalPort;
        try {
            if (containerName.isEmpty() && portPair.isEmpty()) {
                Process send = Runtime.getRuntime().exec(new String[]{"docker", "run", "-i", "-t", "-d", repositoryName});
                send.waitFor();
            } else if (!containerName.isEmpty() && !portPair.isEmpty()) {
                Process send = Runtime.getRuntime().exec(new String[]{"docker", "run", "-i", "-t", "-d", "-p", portPair, "--name", containerName, repositoryName});
                send.waitFor();
            } else if (!containerName.isEmpty() && portPair.isEmpty()) {
                Process send = Runtime.getRuntime().exec(new String[]{"docker", "run", "-i", "-t", "-d", "--name", containerName, repositoryName});
                send.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAndStopGeneratedContainer(String containerId, String status) {
        try {
            Process command;
            if (status.contains("Exited")) {
                command = Runtime.getRuntime().exec(new String[]{"docker", "container", "start", containerId});
            } else {
                command = Runtime.getRuntime().exec(new String[]{"docker", "container", "stop", containerId});
            }
            command.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<DockerContainerGeneral> listContainers(boolean checked) {
        return checked ? listAllContainers() : listRunningContainers();
    }

    private ObservableList<DockerContainerGeneral> listRunningContainers() {

        ObservableList<DockerContainerGeneral> dockerContainers = FXCollections.observableArrayList();

        try {
            Process send = Runtime.getRuntime().exec(new String[]{"docker", "container", "ls"});

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = null;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("\\s+ | \"");
                if (splitedLine.length > 6) {
                    dockerContainers.add(assignValuesWithPort(splitedLine));
                } else {
                    dockerContainers.add(assignValuesWithoutPort(splitedLine));
                }
            }
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dockerContainers;
    }

    private ObservableList<DockerContainerGeneral> listAllContainers() {

        ObservableList<DockerContainerGeneral> allDockerContainers = FXCollections.observableArrayList();

        try {
            Process send = Runtime.getRuntime().exec(new String[]{"docker", "container", "ls", "-a"});

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = null;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("\\s+ | \"");
                if (splitedLine.length > 6) {
                    allDockerContainers.add(assignValuesWithPort(splitedLine));
                } else {
                    allDockerContainers.add(assignValuesWithoutPort(splitedLine));
                }
            }
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allDockerContainers;
    }

    public void deleteContainer(String containerId, String status) {
        try {
            Process command;
            if (status.contains("Exited")) {
                command = Runtime.getRuntime().exec(new String[]{"docker", "container", "rm", containerId});
            } else {
                command = Runtime.getRuntime().exec(new String[]{"docker", "container", "rm", "-f", containerId});
            }
            command.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DockerContainerDescription findContainerDescription(String containerName) {
        String containerId = null;
        String imageId = null;
        String status = null;
        String startedAt = null;
        String createdAt = null;
        String ipAddress = null;
        String internalPort = null;
        String externalPort = null;
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"docker", "inspect", containerName});

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("\"+");
                String[] filteredLine = Arrays.stream(splitedLine)
                        .filter(x -> !x.matches("\\s+") && !x.equals(": ") && !x.equals(",")).toArray(String[]::new);
                if (filteredLine[0].equals("Id")) {
                    containerId = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].equals("Image")) {
                    imageId = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].equals("Created")) {
                    createdAt = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].equals("Status")) {
                    status = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].equals("StartedAt")) {
                    startedAt = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].equals("IPAddress") && filteredLine.length > 1) {
                    ipAddress = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].matches("\\d+\\/\\w+")) {
                    internalPort = filteredLine[0];
                    continue;
                }
                if (filteredLine[0].equals("HostPort")) {
                    externalPort = filteredLine[1];
                }
            }
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DockerContainerDescription(containerId, imageId, createdAt, status, startedAt, ipAddress, internalPort, externalPort);
    }

    public Task<Void> execCommandInTerminal(String containerName, String command) {
        String fullCommand = "docker exec " + containerName + " " + command;
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Process send = Runtime.getRuntime().exec(fullCommand);

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        updateMessage(line);
                        Thread.sleep(1);
                    }
                    send.waitFor();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public ObservableList<DockerRepositoryImage> searchForImagesInHub(String imageName) {
        String fullCommand = "docker search " + imageName + " --no-trunc --format=\"{{.Name}}${{.Description}}${{.StarCount}}${{.IsOfficial}}\"";
        String repositoryImageName;
        String description;
        int stars;
        boolean official = false;
        ObservableList<DockerRepositoryImage> images = FXCollections.observableArrayList();

        try {
            Process send = Runtime.getRuntime().exec(fullCommand);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("\\$");
                if (splitedLine.length == 2) {
                    repositoryImageName = splitedLine[0];
                    description = null;
                    stars = Integer.parseInt(splitedLine[1]);
                    official = false;

                    images.add(new DockerRepositoryImage(repositoryImageName, description, stars, official));
                    continue;
                }
                if (splitedLine.length == 3) {
                    repositoryImageName = splitedLine[0];
                    description = splitedLine[1];
                    stars = Integer.parseInt(splitedLine[2]);
                    official = false;

                    images.add(new DockerRepositoryImage(repositoryImageName, description, stars, official));
                    continue;
                }
                repositoryImageName = splitedLine[0];
                description = splitedLine[1];
                stars = Integer.parseInt(splitedLine[2]);
                if (splitedLine[3].equals("[OK]")) {
                    official = true;
                }
                images.add(new DockerRepositoryImage(repositoryImageName, description, stars, official));
            }
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    public Task<Void> showContainerLogs(String podName) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Process send = Runtime.getRuntime().exec(new String[]{"docker", "logs", podName});

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        updateMessage(line);
                        Thread.sleep(1);
                    }
                    send.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public void pullImagesFromHub(String imageName) {
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"docker", "pull", imageName});
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DockerContainerGeneral assignValuesWithPort(String[] outputLines) {
        String containerId;
        String imageId;
        String command;
        String created;
        String status;
        String ports;
        String containerName;

        containerId = outputLines[0];
        imageId = outputLines[1];
        command = outputLines[2];
        created = outputLines[3];
        status = outputLines[4];
        ports = outputLines[5];
        containerName = outputLines[6];

        return new DockerContainerGeneral(containerId, imageId, created, status, command, ports, containerName);
    }

    private DockerContainerGeneral assignValuesWithoutPort(String[] outputLines) {
        String containerId;
        String imageId;
        String command;
        String created;
        String status;
        String containerName;

        containerId = outputLines[0];
        imageId = outputLines[1];
        command = outputLines[2];
        created = outputLines[3];
        status = outputLines[4];
        containerName = outputLines[5];

        return new DockerContainerGeneral(containerId, imageId, created, status, command, null, containerName);
    }

}
