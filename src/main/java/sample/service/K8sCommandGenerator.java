package sample.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import sample.model.k8s.Namespace;
import sample.model.k8s.PodDescription;
import sample.model.k8s.PodGeneral;
import sample.model.k8s.Pods;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class K8sCommandGenerator {

    public void selectCluster(String name) {
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"kubectl", "config", "use-context", name});
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<String> listClusters() {
        String clusterName;
        ObservableList<String> clusters = FXCollections.observableArrayList();

        try {
            Process send = Runtime.getRuntime().exec(new String[]{"kubectl", "config", "get-clusters"});

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = null;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                clusterName = line;
                clusters.add(clusterName);
            }
            send.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clusters;
    }

    public Task<Void> installCluster(String name) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Process send;
                    BufferedReader bufferedReader;
                    if (name.equals("")) {
                        send = Runtime.getRuntime().exec(new String[]{"minikube", "start"});
                        bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
                        String line = null;

                        while ((line = bufferedReader.readLine()) != null) {
                            updateMessage(line);
                        }
                    } else {
                        send = Runtime.getRuntime().exec(new String[]{"minikube", "start", "-p", name});
                        bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
                        String line = null;

                        while ((line = bufferedReader.readLine()) != null) {
                            updateMessage(line);
                        }
                    }
                    send.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public Task<Void> stopCluster(String name) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Process send = Runtime.getRuntime().exec(new String[]{"minikube", "stop", "-p", name});
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        updateMessage(line);
                    }
                    send.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public Task<Void> deleteCluster(String name) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Process send = Runtime.getRuntime().exec(new String[]{"minikube", "delete", "-p", name});
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        updateMessage(line);
                    }
                    send.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public ObservableList<Namespace> listNamespaces() {
        String name;
        String status;
        String age;
        ObservableList<Namespace> namespaces = FXCollections.observableArrayList();

        try {
            Process send = Runtime.getRuntime().exec(new String[]{"kubectl", "get", "namespace", "-o", "wide"});

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = null;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("\\s+");
                name = splitedLine[0];
                status = splitedLine[1];
                age = splitedLine[2];
                namespaces.add(new Namespace(name, status, age));
            }
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return namespaces;
    }

    public void createNamespace(String name) {
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"kubectl", "create", "namespace", name});
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteNamespace(String name) {
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"kubectl", "delete", "namespace", name});
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Pods> listPods(String selectedName) {
        String name;
        String readiness;
        String status;
        int restarts;
        String age;
        String ip;
        String node;
        String nominatedNode;
        String readinessGate;
        ObservableList<Pods> pods = FXCollections.observableArrayList();

        try {
            Process send = Runtime.getRuntime().exec(new String[]{"kubectl", "get", "pods", "-o", "wide", "--namespace", selectedName});

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = null;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("\\s+");
                String[] filteredLine = Arrays.stream(splitedLine).filter(x -> !x.matches("\\(\\w+") && !x.matches("\\w+\\)")).toArray(String[]::new);
                name = filteredLine[0];
                readiness = filteredLine[1];
                status = filteredLine[2];
                restarts = Integer.parseInt(filteredLine[3]);
                age = filteredLine[4];
                ip = filteredLine[5];
                node = filteredLine[6];
                nominatedNode = filteredLine[7];
                readinessGate = filteredLine[8];
                pods.add(new PodGeneral(name, status, ip, node, restarts, readiness, nominatedNode, readinessGate, age));
            }
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pods;
    }

    public void createPods(String name, String imageName, String namespaceName) {
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"kubectl", "run", name, "--image=" + imageName, "--namespace", namespaceName});
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Task<Void> showPodLogs(String podName, String namespaceName) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Process send = Runtime.getRuntime().exec(new String[]{"kubectl", "logs", podName, "-n", namespaceName, "-f"});

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        updateMessage(line);
                        Thread.sleep(1);
                    }
                    send.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public PodDescription describePod(String podName, String namespaceName) {
        String name = null;
        String namespace = null;
        String status = null;
        String node = null;
        String startTime = null;
        String image = null;
        String ipAddress = null;
        String port = null;
        String hostPort = null;
        String createdAt = null;
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"kubectl", "describe", "pod", podName, "-n", namespaceName});

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("\\:\\s+");
                String[] filteredLine = Arrays.stream(splitedLine)
                        .filter(x -> !x.matches(""))
                        .toArray(String[]::new);
                if (splitedLine.length <= 1) {
                    continue;
                }
                if (filteredLine[0].replaceAll("\\s+", "").equals("Name")) {
                    name = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].replaceAll("\\s+", "").equals("Namespace")) {
                    namespace = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].replaceAll("\\s+", "").equals("Node")) {
                    node = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].equals("Start Time")) {
                    createdAt = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].replaceAll("\\s+", "").equals("Status")) {
                    status = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].replaceAll("\\s+", "").equals("IP")) {
                    ipAddress = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].replaceAll("\\s+", "").equals("Started")) {
                    startTime = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].replaceAll("\\s+", "").equals("Image")) {
                    image = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].replaceAll("\\s+", "").equals("Port")) {
                    port = filteredLine[1];
                    continue;
                }
                if (filteredLine[0].replaceFirst("\\s+", "").equals("Host Port")) {
                    hostPort = filteredLine[1];
                }
            }
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PodDescription(name, status, ipAddress, node, namespace, startTime, createdAt, image, port, hostPort);
    }

    public void deletePod(String podName, String namespace) {
        try {
            Process send = Runtime.getRuntime().exec(new String[]{"kubectl", "delete", "pod", podName, "--namespace", namespace});
            send.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Task<Void> runCommandInPodTerminal(String name, String command, String namespace) {
        String fullCommand = "kubectl exec -ti -n " + namespace + " " + name + " -- " + command;
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Process send = Runtime.getRuntime().exec(fullCommand);

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(send.getInputStream()));
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        updateMessage(line);
                        Thread.sleep(1);
                    }
                    send.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
