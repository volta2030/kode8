import Utility.Companion.toInt

class Utility {
    companion object {

        fun ByteArray.toBinary(): String {
            return this.joinToString("") { byte ->
                byte.toUByte().toString(2).padStart(8, '0')
            }
        }

        fun ByteArray.toInt(): String {
            return this.joinToString("") { byte ->
                byte.toUByte().toInt().toString().padStart(3, '0')
            }
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
                asciiChars[i] = this[i].toInt().toChar()
            }

            return asciiChars.concatToString()
        }
    }
}