class Utility {
    companion object {

        fun Int.toHex() : String{
            return this.toString(16).padStart(2, '0').uppercase()
        }

        fun ByteArray.toHex(): String {
            val hexChars = "0123456789ABCDEF"
            val result = StringBuilder(this.size * 2)

            for (byte in this) {
                val intValue = byte.toInt() and 0xFF
                val firstDigit = hexChars[intValue ushr 4]
                val secondDigit = hexChars[intValue and 0x0F]
                result.append(firstDigit)
                result.append(secondDigit)
            }

            return result.toString()
        }

        fun ByteArray.toASCII() : String {
            val asciiChars = CharArray(this.size)

            for (i in indices) {
                asciiChars[i] = this[i].toChar()
            }

            return asciiChars.concatToString()
        }
    }
}