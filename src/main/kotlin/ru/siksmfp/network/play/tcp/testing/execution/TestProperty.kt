package ru.siksmfp.network.play.tcp.testing.execution

import java.time.LocalDateTime
import kotlin.reflect.KClass

class TestProperty(
        val serverClass: KClass<out Any>,
        val clientClass: KClass<out Any>,
        val clientTestThreads: Int,
        val serverThreads: Int,
        val testFileSize: Int
) {
    val testFile: String = "/tmp/test-${LocalDateTime.now()}.txt"
}
