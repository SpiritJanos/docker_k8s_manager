package sample.model.k8s;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Namespace {
    private String name;
    private String status;
    private String age;

    public Namespace(String name, String status, String age) {
        this.name = name;
        this.status = status;
        this.age = age;
    }

}
