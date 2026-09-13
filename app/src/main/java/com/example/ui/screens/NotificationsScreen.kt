package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NotificationItem
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.theme.*

@Composable
fun NotificationsScreen(
  notifications: List<NotificationItem>,
  onMarkAllAsRead: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
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
            text = "Campus Alerts",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Live peer matches, request answers, and ratings",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )
        }

        TextButton(onClick = onMarkAllAsRead) {
          Text("Mark All Read", color = VioletSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(notifications) { notif ->
          GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.Top
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(
                    when (notif.type) {
                      "RATING" -> AmberRating.copy(alpha = 0.2f)
                      "MATCH" -> NeonCyan.copy(alpha = 0.2f)
                      "ACCEPT" -> NeonEmerald.copy(alpha = 0.2f)
                      else -> VioletPrimary.copy(alpha = 0.2f)
                    }
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = when (notif.type) {
                    "RATING" -> Icons.Default.Star
                    "MATCH" -> Icons.Default.AutoAwesome
                    "ACCEPT" -> Icons.Default.Handshake
                    else -> Icons.Default.Notifications
                  },
                  contentDescription = null,
                  tint = when (notif.type) {
                    "RATING" -> AmberRating
                    "MATCH" -> NeonCyan
                    "ACCEPT" -> NeonEmerald
                    else -> VioletSecondary
                  },
                  modifier = Modifier.size(18.dp)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(notif.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                  if (!notif.isRead) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(VioletSecondary))
                  }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(notif.message, fontSize = 12.sp, color = TextSecondaryDark, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(notif.timestamp, fontSize = 10.sp, color = TextMutedDark)
              }
            }
          }
        }
      }
    }
  }
}
