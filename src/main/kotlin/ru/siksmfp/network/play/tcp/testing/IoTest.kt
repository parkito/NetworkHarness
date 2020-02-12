package ru.siksmfp.network.play.tcp.testing

import ru.siksmfp.network.play.tcp.io.simple.IoClient
import ru.siksmfp.network.play.tcp.io.simple.IoServer

fun main() {
    TestExecutor(
            serverClass = IoServer::class,
            clientClass = IoClient::class,
            testFile = "/Users/parkito/Downloads/test.txt"
    ).executeTest()
}