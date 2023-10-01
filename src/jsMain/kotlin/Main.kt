import Utility.Companion.toHex
import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.CSSAutoKeyword
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
    var size by mutableStateOf(0)
    var cellData by mutableStateOf(
        Array(1) { Array(cols) { "" } }
    )


    renderComposable(rootElementId = "root") {
        Div({
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                justifyContent(JustifyContent.SpaceBetween)
            }
        }) {
            Header({
                style {
                    position(Position.Fixed)
                    top(0.px)
                    left(0.px)
                    right(0.px)
                    color(Color.white)
                    fontWeight(3)
                    backgroundColor(Color.rebeccapurple)
                    display(DisplayStyle.Flex)
                    justifyContent(JustifyContent.SpaceBetween)
                    textAlign("center")
                    padding(5.px)
                }}){

                Text("kode8 - The Byte Code Viewer")

                Input(
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
                                    size = byteArray.size
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

            }

            Main({ style {
                paddingLeft(5.px)
                paddingTop(40.px)
                paddingBottom(40.px)
                paddingRight(5.px)

                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                justifyContent(JustifyContent.SpaceBetween)

            } }) {
                Table({
                    style {
                        fontSize(15.px)
                        border (1.px, LineStyle.Solid, Color.black)
                    }
                }){
                    TableHeader(cols)
                    TableRows(cols, rows, cellData)
                }

                Text("Copyright © 2023 SnackLab(volta2030). All Rights Reserved.")
            }

            Footer({
                style {
                    position(Position.Fixed)
                    bottom(0.px)
                    left(0.px)
                    right(0.px)
                    backgroundColor(Color.rebeccapurple) // Add background color as needed
                    textAlign("right")
                    padding(5.px)
                    color(Color.white)
                    fontWeight(3)
                }
            }) {
                Text("$size Bytes")
            }
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