package museumvisit

import java.lang.UnsupportedOperationException
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class MuseumOutside : MuseumSite {
    override val lock: Lock = ReentrantLock()

    val name = "Outside"
    var occupancy: Int = 0
        private set

    override fun hasCapacity(): Boolean = true

    override fun enter() {
        occupancy++
    }

    override fun exit() {
        throw UnsupportedOperationException()
    }

    override fun toString(): String = name
}
