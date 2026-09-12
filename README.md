# CS2 Marketplace — Beta (Kotlin + Jetpack Compose)

Bu C++ konsol dasturidan (`main.cpp`) Kotlin/Compose Android ilovasiga
o'tkazilgan **beta** versiya. Mantiq bir xil: balans, token/steyking,
bozor, xarid, inventar va Steam'ga chiqarish (hozircha simulyatsiya).

## Muhim: bu yerda APK build qilinmagan

Bu loyiha **manba kodi** sifatida beriladi. APK faylni olish uchun
Android Studio kerak (bu muhitda internet/Android SDK yo'q, shuning
uchun build bu yerda amalga oshirilmadi).

## APK olish qadamlari (Android Studio orqali)

1. Android Studio'ni o'rnating (agar yo'q bo'lsa): https://developer.android.com/studio
2. "Open" → shu `CS2Marketplace` papkasini tanlang.
3. Gradle sinxronlashishini kutib turing (birinchi marta internet talab
   qiladi — kutubxonalarni yuklab oladi).
4. Menyudan: **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
5. Build tugagach, pastda chiqadigan "locate" havolasi orqali
   `app/build/outputs/apk/debug/app-beta-debug.apk` faylini topasiz.
6. Shu APK faylni telefonga o'tkazib, "Noma'lum manbalar"ga ruxsat
   berib o'rnatishingiz mumkin (beta versiya uchun yetarli).

## Loyiha tarkibi

- `model/Models.kt` — Item, Rarity, TradeStatus, UiState
- `viewmodel/MarketplaceViewModel.kt` — barcha biznes-mantiq
  (C++ dagi User/PlatformToken/Marketplace/PaymentGateway/
  SteamWithdrawService klasslarining vazifasi)
- `ui/screens/` — MarketScreen, InventoryScreen, WalletScreen
- `ui/components/ItemCard.kt` — rarity rangi, wear, StatTrak, holat chip
- `ui/theme/` — qora fon + to'q sariq (orange) urg'u bilan mavzu
- `navigation/AppNav.kt` — pastki navigatsiya (Bozor/Inventar/Hamyon)

## Steam'ga chiqarish — hali SIMULYATSIYA

`MarketplaceViewModel.withdrawToSteam()` funksiyasi ichida `TODO(real-api)`
belgisi qo'yilgan joy bor. Real Steam integratsiyasi uchun:

1. `AndroidManifest.xml`da `<uses-permission android:name="android.permission.INTERNET" />`
   qatorini oching.
2. Retrofit/OkHttp kutubxonasini qo'shing.
3. Steam Web API key va bot sessiyasini **hech qachon kodga yozmang** —
   `local.properties` (git'ga tushmaydi) yoki serverdagi maxfiy
   saqlash orqali o'qing.
4. `withdrawToSteam()` ichidagi simulyatsiya qismini haqiqiy HTTP
   so'rovga almashtiring.

## Keyingi qadamlar (production uchun)

- Haqiqiy to'lov tizimlari (Payme, Click, Stripe va h.k.) integratsiyasi
  — hozirgi "USD/UZS/Crypto/Pay/Sberbank" tugmalari faqat mahalliy
  state'ni o'zgartiradi, haqiqiy pul o'tkazmasi yo'q.
- Backend server (foydalanuvchi ma'lumotlarini saqlash uchun) — hozir
  hammasi faqat qurilma xotirasida, ilova yopilsa yo'qoladi.
- App icon (`mipmap`) qo'shish — hozircha standart tizim ikonkasi
  ishlatiladi.
