package ru.siksmfp.network.play.tools

import ru.siksmfp.network.play.model.Entity

val printEntityOnNext = { e: Entity -> println(e) }

val printFormattedEntityOnNext = { e: Entity -> println("-----\n $e") }