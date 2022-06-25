package sample.model.docker;

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

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getInternalPort() {
        return internalPort;
    }

    public void setInternalPort(String internalPort) {
        this.internalPort = internalPort;
    }

    public String getExternalPort() {
        return externalPort;
    }

    public void setExternalPort(String externalPort) {
        this.externalPort = externalPort;
    }
}
