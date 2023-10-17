import CustomBorder.Companion.borderBottom
import CustomBorder.Companion.borderLeft
import CustomBorder.Companion.borderRight
import androidx.compose.runtime.Composable
import kotlinx.browser.document
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*


class CustomComposeUI {
    companion object {
        @Composable
        fun TableHeader(cols: Int, selectedColumn: Int) {

            Th({
                style {
                    textAlign("center")
                    padding(0.px)
                }
            }) {
                Div({
                    style {
                        width(0.px)
                        height(0.px)
                        padding(0.px)
                        margin(0.px)
                        borderBottom(20.px, LineStyle.Solid, Color.rebeccapurple)
                        borderLeft(0.px, LineStyle.Solid, Color.transparent)
                        borderRight(25.px, LineStyle.Solid, Color.transparent)
                    }
                }) {
                }
            }

            repeat(cols) { i ->
                Th({
                    style {
                        textAlign("center")
                        borderBottom(1.px, LineStyle.Solid, Color.black)
                        backgroundColor(if(i == selectedColumn) Color.rebeccapurple else Color.transparent)
                        color(if(i == selectedColumn) Color.lightyellow else Color.black)
                    }
                }) {
                    Text((i + 1).toString())
                }
            }
        }

        @Composable
        fun TableRows(
            columns: Int,
            rowsPerPage: Int,
            pageIndex : Int,
            trimmedCellData: Array<Array<String>>,
            selectedRow : Int, selectedColumn : Int,
            setSelectedCell: (Int, Int) -> Unit,
        ) {

            var repeatTime = rowsPerPage

            if(trimmedCellData.size < rowsPerPage){
                repeatTime = trimmedCellData.size
            }

            repeat(repeatTime) { i ->
                Tr({
                    style {
                        textAlign("center")
                    }
                }) {

                    Td({
                        style {
                            width(25.px)
                            textAlign("center")
                            fontWeight("bold")
                            backgroundColor(if(i == selectedRow) Color.rebeccapurple else Color.transparent)
                            color(if(i == selectedRow) Color.lightyellow else Color.black)
                            borderRight(1.px, LineStyle.Solid, Color.black)
                        }
                    }) {
                        Text(((pageIndex * rowsPerPage) + (i + 1)).toString())
                    }

                    repeat(columns) { j ->

                        val isSelected = (selectedRow == i && selectedColumn == j)

                        Td({
                            style {
                                fontWeight(if (isSelected) "bold" else "normal")
                                backgroundColor(if (isSelected) Color.rebeccapurple else Color.transparent)
                                color(if (isSelected) Color.white else Color.black)
                                textAlign("center")

                            }

                            onClick {
                                if (trimmedCellData[i][j] != "") {
                                    setSelectedCell(i, j)
                                    document.body!!.style.cursor = "default"
                                }
                            }

                            onMouseOver {
                                document.body!!.style.cursor = "pointer"
                            }

                            onMouseOut {
                                document.body!!.style.cursor = "default"
                            }

                        }) {
                            Text(trimmedCellData[i][j])
                        }
                    }
                }
            }
        }

        @Composable
        fun FooterText(versionText : String , copyRightText : String, sourceCodeLink : String){
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

    }
}