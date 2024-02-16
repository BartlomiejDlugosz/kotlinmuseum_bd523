package museumvisit

import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class MuseumRoom(override val name: String, private val capacity: Int) : MuseumSite {
    override val lock: Lock = ReentrantLock()
    override val condition: Condition = lock.newCondition()

    var occupancy: Int = 0
        private set

    init {
        if (capacity < 0) throw IllegalArgumentException()
    }

    override fun hasCapacity() = occupancy < capacity

    override fun enter() {
        if (hasCapacity()) {
            occupancy++
        } else {
            throw UnsupportedOperationException()
        }
    }

    override fun exit() {
        if (occupancy > 0) {
            occupancy--
            condition.signal()
        } else {
            throw UnsupportedOperationException()
        }
    }

    override fun toString(): String = name

    override fun hashCode(): Int = name.hashCode()

    override fun equals(other: Any?): Boolean = other is MuseumRoom && name == other.name
}
