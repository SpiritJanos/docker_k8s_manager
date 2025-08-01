package sample.model.k8s;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Pods {

    private String name;
    private String status;
    private String ip;
    private String node;

}
