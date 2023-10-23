import CustomStyle.Companion.borderCollapse
import CustomComposeUI.Companion.FooterText
import CustomComposeUI.Companion.TableHeader
import CustomComposeUI.Companion.TableRows
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.max
import org.jetbrains.compose.web.attributes.min
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLInputElement
import org.w3c.files.get
import type.Base
import util.DataProcessor.Companion.base
import util.DataProcessor.Companion.byteArray
import util.DataProcessor.Companion.columns
import util.DataProcessor.Companion.getColumn
import util.DataProcessor.Companion.getOrder
import util.DataProcessor.Companion.getRow
import util.DataProcessor.Companion.goToPageIndex
import util.DataProcessor.Companion.load
import util.DataProcessor.Companion.pageIndex
import util.DataProcessor.Companion.refineToString
import util.DataProcessor.Companion.rows
import util.DataProcessor.Companion.rowsPerPage
import util.DataProcessor.Companion.selectedColumn
import util.DataProcessor.Companion.selectedFile
import util.DataProcessor.Companion.selectedRow
import util.DataProcessor.Companion.size
import util.DataProcessor.Companion.trimmedCellData
import util.DataProcessor.Companion.updateCellData
import util.DataProcessor.Companion.updateTrimmedCellData

const val version = "1.12.0"

fun main() {

    val versionText = "Current v.${version}"
    val copyRightText = "Copyright © 2023 SnackLab(volta2030). All Rights Reserved."
    val sourceCodeLink = "https://github.com/volta2030/kode8"

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
                    flexDirection(FlexDirection.Row)
                    justifyContent(JustifyContent.SpaceBetween)
                    alignItems(AlignItems.Center)
                    textAlign("center")
                    padding(5.px)
                }
            }) {
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
                                            updateCellData()
                                            updateTrimmedCellData()
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
                                            updateCellData()
                                            updateTrimmedCellData()
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

                Div({
                    style {
                        width(150.px)
                        height(30.px)
                        border(1.px, LineStyle.Dashed, Color.white)
                        borderRadius(3.px)
                    }
                }) {
                    Div({
                        style {
                            color(Color.lightgray)
                        }

                        onDrop {
                            it.preventDefault()
                            selectedFile = it.dataTransfer?.files?.get(0)
                            load()
                        }

                        onDragOver {
                            it.preventDefault()
                        }

                    }) {
                        Text("Drag and drop file")
                    }
                }

                Input(
                    type = InputType.File,
                    attrs = {
                        onChange { e ->
                            val target = e.target as? HTMLInputElement
                            selectedFile = target?.files?.get(0)
                            load()
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
                Div({
                    style {
                        display(DisplayStyle.Flex)
                        flexDirection(FlexDirection.Row)
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

                    Img("images/anchor.png", "img",
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
                                    window.scrollTo(0.0, document.body!!.scrollHeight.toDouble())
                                    document.body!!.style.cursor = "default"
                                }
                            }
                        }
                    )
                }


                Table({
                    id("table")
                    style {
                        fontSize(15.px)
                        border(1.px, LineStyle.Solid, Color.black)
                        borderCollapse()
                    }
                }) {
                    TableHeader(columns, selectedColumn)
                    TableRows(
                        columns, if (rows < rowsPerPage) rows else rowsPerPage, pageIndex, trimmedCellData,
                        selectedRow, selectedColumn,
                        { newSelectedRow, newSelectedColumn ->
                            selectedRow = newSelectedRow
                            selectedColumn = newSelectedColumn
                        },
                    )
                }

                Div({
                    style {
                        display(DisplayStyle.Flex)
                        flexDirection(FlexDirection.Row)
                        justifyContent(JustifyContent.SpaceBetween)
                    }
                }) {
                    Div({
                        style {
                            width(20.px)
                            height(20.px)
                        }
                    })
                    FooterText(versionText, copyRightText, sourceCodeLink)
                    Img("images/bow.png", "img",
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
                                    window.scrollTo(0.0, document.body!!.scrollTop)
                                    document.body!!.style.cursor = "default"
                                }
                            }
                        }
                    )
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
                        if (pageIndex > 0) {
                            pageIndex--
                            updateTrimmedCellData()
                        }
                    }

                }) {
                    Text("prev")
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
                                backgroundColor(if (pageIndex == i) Color.rebeccapurple else Color.lightgray)
                            }

                            onClick {
                                pageIndex = i
                                updateTrimmedCellData()
                            }
                        }) {
                            Div({
                                style {
                                    color(if (pageIndex == i) Color.white else Color.black)
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
                        if (pageIndex < (rows / rowsPerPage) + 1) {
                            pageIndex++
                            updateTrimmedCellData()
                        }
                    }

                }) { Text("next") }
            }

            Div({
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Row)
                    justifyContent(JustifyContent.SpaceBetween)
                }
            }) {
                Div {
                    Text(
                        if (selectedRow < 0 && selectedColumn < 0) "" else "${
                            getOrder(
                                selectedRow,
                                selectedColumn
                            )
                        }th byte = "
                    )

                    Label {
                        Text("row : ")
                        Text("${getRow(selectedRow)}")
                    }

                    Label {
                        Text(" ")
                    }

                    Label {
                        Text("column : ")
                        Text("${getColumn(selectedColumn)}")
                    }
                }

                Div {
                    NumberInput {
                        defaultValue(goToPageIndex)
                        min("1")
                        max(((rows / rowsPerPage) + 1).toString())
                        onChange { e ->
                            goToPageIndex = e.value as Int
                        }
                    }

                    Button({
                        onClick {
                            pageIndex = goToPageIndex - 1
                            updateTrimmedCellData()
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
