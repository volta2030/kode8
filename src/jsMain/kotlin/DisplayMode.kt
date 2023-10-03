enum class DisplayMode(val chunkSize : Int) {
    BINARY(8),
    INT(3),
    HEX(2),
    ASCII(1)
}