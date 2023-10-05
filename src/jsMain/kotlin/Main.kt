import CustomComposeUI.Companion.TableHeader
import CustomComposeUI.Companion.TableRows
import Utility.Companion.toASCII
import Utility.Companion.toBinary
import Utility.Companion.toDecimal
import Utility.Companion.toHex
import Utility.Companion.toOctal
import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.builders.InputAttrsScope
import org.jetbrains.compose.web.attributes.max
import org.jetbrains.compose.web.attributes.min
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.FileReader
import org.w3c.files.get
import kotlin.math.absoluteValue

val version = "1.4.0"

fun main() {

    val versionText = "Current v.${version}"
    val copyRightText = "Copyright © 2023 SnackLab(volta2030). All Rights Reserved."
    val sourceCodeLink = "https://github.com/volta2030/kode8"
    var selectedFile: File? = null
    var cols by mutableStateOf(64)
    var rows by mutableStateOf(0)
    var size by mutableStateOf(0)

    //radio buttons
    var base by mutableStateOf(Base.HEXA_DECIMAL)

    var cellData by mutableStateOf(
        Array(1) { Array(cols) { "" } }
    )

    var selectedCell by mutableStateOf<Pair<Int, Int>>(Pair(-1,-1))
    var byteArray by mutableStateOf(byteArrayOf())

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
                }
            }) {
                Div({
                    style {
                        fontWeight("bold")
                    }
                }) {
                    Text("kode8 - Byte Code Viewer")
                }


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
                                    byteArray = Int8Array(arrayBuffer).unsafeCast<ByteArray>()
                                    size = byteArray.size
                                    rows = (byteArray.size - 1) / cols + 1
                                    cellData = updateCellData(byteArray, rows, cols, base)
                                }
                            }
                            selectedFile?.let { fileReader.readAsArrayBuffer(it) }
                        }
                    }
                )

            }

            Main({
                style {
                    paddingLeft(5.px)
                    paddingTop(40.px)
                    paddingBottom(40.px)
                    paddingRight(5.px)

                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    justifyContent(JustifyContent.SpaceBetween)

                }
            }) {

                Table({
                    style {
                        fontSize(15.px)
                        border(1.px, LineStyle.Solid, Color.black)
                    }
                }) {
                    TableHeader(cols)
                    TableRows(cols, rows, cellData,
                        selectedCell,
                        {newSelectedCell ->
                            selectedCell = newSelectedCell
                        },
                    )
                }
                Div({
                    style {
                        display(DisplayStyle.Flex)
                        flexDirection(FlexDirection.Row)
                        justifyContent(JustifyContent.Center)
                        color(Color.gray)
                        textAlign("center")
                    }
                }) {
                    Text("$versionText | $copyRightText |")
                    Div({
                        style {
                            width(5.px)
                        }

                    }) {
                    }
                    A(href = sourceCodeLink) {
                        Text("Source Code")
                    }
                }
            }

            Footer({
                style {
                    position(Position.Fixed)
                    bottom(0.px)
                    left(0.px)
                    right(0.px)
                    backgroundColor(Color.rebeccapurple) // Add background color as needed
                    padding(5.px)
                    color(Color.white)
                    fontWeight(3)
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Row)
                    justifyContent(JustifyContent.SpaceBetween)
                }
            }) {

                Div {
                    Text(if (selectedCell.first < 0 && selectedCell.second < 0) "" else "${(selectedCell.first) * cols + selectedCell.second + 1}th byte = ")

                    Label {
                        Text("row : ")
                        NumberInput{
                            style {
                                width(35.px)
                            }

                            defaultValue(if(selectedFile!=null) selectedCell.first + 1 else -1)

                            onChange { e->
                                selectedCell = Pair(e.value as Int - 1, selectedCell.second)
                            }

                            max(rows.toString())
                        }
                    }

                    Label {
                        Text(" ")
                    }

                    Label {
                        Text("column : ")
                        NumberInput {

                            style {
                                width(35.px)
                            }

                            defaultValue(if(selectedFile!=null) selectedCell.second + 1 else -1)

                            onChange { e->
                                selectedCell = Pair(selectedCell.first, e.value as Int - 1)
                            }


                            max(cols.toString())
                        }
                    }
                }

                Div {
                    Fieldset({
                        style {
                            display(DisplayStyle.Flex)
                            flexDirection(FlexDirection.Row)
                            padding(1.px)
                            paddingLeft(2.px)
                            paddingRight(2.px)
                        }
                    }) {
                        Div({
                            style {
                                padding(0.px)
                                fontWeight("bold")
                            }
                        }) {
                            Text("Column")
                        }
                        listOf(
                            16, 32, 64
                        ).forEach { mode ->
                            Label {
                                Input(
                                    type = InputType.Radio,
                                    attrs = {
                                        checked(mode == cols)
                                        onClick {
                                            cols = mode
                                            rows = (byteArray.size - 1) / cols + 1
                                            cellData = updateCellData(byteArray, rows, cols, base)
                                        }
                                    }
                                )
                                Span {
                                    Text(mode.toString())
                                }
                            }
                        }
                    }

                }


                Div {
                    Fieldset({
                        style {
                            display(DisplayStyle.Flex)
                            flexDirection(FlexDirection.Row)
                            padding(1.px)
                            paddingLeft(2.px)
                            paddingRight(2.px)
                        }
                    }) {
                        Div({
                            style {
                                padding(0.px)
                                fontWeight("bold")
                            }
                        }) {
                            Text("Base")
                        }
                        listOf(
                            Base.BINARY,
                            Base.OCTAL,
                            Base.DECIMAL,
                            Base.HEXA_DECIMAL,
                            Base.ASCII
                        ).forEach { mode ->

                            Label {
                                Input(
                                    type = InputType.Radio,
                                    attrs = {
                                        checked(base == mode)
                                        onClick {
                                            base = mode
                                            rows = (byteArray.size - 1) / cols + 1
                                            cellData = updateCellData(byteArray, rows, cols, base)
                                        }
                                    }
                                )
                                Span {
                                    Text(mode.label)
                                }
                            }
                        }
                    }
                }

                Div {
                    Text("Total $size Bytes")
                }

            }
        }
    }
}

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