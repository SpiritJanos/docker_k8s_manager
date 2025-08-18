package sample.configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class DockerClientConfiguration {

    @Value("${docker.host}")
    private String dockerHost;


    private DockerClientConfig createClientConfig(){
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost).build();
    }

    private DockerHttpClient createHttpClient(DockerClientConfig config){
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
    }

    @Bean
    public DockerClient dockerClient(){
        DockerClientConfig config = createClientConfig();
        return DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(createHttpClient(config))
                .build();
    }
}
