package MOEPenCV

import MOEPenCV.MOEPenCVConstants
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.INTER_AREA
import org.opencv.imgproc.Imgproc.INTER_NEAREST


fun Mat.drawText(x: Double, y: Double, text: String, fontSize: Double = 1.0, color: Scalar = MOEPenCVConstants.BLACK) {
    Imgproc.putText(this, text, Point(x, y), 0, fontSize, color)

}

fun Mat.resize(width: Double, height: Double): Mat = resize(Size(width, height))

fun Mat.resize(size: Size): Mat {
    val dest = Mat()
    Imgproc.resize(this, dest, size)
    return dest
}

fun Mat.resize(width: Double, height: Double, fx: Double, fy: Double): Mat {
    val dest = Mat()
    Imgproc.resize(this, dest, Size(width, height), fx, fy, INTER_NEAREST)
    return dest
}
//fun Mat.crop(width: Double, height: Double, fx: Double, fy: Double): Mat {
//    val dest = Mat()
//    Imgproc.sub(this, dest, Size(width, height), fx, fy, INTER_NEAREST)
//    return dest
//}
