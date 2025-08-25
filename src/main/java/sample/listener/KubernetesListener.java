package sample.listener;

import io.fabric8.kubernetes.api.model.Status;
import io.fabric8.kubernetes.client.dsl.ExecListener;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

public class KubernetesListener implements ExecListener {

    private CompletableFuture<String> data;
    private ByteArrayOutputStream outputStream;

    public KubernetesListener(CompletableFuture<String> data, ByteArrayOutputStream outputStream) {
        this.data = data;
        this.outputStream = outputStream;
    }

    @Override
    public void onOpen() {
        System.out.println("Reading data..");
    }

    @Override
    public void onFailure(Throwable t, Response failureResponse) {
        System.err.println(t.getMessage());
        data.completeExceptionally(t);
    }

    @Override
    public void onClose(int i, String s) {
        System.out.println("Exit with: " + i + " and with reason: " + s);
        data.complete(outputStream.toString());
    }
}
