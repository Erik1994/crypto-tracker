package com.example.cryptotracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.cryptotracker.core.presentation.util.ObserveAsEvents
import com.example.cryptotracker.crypto.presentation.coin_list.CoinListAction
import com.example.cryptotracker.crypto.presentation.coin_list.CoinListEvent
import com.example.cryptotracker.crypto.presentation.coin_list.CoinListScreen
import com.example.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    val coinListViewModel = koinViewModel<CoinListViewModel>()
                    val coinListState by coinListViewModel.state.collectAsState()
                    val context = LocalContext.current
                    ObserveAsEvents(flow = coinListViewModel.events) {
                        when (it) {
                            is CoinListEvent.Error -> Toast.makeText(
                                context,
                                it.message.asString(context),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    CoinListScreen(
                        state = coinListState,
                        onAction = coinListViewModel::onAction,
                        modifier = Modifier.padding(paddingValues),
                    )
                }
            }
        }
    }
}