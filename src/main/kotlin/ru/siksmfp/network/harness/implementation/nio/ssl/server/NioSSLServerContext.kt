package ru.siksmfp.network.harness.implementation.nio.ssl.server

import tlschannel.TlsChannel
import java.nio.ByteBuffer

class NioSSLServerContext(
        val tlsChannel: TlsChannel,
        val buffer: ByteBuffer
)