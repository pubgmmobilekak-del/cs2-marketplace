package com.cs2marketplace.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cs2marketplace.app.ui.theme.GoldAccent
import com.cs2marketplace.app.ui.theme.OrangeAccent
import com.cs2marketplace.app.ui.theme.SurfaceElevated
import com.cs2marketplace.app.ui.theme.TextSecondary

@Composable
fun WalletScreen(
    balanceUsd: Double,
    tokenBalance: Double,
    onTopUpUsd: (Double) -> Unit,
    onTopUpUzs: (Double) -> Unit,
    onTopUpCrypto: (Double) -> Unit,
    onTopUpSkinTradeIn: () -> Unit,
    onTopUpPay: (Double) -> Unit,
    onTopUpSberbank: (Double) -> Unit,
    onStakeTokens: (Double) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = "Hamyon", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        BalanceCard(balanceUsd = balanceUsd, tokenBalance = tokenBalance)

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Balansni to'ldirish", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))

        TopUpSection(
            onTopUpUsd = onTopUpUsd,
            onTopUpUzs = onTopUpUzs,
            onTopUpCrypto = onTopUpCrypto,
            onTopUpSkinTradeIn = onTopUpSkinTradeIn,
            onTopUpPay = onTopUpPay,
            onTopUpSberbank = onTopUpSberbank
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "CS2 Token / Steyking", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))

        StakingSection(tokenBalance = tokenBalance, onStakeTokens = onStakeTokens)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun BalanceCard(balanceUsd: Double, tokenBalance: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "Asosiy balans", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "$${"%.2f".format(balanceUsd)}",
                color = OrangeAccent,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Platforma Tokeni (CS2T)", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "${"%.2f".format(tokenBalance)} CS2T",
                color = GoldAccent,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun TopUpSection(
    onTopUpUsd: (Double) -> Unit,
    onTopUpUzs: (Double) -> Unit,
    onTopUpCrypto: (Double) -> Unit,
    onTopUpSkinTradeIn: () -> Unit,
    onTopUpPay: (Double) -> Unit,
    onTopUpSberbank: (Double) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    val amount = amountText.toDoubleOrNull()

    OutlinedTextField(
        value = amountText,
        onValueChange = { amountText = it },
        label = { Text("Miqdor") },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(10.dp))

    val rowSpacing = 8.dp
    Row(horizontalArrangement = Arrangement.spacedBy(rowSpacing), modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.weight(1f),
            enabled = amount != null && amount > 0,
            onClick = { amount?.let { onTopUpUsd(it); amountText = "" } }
        ) { Text("USD") }
        Button(
            modifier = Modifier.weight(1f),
            enabled = amount != null && amount > 0,
            onClick = { amount?.let { onTopUpUzs(it); amountText = "" } }
        ) { Text("UZS") }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(rowSpacing), modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.weight(1f),
            enabled = amount != null && amount > 0,
            onClick = { amount?.let { onTopUpCrypto(it); amountText = "" } }
        ) { Text("Crypto") }
        Button(
            modifier = Modifier.weight(1f),
            enabled = amount != null && amount > 0,
            onClick = { amount?.let { onTopUpPay(it); amountText = "" } }
        ) { Text("Pay") }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(rowSpacing), modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.weight(1f),
            enabled = amount != null && amount > 0,
            onClick = { amount?.let { onTopUpSberbank(it); amountText = "" } }
        ) { Text("Sberbank") }
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = onTopUpSkinTradeIn
        ) { Text("Skin almashtirish") }
    }
}

@Composable
private fun StakingSection(tokenBalance: Double, onStakeTokens: (Double) -> Unit) {
    var stakeText by remember { mutableStateOf("") }
    val stakeAmount = stakeText.toDoubleOrNull()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tokenlaringizni steykingga qo'ying va +5% mukofot oling",
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = stakeText,
                onValueChange = { stakeText = it },
                label = { Text("CS2T miqdori") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = stakeAmount != null && stakeAmount > 0 && stakeAmount <= tokenBalance,
                onClick = { stakeAmount?.let { onStakeTokens(it); stakeText = "" } }
            ) { Text("Steykingga qo'yish") }
        }
    }
}
