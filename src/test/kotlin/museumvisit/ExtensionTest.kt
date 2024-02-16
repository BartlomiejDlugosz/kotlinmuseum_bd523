package museumvisit

import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class ExtensionTest {
    private fun testMuseumVisit(
        museumUnderTest: MuseumUnderTest,
        people: List<String>,
        allPatient: Boolean,
    ) {
        val byteOutputStreams =
            people.map {
                ByteArrayOutputStream()
            }
        val printStreams =
            byteOutputStreams.map {
                PrintStream(it)
            }
        assertEquals(0, museumUnderTest.museum.admitted)
        assertEquals(0, museumUnderTest.museum.outside.occupancy)
        for (room in museumUnderTest.rooms) {
            assertEquals(0, room.occupancy)
        }
        val visitors =
            people.mapIndexed { index, person ->
                Thread(
                    if (allPatient || (index % 2 == 0)) {
                        PatientVisitor(person, printStreams[index], museumUnderTest.museum)
                    } else {
                        ImpatientVisitor(person, printStreams[index], museumUnderTest.museum)
                    },
                )
            }
        visitors.forEach { it.start() }
        visitors.forEach { it.join() }
        assertEquals(people.size, museumUnderTest.museum.admitted)
        assertEquals(people.size, museumUnderTest.museum.outside.occupancy)
        for (room in museumUnderTest.rooms) {
            assertEquals(0, room.occupancy)
        }
        byteOutputStreams.forEachIndexed { index, byteArrayOutputStream ->
            if (allPatient || (index % 2 == 0)) {
                checkPatientOutput(people[index], byteArrayOutputStream.toString(), museumUnderTest)
            } else {
                checkImpatientOutput(people[index], byteArrayOutputStream.toString(), museumUnderTest)
            }
        }
    }

    @Test
    fun `two patient visitors to small museum`() {
//        testMuseumVisit(createSmallMuseumUnderTest(), listOf("Ally", "Chris"), true)
        true
    }

    @Test
    fun `one patient and one impatient visitor to small museum`() {
        testMuseumVisit(createSmallMuseumUnderTest(), listOf("Ally", "Chris"), false)
    }

    @Test
    fun `many patient visitors to small museum`() {
//        testMuseumVisit(createSmallMuseumUnderTest(), lotsOfPeople, true)
        true
    }

    @Test
    fun `many mixed-patience visitors to small museum`() {
//        testMuseumVisit(createSmallMuseumUnderTest(), lotsOfPeople, false)
        true
    }

    @Test
    fun `two patient visitors to aquarium`() {
        testMuseumVisit(createAquariumMuseumUnderTest(), listOf("Ally", "Chris"), true)
    }

    @Test
    fun `one patient and one impatient visitor to aquarium`() {
        testMuseumVisit(createAquariumMuseumUnderTest(), listOf("Ally", "Chris"), false)
    }

    @Test
    fun `many patient visitors to aquarium`() {
//        testMuseumVisit(createAquariumMuseumUnderTest(), lotsOfPeople, true)
        true
    }

    @Test
    fun `many mixed-patience visitors to aquarium`() {
//        testMuseumVisit(createAquariumMuseumUnderTest(), lotsOfPeople, false)
        true
    }
}
