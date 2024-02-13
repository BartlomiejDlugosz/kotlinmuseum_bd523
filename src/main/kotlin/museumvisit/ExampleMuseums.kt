package museumvisit

fun createArtGallery(): Museum {
    val entrance = MuseumRoom("Entrance hall", 10)
    val exhibition = MuseumRoom("Exhibition room", 5)

    val museum = Museum("Art Gallery", entrance)

    museum.addRoom(exhibition)
    museum.connectRoomTo(entrance, exhibition)
    museum.connectRoomToExit(exhibition)

    return museum
}

fun createAnimalSanctuary(): Museum {
    val entrance = MuseumRoom("Entrance hall", 20)
    val bats = MuseumRoom("Bats", 10)
    val lizards = MuseumRoom("Lizards", 10)
    val snakes = MuseumRoom("Snakes", 10)
    val insects = MuseumRoom("Insects", 10)
    val giftShop = MuseumRoom("Gift shop", 20)

    val museum = Museum("Animal sanctuary", entrance)

    museum.addRoom(bats)
    museum.addRoom(lizards)
    museum.addRoom(giftShop)
    museum.addRoom(insects)
    museum.addRoom(snakes)

    museum.connectRoomTo(entrance, bats)
    museum.connectRoomTo(bats, lizards)
    museum.connectRoomTo(lizards, insects)
    museum.connectRoomTo(lizards, giftShop)
    museum.connectRoomTo(insects, snakes)
    museum.connectRoomTo(insects, giftShop)
    museum.connectRoomTo(snakes, entrance)

    museum.connectRoomToExit(giftShop)

    return museum
}

fun createAnimalSanctuaryWithUnreachableRooms(): Museum {
    val entrance = MuseumRoom("Entrance hall", 20)
    val bats = MuseumRoom("Bats", 10)
    val lizards = MuseumRoom("Lizards", 10)
    val snakes = MuseumRoom("Snakes", 10)
    val insects = MuseumRoom("Insects", 10)
    val giftShop = MuseumRoom("Gift shop", 20)

    val museum = Museum("Animal sanctuary", entrance)

    museum.addRoom(bats)
    museum.addRoom(lizards)
    museum.addRoom(giftShop)
    museum.addRoom(insects)
    museum.addRoom(snakes)

    museum.connectRoomTo(bats, lizards)
    museum.connectRoomTo(lizards, insects)
    museum.connectRoomTo(lizards, giftShop)
    museum.connectRoomTo(insects, snakes)
    museum.connectRoomTo(insects, giftShop)
    museum.connectRoomTo(snakes, entrance)

    museum.connectRoomToExit(giftShop)

    return museum
}

fun createAnimalSanctuaryWithRoomsThatDoNotLeadToExit(): Museum {
    val entrance = MuseumRoom("Entrance hall", 20)
    val bats = MuseumRoom("Bats", 10)
    val lizards = MuseumRoom("Lizards", 10)
    val snakes = MuseumRoom("Snakes", 10)
    val insects = MuseumRoom("Insects", 10)
    val giftShop = MuseumRoom("Gift shop", 20)

    val museum = Museum("Animal sanctuary", entrance)

    museum.addRoom(bats)
    museum.addRoom(lizards)
    museum.addRoom(giftShop)
    museum.addRoom(insects)
    museum.addRoom(snakes)

    museum.connectRoomTo(entrance, bats)
    museum.connectRoomTo(bats, lizards)
    museum.connectRoomTo(lizards, insects)
    museum.connectRoomTo(lizards, giftShop)
    museum.connectRoomTo(insects, snakes)

    museum.connectRoomToExit(giftShop)

    return museum
}