package museumvisit

import java.util.concurrent.TimeUnit
import kotlin.random.Random

const val TIMEOUT: Long = 500L
val TIMEUNIT = TimeUnit.MILLISECONDS
const val MAXRETRIES: Int = 5

val rnd = java.util.Random()

fun randomNumberBetween(
    a: Int,
    b: Int,
): Int = Random.nextInt(a, b)

class UnreachableRoomsException(private val rooms: List<MuseumRoom>) : Exception() {
    override fun toString(): String = "Unreachable rooms: ${rooms.sortedBy { it.name }.joinToString(", ")}"
}

class CannotExitMuseumException(private val rooms: List<MuseumRoom>) : Exception() {
    override fun toString(): String = "Impossible to leave museum from: ${rooms.sortedBy { it.name }.joinToString(", ") }}"
}