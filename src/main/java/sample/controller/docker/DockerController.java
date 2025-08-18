package sample.controller.docker;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.service.DockerClientService;

import java.util.List;

@RestController
public class DockerController {

    private final DockerClientService dockerClientService;

    public DockerController(DockerClientService dockerClientService) {
        this.dockerClientService = dockerClientService;
    }

    @RequestMapping("/containers")
    public ResponseEntity<List<Container>> listContainers(){
        return ResponseEntity.ok(dockerClientService.listContainers());
    }

    @RequestMapping("/images")
    public ResponseEntity<List<Image>> listImages(){
        return ResponseEntity.ok(dockerClientService.listImages());
    }

    @RequestMapping("/images/search/{searchTerm}")
    public ResponseEntity<List<SearchItem>> searchImage(@PathVariable String searchTerm){
        return ResponseEntity.ok(dockerClientService.searchImages(searchTerm));
    }

    @RequestMapping("/image/pull/{imageName}")
    public ResponseEntity<String> pullImage(@PathVariable String imageName){
        try {
            dockerClientService.pullImages(imageName);
        } catch (InterruptedException ie){
            return ResponseEntity.internalServerError().body("Image pull was failed. Check logs for further details!");
        }
        return ResponseEntity.ok("Image pull was successful");
    }

    @RequestMapping("/image/rm/{imageName}")
    public void removeImage(@PathVariable String imageName){
        dockerClientService.removeImage(imageName);
    }

    @RequestMapping("/container/create/{imageName}")
    public void createContainer(@PathVariable String imageName){
        dockerClientService.createContainer(imageName);
    }

    @RequestMapping("/container/rm/{containerName}")
    public void removeContainer(@PathVariable String containerName){
        dockerClientService.removeContainer(containerName);
    }

    @RequestMapping("/container/start/{containerName}")
    public void startContainer(@PathVariable String containerName){
        dockerClientService.startContainer(containerName);
    }

    @RequestMapping("/container/stop/{containerName}")
    public void stopContainer(@PathVariable String containerName){
        dockerClientService.stopContainer(containerName);
    }

    @RequestMapping("/container/inspect/{containerName}")
    public ResponseEntity<InspectContainerResponse> inspectContainer(@PathVariable String containerName){
        return ResponseEntity.ok(dockerClientService.inspectContainer(containerName));
    }
}
