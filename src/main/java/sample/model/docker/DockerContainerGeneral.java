package sample.model.docker;

public class DockerContainerGeneral extends DockerContainer {
    private String command;
    private String ports;
    private String containerName;

    public DockerContainerGeneral(String containerId, String imageId, String created, String status, String command, String ports, String containerName) {
        super(containerId, imageId, created, status);
        this.command = command;
        this.ports = ports;
        this.containerName = containerName;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }
}
