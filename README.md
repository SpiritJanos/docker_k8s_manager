# Docker and Kubernetes Manager

Szükséges szoftverek:
- Docker Desktop: https://docs.docker.com/desktop/windows/install/
- Minikube: https://minikube.sigs.k8s.io/docs/start/
- Java 17 jdk

## Script futtatása
Készítettem egy futtatható jar filet amit az out mappában az artifactsen belül található. az alábbi parancsot futtatva tudjuk inditani a programot:

```shell
java --module-path "<projekt_eleresi_utja>\util\openjfx-17.0.2_windows-x64_bin-sdk\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar docker_k8s_manager.jar
```
## Alternatív futtatas maven pluginnal
Így 
```shell
mvn clean javafx:run
```
