package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.AiService
import com.example.model.Peer
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.components.StatusBadge
import com.example.ui.components.UserAvatar
import com.example.ui.theme.*

enum class MatchingDirection {
  LOOKING_FOR_HELP,
  OFFERING_HELP
}

@Composable
fun AiMatchingScreen(
  peers: List<Peer>,
  onStartChatWithPeer: (Peer) -> Unit,
  onLaunchVoiceCallWithPeer: (Peer) -> Unit
) {
  var direction by remember { mutableStateOf(MatchingDirection.LOOKING_FOR_HELP) }
  var searchQuery by remember { mutableStateOf("") }
  var selectedCollegeFilter by remember { mutableStateOf("All Universities") }

  val universities = listOf("All Universities", "Stanford University", "MIT", "UC Berkeley", "Carnegie Mellon", "Harvard University")

  val filteredPeers = peers.filter { peer ->
    val matchesSearch = searchQuery.isBlank() ||
      peer.name.contains(searchQuery, ignoreCase = true) ||
      peer.topicsCanExplain.any { it.contains(searchQuery, ignoreCase = true) } ||
      peer.major.contains(searchQuery, ignoreCase = true)

    val matchesCollege = selectedCollegeFilter == "All Universities" || peer.university == selectedCollegeFilter

    matchesSearch && matchesCollege
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 18.dp)
        .padding(top = 40.dp, bottom = 80.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "AI Peer Matchmaker",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Adaptive vector matching across colleges",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )
        }
        GlowingPill(text = "LIVE AI ENGINE", color = NeonCyan, fontSize = 10)
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Bi-Directional Matching Switch (Looking for help VS Offering help)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(ObsidianCardBg)
          .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
          .padding(4.dp)
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (direction == MatchingDirection.LOOKING_FOR_HELP) VioletPrimary else Color.Transparent)
            .clickable { direction = MatchingDirection.LOOKING_FOR_HELP }
            .padding(vertical = 10.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = null,
              tint = if (direction == MatchingDirection.LOOKING_FOR_HELP) Color.White else TextSecondaryDark,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "I Need an Explainer",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (direction == MatchingDirection.LOOKING_FOR_HELP) Color.White else TextSecondaryDark
            )
          }
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (direction == MatchingDirection.OFFERING_HELP) VioletPrimary else Color.Transparent)
            .clickable { direction = MatchingDirection.OFFERING_HELP }
            .padding(vertical = 10.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.VolunteerActivism,
              contentDescription = null,
              tint = if (direction == MatchingDirection.OFFERING_HELP) Color.White else TextSecondaryDark,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "I Want to Offer Help",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (direction == MatchingDirection.OFFERING_HELP) Color.White else TextSecondaryDark
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Search bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search by topic, e.g., 'Raft', 'Dijkstra', 'Chem'", fontSize = 13.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = VioletSecondary) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextSecondaryDark)
            }
          }
        },
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = VioletPrimary,
          unfocusedBorderColor = ObsidianCardBorder,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(10.dp))

      // College Filter Chips (Horizontal)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        universities.take(3).forEach { uni ->
          val isSelected = selectedCollegeFilter == uni
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(if (isSelected) VioletPrimary.copy(alpha = 0.25f) else ObsidianCardBg)
              .border(1.dp, if (isSelected) VioletPrimary else ObsidianCardBorder, RoundedCornerShape(20.dp))
              .clickable { selectedCollegeFilter = uni }
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = uni,
              fontSize = 11.sp,
              color = if (isSelected) Color.White else TextSecondaryDark,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Matched Peers List
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(filteredPeers) { peer ->
          val dynamicScore = AiService.calculateMatchScore(peer, searchQuery)

          GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              StatusBadge(isOnline = peer.isOnline)
              GlowingPill(
                text = "$dynamicScore% AI Match",
                color = if (dynamicScore >= 95) NeonEmerald else NeonCyan,
                fontSize = 11
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              UserAvatar(name = peer.name, size = 48.dp)
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = peer.name,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                Text(
                  text = "${peer.university} • ${peer.major}",
                  fontSize = 12.sp,
                  color = TextSecondaryDark
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Star, contentDescription = null, tint = AmberRating, modifier = Modifier.size(13.dp))
                  Spacer(modifier = Modifier.width(3.dp))
                  Text("${peer.rating} (${peer.reviewCount} reviews)", fontSize = 11.sp, color = AmberRating, fontWeight = FontWeight.Bold)
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = peer.bio,
              fontSize = 12.sp,
              color = TextSecondaryDark,
              lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Topics Can Explain
            Text(
              text = "Can Explain:",
              fontSize = 11.sp,
              color = VioletSecondary,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              peer.topicsCanExplain.take(2).forEach { topic ->
                GlowingPill(text = topic, color = VioletPrimary, fontSize = 9)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons (Chat & Voice Call)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = { onStartChatWithPeer(peer) },
                modifier = Modifier
                  .weight(1f)
                  .height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Start Chat", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }

              OutlinedButton(
                onClick = { onLaunchVoiceCallWithPeer(peer) },
                modifier = Modifier
                  .weight(1f)
                  .height(42.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp), tint = NeonCyan)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Voice Call", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}
