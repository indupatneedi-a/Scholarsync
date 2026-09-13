package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
  currentTheme: AppThemeMode,
  onThemeChanged: (AppThemeMode) -> Unit,
  onManageAccount: () -> Unit = {}
) {
  val scrollState = rememberScrollState()

  var privateByDefault by remember { mutableStateOf(true) }
  var anonymousMatching by remember { mutableStateOf(false) }
  var notifyOnMatch by remember { mutableStateOf(true) }
  var notifyOnVoiceCall by remember { mutableStateOf(true) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 18.dp)
        .padding(top = 40.dp, bottom = 90.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Platform Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Theme preferences, privacy, and voice engine",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )
        }
        GlowingPill(text = "STUDENT VAULT", color = NeonCyan, fontSize = 10)
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 1. Theme Configuration
      Text("THEME & VISUAL DISPLAY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletSecondary, letterSpacing = 1.sp)
      Spacer(modifier = Modifier.height(10.dp))

      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Text("Saved Workspace Theme", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Preference persists automatically across app sessions.", fontSize = 12.sp, color = TextSecondaryDark)

        Spacer(modifier = Modifier.height(14.dp))

        listOf(
          Triple(AppThemeMode.DARK, "Dark Obsidian", "High contrast black canvas with neon violet glassmorphism"),
          Triple(AppThemeMode.LIGHT, "Light Studio", "Crisp light canvas with refined purple accents"),
          Triple(AppThemeMode.SYSTEM, "System Default", "Follows device OS appearance automatically")
        ).forEach { (mode, title, desc) ->
          val isSelected = currentTheme == mode
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) VioletPrimary.copy(alpha = 0.2f) else ObsidianDarkSurface)
              .border(1.dp, if (isSelected) VioletPrimary else ObsidianCardBorder, RoundedCornerShape(12.dp))
              .clickable { onThemeChanged(mode) }
              .padding(12.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              RadioButton(
                selected = isSelected,
                onClick = { onThemeChanged(mode) },
                colors = RadioButtonDefaults.colors(selectedColor = VioletPrimary)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(desc, fontSize = 11.sp, color = TextSecondaryDark)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 2. Privacy & File Vault
      Text("PRIVACY & VAULT SECURITY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletSecondary, letterSpacing = 1.sp)
      Spacer(modifier = Modifier.height(10.dp))

      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Private by Default for all uploads", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Files stay encrypted in your local vault until explicitly shared in a peer study session.", fontSize = 11.sp, color = TextSecondaryDark)
          }
          Switch(
            checked = privateByDefault,
            onCheckedChange = { privateByDefault = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = VioletPrimary)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = ObsidianCardBorder)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Anonymous Matching Mode", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Hide real university name until a study request is mutually confirmed.", fontSize = 11.sp, color = TextSecondaryDark)
          }
          Switch(
            checked = anonymousMatching,
            onCheckedChange = { anonymousMatching = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = VioletPrimary)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 3. Notification Settings
      Text("NOTIFICATION PREFERENCES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletSecondary, letterSpacing = 1.sp)
      Spacer(modifier = Modifier.height(10.dp))

      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("AI Peer Match Alerts", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Notify me when a high-similarity student is studying the same topic.", fontSize = 11.sp, color = TextSecondaryDark)
          }
          Switch(
            checked = notifyOnMatch,
            onCheckedChange = { notifyOnMatch = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = VioletPrimary)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = ObsidianCardBorder)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Incoming Peer Voice Call Rings", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Audible ringtone when an approved study partner initiates a voice call.", fontSize = 11.sp, color = TextSecondaryDark)
          }
          Switch(
            checked = notifyOnVoiceCall,
            onCheckedChange = { notifyOnVoiceCall = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = VioletPrimary)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 4. University & Firebase Authentication
      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("Firebase Account Security", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
              Text("Manage credentials, Google login & password", fontSize = 11.sp, color = TextSecondaryDark)
            }
          }
          Button(
            onClick = onManageAccount,
            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text("Manage", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
