package ru.siksmfp.network.play.tcp.testing.execution

import kotlin.reflect.KClass

class TestProperty(
        val serverClass: KClass<out Any>,
        val clientClass: KClass<out Any>,
        val testFile: String,
        val clientTestThreads: Int,
        val serverThreads: Int
)
