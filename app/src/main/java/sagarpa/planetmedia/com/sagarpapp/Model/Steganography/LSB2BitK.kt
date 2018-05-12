package sagarpa.planetmedia.com.sagarpapp.Model.Steganography

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import java.nio.charset.Charset
import java.util.ArrayList

class LSB2BitK {

    private val TAG = LSB2BitK::class.java.name
    var END_MESSAGE_COSTANT = "#!@"
    var START_MESSAGE_COSTANT = "@!#"
    val SQUARE_BLOCK = 512

    /**
     * Calculate the numbers of pixel needed
     *
     * @param message Message to encode
     * @return The number of pixel
     */
    fun numberOfPixelForMessage(message: String?): Int {
        var message = message
        var result = -1
        if (message != null) {
            message += END_MESSAGE_COSTANT
            message = START_MESSAGE_COSTANT + message
            result = message!!.toByteArray(Charset.forName("UTF-8")).size * 4 / 3
        }

        return result
    }

    fun squareBlockNeeded(pixels: Int): Int {
        var result = 0
        val quadrato = SQUARE_BLOCK * SQUARE_BLOCK
        val divid = pixels / quadrato
        val resto = pixels % quadrato
        result = divid + if (resto > 0) 1 else 0
        return result
    }

    fun splitImage(bitmap: Bitmap): List<Bitmap> {

        //For the number of rows and columns of the grid to be displayed


        //For height and width of the small image chunks
        var chunkHeight: Int
        var chunkWidth: Int

        //To store all the small image chunks in bitmap format in this list
        val chunkedImages = ArrayList<Bitmap>()


        var rows = bitmap.height / SQUARE_BLOCK
        var cols = bitmap.width / SQUARE_BLOCK

        val chunkH_mod = bitmap.height % SQUARE_BLOCK
        val chunkW_mod = bitmap.width % SQUARE_BLOCK


        if (chunkH_mod > 0)
            rows++
        if (chunkW_mod > 0)
            cols++


        //xCoord and yCoord are the pixel positions of the image chunks
        var yCoord = 0
        for (x in 0 until rows) {
            var xCoord = 0
            for (y in 0 until cols) {
                chunkHeight = SQUARE_BLOCK
                chunkWidth = SQUARE_BLOCK

                if (y == cols - 1 && chunkW_mod > 0)
                    chunkWidth = chunkW_mod

                if (x == rows - 1 && chunkH_mod > 0)
                    chunkHeight = chunkH_mod

                chunkedImages.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, chunkWidth, chunkHeight))
                xCoord += SQUARE_BLOCK
            }
            yCoord += SQUARE_BLOCK
        }


        return chunkedImages
    }




    fun mergeImage(images: List<Bitmap>, originalHeight: Int, originalWidth: Int): Bitmap {

        var rows = originalHeight / SQUARE_BLOCK
        var cols = originalWidth / SQUARE_BLOCK

        val chunkH_mod = originalHeight % SQUARE_BLOCK
        val chunkW_mod = originalWidth % SQUARE_BLOCK


        if (chunkH_mod > 0)
            rows++
        if (chunkW_mod > 0)
            cols++

        //create a bitmap of a size which can hold the complete image after merging
        Log.d(TAG, "Size width $originalWidth size height $originalHeight")
        val bitmap = Bitmap.createBitmap(originalWidth, originalHeight, Bitmap.Config.ARGB_4444)

        val canvas = Canvas(bitmap)
        var count = 0
        val chunkWidth = SQUARE_BLOCK
        val chunkHeight = SQUARE_BLOCK

        for (irows in 0 until rows) {
            for (icols in 0 until cols) {

                canvas.drawBitmap(images[count], (chunkWidth * icols).toFloat(), (chunkHeight * irows).toFloat(), null)
                count++

            }
        }

        return bitmap
    }


}