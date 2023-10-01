import Utility.Companion.toHex
import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.FileReader
import org.w3c.files.get

fun main() {
    val cols = 64
    val chunkSize = 2

    var selectedFile: File? = null
    var string by mutableStateOf("")
    var rows by mutableStateOf(0)
    var cellData by mutableStateOf(
        Array(1) { Array(cols) { "" } }
    )


    renderComposable(rootElementId = "root") {

        Header({
            style {
                position(Position.Absolute)
                top(0.px)
                left(0.px)
                right(0.px)
                color(Color.white)
                fontWeight(2)
                backgroundColor(Color.rebeccapurple)
                padding(10.px)
                textAlign("center")
            }}){
                Text("kode8 - The Byte Code Viewer")
            }

        Div({ style {
            position(Position.Absolute)
            top(30.px)
            padding(20.px)
        } }) {
            val fileInput = Input(
                type = InputType.File,
                attrs = {
                    onChange { e ->
                        val target = e.target as? HTMLInputElement
                        selectedFile = target?.files?.get(0)

                        val fileReader = FileReader()
                        fileReader.onload = { event ->
                            val arrayBuffer = event.target.asDynamic().result as? ArrayBuffer
                            if (arrayBuffer != null) {
                                val byteArray = Int8Array(arrayBuffer).unsafeCast<ByteArray>()
                                string = byteArray.toHex()
                                rows = (byteArray.size -1) / cols + 1

                                cellData = Array(rows) {Array(cols) {""}}
                                for (i in 0 until rows) {
                                    for (j in 0 until cols) {
                                        val startIndex = i * cols * chunkSize + j * chunkSize
                                        val endIndex = startIndex + chunkSize
                                        cellData[i][j] = string.substring(startIndex, endIndex)
                                    }
                                }
                            }
                        }
                        selectedFile?.let { fileReader.readAsArrayBuffer(it) }
                    }
                }
            )

            Div({
                style {
                    padding(5.px)
                    fontSize(15.px)
                }
            }
            ){
                Table{
                    TableHeader(cols)
                    TableRows(cols, rows, cellData)
                }
            }
        }

        Footer({
            style {
                position(Position.Fixed)
                bottom(0.px)
                left(0.px)
                right(0.px)
                backgroundColor(Color.lightgray) // Add background color as needed
                padding(10.px)
                textAlign("center")
            }
        }) {

            Text("Copyright © 2023 SnackLab(volta2030). All Rights Reserved.")
        }

    }
}

@Composable
fun TableHeader(cols : Int){
    repeat(cols){ i->
        Th{
            Text(i.toHex())
        }
    }
}

@Composable
fun TableRows(cols : Int, numberOfRows : Int, cellData : Array<Array<String>>){
    repeat(numberOfRows) { i ->
        Tr{
            repeat(cols){j ->
                Td {
                    Text(cellData[i][j])
                }
            }
        }
    }
}