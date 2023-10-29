package util

import type.Base

class Converter {
    companion object {

        fun ByteArray.toBinary(): String {
            return this.joinToString("") { byte ->
                byte.toUByte().toString(2).padStart(8, '0')
            }
        }

        fun ByteArray.toOctal(): String {
            return this.joinToString("") { byte ->
                byte.toUByte().toString(8).padStart(3, '0')
            }
        }

        fun ByteArray.toDecimal(): String {
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
                asciiChars[i] = this[i].toUByte().toInt().toChar()
            }

            return asciiChars.concatToString()
        }

        fun String.toCSVFormat(columns : Int, base : Base) : String{

            var str = ""
            var columnCount = 0

            for (i : Int in indices step base.chunkSize){
                if(columnCount==columns){
                    str +="\n"
                    columnCount = 0
                }
                str += this.substring(i, i + base.chunkSize) + ", "
                columnCount++
            }

            return str
        }
    }
}