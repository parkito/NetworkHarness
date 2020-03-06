package ru.siksmfp.network.harness.implementation.nio.ssl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import ru.siksmfp.network.harness.implementation.SSLUtils;
import tlschannel.ClientTlsChannel;
import tlschannel.TlsChannel;

/**
 * Client example. Connects to a public TLS reporting service.
 */
public class SimpleBlockingClient {

    private static final Charset utf8 = StandardCharsets.UTF_8;

    public static final String domain = "www.howsmyssl.com";
    public static final String httpLine =
            "GET https://www.howsmyssl.com/a/check HTTP/1.0\nHost: www.howsmyssl.com\n\n";

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        // initialize the SSLContext, a configuration holder, reusable object
//    SSLContext sslContext = SSLContext.getDefault();

        // connect raw socket channel normally
        try (SocketChannel rawChannel = SocketChannel.open()) {
            rawChannel.connect(new InetSocketAddress("localhost", 8081));

            // create TlsChannel builder, combining the raw channel and the SSLEngine, using minimal
            // options
            ClientTlsChannel.Builder builder = ClientTlsChannel.newBuilder(rawChannel, SSLUtils.INSTANCE.constructSSLContext());

            // instantiate TlsChannel
            try (TlsChannel tlsChannel = builder.build()) {

                // do HTTP interaction and print result
                tlsChannel.write(ByteBuffer.wrap(httpLine.getBytes()));
                ByteBuffer res = ByteBuffer.allocate(10000);
                // being HTTP 1.0, the server will just close the connection at the end
                while (tlsChannel.read(res) != -1) ;
                res.flip();
                System.out.println(utf8.decode(res).toString());
            }
        }
    }
}
