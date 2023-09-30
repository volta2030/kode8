class Utility {
    companion object {
        fun Int.toHex() : String{
            // 정수를 두 자리 16진수 문자열로 변환
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

        fun String.trim(column : Int) : String{

            var trimedString = ""

            for (i : Int in indices step column){
                trimedString += this.slice(i until i + column) + "\n"
            }

            return trimedString
        }
    }
}