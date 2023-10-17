package util

import Base
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import util.Converter.Companion.toASCII
import util.Converter.Companion.toBinary
import util.Converter.Companion.toDecimal
import util.Converter.Companion.toHex
import util.Converter.Companion.toOctal

class DataProcessor {
    companion object {

        var columns by mutableStateOf(64)
        var rows by mutableStateOf(0)
        var size by mutableStateOf(0)

        var pageIndex by mutableStateOf(0)
        var goToPageIndex by mutableStateOf(1)
        var rowsPerPage by mutableStateOf(100)

        //radio buttons
        var base by mutableStateOf(Base.HEXA_DECIMAL)

        var cellData by mutableStateOf(
            Array(1) { Array(columns) { "" } }
        )

        var trimmedCellData by mutableStateOf(
            Array(1) { Array(1) { "" } }
        )

        var selectedRow by mutableStateOf(-1)
        var selectedColumn by mutableStateOf(-1)

        var byteArray by mutableStateOf(byteArrayOf())

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