package type

enum class Base(val chunkSize : Int, val label : String) {
    BINARY(8, "Binary"),
    OCTAL(3, "Octal"),
    DECIMAL(3, "Decimal"),
    HEXA_DECIMAL(2, "Hexadecimal"),
    ASCII(1, "ASCII")
}