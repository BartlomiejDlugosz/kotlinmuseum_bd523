package museumvisit

import java.io.PrintStream
import kotlin.random.Random

fun randomNumberBetween(
    a: Int,
    b: Int,
): Int = Random.nextInt(a, b)

class ImpatientVisitor(val name: String, val ptStream: PrintStream, val museum: Museum) : Runnable {
    private var currentRoom: MuseumSite = museum.outside
        set(v) {
            if (v !is MuseumOutside) {
                ptStream.println("$name has entered $v.")
            }
            field = v
        }

    override fun run() {
        var room = museum.enterIfPossible()
        while (room == null) {
            ptStream.println("$name could not get into ${museum.name} but will try again soon.")
            Thread.sleep(10)
            ptStream.println("$name is ready to try again.")
            room = museum.enterIfPossible()
        }
        ptStream.println("$name has entered ${museum.name}.")
        currentRoom = room

        while (currentRoom !is MuseumOutside) {
            Thread.sleep(randomNumberBetween(1, 200).toLong())
            ptStream.println("$name wants to leave $currentRoom.")

            var nextRoom = museum[currentRoom as MuseumRoom]!![randomNumberBetween(0, museum[currentRoom as MuseumRoom]!!.size)]
            var success = museum.passThroughTurnstile(currentRoom, nextRoom)
            while (success == null) {
                ptStream.println("$name failed to leave $currentRoom but will try again soon.")
                Thread.sleep(10)
                ptStream.println("$name is ready to try leaving $currentRoom again.")
                nextRoom = museum[currentRoom as MuseumRoom]!![randomNumberBetween(0, museum[currentRoom as MuseumRoom]!!.size)]
                success = museum.passThroughTurnstile(currentRoom, nextRoom)
            }
            ptStream.println("$name has left $currentRoom.")
            currentRoom = success
        }
        ptStream.println("$name has left ${museum.name}.")
        return
    }
}
