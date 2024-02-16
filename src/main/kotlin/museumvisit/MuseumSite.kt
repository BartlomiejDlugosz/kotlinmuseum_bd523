package museumvisit

import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock

interface MuseumSite {
    val name: String
    val lock: Lock
    val condition: Condition

    fun hasCapacity(): Boolean

    fun enter()

    fun exit()
}
