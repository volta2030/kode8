package util

import Base
import util.Converter.Companion.toASCII
import util.Converter.Companion.toBinary
import util.Converter.Companion.toDecimal
import util.Converter.Companion.toHex
import util.Converter.Companion.toOctal

class DataProcessor {
    companion object {

        fun refineToString(byteArray: ByteArray, base: Base): String {

            return when (base) {
                Base.BINARY -> byteArray.toBinary()
                Base.OCTAL -> byteArray.toOctal()
                Base.DECIMAL -> byteArray.toDecimal()
                Base.HEXA_DECIMAL -> byteArray.toHex()
                Base.ASCII -> byteArray.toASCII()
                else -> byteArray.toHex()
            }
        }

        fun updateCellData(byteArray: ByteArray, rows: Int, cols: Int, base: Base): Array<Array<String>> {
            var cellData = Array(rows) { Array(cols) { "" } }
            val chunkSize = base.chunkSize
            val string = refineToString(byteArray, base)

            for (i in 0 until rows) {
                for (j in 0 until cols) {
                    val startIndex = i * cols * chunkSize + j * chunkSize
                    val endIndex = startIndex + chunkSize
                    cellData[i][j] = string.substring(startIndex, endIndex)
                }
            }

            return cellData
        }

        fun updateTrimmedCellData(cellData: Array<Array<String>>, rows : Int, rowsPerPage: Int, pageIndex: Int): Array<Array<String>> {

            if(rows < rowsPerPage) {
                return cellData
            }

            if(cellData.size < (pageIndex + 1) * rowsPerPage){
                return cellData.sliceArray(pageIndex * rowsPerPage until cellData.size)
            }

            return cellData.sliceArray(pageIndex * rowsPerPage until (pageIndex + 1) * rowsPerPage)
        }
    }
}