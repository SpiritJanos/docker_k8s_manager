package sample.model.k8s;

public class PodDescription extends Pods {

    private String namespace;
    private String startTime;
    private String createdAt;
    private String image;
    private String port;
    private String hostPort;

    public PodDescription(String name, String status, String ip, String node, String namespace, String startTime, String createdAt, String image, String port, String hostPort) {
        super(name, status, ip, node);
        this.namespace = namespace;
        this.startTime = startTime;
        this.createdAt = createdAt;
        this.image = image;
        this.port = port;
        this.hostPort = hostPort;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }
}
