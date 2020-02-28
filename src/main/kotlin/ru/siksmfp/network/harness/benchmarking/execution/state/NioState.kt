package ru.siksmfp.network.harness.benchmarking.execution.state

import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import ru.siksmfp.network.harness.benchmarking.api.AbstractState
import ru.siksmfp.network.harness.implementation.nio.NioClient
import ru.siksmfp.network.harness.implementation.nio.NioServer

@State(Scope.Benchmark)
open class NioState : AbstractState(
        serverClass = NioServer::class,
        clientClass = NioClient::class,
        clientTestThreads = 5,
        serverThreads = 6
)