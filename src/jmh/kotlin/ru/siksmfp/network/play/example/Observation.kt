package ru.siksmfp.network.play.example

import io.reactivex.Observable
import ru.siksmfp.network.play.model.Db
import ru.siksmfp.network.play.model.Entity
//import ru.siksmfp.network.play.tools.printEntityOnNext
//import ru.siksmfp.network.play.tools.printExMessage
//import ru.siksmfp.network.play.tools.printFormattedEntityOnNext

//Emits 0 or n items and terminates with an success or an error event.

fun main() {
    val entityObservable = Observable.create<Entity> { emitter ->
        try {
            println(Thread.currentThread().name)
            for (entity in Db.getEntities()) {
                if (entity.age == 2) {
                    throw IllegalStateException("Exception occurred while processing $entity")
                }
                emitter.onNext(entity)
            }
            emitter.onComplete()
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }.filter { e -> e.age > 0 }

//    entityObservable.subscribe(printEntityOnNext, printExMessage, { print("completed") })
//    entityObservable.subscribe(printFormattedEntityOnNext, printExMessage, { print("completed") })
    entityObservable.subscribe { print("completed") }

    entityObservable.scan { t1: Entity, t2: Entity -> t2 }
//            .subscribe(printFormattedEntityOnNext, printExMessage, { print("completed") })
}