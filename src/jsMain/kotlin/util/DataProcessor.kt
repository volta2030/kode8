package util

import androidx.compose.runtime.*
import kotlinx.browser.document
import org.jetbrains.compose.web.attributes.InputType
import type.Base
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import org.w3c.files.File
import org.w3c.files.FileReader
import type.Extension
import util.Converter.Companion.toASCII
import util.Converter.Companion.toBinary
import util.Converter.Companion.toCSVFormat
import util.Converter.Companion.toDecimal
import util.Converter.Companion.toHex
import util.Converter.Companion.toOctal

class DataProcessor {
    companion object {

        var selectedFile: File? = null

        var fileName by mutableStateOf("")

        var rows by mutableStateOf(0)
        var size by mutableStateOf(0)

        var pageIndex by mutableStateOf(0)
        var goToPageIndex by mutableStateOf(1)
        var rowsPerPage by mutableStateOf(100)

        //radio buttons
        var columns by mutableStateOf(64)
        var base by mutableStateOf(Base.HEXA_DECIMAL)
        var extension by mutableStateOf(mutableListOf(Extension.TXT))

        private var cellData by mutableStateOf(
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

            if (rows < rowsPerPage) {
                trimmedCellData = cellData
                return
            }

            if (cellData.size < (pageIndex + 1) * rowsPerPage) {
                trimmedCellData = cellData.sliceArray(pageIndex * rowsPerPage until cellData.size)
                return
            }

            trimmedCellData = cellData.sliceArray(pageIndex * rowsPerPage until (pageIndex + 1) * rowsPerPage)
        }

        fun load() {
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
                    (document.getElementById("downloadButton") as HTMLButtonElement).disabled = false
                }
            }
            fileName = selectedFile!!.name
            selectedFile?.let { fileReader.readAsArrayBuffer(it) }
        }

        fun download() {

            if(selectedFile==null){
                return
            }

            if (extension.contains(Extension.TXT)) {
                val blobPropertyBag = BlobPropertyBag(type = "text/plain")
                val blob = Blob(arrayOf(refineToString(byteArray, base)), blobPropertyBag)

                val url = URL.createObjectURL(blob)

                val a = document.createElement("a") as HTMLAnchorElement
                a.href = url
                a.download = "${fileName.split(".")[0]}.txt"

                a.click()
            }
            if (extension.contains(Extension.CSV)) {
                val blobPropertyBag = BlobPropertyBag(type = "text/csv")
                val blob = Blob(arrayOf(refineToString(byteArray, base).toCSVFormat(columns, base)), blobPropertyBag)

                val url = URL.createObjectURL(blob)

                val a = document.createElement("a") as HTMLAnchorElement
                a.href = url
                a.download = "${fileName.split(".")[0]}.csv"

                a.click()
            }
        }

        fun getOrder(row: Int, column: Int): Int {
            return (pageIndex * rowsPerPage + row) * columns + column + 1
        }

        fun getRow(row: Int): Int {
            return pageIndex * rowsPerPage + (row + 1)
        }

        fun getColumn(column: Int): Int {
            return column + 1
        }

        fun getMaxRowEachPage() : Int {
            return if (rows < rowsPerPage || pageIndex == (rows / rowsPerPage)) rows % 100 else rowsPerPage
        }

        fun isCellFilled(row: Int, column: Int): Boolean {
            return trimmedCellData[row][column] != ""
        }
    }
}