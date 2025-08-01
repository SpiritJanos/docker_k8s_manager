package sample.model.docker;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
