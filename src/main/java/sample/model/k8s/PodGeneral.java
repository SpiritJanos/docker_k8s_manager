package sample.model.k8s;

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

    public Integer getRestarts() {
        return restarts;
    }

    public void setRestarts(Integer restarts) {
        this.restarts = restarts;
    }

    public String getReadiness() {
        return readiness;
    }

    public void setReadiness(String readiness) {
        this.readiness = readiness;
    }

    public String getNominatedNode() {
        return nominatedNode;
    }

    public void setNominatedNode(String nominatedNode) {
        this.nominatedNode = nominatedNode;
    }

    public String getReadinessGates() {
        return readinessGates;
    }

    public void setReadinessGates(String readinessGates) {
        this.readinessGates = readinessGates;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
