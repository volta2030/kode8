import CustomBorder.Companion.borderBottom
import CustomBorder.Companion.borderLeft
import CustomBorder.Companion.borderRight
import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

class CustomComposeUI {
    companion object {
        @Composable
        fun TableHeader(cols: Int) {

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
                        borderRight(20.px, LineStyle.Solid, Color.transparent)
                    }
                }) {
                }
            }

            repeat(cols) { i ->
                Th({
                    style {
                        textAlign("center")
                        borderBottom(1.px, LineStyle.Solid, Color.black)
                    }
                }) {
                    Text((i + 1).toString())
                }
            }
        }

        @Composable
        fun TableRows(
            cols: Int,
            numberOfRows: Int,
            cellData: Array<Array<String>>,
            selectedRow : Int, selectedColumn : Int,
            setSelectedCell: (Int, Int) -> Unit,
        ) {

            repeat(numberOfRows) { i ->
                Tr {

                    Td({
                        style {
                            textAlign("center")
                            fontWeight("bold")
                            borderRight(1.px, LineStyle.Solid, Color.black)
                        }
                    }) {
                        Text((i + 1).toString())
                    }

                    repeat(cols) { j ->

                        val isSelected = (selectedRow == i && selectedColumn == j)

                        Td({
                            style {
                                fontWeight(if (isSelected) "bold" else "normal")
                                backgroundColor(if (isSelected) Color.rebeccapurple else Color.transparent)
                                color(if (isSelected) Color.white else Color.black)
                                textAlign("center")

                            }

                            onClick {
                                if (cellData[i][j] != "") {
                                    setSelectedCell(i, j)
                                }
                            }

                        }) {
                            Text(cellData[i][j])
                        }
                    }
                }
            }
        }
    }
}