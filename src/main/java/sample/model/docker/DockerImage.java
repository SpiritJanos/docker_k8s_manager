package sample.model.docker;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DockerImage {

    private String repository;
    private String tag;
    private String imageId;
    private String createdAt;
    private String size;

    public DockerImage(String repository, String tag, String imageId, String createdAt, String size) {
        this.repository = repository;
        this.tag = tag;
        this.imageId = imageId;
        this.createdAt = createdAt;
        this.size = size;
    }

}
