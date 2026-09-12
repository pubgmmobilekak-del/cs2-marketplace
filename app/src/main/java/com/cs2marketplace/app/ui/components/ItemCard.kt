package com.cs2marketplace.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cs2marketplace.app.model.Item
import com.cs2marketplace.app.model.TradeStatus
import com.cs2marketplace.app.ui.theme.SuccessGreen
import com.cs2marketplace.app.ui.theme.SurfaceElevated
import com.cs2marketplace.app.ui.theme.TextSecondary
import com.cs2marketplace.app.ui.theme.WarningYellow

@Composable
fun ItemCard(
    item: Item,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    actionEnabled: Boolean = true,
    onAction: (() -> Unit)? = null,
    showStatus: Boolean = false
) {
    val rarityColor = Color(item.rarity.colorHex)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(rarityColor.copy(alpha = 0.16f), Color.Transparent)
                        )
                    )
            )

            Row(modifier = Modifier.padding(14.dp)) {
                ItemTypeBadge(type = item.type, rarityColor = rarityColor)

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (item.isStatTrak) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Filled.LocalFireDepartment,
                                    contentDescription = "StatTrak",
                                    tint = WarningYellow,
                                    modifier = Modifier.height(18.dp)
                                )
                            }
                        }
                        Text(
                            text = "$${"%.2f".format(item.price)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${item.rarity.label} • ${item.type} • ${item.wearLabel} • Float ${"%.4f".format(item.floatValue)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    if (showStatus) {
                        Spacer(modifier = Modifier.height(6.dp))
                        AnimatedContent(
                            targetState = item.tradeStatus,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "status-chip"
                        ) { status ->
                            StatusChip(status)
                        }
                    }

                    if (actionLabel != null && onAction != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onAction,
                            enabled = actionEnabled,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(actionLabel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemTypeBadge(type: String, rarityColor: Color) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(rarityColor.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconForType(type),
            contentDescription = type,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

private fun iconForType(type: String): ImageVector = when (type) {
    "Knife" -> Icons.Filled.ContentCut
    "Case" -> Icons.Filled.Inventory2
    "Gloves" -> Icons.Filled.PanTool
    "Skin" -> Icons.Filled.Colorize
    else -> Icons.Filled.Category
}

@Composable
private fun StatusChip(status: TradeStatus) {
    val (bg, label) = when (status) {
        TradeStatus.INVENTORDA -> TextSecondary to status.label
        TradeStatus.PENDING -> WarningYellow to status.label
        TradeStatus.YUBORILGAN -> SuccessGreen to status.label
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg.copy(alpha = 0.18f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = label, color = bg, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
    }
}
