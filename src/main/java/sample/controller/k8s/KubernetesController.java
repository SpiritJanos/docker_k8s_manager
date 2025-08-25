package sample.controller.k8s;

import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.service.KubernetesClientService;

import java.util.List;

@RestController
public class KubernetesController {

    private final KubernetesClientService clientService = new KubernetesClientService();

    @RequestMapping("/namespaces")
    private String listNamespaces(){
        return clientService.listNamespaces().toString();
    }

    @RequestMapping("/pods/{namespace}")
    private ResponseEntity<List<Pod>> listPods(@PathVariable String namespace){
        return ResponseEntity.ok(clientService.listPods(namespace).getItems());
    }

    @RequestMapping("/namespace/{name}/create")
    private void createNamespace(@PathVariable String name){
        clientService.createNamespace(name);
    }

    @RequestMapping("/namespace/{name}/delete")
    private void deleteNamespace(@PathVariable String name){
        clientService.deleteNamespace(name);
    }

    @RequestMapping("/pod/create/{namespace}/{name}/{image}")
    private void createPodInNamespace(@PathVariable String namespace, @PathVariable String name, @PathVariable String image){
        clientService.createPod(namespace, name, image);
    }

    @RequestMapping("/pod/delete/{namespace}/{name}")
    private void deletePodInNamespace(@PathVariable String namespace, @PathVariable String name){
        clientService.deletePod(namespace, name);
    }

    @RequestMapping("/pod/{namespace}/{name}/logs")
    private String showPodLogsInNamespace(@PathVariable String namespace, @PathVariable String name){
        return clientService.showPodLogs(namespace, name);
    }

    @RequestMapping("/pod/{namespace}/{name}/exec/{command}")
    private String execCommandInPod(@PathVariable String namespace, @PathVariable String name, @PathVariable String command){
        return clientService.execCommandInPod(namespace, name, command.split(" "));
    }
}
