package sample.model.docker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DockerContainer {

    private String containerId;
    private String imageId;
    private String created;
    private String status;

}
