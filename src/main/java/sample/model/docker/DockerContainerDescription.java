package sample.model.docker;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DockerContainerDescription extends DockerContainer {
    private String startedAt;
    private String ipAddress;
    private String internalPort;
    private String externalPort;

    public DockerContainerDescription(String containerId, String imageId, String created, String status, String startedAt, String ipAddress, String internalPort, String externalPort) {
        super(containerId, imageId, created, status);
        this.startedAt = startedAt;
        this.ipAddress = ipAddress;
        this.internalPort = internalPort;
        this.externalPort = externalPort;
    }

}
