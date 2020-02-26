package ru.siksmfp.network.harness.implementation.nio.server

import java.nio.channels.SelectionKey

interface SelectionHandler {

    fun handle(selectionKey: SelectionKey)
}