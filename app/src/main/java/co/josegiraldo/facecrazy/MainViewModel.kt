package co.josegiraldo.facecrazy

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val snapchatifyBitmapProcessor  = SnapchatifyBitmapProcessor(application)

    var processedBitmap : MutableLiveData<Bitmap> = MutableLiveData()

    val textResult = MutableLiveData<String>()

    fun faceDetect(bitmap: Bitmap?) {
        bitmap?.let {
            doFaceAnnotation(bitmap)
        }
    }

    private fun doFaceAnnotation(bitmap: Bitmap) {

        //<editor-fold desc="faceDetection">
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
            .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .build()

        val image = FirebaseVisionImage.fromBitmap(bitmap)

        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)

        detector
            .detectInImage(image)
            .addOnSuccessListener {
               processedBitmap.postValue( snapchatifyBitmapProcessor.annotateFace(bitmap, it))
            }
            .addOnFailureListener {
                Toast.makeText(getApplication(), "Error detecting faces $it", Toast.LENGTH_SHORT).show()
            }
        //</editor-fold>
    }

}
