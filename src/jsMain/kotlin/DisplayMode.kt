enum class DisplayMode(val chunkSize : Int, val label : String) {
    BINARY(8, "Binary"),
    INTEGER(3, "Integer"),
    HEX(2, "Hex"),
    ASCII(1, "ASCII")
}