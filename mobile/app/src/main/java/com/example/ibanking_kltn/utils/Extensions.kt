package com.example.ibanking_kltn.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.serialization.json.Json
import java.text.Normalizer
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun removeVietnameseAccents(text: String): String {
    var normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
    normalized = normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    normalized = normalized.replace("đ", "d").replace("Đ", "D")
    return normalized
}

fun formatterDateString(date: LocalDate): String {
    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

fun formatterVND(
    amount: Long
): String {
    val format = NumberFormat.getInstance()
    return format.format(amount)
}

fun generateQrBitmap(
    payload: String,
    size: Int = 500
): Bitmap {
    val bits = QRCodeWriter().encode(payload, BarcodeFormat.QR_CODE, size, size)
    val bmp = createBitmap(size, size, Bitmap.Config.RGB_565)

    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp[x, y] =
                if (bits.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE
        }
    }
    return bmp
}

fun jsonInstance(): Json {
    return Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
}


fun saveBitmapToInternalStorage(
    context: Context,
    bitmap: Bitmap,
    fileName: String
): Uri? {
    val resolver = context.contentResolver

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(
            MediaStore.Images.Media.RELATIVE_PATH,
            Environment.DIRECTORY_PICTURES + "/MyApp"
        )
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val imageUri = resolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ) ?: return null

    resolver.openOutputStream(imageUri).use { output ->
        output?.let { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    contentValues.clear()
    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
    resolver.update(imageUri, contentValues, null, null)

    return imageUri
}

fun shareText(
    context: Context,
    text: String
){
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT,text)
    }

    val shareIntent = Intent.createChooser(sendIntent, "Chia sẻ qua")
    context.startActivity(shareIntent)
}
