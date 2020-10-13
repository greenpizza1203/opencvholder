import MOEPenCV.MOEPipeline
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.highgui.HighGui
import org.opencv.videoio.VideoCapture
import org.opencv.videoio.Videoio
import java.awt.Container
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel

lateinit var jframe: JFrame
lateinit var vidpanel: JLabel
lateinit var pipeline: MOEPipeline

fun main() {

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    pipeline = MOEPipeline()
    fun firstTime(frame: Mat) {
        val proccessFrame = pipeline.processFrame(pipeline.preprocess(frame))
        jframe.size = proccessFrame.jDimension()
        jframe.title = "Opencv"

    }

    val frame = Mat()
    //0; default video device id
    val camera = setupWebcam()

    setupJframe()
    jframe.title = "Loading..."

    var firsttime = true
    while (true) {
        if (camera.read(frame)) {
            if (firsttime) {
                firstTime(frame)
                firsttime = false
            }
            val processFrame = pipeline.processFrame(pipeline.preprocess(frame))
            val image = ImageIcon(HighGui.toBufferedImage(processFrame))

            vidpanel.icon = image
            vidpanel.repaint()
        }
    }
}


private fun Mat.jDimension(): Dimension {
    return Dimension(width(), height())
}

private fun setupWebcam(): VideoCapture {
    val absurdlyHighRes = 100000.0
    val camera = VideoCapture(1)
    camera[Videoio.CAP_PROP_FRAME_WIDTH] = 640.0
    camera[Videoio.CAP_PROP_FRAME_HEIGHT] = 480.0


    // Restore resolution
    println(camera[Videoio.CAP_PROP_FRAME_WIDTH])
    println(camera[Videoio.CAP_PROP_FRAME_HEIGHT])
    return camera
}

@Volatile
var switch = false
private fun setupJframe(): JFrame {
    jframe = JFrame()
    jframe.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    vidpanel = JLabel()
    var originalPanel = JLabel()
    jframe.contentPane = vidpanel
//    vidpanel.add(originalPanel)
    jframe.addMouseListener(object : BaseMouseListener() {
        override fun mouseClicked(e: MouseEvent?) {
            when (e?.button) {
                //left button pressed
                MouseEvent.BUTTON1 -> {
                    switch = !switch

                }
                //right button pressed

                MouseEvent.BUTTON3 -> pipeline.frameRequested = true
            }
//            if (e?.button == MouseEvent.BUTTON2) {

//            }
        }


    })
    jframe.isVisible = true
//    val jButton = JButton("yes")
//    jframe.add(jButton)
    return jframe
}
