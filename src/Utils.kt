import org.opencv.core.Scalar
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

abstract class BaseMouseListener : MouseListener {
    override fun mouseClicked(e: MouseEvent?) {

    }

    override fun mousePressed(e: MouseEvent?) {
    }

    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }
}

fun Scalar(v0: Int, v1: Int, v2: Int) = Scalar(v0.toDouble(), v1.toDouble(), v2.toDouble())