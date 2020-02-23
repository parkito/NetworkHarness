package ru.siksmfp.network.harness.benchmarking.execution.state

import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import ru.siksmfp.network.harness.benchmarking.api.AbstractState
import ru.siksmfp.network.harness.implementation.io.simple.IoClient
import ru.siksmfp.network.harness.implementation.io.simple.IoServer

@State(Scope.Benchmark)
open class IoSimpleFewThreadsState : AbstractState(
        serverClass = IoServer::class,
        clientClass = IoClient::class,
        clientTestThreads = 5,
        serverThreads = 6
)