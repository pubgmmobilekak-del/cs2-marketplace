package com.cs2marketplace.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs2marketplace.app.model.Item
import com.cs2marketplace.app.model.Rarity
import com.cs2marketplace.app.model.TradeStatus
import com.cs2marketplace.app.model.UiState

/**
 * Butun ilovaning biznes-mantiqi shu yerda.
 * C++ versiyadagi User, PlatformToken, Marketplace, PaymentGateway va
 * SteamWithdrawService klasslarining vazifasini bajaradi.
 */
class MarketplaceViewModel : ViewModel() {

    var uiState by mutableStateOf(UiState(market = initialMarket()))
        private set

    private var nextMarketId = 106

    // ---------- BALANSNI TO'LDIRISH (PaymentGateway o'rnida) ----------

    fun topUpUsd(amount: Double) {
        if (amount <= 0) return
        uiState = uiState.copy(
            balanceUsd = uiState.balanceUsd + amount,
            lastMessage = "Muvaffaqiyatli! $${"%.2f".format(amount)} balansga qo'shildi."
        )
    }

    fun topUpUzs(amountUzs: Double) {
        if (amountUzs <= 0) return
        val rate = 12800.0
        val usd = amountUzs / rate
        uiState = uiState.copy(
            balanceUsd = uiState.balanceUsd + usd,
            lastMessage = "$amountUzs so'm ekvivalentida $${"%.2f".format(usd)} qo'shildi."
        )
    }

    fun topUpCrypto(usdtAmount: Double) {
        if (usdtAmount <= 0) return
        uiState = uiState.copy(
            balanceUsd = uiState.balanceUsd + usdtAmount,
            lastMessage = "Kripto tranzaksiya tasdiqlandi! $${"%.2f".format(usdtAmount)} qo'shildi."
        )
    }

    fun topUpSkinTradeIn() {
        // C++ versiyadagi kabi - test uchun belgilangan qiymat
        val usd = 5.50
        uiState = uiState.copy(
            balanceUsd = uiState.balanceUsd + usd,
            lastMessage = "Skin baholanib, balansga $${"%.2f".format(usd)} qo'shildi."
        )
    }

    fun topUpPay(amount: Double) {
        if (amount <= 0) return
        uiState = uiState.copy(
            balanceUsd = uiState.balanceUsd + amount,
            lastMessage = "Pay orqali $${"%.2f".format(amount)} qo'shildi."
        )
    }

    fun topUpSberbank(rubAmount: Double) {
        if (rubAmount <= 0) return
        val rate = 95.0
        val usd = rubAmount / rate
        uiState = uiState.copy(
            balanceUsd = uiState.balanceUsd + usd,
            lastMessage = "Sberbank orqali $${"%.2f".format(usd)} qo'shildi."
        )
    }

    // ---------- TOKEN / STEYKING (PlatformToken o'rnida) ----------

    fun stakeTokens(amount: Double): Boolean {
        if (amount <= 0 || uiState.tokenBalance < amount) {
            uiState = uiState.copy(lastMessage = "Hisobingizda yetarli token yo'q!")
            return false
        }
        val stakingRate = 0.05
        val reward = amount * stakingRate
        uiState = uiState.copy(
            tokenBalance = uiState.tokenBalance + reward,
            lastMessage = "Tokenlar steykingga qo'yildi! Mukofot: +${"%.2f".format(reward)} CS2T"
        )
        return true
    }

    // ---------- BOZOR / XARID (Marketplace o'rnida) ----------

    fun buyItem(item: Item): Boolean {
        if (uiState.balanceUsd < item.price) {
            uiState = uiState.copy(
                lastMessage = "Balansingiz yetarli emas! Narx: $${item.price}, Sizda: $${"%.2f".format(uiState.balanceUsd)}"
            )
            return false
        }
        val tokenReward = item.price * 0.10
        uiState = uiState.copy(
            balanceUsd = uiState.balanceUsd - item.price,
            tokenBalance = uiState.tokenBalance + tokenReward,
            market = uiState.market.filterNot { it.id == item.id },
            inventory = uiState.inventory + item.copy(tradeStatus = TradeStatus.INVENTORDA),
            lastMessage = "Tabriklaymiz! Xarid qilindi: ${item.name} ($${item.price}). Bonus: +${"%.2f".format(tokenReward)} CS2T"
        )
        return true
    }

    // ---------- STEAM'GA CHIQARISH (SteamWithdrawService o'rnida) ----------
    // DIQQAT: hozircha SIMULYATSIYA. Real Steam Web API/Trade Offer
    // integratsiyasi uchun bu funksiya ichida haqiqiy tarmoq chaqiruvi
    // (masalan Retrofit/OkHttp orqali) qo'shilishi kerak. Bot akkaunt
    // sessiyasi, Steam API key va Steam Guard tasdiqlash kodini bu faylga
    // yoki boshqa manba kodiga hech qachon qattiq yozmang - BuildConfig
    // yoki local.properties (git'ga tushmaydigan fayl) orqali o'qing.

    fun withdrawToSteam(item: Item) {
        when (item.tradeStatus) {
            TradeStatus.YUBORILGAN -> {
                uiState = uiState.copy(lastMessage = "Bu item allaqachon Steam'ga yuborilgan.")
                return
            }
            TradeStatus.PENDING -> {
                uiState = uiState.copy(lastMessage = "Bu item hozir Kutilmoqda holatida.")
                return
            }
            TradeStatus.INVENTORDA -> Unit
        }

        val tradeOfferId = "TO-" + (100_000_000..999_999_999).random()

        // 1-bosqich: Pending
        uiState = uiState.copy(
            inventory = uiState.inventory.map {
                if (it.id == item.id) it.copy(tradeStatus = TradeStatus.PENDING) else it
            },
            lastTradeOfferId = tradeOfferId,
            lastMessage = "Trade Offer yaratildi: $tradeOfferId. Holati: Kutilmoqda..."
        )

        // 2-bosqich: simulyatsiyada avtomatik tasdiqlanadi -> Yuborilgan
        // TODO(real-api): bu joyni real Steam trade-offer holatini
        // so'rovdan keyin (webhook yoki polling orqali) yangilashga almashtiring.
        uiState = uiState.copy(
            inventory = uiState.inventory.map {
                if (it.id == item.id) it.copy(tradeStatus = TradeStatus.YUBORILGAN) else it
            },
            lastMessage = "Trade Offer tasdiqlandi! Yakuniy holat: Yuborilgan ($tradeOfferId)"
        )
    }

    fun clearLastMessage() {
        uiState = uiState.copy(lastMessage = null)
    }

    private fun initialMarket(): List<Item> = listOf(
        Item(101, "AK-47 | Fire Serpent", "Skin", Rarity.COVERT, 0.08, true, 350.00),
        Item(102, "AWP | Asiimov", "Skin", Rarity.COVERT, 0.22, false, 85.50),
        Item(103, "Karambit | Doppler", "Knife", Rarity.EXTRAORDINARY, 0.02, false, 950.00),
        Item(104, "Operation Bravo Case", "Case", Rarity.BASE, 0.00, false, 45.00),
        Item(105, "M4A4 | Howl", "Skin", Rarity.CONTRABAND, 0.12, true, 1800.00),
    )
}
