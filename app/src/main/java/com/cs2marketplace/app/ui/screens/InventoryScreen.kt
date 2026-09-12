package com.cs2marketplace.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cs2marketplace.app.model.Item
import com.cs2marketplace.app.model.TradeStatus
import com.cs2marketplace.app.ui.components.ItemCard
import com.cs2marketplace.app.ui.theme.TextSecondary

@Composable
fun InventoryScreen(
    inventory: List<Item>,
    onWithdrawToSteam: (Item) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(top = 4.dp)) {
        Text(
            text = "Inventarim",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        if (inventory.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Inventaringiz bo'sh.\nBozordan biror narsa sotib oling.",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            return
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(inventory, key = { it.id }) { item ->
                val isSendable = item.tradeStatus == TradeStatus.INVENTORDA
                ItemCard(
                    item = item,
                    showStatus = true,
                    actionLabel = when (item.tradeStatus) {
                        TradeStatus.INVENTORDA -> "Steam'ga chiqarish"
                        TradeStatus.PENDING -> "Kutilmoqda..."
                        TradeStatus.YUBORILGAN -> "Yuborilgan"
                    },
                    actionEnabled = isSendable,
                    onAction = { onWithdrawToSteam(item) }
                )
            }
        }
    }
}
