package sample.model.k8s;

public class Pods {

    private String name;
    private String status;
    private String ip;
    private String node;

    public Pods(String name, String status, String ip, String node) {
        this.name = name;
        this.status = status;
        this.ip = ip;
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
