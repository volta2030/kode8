enum class DisplayMode(val isSelected : Boolean, val chunkSize : Int) {
    BINARY(false, 8),
    INT(false, 3),
    HEX(false, 2),
    ASCII(false, 1)
}