enum class DisplayMode(val chunkSize : Int) {
    BINARY(8),
    INTEGER(3),
    HEX(2),
    ASCII(1)
}