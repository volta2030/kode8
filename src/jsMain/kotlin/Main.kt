import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
    var selectedFile: File? = null
    var string by mutableStateOf("")
    var byteData by mutableStateOf<ByteArray?>(null)


    renderComposable(rootElementId = "root") {
        Div({style { padding(25.px) }}){
            Text("kode8 : The Awesome Byte Code Viewer -- SnackLab")
        }
        Div({ style { padding(25.px) } }) {


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
                                byteData = byteArray
                                string = byteArrayToHexString(byteArray)

                            }
                        }
                        selectedFile?.let { fileReader.readAsArrayBuffer(it) }
                    }
                }
            )

            Text(string)
        }
    }
}

fun byteArrayToHexString(byteArray: ByteArray): String {
    val hexChars = "0123456789ABCDEF"
    val result = StringBuilder(byteArray.size * 2)

    for (byte in byteArray) {
        val intValue = byte.toInt() and 0xFF
        val firstDigit = hexChars[intValue ushr 4]
        val secondDigit = hexChars[intValue and 0x0F]
        result.append(firstDigit)
        result.append(secondDigit)
    }

    return result.toString()
}
