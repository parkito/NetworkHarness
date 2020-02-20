package ru.siksmfp.network.play.tcp.testing.state

import ru.siksmfp.network.play.api.Client
import ru.siksmfp.network.play.api.Server
import ru.siksmfp.network.play.tcp.testing.execution.TestProperty
import kotlin.reflect.KClass

abstract class AbstractState(
        private val serverClass: KClass<out Server<*>>,
        private val clientClass: KClass<out Client<*>>,
        private val clientTestThreads: Int,
        private val serverThreads: Int
) {

    fun getPropertyForSmall(): TestProperty {
        return TestProperty(
                serverClass,
                clientClass,
                clientTestThreads,
                serverThreads,
                testFileSize = 10
        )
    }

    fun getPropertyForMiddle(): TestProperty {
        return TestProperty(
                serverClass,
                clientClass,
                clientTestThreads,
                serverThreads,
                testFileSize = 1_000
        )
    }

    fun getPropertyForBig(): TestProperty {
        return TestProperty(
                serverClass,
                clientClass,
                clientTestThreads,
                serverThreads,
                testFileSize = 100_000
        )
    }

    fun getPropertyForLarge(): TestProperty {
        return TestProperty(
                serverClass,
                clientClass,
                clientTestThreads,
                serverThreads,
                testFileSize = 1_000_000
        )
    }
}