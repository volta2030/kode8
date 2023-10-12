import CustomComposeUI.Companion.FooterText
import CustomComposeUI.Companion.TableHeader
import CustomComposeUI.Companion.TableRows
import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.InputType
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
import util.Data.Companion.refineToString
import util.Data.Companion.updateCellData
import util.Data.Companion.updateTrimmedCellData

const val version = "1.8.0"

fun main() {

    val versionText = "Current v.${version}"
    val copyRightText = "Copyright © 2023 SnackLab(volta2030). All Rights Reserved."
    val sourceCodeLink = "https://github.com/volta2030/kode8"
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
                                        checked(mode == columns)
                                        onClick {
                                            columns = mode
                                            rows = (byteArray.size - 1) / columns + 1

                                            if (selectedRow > rows || selectedColumn > columns) {
                                                selectedRow = 0
                                                selectedColumn = 0
                                            }

                                            cellData = updateCellData(byteArray, rows, columns, base)
                                            trimmedCellData = updateTrimmedCellData(cellData, rows, rowsPerPage, pageIndex)
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
                                            rows = (byteArray.size - 1) / columns + 1
                                            cellData = updateCellData(byteArray, rows, columns, base)
                                            trimmedCellData = updateTrimmedCellData(cellData, rows, rowsPerPage, pageIndex)
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
                                    selectedRow = -1
                                    selectedColumn = -1
                                    byteArray = Int8Array(arrayBuffer).unsafeCast<ByteArray>()
                                    size = byteArray.size
                                    rows = (byteArray.size - 1) / columns + 1
                                    cellData = updateCellData(byteArray, rows, columns, base)
                                    trimmedCellData = updateTrimmedCellData(cellData, rows, rowsPerPage, pageIndex)
                                    pageIndex = 0
                                    goToPageIndex = 1
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
                    paddingBottom(80.px)
                    paddingRight(5.px)

                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    justifyContent(JustifyContent.SpaceBetween)

                }
            }) {

                Img("images/copy.png", "img",
                    attrs = {
                        style {
                            maxWidth(20.px)
                            maxHeight(20.px)
                            padding(3.px)
                        }
                        onMouseOver {

                            if (selectedFile != null) {
                                document.body!!.style.cursor = "pointer"
                            }
                        }
                        onMouseOut {
                            document.body!!.style.cursor = "default"
                        }
                        onClick {
                            if (selectedFile != null) {
                                window.navigator.clipboard.writeText(refineToString(byteArray, base))
                                document.body!!.style.cursor = "default"
                                window.alert("Copied!")
                            }
                        }
                    }
                )

                Table({
                    id("table")
                    style {
                        fontSize(15.px)
                        border(1.px, LineStyle.Solid, Color.black)
                    }
                }) {
                    TableHeader(columns, selectedColumn)
                    TableRows(
                        columns, if(rows < rowsPerPage) rows else rowsPerPage, pageIndex, trimmedCellData,
                        selectedRow, selectedColumn,
                        { newSelectedRow, newSelectedColumn ->
                            selectedRow = newSelectedRow
                            selectedColumn = newSelectedColumn
                        },
                    )
                }

                FooterText(versionText, copyRightText, sourceCodeLink)

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
                    flexDirection(FlexDirection.Column)
                    justifyContent(JustifyContent.SpaceBetween)
                }
            }) {

                Div({
                    style {
                        display(DisplayStyle.Flex)
                        flexDirection(FlexDirection.Row)
                        justifyContent(JustifyContent.Center)
                        paddingBottom(5.px)
                    }
                }) {
                    Div({
                        style {
                            marginRight(5.px)
                        }

                        onMouseOver {
                          document.body!!.style.cursor = "pointer"
                        }
                        onMouseOut {
                            document.body!!.style.cursor = "default"
                        }
                        onClick {
                            if(pageIndex > 0){
                                pageIndex--
                                trimmedCellData = updateTrimmedCellData(cellData, rows, rowsPerPage, pageIndex)
                            }
                        }

                    }){ Text("prev")
                    }

                    repeat((rows / rowsPerPage) + 1) { i ->

                        Div({
                            style {
                                paddingLeft(1.px)
                                paddingRight(1.px)
                            }
                        }) {
                            Button({
                                style {
                                    backgroundColor(if(pageIndex == i) Color.rebeccapurple else Color.lightgray)
                                }

                                onClick {
                                    pageIndex = i
                                    trimmedCellData = updateTrimmedCellData(cellData, rows, rowsPerPage, pageIndex)
                                }
                            }) {
                                Div({
                                    style {
                                        color(if(pageIndex == i) Color.white else Color.black)
                                    }
                                }) {
                                    Text((i + 1).toString())
                                }

                            }
                        }
                    }

                    Div({
                        style {
                            marginLeft(5.px)
                        }

                        onMouseOver {
                            document.body!!.style.cursor = "pointer"
                        }
                        onMouseOut {
                            document.body!!.style.cursor = "default"
                        }
                        onClick {
                            if(pageIndex < (rows / rowsPerPage) + 1){
                                pageIndex++
                                trimmedCellData = updateTrimmedCellData(cellData, rows, rowsPerPage, pageIndex)
                            }
                        }

                    }){ Text("next") }
                }

                Div({
                    style {
                        display(DisplayStyle.Flex)
                        flexDirection(FlexDirection.Row)
                        justifyContent(JustifyContent.SpaceBetween)
                    }
                }){
                    Div {
                        Text(if (selectedRow < 0 && selectedColumn < 0) "" else "${(pageIndex * rowsPerPage + selectedRow) * columns + selectedColumn + 1}th byte = ")

                        Label {
                            Text("row : ")
                            Text((pageIndex * rowsPerPage + (selectedRow + 1)).toString())
                        }

                        Label {
                            Text(" ")
                        }

                        Label {
                            Text("column : ")
                            Text((selectedColumn + 1).toString())
                        }
                    }

                    Div{
                        NumberInput{
                            defaultValue(goToPageIndex)
                            min("1")
                            max(((rows / rowsPerPage) + 1).toString())
                            onChange { e->
                                goToPageIndex = e.value as Int
                            }
                        }

                        Button({
                            onClick {
                                pageIndex = goToPageIndex - 1
                                trimmedCellData = updateTrimmedCellData(cellData, rows, rowsPerPage, pageIndex)
                            }
                        }) {
                            Text("go")
                        }
                    }

                    Div {
                        Text("Total $size Bytes")
                    }
                }


            }
        }
    }
}