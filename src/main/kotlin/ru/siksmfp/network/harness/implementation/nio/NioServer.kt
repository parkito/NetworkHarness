package ru.siksmfp.network.harness.implementation.nio

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server

class NioServer(
        private val port: Int,
        threadNumber: Int?
) : Server<String> {

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setHandler(handler: Handler<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}