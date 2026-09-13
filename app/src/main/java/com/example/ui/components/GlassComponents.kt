package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun GlassCard(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(20.dp),
  borderBrush: Brush = Brush.linearGradient(
    colors = listOf(
      Color(0x55A855F7),
      Color(0x22A855F7),
      Color(0x11C084FC)
    )
  ),
  borderWidth: Dp = 1.dp,
  backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
  content: @Composable ColumnScope.() -> Unit
) {
  Column(
    modifier = modifier
      .clip(shape)
      .background(backgroundColor)
      .border(BorderStroke(borderWidth, borderBrush), shape = shape)
      .padding(16.dp),
    content = content
  )
}

@Composable
fun GlowingPill(
  text: String,
  modifier: Modifier = Modifier,
  color: Color = VioletPrimary,
  textColor: Color = Color.White,
  fontSize: Int = 11
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(50.dp))
      .background(color.copy(alpha = 0.15f))
      .border(BorderStroke(1.dp, color.copy(alpha = 0.5f)), RoundedCornerShape(50.dp))
      .padding(horizontal = 10.dp, vertical = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      color = textColor,
      fontSize = fontSize.sp,
      fontWeight = FontWeight.SemiBold,
      letterSpacing = 0.5.sp
    )
  }
}

@Composable
fun StatusBadge(
  isOnline: Boolean,
  modifier: Modifier = Modifier
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .clip(CircleShape)
      .background(if (isOnline) NeonEmerald.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.15f))
      .border(1.dp, if (isOnline) NeonEmerald.copy(alpha = 0.4f) else Color.Gray.copy(alpha = 0.3f), CircleShape)
      .padding(horizontal = 8.dp, vertical = 3.dp)
  ) {
    Box(
      modifier = Modifier
        .size(6.dp)
        .clip(CircleShape)
        .background(if (isOnline) NeonEmerald else Color.Gray)
    )
    Spacer(modifier = Modifier.width(5.dp))
    Text(
      text = if (isOnline) "Active Now" else "Offline",
      fontSize = 10.sp,
      color = if (isOnline) NeonEmerald else Color.Gray,
      fontWeight = FontWeight.Medium
    )
  }
}

@Composable
fun UserAvatar(
  name: String,
  modifier: Modifier = Modifier,
  size: Dp = 44.dp,
  backgroundColor: Color = VioletDeep
) {
  val initial = name.firstOrNull()?.toString() ?: "U"
  Box(
    modifier = modifier
      .size(size)
      .clip(CircleShape)
      .background(
        Brush.linearGradient(
          colors = listOf(VioletPrimary, VioletDeep, Color(0xFF3B0764))
        )
      )
      .border(1.5.dp, VioletSecondary.copy(alpha = 0.6f), CircleShape),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = initial,
      color = Color.White,
      fontSize = (size.value * 0.42f).sp,
      fontWeight = FontWeight.Bold
    )
  }
}
