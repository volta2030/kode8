import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
            cols: Int, numberOfRows: Int, cellData: Array<Array<String>>,
            setCoordinate: (List<Int>) -> Unit
        ) {

            val selectedCell = remember { mutableStateOf<Pair<Int, Int>?>(null) }


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

                        val isSelected = selectedCell.value == Pair(i, j)

                        Td({
                            style {
                                backgroundColor(if (isSelected) Color.rebeccapurple else Color.transparent)
                                color(if (isSelected) Color.white else Color.black)
                                textAlign("center")

                            }

                            onClick {
                                if (cellData[i][j] != "") {
                                    selectedCell.value = Pair(i, j)
                                    setCoordinate(listOf(i + 1, j + 1))
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