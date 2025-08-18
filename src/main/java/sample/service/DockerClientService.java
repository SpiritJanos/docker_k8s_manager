package sample.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DockerClientService {

    private final DockerClient dockerClient;

    public DockerClientService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public List<Container> listContainers(){
        return dockerClient.listContainersCmd().withShowAll(true).exec();
    }

    public void createContainer(String imageName){
        dockerClient.createContainerCmd(imageName).exec();
    }

    public void removeContainer(String containerName){
        dockerClient.removeContainerCmd(containerName).exec();
    }

    public void startContainer(String containerName){
        dockerClient.startContainerCmd(containerName).exec();
    }

    public void stopContainer(String containerName){
        dockerClient.stopContainerCmd(containerName).exec();
    }

    public InspectContainerResponse inspectContainer(String containerName){
        return dockerClient.inspectContainerCmd(containerName).exec();
    }

    public List<Image> listImages(){
        return dockerClient.listImagesCmd().exec();
    }

    public List<SearchItem> searchImages(String term){
        return dockerClient.searchImagesCmd(term).exec();
    }

    public void pullImages(String imageName) throws InterruptedException {
        dockerClient.pullImageCmd(imageName).exec(new PullImageResultCallback()).awaitCompletion(30, TimeUnit.SECONDS);
    }

    public void removeImage(String imageName){
        dockerClient.removeImageCmd(imageName).exec();
    }
}
