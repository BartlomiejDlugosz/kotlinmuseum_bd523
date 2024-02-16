package museumvisit

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.concurrent.withLock

class UnreachableRoomsException(private val rooms: List<MuseumRoom>) : Exception() {
    override fun toString(): String = "Unreachable rooms: ${rooms.sortedBy { it.name }.joinToString(", ")}"
}

class CannotExitMuseumException(private val rooms: List<MuseumRoom>) : Exception() {
    override fun toString(): String = "Impossible to leave museum from: ${rooms.sortedBy { it.name }.joinToString(", ") }}"
}

class Museum(val name: String, private val entrance: MuseumRoom) {
    var admitted: Int = 0
        private set
    val outside = MuseumOutside()
    private val rooms: HashMap<MuseumSite, MutableList<MuseumSite>> = hashMapOf(entrance to mutableListOf(), outside to mutableListOf())

    fun entranceHasCapacity() = entrance.hasCapacity()

    fun enter() {
        if (!entranceHasCapacity()) throw UnsupportedOperationException()
        admitted++
        entrance.enter()
    }

    fun addRoom(room: MuseumRoom) {
        if (rooms.containsKey(room)) throw IllegalArgumentException()
        rooms[room] = mutableListOf()
    }

    fun connectRoomTo(
        roomFrom: MuseumRoom,
        roomTo: MuseumRoom,
    ) {
        if (!(rooms.containsKey(roomFrom) && rooms.containsKey(roomTo))) throw IllegalArgumentException()
        if (rooms[roomFrom]!!.contains(roomTo) || roomFrom == roomTo) throw IllegalArgumentException()
        rooms[roomFrom]!!.add(roomTo)
    }

    fun connectRoomToExit(room: MuseumRoom) {
        if (!rooms.containsKey(room) || rooms[room]!!.contains(outside)) throw IllegalArgumentException()
        rooms[room]!!.add(outside)
    }

    fun checkWellFormed() {
        // check unreachable
        val toVisit: HashSet<MuseumRoom> = rooms.keys.filterIsInstance<MuseumRoom>().toHashSet()
        val queue: Queue<MuseumRoom> = LinkedList(listOf(entrance))
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            queue.addAll(rooms[current]!!.filter { (it is MuseumRoom) && (toVisit.contains(it)) } as List<MuseumRoom>)
            toVisit.remove(current)
        }
        if (toVisit.isNotEmpty()) throw UnreachableRoomsException(toVisit.toList())

        // check no exit
        val roomsWithExit: HashSet<MuseumSite> = hashSetOf(outside)
        for ((_, _) in rooms) {
            for ((room, connectedRooms) in rooms) {
                if (connectedRooms.any { it in roomsWithExit }) {
                    roomsWithExit.add(room)
                }
            }
        }
        val roomsWithNoExit = rooms.keys.filterIsInstance<MuseumRoom>().toList().filter { it !in roomsWithExit }
        if (roomsWithNoExit.isNotEmpty()) throw CannotExitMuseumException(roomsWithNoExit)
    }

    fun enterIfPossible(): MuseumRoom? {
        entrance.lock.withLock {
            if (entranceHasCapacity()) {
                enter()
                return entrance
            }
        }
        return null
    }

    fun passThroughTurnstile(
        from: MuseumSite,
        to: MuseumSite,
    ): MuseumSite? {
        from.lock.withLock {
            to.lock.withLock {
                if (to.hasCapacity()) {
                    from.exit()
                    to.enter()
                    return to
                }
            }
        }
        return null
    }

    override fun toString(): String {
        val str = StringBuilder(name + "\n")
        val visited: HashSet<MuseumRoom> = hashSetOf()
        val queue: Queue<MuseumRoom> = LinkedList(listOf(entrance))
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (!visited.contains(current) && current.name != "Outside") {
                val connectedRooms = rooms[current]!!
                str.appendLine("${current.name} leads to: ${connectedRooms.joinToString(", ")}")
                visited.add(current)
                queue.addAll(connectedRooms.filter { (it is MuseumRoom) && !visited.contains(it) } as List<MuseumRoom>)
            }
        }

        return str.toString()
    }

    operator fun get(room: MuseumRoom) = rooms[room]
}
