package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
  user: UserProfile,
  peers: List<Peer>,
  requests: List<HelpRequest>,
  uploadedFiles: List<UploadedFileItem>,
  communities: List<CommunityGroup>,
  chatMessages: List<ChatMessage>,
  learningProgress: List<LearningSubjectProgress>,
  unreadNotificationCount: Int,
  onNavigateToMatching: () -> Unit,
  onNavigateToUpload: () -> Unit,
  onNavigateToRequests: () -> Unit,
  onNavigateToChat: () -> Unit,
  onNavigateToCommunities: () -> Unit,
  onNavigateToHistory: () -> Unit,
  onNavigateToNotifications: () -> Unit,
  onNavigateToLeaderboard: () -> Unit,
  onNavigateToProfile: () -> Unit
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
      // Top Campus Status Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { onNavigateToProfile() }
        ) {
          UserAvatar(name = user.name, size = 46.dp)
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = user.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = "Verified Student",
                tint = NeonCyan,
                modifier = Modifier.size(16.dp)
              )
            }
            Text(
              text = "${user.university} • Lvl ${user.level}",
              fontSize = 12.sp,
              color = TextSecondaryDark
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Streak Counter
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(30.dp))
              .background(Color(0xFF261208))
              .border(1.dp, Color(0xFFF97316).copy(alpha = 0.5f), RoundedCornerShape(30.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(text = "🔥 ${user.streakDays}d", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFB923C))
          }

          // Karma Points
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(30.dp))
              .background(VioletDeep.copy(alpha = 0.4f))
              .border(1.dp, VioletPrimary.copy(alpha = 0.5f), RoundedCornerShape(30.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(text = "⚡ ${user.karmaPoints} XP", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletTertiary)
          }

          // Notification Bell
          IconButton(
            onClick = onNavigateToNotifications,
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(ObsidianCardBg)
              .border(1.dp, ObsidianCardBorder, CircleShape)
          ) {
            Box {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
              if (unreadNotificationCount > 0) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEF4444))
                    .align(Alignment.TopEnd)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Action Quick Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(
              Brush.linearGradient(
                colors = listOf(VioletPrimary, VioletDeep)
              )
            )
            .clickable { onNavigateToMatching() }
            .padding(14.dp)
        ) {
          Column {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text("Find a Peer", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("AI Smart Match", fontSize = 11.sp, color = VioletTertiary)
          }
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianCardBg)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(16.dp))
            .clickable { onNavigateToUpload() }
            .padding(14.dp)
        ) {
          Column {
            Icon(Icons.Default.CloudUpload, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text("Upload File", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Extract Topics", fontSize = 11.sp, color = TextSecondaryDark)
          }
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianCardBg)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(16.dp))
            .clickable { onNavigateToRequests() }
            .padding(14.dp)
        ) {
          Column {
            Icon(Icons.Default.AddComment, contentDescription = null, tint = AmberRating, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text("Request Help", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Post Question", fontSize = 11.sp, color = TextSecondaryDark)
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // 1. Matched Peers & Recommended Helpers
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("RECOMMENDED PEER HELPERS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletSecondary, letterSpacing = 1.sp)
        Text(
          text = "View All",
          fontSize = 12.sp,
          color = NeonCyan,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.clickable { onNavigateToMatching() }
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(peers) { peer ->
          GlassCard(
            modifier = Modifier
              .width(220.dp)
              .clickable { onNavigateToChat() }
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              StatusBadge(isOnline = peer.isOnline)
              GlowingPill(text = "${peer.matchScore}% Match", color = NeonCyan, fontSize = 10)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              UserAvatar(name = peer.name, size = 38.dp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(peer.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(peer.university, fontSize = 11.sp, color = TextSecondaryDark)
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Star, contentDescription = null, tint = AmberRating, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(3.dp))
              Text("${peer.rating} (${peer.reviewCount})", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
              Spacer(modifier = Modifier.width(8.dp))
              Text("• ${peer.hourlyRate}", fontSize = 11.sp, color = NeonEmerald)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = peer.topicsCanExplain.firstOrNull() ?: "General CS",
              fontSize = 11.sp,
              color = VioletSecondary,
              maxLines = 1
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // 2. Active Help Requests
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("ACTIVE HELP REQUESTS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletSecondary, letterSpacing = 1.sp)
        Text(
          text = "Explore Requests",
          fontSize = 12.sp,
          color = NeonCyan,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.clickable { onNavigateToRequests() }
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      requests.take(2).forEach { req ->
        GlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onNavigateToRequests() }
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            GlowingPill(
              text = req.courseCode,
              color = if (req.urgency == UrgencyLevel.EXAM_PREP) Color(0xFFEF4444) else VioletPrimary,
              fontSize = 10
            )
            GlowingPill(text = req.status.label, color = NeonCyan, fontSize = 10)
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(req.topic, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
          Text(req.description, fontSize = 12.sp, color = TextSecondaryDark, maxLines = 2)

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("${req.requesterName} • ${req.timestamp}", fontSize = 11.sp, color = TextMutedDark)
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.QuestionAnswer, contentDescription = null, tint = VioletSecondary, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("${req.repliesCount} replies", fontSize = 11.sp, color = Color.White)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // 3. Learning Progress Visualization
      Text("LEARNING PROGRESS & RETENTION", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletSecondary, letterSpacing = 1.sp)
      Spacer(modifier = Modifier.height(10.dp))

      GlassCard(modifier = Modifier.fillMaxWidth()) {
        learningProgress.forEach { item ->
          Column(modifier = Modifier.padding(vertical = 6.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(item.subject, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
              Text("${(item.progressPercentage * 100).toInt()}% Mastery", fontSize = 12.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
              progress = { item.progressPercentage },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(6.dp)),
              color = VioletPrimary,
              trackColor = ObsidianBlack
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // 4. Recent Uploads (Private Vault)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text("RECENT UPLOADS (PRIVATE VAULT)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletSecondary, letterSpacing = 1.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Icon(Icons.Default.Lock, contentDescription = "Private", tint = NeonCyan, modifier = Modifier.size(14.dp))
        }
        Text("Uploads", fontSize = 12.sp, color = NeonCyan, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { onNavigateToUpload() })
      }

      Spacer(modifier = Modifier.height(10.dp))

      uploadedFiles.take(2).forEach { file ->
        GlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onNavigateToUpload() }
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Description, contentDescription = null, tint = VioletSecondary, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(file.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                Text("${file.type} • ${file.size}", fontSize = 11.sp, color = TextSecondaryDark)
              }
            }
            GlowingPill(text = if (file.isPrivate) "Private" else "Shared", color = if (file.isPrivate) Color.Gray else NeonCyan, fontSize = 9)
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text("Extracted: ${file.extractedTopics.joinToString(" • ")}", fontSize = 11.sp, color = VioletSecondary, maxLines = 1)
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // 5. Chat History & Communities quick links
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        GlassCard(
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToChat() }
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Forum, contentDescription = null, tint = VioletSecondary)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Chat History", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text("Active with Sophia Chen", fontSize = 11.sp, color = TextSecondaryDark)
          Text("Tap to enter voice/text", fontSize = 11.sp, color = NeonCyan)
        }

        GlassCard(
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToCommunities() }
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Groups, contentDescription = null, tint = NeonEmerald)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Communities", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text("${communities.filter { it.isJoined }.size} Guilds Joined", fontSize = 11.sp, color = TextSecondaryDark)
          Text("UC Berkeley EECS +", fontSize = 11.sp, color = NeonEmerald)
        }
      }
    }
  }
}
