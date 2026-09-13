package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.Peer
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun VoiceCallDialog(
  peer: Peer,
  onEndCall: () -> Unit
) {
  var callSeconds by remember { mutableIntStateOf(0) }
  var isMuted by remember { mutableStateOf(false) }
  var isSpeakerOn by remember { mutableStateOf(true) }

  LaunchedEffect(Unit) {
    while (true) {
      delay(1000)
      callSeconds++
    }
  }

  val minutes = callSeconds / 60
  val seconds = callSeconds % 60
  val timeFormatted = String.format("%02d:%02d", minutes, seconds)

  val infiniteTransition = rememberInfiniteTransition(label = "pulse_rings")
  val ringAnim1 by infiniteTransition.animateFloat(
    initialValue = 0.8f,
    targetValue = 1.35f,
    animationSpec = infiniteRepeatable(
      animation = tween(1800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "ring1"
  )
  val ringAnim2 by infiniteTransition.animateFloat(
    initialValue = 0.5f,
    targetValue = 1.6f,
    animationSpec = infiniteRepeatable(
      animation = tween(2400, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "ring2"
  )

  Dialog(
    onDismissRequest = onEndCall,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color(0xFF130829),
              Color(0xFF090414),
              Color(0xFF040208)
            )
          )
        )
        .padding(24.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
          .fillMaxSize()
          .systemBarsPadding()
      ) {
        // Top Header
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(top = 24.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(30.dp))
              .background(NeonEmerald.copy(alpha = 0.15f))
              .border(1.dp, NeonEmerald.copy(alpha = 0.4f), RoundedCornerShape(30.dp))
              .padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(NeonEmerald)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "ENCRYPTED PEER VOICE CALL",
              color = NeonEmerald,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
          }

          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = peer.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "${peer.university} • ${peer.major}",
            fontSize = 14.sp,
            color = TextSecondaryDark
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = timeFormatted,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = VioletSecondary
          )
        }

        // Center Pulsing Sound Waves & Avatar
        Box(
          modifier = Modifier
            .size(260.dp),
          contentAlignment = Alignment.Center
        ) {
          // Pulsing animated wave rings
          Canvas(modifier = Modifier.fillMaxSize()) {
            val center = center
            drawCircle(
              color = VioletPrimary.copy(alpha = (1.6f - ringAnim2).coerceIn(0f, 0.4f)),
              radius = 80.dp.toPx() * ringAnim2,
              center = center
            )
            drawCircle(
              color = NeonCyan.copy(alpha = (1.35f - ringAnim1).coerceIn(0f, 0.5f)),
              radius = 70.dp.toPx() * ringAnim1,
              center = center
            )
          }

          // Large Avatar
          Box(
            modifier = Modifier
              .size(110.dp)
              .clip(CircleShape)
              .background(
                Brush.linearGradient(
                  colors = listOf(VioletPrimary, VioletDeep)
                )
              )
              .border(3.dp, Color.White.copy(alpha = 0.8f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = peer.avatarLetter,
              color = Color.White,
              fontSize = 44.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Action Buttons Row
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(bottom = 32.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Mute Button
            IconButton(
              onClick = { isMuted = !isMuted },
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (isMuted) Color.White else ObsidianCardBg)
                .border(1.dp, ObsidianCardBorder, CircleShape)
            ) {
              Icon(
                imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                contentDescription = "Mute",
                tint = if (isMuted) ObsidianBlack else Color.White
              )
            }

            // End Call Button
            IconButton(
              onClick = onEndCall,
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFEF4444))
            ) {
              Icon(
                imageVector = Icons.Default.CallEnd,
                contentDescription = "End Call",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
              )
            }

            // Speaker Button
            IconButton(
              onClick = { isSpeakerOn = !isSpeakerOn },
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (isSpeakerOn) VioletPrimary else ObsidianCardBg)
                .border(1.dp, ObsidianCardBorder, CircleShape)
            ) {
              Icon(
                imageVector = if (isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                contentDescription = "Speaker",
                tint = Color.White
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "AI Study Buddy is taking bullet summaries in real-time",
            fontSize = 12.sp,
            color = TextMutedDark
          )
        }
      }
    }
  }
}
