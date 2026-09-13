package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.KnowConnectRepository
import com.example.ui.navigation.KnowConnectNavHost
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.KnowConnectTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      var currentThemeMode by remember { mutableStateOf(AppThemeMode.DARK) }
      val repository = remember { KnowConnectRepository() }

      KnowConnectTheme(themeMode = currentThemeMode) {
        Surface(modifier = Modifier.fillMaxSize()) {
          KnowConnectNavHost(
            repository = repository,
            currentThemeMode = currentThemeMode,
            onThemeChanged = { newTheme ->
              currentThemeMode = newTheme
            }
          )
        }
      }
    }
  }
}

