package museumvisit

import java.io.PrintStream

class PatientVisitor(val name: String, private val ptStream: PrintStream, val museum: Museum) : Runnable {
    private var currentRoom: MuseumSite = museum.outside
        set(v) {
            if (v !is MuseumOutside) {
                ptStream.println("$name has entered $v.")
            }
            field = v
        }

    override fun run() {
        var room = museum.enterByWaiting()
        while (room == null) {
            ptStream.println("$name could not get into ${museum.name} but will try again soon.")
            Thread.sleep(10)
            ptStream.println("$name is ready to try again.")
            room = museum.enterByWaiting()
        }
        ptStream.println("$name has entered ${museum.name}.")
        currentRoom = room

        while (currentRoom !is MuseumOutside) {
            Thread.sleep(randomNumberBetween(1, 200).toLong())
            ptStream.println("$name wants to leave $currentRoom.")

            var nextRoom = museum.getNextRandomRoom(currentRoom)

            var success = museum.passThroughTurnstileByWaiting(currentRoom, nextRoom)
            while (success == null) {
                ptStream.println("$name failed to leave $currentRoom but will try again soon.")
                Thread.sleep(10)
                ptStream.println("$name is ready to try leaving $currentRoom again.")
                nextRoom = museum.getNextRandomRoom(currentRoom)
                success = museum.passThroughTurnstileByWaiting(currentRoom, nextRoom)
            }

            ptStream.println("$name has left $currentRoom.")
            currentRoom = success
        }
        ptStream.println("$name has left ${museum.name}.")
        return
    }
}
