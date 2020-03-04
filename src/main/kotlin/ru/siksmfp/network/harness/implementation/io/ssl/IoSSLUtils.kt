package ru.siksmfp.network.harness.implementation.io.ssl

import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLServerSocketFactory
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory

object IoSSLUtils {
    private val sslContext: SSLContext

    init {
        val ks = KeyStore.getInstance("pkcs12")
        ks.load(Thread.currentThread().contextClassLoader.getResourceAsStream("selfsigned.jks"), "11223344".toCharArray())

        val kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "11223344".toCharArray());

        val tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.keyManagers, tmf.trustManagers, null)
    }

    fun constructSSLServerFactory(): SSLServerSocketFactory {
        return sslContext.serverSocketFactory
    }

    fun constructSSLClientFactory(): SSLSocketFactory {
        return sslContext.socketFactory
    }
}
