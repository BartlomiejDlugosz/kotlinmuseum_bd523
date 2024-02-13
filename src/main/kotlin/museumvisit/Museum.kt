package museumvisit

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class UnreachableRoomsException(private val rooms: List<MuseumRoom>): Exception() {
    override fun toString(): String = "Unreachable rooms: ${rooms.sortedBy { it.name }.joinToString(", ")}"
}

class CannotExitMuseumException(private val rooms: List<MuseumRoom>): Exception() {
    override fun toString(): String = "Impossible to leave museum from: ${rooms.sortedBy { it.name }.joinToString(", ") }}"
}

class Museum(val name: String, private val entrance: MuseumRoom) {
    private var admitted: Int = 0
        private set
    private val rooms: HashMap<MuseumRoom, MutableList<MuseumRoom>> = hashMapOf(entrance to mutableListOf())
    private val outside = MuseumRoom("Outside", 0)

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

    fun connectRoomTo(roomFrom: MuseumRoom, roomTo: MuseumRoom) {
        if (!(rooms.containsKey(roomFrom) && rooms.containsKey(roomTo))) throw IllegalArgumentException()
        if (rooms[roomFrom]!!.contains(roomTo) || roomFrom == roomTo) throw IllegalArgumentException()
        rooms[roomFrom]!!.add(roomTo)
    }

    fun connectRoomToExit(room: MuseumRoom) {
        if (!rooms.containsKey(room) || rooms[room]!!.contains(outside)) throw IllegalArgumentException()
        rooms[room]!!.add(outside)
    }

    fun checkWellFormed() {
        //check unreachable
        val toVisit: MutableSet<MuseumRoom> = rooms.keys.toMutableSet()
        val queue: Queue<MuseumRoom> = LinkedList(listOf(entrance))
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.name != "Outside") {
                queue.addAll(rooms[current]!!.filter {toVisit.contains(it)})
                toVisit.remove(current)
            }
        }
        if (toVisit.isNotEmpty()) throw UnreachableRoomsException(toVisit.toList())

        //check no exit
        val roomsWithNoExit = rooms.any {it.value.contains(outside)}
        if (!roomsWithNoExit) throw CannotExitMuseumException(rooms.keys.toList())
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
                queue.addAll(connectedRooms.filter{!visited.contains(it)})
            }
        }

        return str.toString()
    }
}