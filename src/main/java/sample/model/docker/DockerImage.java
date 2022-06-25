package sample.model.docker;

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

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
