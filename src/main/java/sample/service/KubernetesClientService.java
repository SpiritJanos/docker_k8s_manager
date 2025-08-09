package sample.service;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.jvnet.hk2.annotations.Service;

import java.util.List;

@Service
public class KubernetesClientService {

    private final KubernetesClient client = new KubernetesClientBuilder().build();

    public List<String> listNamespaces(){
        NamespaceList list = client.namespaces().list();
        List<String> nsName = list.getItems().stream()
                .map(
                        ns -> ns.getMetadata().getLabels().get("kubernetes.io/metadata.name")
                )
                .toList();
        return nsName;
    }

    public void createNamespace(String name){
        client.namespaces().create(new NamespaceBuilder()
                .withNewMetadata()
                .withName(name)
                .addToLabels("whatever", "test")
                .endMetadata().build());
    }

    public void deleteNamespace(String name) {
        client.namespaces().withName(name).delete();
    }

    public PodList listPods(String namespace) {
        return client.pods().inNamespace(namespace).list();
    }

    public void createPod(String namespace, String name, String image){
        Pod pod = new Pod();
        pod.setSpec(new PodSpecBuilder()
                        .addNewContainer()
                        .withName(name)
                        .withImage(image)
                        .endContainer()
                .build());
        pod.setMetadata(new ObjectMetaBuilder()
                .withName(name)
                .build());
        client.pods().inNamespace(namespace).resource(pod).create();
    }

    public void deletePod(String namespace, String name) {
        client.pods().inNamespace(namespace).withName(name).delete();
    }

}
