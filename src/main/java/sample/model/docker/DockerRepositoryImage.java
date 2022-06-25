package sample.model.docker;

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

    public String getRepositoryImageName() {
        return repositoryImageName;
    }

    public void setRepositoryImageName(String repositoryImageName) {
        this.repositoryImageName = repositoryImageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }
}
