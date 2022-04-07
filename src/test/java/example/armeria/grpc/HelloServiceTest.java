package example.armeria.grpc;

import com.linecorp.armeria.client.grpc.GrpcClients;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.testing.junit5.server.ServerExtension;
import example.armeria.grpc.Hello.HelloRequest;
import example.armeria.grpc.HelloServiceGrpc.HelloServiceBlockingStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static example.armeria.grpc.Main.configureServices;
import static org.assertj.core.api.Assertions.assertThat;

class HelloServiceTest {

    @RegisterExtension
    static final ServerExtension server = new ServerExtension() {
        @Override
        protected void configure(ServerBuilder sb) throws Exception {
            configureServices(sb);
        }
    };

    @Test
    void getReplyWorking() {
        final HelloServiceBlockingStub helloService =
                GrpcClients.newClient("gproto+http://127.0.0.1:" + server.httpPort() + "/", HelloServiceBlockingStub.class);
        assertThat(helloService.hello(HelloRequest.newBuilder().setName("Armeria").build()).getMessage())
                .isEqualTo("Hello, Armeria!");
    }

    @Test
    void getReplyNotWorking() {
        final HelloServiceBlockingStub helloService =
                GrpcClients.newClient("gproto+http://localhost:" + server.httpPort() + "/", HelloServiceBlockingStub.class);
        assertThat(helloService.hello(HelloRequest.newBuilder().setName("Armeria").build()).getMessage())
                .isEqualTo("Hello, Armeria!");
    }
}
