package com.cs2marketplace.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cs2marketplace.app.ui.screens.InventoryScreen
import com.cs2marketplace.app.ui.screens.MarketScreen
import com.cs2marketplace.app.ui.screens.WalletScreen
import com.cs2marketplace.app.viewmodel.MarketplaceViewModel

private sealed class Tab(val route: String, val label: String) {
    object Market : Tab("market", "Bozor")
    object Inventory : Tab("inventory", "Inventar")
    object Wallet : Tab("wallet", "Hamyon")
}

private val tabs = listOf(Tab.Market, Tab.Inventory, Tab.Wallet)

@Composable
fun AppNavHost(viewModel: MarketplaceViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState = viewModel.uiState

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                tabs.forEach { tab ->
                    val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val icon = when (tab) {
                                Tab.Market -> Icons.Filled.Storefront
                                Tab.Inventory -> Icons.Filled.Inventory2
                                Tab.Wallet -> Icons.Filled.AccountBalanceWallet
                            }
                            Icon(imageVector = icon, contentDescription = tab.label)
                        },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Tab.Market.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Tab.Market.route) {
                MarketScreen(
                    market = uiState.market,
                    balanceUsd = uiState.balanceUsd,
                    onBuy = viewModel::buyItem
                )
            }
            composable(Tab.Inventory.route) {
                InventoryScreen(
                    inventory = uiState.inventory,
                    onWithdrawToSteam = viewModel::withdrawToSteam
                )
            }
            composable(Tab.Wallet.route) {
                WalletScreen(
                    balanceUsd = uiState.balanceUsd,
                    tokenBalance = uiState.tokenBalance,
                    onTopUpUsd = viewModel::topUpUsd,
                    onTopUpUzs = viewModel::topUpUzs,
                    onTopUpCrypto = viewModel::topUpCrypto,
                    onTopUpSkinTradeIn = viewModel::topUpSkinTradeIn,
                    onTopUpPay = viewModel::topUpPay,
                    onTopUpSberbank = viewModel::topUpSberbank,
                    onStakeTokens = viewModel::stakeTokens
                )
            }
        }
    }
}
