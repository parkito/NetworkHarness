package ru.siksmfp.rx.play.tools

import ru.siksmfp.rx.play.model.Entity

val printEntityOnNext = { e: Entity -> println(e) }

val printFormattedEntityOnNext = { e: Entity -> println("-----\n $e") }