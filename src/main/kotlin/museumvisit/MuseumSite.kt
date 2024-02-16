package museumvisit

import java.util.concurrent.locks.Lock

interface MuseumSite {
    val lock: Lock

    fun hasCapacity(): Boolean

    fun enter()

    fun exit()
}
