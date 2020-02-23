package ru.siksmfp.network.play.benchmarking.execution.model

import ru.siksmfp.network.play.api.Client
import ru.siksmfp.network.play.api.Server
import java.time.LocalDateTime
import kotlin.reflect.KClass

class BenchmarkProperty(
        val serverClass: KClass<out Server<*>>,
        val clientClass: KClass<out Client<*>>,
        val clientTestThreads: Int,
        val serverThreads: Int,
        val testFileSize: Int
) {
    val testFile: String = "/tmp/test-${LocalDateTime.now()}.txt"
}
