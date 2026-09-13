package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.model.Peer
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.components.UserAvatar
import com.example.ui.theme.*

@Composable
fun LeaderboardScreen(
  peers: List<Peer>,
  onRateHelper: (Peer) -> Unit,
  onOpenPeerProfile: (Peer) -> Unit
) {
  var isWeeklyTimeframe by remember { mutableStateOf(true) }

  // Sort peers by karma / rating
  val sortedPeers = peers.sortedByDescending { it.rating * 100 + it.reviewCount }

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
            text = "Collegiate Honor Roll",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Top student explainers ranked by verified learner feedback",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )
        }
        GlowingPill(text = "VERIFIED KARMA", color = AmberRating, fontSize = 10)
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Timeframe Switch: Weekly vs All-Time
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(ObsidianCardBg)
          .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
          .padding(4.dp)
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isWeeklyTimeframe) VioletPrimary else Color.Transparent)
            .clickable { isWeeklyTimeframe = true }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Weekly Sprint (Streak Bonus)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isWeeklyTimeframe) Color.White else TextSecondaryDark
          )
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(if (!isWeeklyTimeframe) VioletPrimary else Color.Transparent)
            .clickable { isWeeklyTimeframe = false }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "All-Time Scholars",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (!isWeeklyTimeframe) Color.White else TextSecondaryDark
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Podium for Top 3 (visual glory)
      if (sortedPeers.size >= 3) {
        val first = sortedPeers[0]
        val second = sortedPeers[1]
        val third = sortedPeers[2]

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.Bottom
        ) {
          // #2 Silver
          PodiumCard(
            peer = second,
            rank = 2,
            rankColor = Color(0xFFE2E8F0),
            modifier = Modifier.weight(1f).height(150.dp)
          )

          // #1 Gold
          PodiumCard(
            peer = first,
            rank = 1,
            rankColor = AmberRating,
            modifier = Modifier.weight(1.1f).height(175.dp)
          )

          // #3 Bronze
          PodiumCard(
            peer = third,
            rank = 3,
            rankColor = Color(0xFFCD7F32),
            modifier = Modifier.weight(1f).height(140.dp)
          )
        }

        Spacer(modifier = Modifier.height(20.dp))
      }

      // Ranking List
      LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(sortedPeers) { index, peer ->
          GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "#${index + 1}",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (index < 3) AmberRating else TextMutedDark,
                  modifier = Modifier.width(32.dp)
                )

                UserAvatar(name = peer.name, size = 40.dp)
                Spacer(modifier = Modifier.width(10.dp))

                Column {
                  Text(peer.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                  Text("${peer.university} • ${peer.major}", fontSize = 11.sp, color = TextSecondaryDark)
                }
              }

              Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Star, contentDescription = null, tint = AmberRating, modifier = Modifier.size(13.dp))
                  Spacer(modifier = Modifier.width(2.dp))
                  Text("${peer.rating}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AmberRating)
                }
                Text("${peer.reviewCount} reviews", fontSize = 10.sp, color = TextMutedDark)
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                peer.badges.forEach { badge ->
                  GlowingPill(text = badge, color = VioletSecondary, fontSize = 9)
                }
              }

              // Learner rate helper CTA
              TextButton(
                onClick = { onRateHelper(peer) },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
              ) {
                Icon(Icons.Default.RateReview, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Rate Helper", fontSize = 11.sp, color = NeonCyan)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun PodiumCard(
  peer: Peer,
  rank: Int,
  rankColor: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(
            VioletDeep.copy(alpha = 0.6f),
            ObsidianDarkSurface
          )
        )
      )
      .border(1.dp, rankColor.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
      .padding(8.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Box(
        modifier = Modifier
          .size(24.dp)
          .clip(CircleShape)
          .background(rankColor),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$rank",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = ObsidianBlack
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      UserAvatar(name = peer.name, size = 34.dp)
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = peer.name.split(" ").first(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
      Text(
        text = "${peer.rating} ⭐",
        fontSize = 11.sp,
        color = AmberRating,
        fontWeight = FontWeight.Bold
      )
    }
  }
}
