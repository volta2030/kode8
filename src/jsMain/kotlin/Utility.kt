class Utility {
    companion object {
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

    }
}