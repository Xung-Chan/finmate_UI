package com.example.ibanking_kltn.utils

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