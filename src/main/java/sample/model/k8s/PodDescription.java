package sample.model.k8s;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
