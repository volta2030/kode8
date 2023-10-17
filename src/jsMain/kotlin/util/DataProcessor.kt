package util

import Base
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.files.File
import org.w3c.files.FileReader
import util.Converter.Companion.toASCII
import util.Converter.Companion.toBinary
import util.Converter.Companion.toDecimal
import util.Converter.Companion.toHex
import util.Converter.Companion.toOctal

class DataProcessor {
    companion object {

        var selectedFile: File? = null

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

        fun updateCellData() {
            cellData = Array(rows) { Array(columns) { "" } }
            val chunkSize = base.chunkSize
            val string = refineToString(byteArray, base)

            for (i in 0 until rows) {
                for (j in 0 until columns) {
                    val startIndex = i * columns * chunkSize + j * chunkSize
                    val endIndex = startIndex + chunkSize
                    cellData[i][j] = string.substring(startIndex, endIndex)
                }
            }
        }

        fun updateTrimmedCellData() {

            if(rows < rowsPerPage) {
                trimmedCellData = cellData
                return
            }

            if(cellData.size < (pageIndex + 1) * rowsPerPage){
                trimmedCellData = cellData.sliceArray(pageIndex * rowsPerPage until cellData.size)
                return
            }

            trimmedCellData =  cellData.sliceArray(pageIndex * rowsPerPage until (pageIndex + 1) * rowsPerPage)
        }

        fun load(){
            val fileReader = FileReader()
            fileReader.onload = { event ->
                val arrayBuffer = event.target.asDynamic().result as? ArrayBuffer
                if (arrayBuffer != null) {
                    selectedRow = -1
                    selectedColumn = -1
                    byteArray = Int8Array(arrayBuffer).unsafeCast<ByteArray>()
                    size = byteArray.size
                    rows = (byteArray.size - 1) / columns + 1
                    updateCellData()
                    updateTrimmedCellData()
                    pageIndex = 0
                    goToPageIndex = 1
                }
            }
            selectedFile?.let { fileReader.readAsArrayBuffer(it) }
        }

        fun getOrder() : Int{
           return (pageIndex * rowsPerPage + selectedRow) * columns + selectedColumn + 1
        }
    }
}