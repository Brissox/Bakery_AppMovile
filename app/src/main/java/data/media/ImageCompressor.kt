package Data.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

object ImageCompressor {

    fun compressToTempFile(
        context: Context,
        uri: Uri,
        maxWidth: Int = 1024,
        maxHeight: Int = 1024,
        maxBytes: Int = 700_000,
        initialQuality: Int = 85
    ): File {
        val srcBitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val src = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(src) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                decoder.isMutableRequired = false
            } ?: error("No se pudo decodificar imagen (Bitmap nulo)")
        } else {
            context.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input)
            } ?: error("No se pudo decodificar imagen (InputStream nulo)")
        }

        val (w, h) = srcBitmap.width to srcBitmap.height
        val scale = min(maxWidth.toFloat() / w, maxHeight.toFloat() / h).coerceAtMost(1f)
        val targetW = (w * scale).roundToInt().coerceAtLeast(1)
        val targetH = (h * scale).roundToInt().coerceAtLeast(1)
        val scaled = if (scale < 1f) {
            Bitmap.createScaledBitmap(srcBitmap, targetW, targetH, true)
        } else srcBitmap

        val baos = ByteArrayOutputStream()
        var q = initialQuality
        scaled.compress(Bitmap.CompressFormat.JPEG, q, baos)
        while (baos.size() > maxBytes && q > 50) {
            baos.reset()
            q -= 5
            scaled.compress(Bitmap.CompressFormat.JPEG, q, baos)
        }

        val outFile = File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
        FileOutputStream(outFile).use { it.write(baos.toByteArray()) }

        if (scaled !== srcBitmap) scaled.recycle()

        return outFile
    }
}
