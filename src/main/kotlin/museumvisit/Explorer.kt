package museumvisit

fun main() {
    val museums = listOf(createArtGallery(), createAnimalSanctuary())
    while (true) {
        println("Which museum would you like to explore?")
        println("\t" + museums.joinToString(", ") { it.name })
        var input: String? = readlnOrNull() ?: break
        var selectedMuseum = museums.first { it.name == input }
        var currentRoom: MuseumSite = selectedMuseum.enter()
        println("Welcome to ${selectedMuseum.name}! Let’s explore.")
        while (true) {
            println("You are in $currentRoom")
            println("Have a good look around. Bored yet? Where do you want to go?")
            println("From here, you can go to:")
            println("\t${selectedMuseum[currentRoom].joinToString(", ")}")
            input = readlnOrNull() ?: break
            if (selectedMuseum[currentRoom].any { it.name == input }) {
                val newRoom = selectedMuseum[currentRoom].first { it.name == input }
                selectedMuseum.passThroughTurnstile(currentRoom, newRoom)
                currentRoom = newRoom
                if (currentRoom is MuseumOutside) {
                    println("We hope you had a good time in the ${selectedMuseum.name} museum - goodbye!!")
                    break
                }
            } else {
                println("I’m sorry, but that’s not one of the next places you can go. Let’s try again.")
            }
        }
    }
    println("You have had enough of this game - what is wrong with you? Goodbye.")
}
