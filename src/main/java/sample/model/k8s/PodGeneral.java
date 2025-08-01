package sample.model.k8s;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PodGeneral extends Pods {

    private Integer restarts;
    private String readiness;
    private String nominatedNode;
    private String readinessGates;
    private String age;

    public PodGeneral(String name, String status, String ip, String node, Integer restarts, String readiness, String nominatedNode, String readinessGates, String age) {
        super(name, status, ip, node);
        this.restarts = restarts;
        this.readiness = readiness;
        this.nominatedNode = nominatedNode;
        this.readinessGates = readinessGates;
        this.age = age;
    }

}
