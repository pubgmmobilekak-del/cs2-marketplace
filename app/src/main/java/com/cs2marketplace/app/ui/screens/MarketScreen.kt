package com.cs2marketplace.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cs2marketplace.app.model.Item
import com.cs2marketplace.app.ui.components.ItemCard

@Composable
fun MarketScreen(
    market: List<Item>,
    balanceUsd: Double,
    onBuy: (Item) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(top = 4.dp)) {
        Text(
            text = "CS2 Global Marketplace",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(market, key = { it.id }) { item ->
                ItemCard(
                    item = item,
                    actionLabel = "Sotib olish",
                    actionEnabled = balanceUsd >= item.price,
                    onAction = { onBuy(item) }
                )
            }
        }
    }
}
