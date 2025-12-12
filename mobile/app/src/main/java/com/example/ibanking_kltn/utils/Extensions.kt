package com.example.ibanking_kltn.utils

import android.graphics.Bitmap
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
        classDiscriminator = "type"
        ignoreUnknownKeys = true
    }
}