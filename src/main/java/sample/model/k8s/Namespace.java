package sample.model.k8s;

public class Namespace {
    private String name;
    private String status;
    private String age;

    public Namespace(String name, String status, String age) {
        this.name = name;
        this.status = status;
        this.age = age;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
