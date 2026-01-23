package com.example.ibanking_kltn.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.ibanking_kltn.AppSessionEntryPoint
import com.example.ibanking_kltn.data.di.AppSessionManager
import com.example.ibanking_kltn.data.dtos.ServiceType
import com.example.ibanking_kltn.data.dtos.responses.TransactionHistoryResponse
import com.example.ibanking_kltn.ui.theme.Black1
import com.example.ibanking_kltn.ui.theme.Blue5
import com.example.ibanking_kltn.ui.theme.Gray2
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import dagger.hilt.android.EntryPointAccessors
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.Normalizer
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID


fun removeVietnameseAccents(text: String): String {
    var normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
    normalized = normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    normalized = normalized.replace("đ", "d").replace("Đ", "D")
    return normalized
}

fun formatterDateString(date: LocalDate, pattern: String = "dd/MM/yyyy"): String {
    return date.format(DateTimeFormatter.ofPattern(pattern))
}

fun formatterDateTimeString(date: LocalDateTime, pattern: String = "HH:mm-dd/MM/yyyy"): String {
    return date.format(DateTimeFormatter.ofPattern(pattern))
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
) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }

    val shareIntent = Intent.createChooser(sendIntent, "Chia sẻ qua")
    context.startActivity(shareIntent)
}


data class DateSelection(val startDate: LocalDate? = null, val endDate: LocalDate? = null) {
    val daysBetween by lazy(LazyThreadSafetyMode.NONE) {
        if (startDate == null || endDate == null) {
            null
        } else {
            ChronoUnit.DAYS.between(startDate, endDate)
        }
    }
}

private val rangeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
fun dateRangeDisplayText(startDate: LocalDate, endDate: LocalDate): String {
    return "Selected: ${rangeFormatter.format(startDate)} - ${rangeFormatter.format(endDate)}"
}

object ContinuousSelectionHelper {
    fun getSelection(
        clickedDate: LocalDate,
        dateSelection: DateSelection,
    ): DateSelection {
        val (selectionStartDate, selectionEndDate) = dateSelection
        return if (selectionStartDate != null) {
            if (clickedDate < selectionStartDate || selectionEndDate != null) {
                DateSelection(startDate = clickedDate, endDate = null)
            } else if (clickedDate != selectionStartDate) {
                DateSelection(startDate = selectionStartDate, endDate = clickedDate)
            } else {
                DateSelection(startDate = clickedDate, endDate = null)
            }
        } else {
            DateSelection(startDate = clickedDate, endDate = null)
        }
    }

    fun isInDateBetweenSelection(
        inDate: LocalDate,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (inDate.yearMonth == startDate.yearMonth) return true
        val firstDateInThisMonth = inDate.yearMonth.nextMonth.atStartOfMonth()
        return firstDateInThisMonth in startDate..endDate && startDate != firstDateInThisMonth
    }

    fun isOutDateBetweenSelection(
        outDate: LocalDate,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (outDate.yearMonth == endDate.yearMonth) return true
        val lastDateInThisMonth = outDate.yearMonth.previousMonth.atEndOfMonth()
        return lastDateInThisMonth in startDate..endDate && endDate != lastDateInThisMonth
    }
}


fun Modifier.backgroundHighlight(
    day: CalendarDay,
    today: LocalDate,
    selection: DateSelection,
    textColorSetter: (Color) -> Unit,
): Modifier {
    val (selectionStartDate, selectionEndDate) = selection
    val backgroundColor = when {
        day.date < today -> {
            textColorSetter(
                Gray2.copy(alpha = 0.4f)
            )
            Color.Transparent

        }

        day.date == today -> {
            textColorSetter(Color.White)
            Black1
        }

        day.date == selectionStartDate || day.date == selectionEndDate -> {
            textColorSetter(Color.White)
            Blue5
        }

        selectionStartDate != null && selectionEndDate != null &&
                day.date in (selectionStartDate..selectionEndDate) -> {
            textColorSetter(Color.Black)
            Gray2.copy(alpha = 0.4f)
        }

        else -> {
            textColorSetter(Black1)
            Color.Transparent
        }
    }
    return this.background(backgroundColor, shape = RoundedCornerShape(4.dp))
}

fun Context.getFileInfo(uri: Uri): Triple<String?, String?, Long?> {
    var fileName: String? = null
    var size: Long? = null
    var extension: String? = null

    contentResolver.query(
        uri,
        arrayOf(
            OpenableColumns.DISPLAY_NAME,
            OpenableColumns.SIZE
        ),
        null,
        null,
        null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            fileName =
                cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            size =
                cursor.getLong(cursor.getColumnIndexOrThrow(OpenableColumns.SIZE))
        }
    }

    // Lấy extension từ tên
    extension = fileName?.substringAfterLast('.', "")

    return Triple(fileName, extension, size)
}


fun formatReadableSize(context: Context, size: Long): String {
    return Formatter.formatFileSize(context, size)
}

fun Modifier.customClick(
    enable: Boolean = true,
    shape: Shape = RoundedCornerShape(5.dp),
    onClick: () -> Unit,
): Modifier {
    return if (enable) this
        .shadow(
            elevation = 10.dp, shape = shape,
            ambientColor = Color.Transparent,
            spotColor = Color.Transparent
        )
        .clickable {
            onClick()
        }
    else this
        .background(
            color = Gray2.copy(
                alpha = 0.8f
            ),
            shape = shape
        )

}

fun createMultipartFromUri(
    uri: Uri,
    partName: String,
    context: Context
): MultipartBody.Part {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bytes = inputStream?.readBytes() ?: ByteArray(0)
    inputStream?.close()

    val fileName = getFileName(uri, context) ?: "${UUID.randomUUID().toString()}.jpg"
    val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
    val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(
        partName,
        fileName,
        requestBody
    )
}

fun getFileName(uri: Uri, context: Context): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    result = it.getString(index)
                }
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != -1) {
            result = result?.substring(cut!! + 1)
        }
    }
    return result
}

fun colorFromLabel(label: String): Color {
    val hash = label.hashCode()
    val r = (hash shr 16 and 0xFF) / 255f
    val g = (hash shr 8 and 0xFF) / 255f
    val b = (hash and 0xFF) / 255f
    return Color(r, g, b)
}


fun checkMoneyFlow(transaction: TransactionHistoryResponse, myWalletNumber: String): String {
    if (transaction.transactionType == ServiceType.CASH_DEPOSIT ||
        transaction.transactionType == ServiceType.E_GATEWAY_DEPOSIT
    ) {
        return "+"
    }
    if (transaction.transactionType == ServiceType.CASH_WITHDRAW) {
        return "-"
    }
    if (transaction.toWalletNumber == myWalletNumber) {
        return "+"
    }
    return "-"
}

fun LocalDateTime.formateDateTimeString(pattern: String = "dd/MM/yyyy • HH:mm"): String {
    if (LocalDate.now() == this.toLocalDate()) {
        return "Hôm nay • " + this.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
    if (LocalDate.now().minusDays(1) == this.toLocalDate()) {
        return "Hôm qua • " + this.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.getViewModel(
    navController: NavController,
    route: String
): T {
    val entry = remember(this) {
        navController.getBackStackEntry(route)
    }
    return hiltViewModel<T>(entry)
}

fun Context.appSessionManager(): AppSessionManager {
    val entryPoint = EntryPointAccessors.fromApplication(
        this,
        AppSessionEntryPoint::class.java
    )
    return entryPoint.appSessionManager()
}
