package com.example.ibanking_kltn.dtos.definitions

import androidx.annotation.DrawableRes
import com.example.ibanking_kltn.R

enum class CategoryIcon(
    val code: String,
    @DrawableRes val resId: Int
) {
    BASKETBALL("basketball", R.drawable.basketball),
    CAR("car", R.drawable.car),
    CART("cart", R.drawable.cart),
    CHILD("child", R.drawable.child),
    COFFEE("coffee", R.drawable.coffee),
    FRUIT("fruit", R.drawable.fruit),
    GAME("game", R.drawable.game),
    GIFT("gift", R.drawable.gift),
    HEALTH("health", R.drawable.health),
    IPHONE("iphone", R.drawable.iphone),
    PET("pet", R.drawable.pet),
    PIZZA("pizza", R.drawable.pizza),
    RESTAURANT("restaurant", R.drawable.restaurant),
    SMOKE("smoke", R.drawable.smoke),
    SPORT("sport", R.drawable.sport),
    BEAUTY("beauty", R.drawable.beauty),
    TOOL("tool", R.drawable.tool),
    TRAVEL("travel", R.drawable.airplane_service),
    PC("pc", R.drawable.pc),
    SOCIAL_MEDIA("social_media", R.drawable.social_media),
    TRANSPORT("transport", R.drawable.transport),
    UNKNOWN("unknown", R.drawable.unknown);

    companion object {
        fun fromCode(code: String): CategoryIcon =
            entries.firstOrNull { it.code == code } ?: UNKNOWN
    }
}
