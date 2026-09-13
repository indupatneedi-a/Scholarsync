package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.components.UserAvatar
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
  user: UserProfile,
  onOpenSettings: () -> Unit,
  onManageAccount: () -> Unit = {}
) {
  val scrollState = rememberScrollState()

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
            text = "Scholar Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Collegiate trust verification & merit credentials",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          IconButton(
            onClick = onManageAccount,
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(VioletPrimary.copy(alpha = 0.2f))
              .border(1.dp, VioletSecondary.copy(alpha = 0.5f), CircleShape)
          ) {
            Icon(Icons.Default.AccountCircle, contentDescription = "Manage Account", tint = VioletSecondary)
          }

          IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(ObsidianCardBg)
              .border(1.dp, ObsidianCardBorder, CircleShape)
          ) {
            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Main Profile Glass Card
      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          UserAvatar(name = user.name, size = 64.dp)
          Spacer(modifier = Modifier.width(16.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = user.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(6.dp))
              if (user.isVerified) {
                Icon(
                  imageVector = Icons.Default.Verified,
                  contentDescription = "Verified College Student",
                  tint = NeonCyan,
                  modifier = Modifier.size(18.dp)
                )
              }
            }

            Text(
              text = "${user.university} • Class of '26",
              fontSize = 13.sp,
              color = VioletSecondary
            )
            Text(
              text = user.major,
              fontSize = 12.sp,
              color = TextSecondaryDark
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(user.bio, fontSize = 13.sp, color = TextPrimaryDark, lineHeight = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Matrix: Level, Streak, Rating, Karma
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("LEVEL", fontSize = 10.sp, color = TextMutedDark, fontWeight = FontWeight.Bold)
            Text("Lvl ${user.level}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("STREAK", fontSize = 10.sp, color = TextMutedDark, fontWeight = FontWeight.Bold)
            Text("🔥 ${user.streakDays}d", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFB923C))
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("RATING", fontSize = 10.sp, color = TextMutedDark, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Star, contentDescription = null, tint = AmberRating, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(2.dp))
              Text("${user.rating}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AmberRating)
            }
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("KARMA", fontSize = 10.sp, color = TextMutedDark, fontWeight = FontWeight.Bold)
            Text("⚡ ${user.karmaPoints}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NeonEmerald)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Badges Showcase
      Text("EARNED SCHOLAR BADGES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletSecondary, letterSpacing = 1.sp)
      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        user.badges.forEach { badge ->
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(14.dp))
              .background(ObsidianCardBg)
              .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
              .padding(10.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(badge.icon, fontSize = 24.sp)
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = badge.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Topics Breakdown: Can Explain VS Need Help
      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Text("Topics I Can Explain (Helper)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NeonEmerald)
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          for (topic in user.topicsCanExplain) {
            GlowingPill(text = topic, color = NeonEmerald, fontSize = 10)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("Topics I Need Help With (Learner)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VioletSecondary)
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          for (topic in user.topicsNeedHelp) {
            GlowingPill(text = topic, color = VioletPrimary, fontSize = 10)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Reviews From Learners
      Text("VERIFIED LEARNER ENDORSEMENTS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletSecondary, letterSpacing = 1.sp)
      Spacer(modifier = Modifier.height(10.dp))

      for (review in user.reviewsFromLearners) {
        GlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${review.learnerName} • ${review.courseTopic}",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Row {
              repeat(review.stars) {
                Icon(Icons.Default.Star, contentDescription = null, tint = AmberRating, modifier = Modifier.size(14.dp))
              }
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(review.comment, fontSize = 12.sp, color = TextSecondaryDark, lineHeight = 16.sp)

          Spacer(modifier = Modifier.height(8.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            for (tag in review.tags) {
              GlowingPill(text = tag, color = VioletPrimary, fontSize = 9)
            }
          }
        }
      }
    }
  }
}
