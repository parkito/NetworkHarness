package ru.siksmfp.network.harness.implementation.nio

import tlschannel.TlsChannel
import java.nio.ByteBuffer

class NioServerContext(
        val tlsChannel: TlsChannel,
        val buffer: ByteBuffer
)