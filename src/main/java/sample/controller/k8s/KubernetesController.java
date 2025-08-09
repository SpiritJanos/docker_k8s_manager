package sample.controller.k8s;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.service.KubernetesClientService;

@RestController
public class KubernetesController {

    private final KubernetesClientService clientService = new KubernetesClientService();

    @RequestMapping("/namespaces")
    private String listNamespaces(){
        return clientService.listNamespaces().toString();
    }

    @RequestMapping("/pods/{namespace}")
    private String listPods(@PathVariable String namespace){
        return clientService.listPods(namespace).toString();
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
}
