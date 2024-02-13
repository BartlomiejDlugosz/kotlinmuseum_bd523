package museumvisit

class MuseumRoom(val name: String, private val capacity: Int) {
    var occupancy: Int = 0
        private set
    init {
        if (capacity < 0) throw IllegalArgumentException()
    }

    fun hasCapacity() = occupancy < capacity

    fun enter() {
        if (hasCapacity()) occupancy++
        else throw UnsupportedOperationException()
    }

    fun exit() {
        if (occupancy > 0) occupancy--
        else throw UnsupportedOperationException()
    }

    override fun toString(): String = name
    override fun hashCode(): Int = name.hashCode()
    override fun equals(other: Any?): Boolean = other is MuseumRoom && name == other.name
}