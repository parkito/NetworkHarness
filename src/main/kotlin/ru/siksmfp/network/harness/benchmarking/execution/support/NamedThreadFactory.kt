package ru.siksmfp.network.harness.benchmarking.execution.support

import java.util.concurrent.ThreadFactory

class NamedThreadFactory(
        private val name: String
) : ThreadFactory {
    private var counter = 0
    override fun newThread(r: Runnable): Thread {
        return Thread(r, "$name-${counter++}")
    }
}
