@file:Suppress("UNNECESSARY_NOT_NULL_ASSERTION")

import MOEPenCV.resize
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.highgui.HighGui
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.VideoCapture
import java.awt.BorderLayout
import java.awt.Container
import java.awt.Image
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*
import kotlin.system.exitProcess

fun main() {
    // Load the native OpenCV library
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater { ThresholdInRange() }
}

class ThresholdInRange {
    private lateinit var sliderH: MOESlider
    private lateinit var sliderS: MOESlider
    private lateinit var sliderV: MOESlider

    private val cap: VideoCapture
    private val matFrame = Mat()
    private val frame: JFrame
    private lateinit var imgCaptureLabel: JLabel
    private lateinit var imgDetectionLabel: JLabel
    private val captureTask: CaptureTask

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    private inner class CaptureTask : SwingWorker<Void?, Mat?>() {
        override fun doInBackground(): Void? {
            while (!isCancelled) {
                if (!cap.read(matFrame)) break
                publish(matFrame.clone())
            }
            return null
        }

        override fun process(frames: List<Mat?>) {
            val frame = frames.last() ?: return
            val frameHSV = Mat()
            Imgproc.cvtColor(frame, frameHSV, Imgproc.COLOR_BGR2HSV)
            val thresh = Mat()
            Core.inRange(frameHSV,
                    Scalar(sliderH.lowerValue, sliderS!!.lowerValue, sliderV!!.lowerValue),
                    Scalar(sliderH!!.upperValue, sliderS!!.upperValue, sliderV!!.upperValue),
                    thresh)
            update(frame, thresh)
        }
    }

    private fun addComponentsToPane(pane: Container, img: Image) {

        val sliderPanel = JPanel()
        sliderPanel.layout = BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS)

        sliderH = sliderPanel.add("Hue", high = 180)
        sliderS = sliderPanel.add("Saturation", 0, 255)
        sliderV = sliderPanel.add("Value", high = 255)

        pane.add(sliderPanel, BorderLayout.PAGE_START)
        val framePanel = JPanel()
        imgCaptureLabel = JLabel(ImageIcon(img))
        framePanel.add(imgCaptureLabel)
        imgDetectionLabel = JLabel(ImageIcon(img))
        framePanel.add(imgDetectionLabel)
        pane.add(framePanel, BorderLayout.CENTER)
    }

    private fun update(imgCapture: Mat, imgThresh: Mat) {

        imgCaptureLabel!!.icon = ImageIcon(HighGui.toBufferedImage(imgCapture))
        imgDetectionLabel!!.icon = ImageIcon(HighGui.toBufferedImage(imgThresh))
        frame.repaint()
    }

    companion object {
        private const val WINDOW_NAME = "Thresholding Operations using inRange demo"
    }

    init {
        var cameraDevice = 1

        cap = VideoCapture(cameraDevice)
        if (!cap.isOpened) {
            System.err.println("Cannot open camera: $cameraDevice")
            exitProcess(0)
        }
        if (!cap.read(matFrame)) {
            System.err.println("Cannot read camera stream.")
            exitProcess(0)
        }
        // Create and set up the window.
        frame = JFrame(WINDOW_NAME)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        captureTask = CaptureTask()

        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(windowEvent: WindowEvent) {
                captureTask.cancel(true)
            }
        })
        // Set up the content pane.
        val img = HighGui.toBufferedImage(matFrame)
        addComponentsToPane(frame.contentPane, img)
        frame.pack()
        frame.isVisible = true
        captureTask.execute()
    }

    private fun JComponent.add(label: String, low: Int = 0, high: Int): MOESlider {
        val slider = MOESlider(low, high)
        add(slider)
        add(JLabel(label))
        return slider


    }
}