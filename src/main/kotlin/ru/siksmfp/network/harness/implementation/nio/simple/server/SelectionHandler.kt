package ru.siksmfp.network.harness.implementation.nio.simple.server

import java.nio.channels.SelectionKey

interface SelectionHandler {

    fun handle(selectionKey: SelectionKey)

    fun close()
}