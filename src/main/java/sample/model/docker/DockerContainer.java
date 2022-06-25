package sample.model.docker;

public class DockerContainer {
    private String containerId;
    private String imageId;
    private String created;
    private String status;

    public DockerContainer(String containerId, String imageId, String created, String status) {
        this.containerId = containerId;
        this.imageId = imageId;
        this.created = created;
        this.status = status;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
