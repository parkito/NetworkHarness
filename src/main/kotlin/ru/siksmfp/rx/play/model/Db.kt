package ru.siksmfp.rx.play.model

object Db {

    fun getEntities(): List<Entity> {
        return listOf(
                Entity("name1", 1),
                Entity("name2", 2),
                Entity("name3", 3),
                Entity("name4", 4),
                Entity("name5", 5),
                Entity("name6", 6),
                Entity("name7", 7)
        )
    }
}