package ru.siksmfp.network.harness.implementation.nio

import ru.siksmfp.network.harness.api.Client

class NioClient(
        private val host: String,
        private val port: Int
) : Client<String> {

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun send(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}