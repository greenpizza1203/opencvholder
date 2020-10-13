package MOEPenCV

import MOEPenCV.MOEPenCVConstants.GREEN
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.COLOR_BGR2HSV
import switch


object instance {
    object config {
        val drawOverlay = true;
        val processExtra = true;

        object autonConfig {
            val cropRectangle = Rect(0, 0, 800, 448)
        }
    }
}

class MOEPipeline {
    var frameRequested = false
    var rows = 480
    var cols = 640
    val frame2 = Mat(rows, cols, 16)
    var shifter = 1
    var imageCodecs = Imgcodecs()

    //    lateinit var location: SkyStoneLocation
    fun processFrame(input: Mat): Mat {

        if (frameRequested) {

            saveFile(input)
            frameRequested = false
        }
//        val subMatrix = input.submat(instance.config.autonConfig.cropRectangle)
//        val newMat = Mat(Size(798.0, 310.0), input.type())
//
//        Imgproc.resize(subMatrix, newMat.submat(Rect(0, 0, 798, 266)), Size(798.0, 266.0))
//
//        if (instance.config.drawOverlay) {
//            drawLines(newMat)
//        }
//        if (instance.config.processExtra) {
//            drawText(newMat)
//        }
//        input.release()

        return input;
    }

    private fun saveFile(input: Mat) {
        val file2 = "sample$switch.png"

        Imgcodecs.imwrite(file2, input)
    }

    fun preprocess(framer: Mat): Mat {
        val sub = framer.submat(130, 410, 70, 420)

        if (switch) {
            return sub
        }
        val resize = sub.resize(1.0, 4.0)
        val frame = resize.resize(framer.cols().toDouble(), framer.rows().toDouble(), 0.5, 0.5)

        frame.copyTo(frame2)

        Imgproc.cvtColor(frame, frame, COLOR_BGR2HSV)

        for (i in 0 until frame.rows()) {
            for (j in 0 until frame.cols()) {
                val pixel = frame.get(i, j)
                val h = pixel[0]
                val s = pixel[1]
                var g = 0.0
                var r = 0.0
                if (s >= 127.0) {
                    g = 255.0

                } else {
                    r = 255.0
                }
//                val get = frame2.get(i, j)

                frame2.put(i, j, 0.0, g, r)
                frame2.put(i, j, 0.0, g, r)
//                println(v)
            }
        }
        return frame2


    }
}

//private fun drawText(newMat: Mat) {
//    val croppedMat = Mat()
////    Imgproc.resize(newMat, croppedMat, Size(4.0, 1.0))
////    Imgproc.cvtColor(croppedMat, croppedMat, COLOR_BGR2HSV)
////    va
//
//    data.forEachIndexed { index, it ->
//        newMat.drawText(60.0 + 300 * index, 295.0, it.roundToInt().toString(), color = PINK)
//    }
//
//}

private fun processData(croppedMat: Mat): MutableList<Double> {
    val data = MutableList(4) {
        croppedMat.get(0, it)[2]
    }

    data[1] = (data[1] + data[2]) / 2.0
    data.removeAt(2)
    return data
}

private fun drawLines(newMat: Mat) {
    val width = newMat.width().toDouble()
    val height = newMat.height().toDouble()
    Imgproc.line(newMat, Point(width * (1 / 4.0), 0.0), Point(width * (1 / 4.0), height), GREEN, 4)
    Imgproc.line(newMat, Point(width * (3 / 4.0), 0.0), Point(width * (3 / 4.0), height), GREEN, 4)
}


