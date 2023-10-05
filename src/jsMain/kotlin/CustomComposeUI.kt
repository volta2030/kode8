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
                }
            }) {
                Text("┏")
            }

            repeat(cols) { i ->
                Th({
                    style {
                        textAlign("center")
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
            selectedCell: Pair<Int, Int>,
            setSelectedCell: (Pair<Int, Int>) -> Unit,
        ) {

            repeat(numberOfRows) { i ->
                Tr {

                    Td({
                        style {
                            textAlign("center")
                            fontWeight("bold")
                        }
                    }) {
                        Text((i + 1).toString())
                    }

                    repeat(cols) { j ->

                        val isSelected = (selectedCell == Pair(i, j))

                        Td({
                            style {
                                fontWeight(if (isSelected) "bold" else "normal")
                                backgroundColor(if (isSelected) Color.rebeccapurple else Color.transparent)
                                color(if (isSelected) Color.white else Color.black)
                                textAlign("center")

                            }

                            onClick {
                                if (cellData[i][j] != "") {
                                    setSelectedCell(Pair(i, j))
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