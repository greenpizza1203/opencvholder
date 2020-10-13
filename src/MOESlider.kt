import slider.RangeSlider
import javax.swing.JSlider
import javax.swing.event.ChangeListener

class MOESlider(min: Int, max: Int) : RangeSlider(min, max) {
    init {
        majorTickSpacing = 50
        minorTickSpacing = 10
        paintTicks = true
        paintLabels = true
        lowerValue = min
        upperValue = max
    }

//    fun addChangeListener(func: (min: Int, max: Int, slider: RangeSlider) -> Unit) {
//
//        super.addChangeListener({
//
//        })
//    }
}