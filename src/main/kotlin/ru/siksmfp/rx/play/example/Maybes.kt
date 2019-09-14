package ru.siksmfp.rx.play.example

import io.reactivex.Maybe
import ru.siksmfp.rx.play.model.Db
import ru.siksmfp.rx.play.model.Entity


fun main() {
    val maybes = Maybe.create<List<Entity>> { emitter ->
        try {
            val entities = Db.getEntities()
            if (entities.isNotEmpty()) {
                emitter.onSuccess(entities)
            } else {
                emitter.onComplete()
            }
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }
}