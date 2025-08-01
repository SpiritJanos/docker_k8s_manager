package sample.model.docker;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DockerRepositoryImage {
    private String repositoryImageName;
    private String description;
    private Integer stars;
    private boolean official;

    public DockerRepositoryImage(String repositoryImageName, String description, Integer stars, boolean official) {
        this.repositoryImageName = repositoryImageName;
        this.description = description;
        this.stars = stars;
        this.official = official;
    }

}
