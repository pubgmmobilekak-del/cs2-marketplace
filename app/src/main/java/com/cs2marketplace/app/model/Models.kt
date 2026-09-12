package com.cs2marketplace.app.model

/**
 * Itemning Steam'ga chiqarish holati.
 * INVENTORDA -> PENDING -> YUBORILGAN
 * (C++ versiyadagi tradeStatus string maydoniga mos keladi)
 */
enum class TradeStatus(val label: String) {
    INVENTORDA("Inventarda"),
    PENDING("Kutilmoqda"),
    YUBORILGAN("Yuborilgan")
}

/**
 * Rarity darajalari va ularga mos ranglar - CS2'dagi umumiy rang
 * konventsiyasiga asoslangan (bu shunchaki UI ranglash sxemasi,
 * hech qanday Valve grafik materiali ishlatilmagan).
 */
enum class Rarity(val label: String, val colorHex: Long) {
    CONSUMER("Consumer", 0xFFB0C3D9),
    MIL_SPEC("Mil-Spec", 0xFF4B69FF),
    RESTRICTED("Restricted", 0xFF8847FF),
    CLASSIFIED("Classified", 0xFFD32CE6),
    COVERT("Covert", 0xFFEB4B4B),
    EXTRAORDINARY("Extraordinary", 0xFFFFD700),
    CONTRABAND("Contraband", 0xFFE4AE39),
    BASE("Base", 0xFF9E9E9E)
}

/**
 * Bitta market/inventar itemi.
 *
 * steamTradeUrl HOZIRCHA TEST QIYMATI bilan keladi - real ilovada
 * bu foydalanuvchining o'z Steam profilidan olinishi kerak, hech
 * qachon shu tarzda qattiq yozilmasligi kerak.
 */
data class Item(
    val id: Int,
    val name: String,
    val type: String,
    val rarity: Rarity,
    val floatValue: Double,
    val isStatTrak: Boolean,
    val price: Double,
    val tradeStatus: TradeStatus = TradeStatus.INVENTORDA,
    val steamTradeUrl: String = "https://steamcommunity.com/tradeoffer/new/?partner=123456789&token=AbCdEfGh"
) {
    /** Float qiymatiga qarab wear nomini qaytaradi (FN/MW/FT/WW/BS) */
    val wearLabel: String
        get() = when {
            floatValue < 0.07 -> "FN"
            floatValue < 0.15 -> "MW"
            floatValue < 0.38 -> "FT"
            floatValue < 0.45 -> "WW"
            else -> "BS"
        }
}

/** Ilovaning bitta ekranda ko'rsatiladigan to'liq holati. */
data class UiState(
    val balanceUsd: Double = 120.0,
    val tokenBalance: Double = 0.0,
    val market: List<Item> = emptyList(),
    val inventory: List<Item> = emptyList(),
    val lastTradeOfferId: String? = null,
    val lastMessage: String? = null
)
