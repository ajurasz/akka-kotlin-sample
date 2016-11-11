import java.io.Serializable
import Models.Point

class Protocols {
    data class FilePathMessage(val path: String) : Serializable
    data class DataMessage(val id: Int, val point: Point, val marketIds: List<Int> = emptyList())
    class Start() : Serializable
    class Stop() : Serializable
}