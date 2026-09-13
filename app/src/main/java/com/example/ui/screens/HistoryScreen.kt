package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.*
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.theme.*

enum class HistoryTab(val title: String) {
  FILES("Files Vault"),
  REQUESTS("My Requests"),
  ACCEPTED_STUDENTS("Accepted By"),
  CHATS("Chats & AI Memory"),
  MATCHES("Peer Matches")
}

@Composable
fun HistoryScreen(
  files: List<UploadedFileItem>,
  requests: List<HelpRequest>,
  peers: List<Peer>,
  chatMessages: List<ChatMessage>,
  onDeleteFile: (String) -> Unit,
  onClearChat: () -> Unit,
  onOpenPeerChat: (Peer) -> Unit
) {
  var selectedTab by remember { mutableStateOf(HistoryTab.FILES) }
  var searchQuery by remember { mutableStateOf("") }

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
      // Top Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Learning History",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "All study assets, peer connections, and sessions",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )
        }

        GlowingPill(text = "FULL ARCHIVE", color = VioletSecondary, fontSize = 10)
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Horizontal Tabs (scrollable or wrapped)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        HistoryTab.values().take(3).forEach { tab ->
          val isSelected = selectedTab == tab
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (isSelected) VioletPrimary else ObsidianCardBg)
              .border(1.dp, if (isSelected) Color.White.copy(alpha = 0.5f) else ObsidianCardBorder, RoundedCornerShape(10.dp))
              .clickable { selectedTab = tab }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = tab.title,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = if (isSelected) Color.White else TextSecondaryDark,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        HistoryTab.values().takeLast(2).forEach { tab ->
          val isSelected = selectedTab == tab
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (isSelected) VioletPrimary else ObsidianCardBg)
              .border(1.dp, if (isSelected) Color.White.copy(alpha = 0.5f) else ObsidianCardBorder, RoundedCornerShape(10.dp))
              .clickable { selectedTab = tab }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = tab.title,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = if (isSelected) Color.White else TextSecondaryDark,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Tab Content
      when (selectedTab) {
        HistoryTab.FILES -> {
          // Uploaded Files Archive
          LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(files) { file ->
              GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = VioletSecondary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                      Text(file.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                      Text("${file.size} • ${file.uploadDate}", fontSize = 11.sp, color = TextSecondaryDark)
                    }
                  }

                  IconButton(onClick = { onDeleteFile(file.id) }) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = TextSecondaryDark, modifier = Modifier.size(18.dp))
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("Extracted: ${file.extractedTopics.joinToString(", ")}", fontSize = 11.sp, color = NeonCyan)
                  GlowingPill(text = if (file.isPrivate) "Private Vault" else "Shared", color = if (file.isPrivate) Color.Gray else NeonEmerald, fontSize = 9)
                }
              }
            }
          }
        }

        HistoryTab.REQUESTS -> {
          // Help Requests Archive
          LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(requests) { req ->
              GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  GlowingPill(text = req.courseCode, color = VioletSecondary, fontSize = 10)
                  GlowingPill(text = req.status.label, color = NeonCyan, fontSize = 10)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(req.topic, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(req.description, fontSize = 12.sp, color = TextSecondaryDark)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Created: ${req.timestamp} • ${req.repliesCount} peer replies", fontSize = 11.sp, color = TextMutedDark)
              }
            }
          }
        }

        HistoryTab.ACCEPTED_STUDENTS -> {
          // Students who accepted my requests
          val acceptedPeers = peers.filter { it.rating >= 4.8 }
          LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(acceptedPeers) { peer ->
              GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text(peer.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Accepted request on Distributed Systems", fontSize = 11.sp, color = NeonEmerald)
                  }
                  Button(
                    onClick = { onOpenPeerChat(peer) },
                    colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                  ) {
                    Text("Chat", fontSize = 11.sp)
                  }
                }
              }
            }
          }
        }

        HistoryTab.CHATS -> {
          // Chats & AI Memory
          Column {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text("AI Context Memory", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                  Text("${chatMessages.size} messages indexed in neural context", fontSize = 11.sp, color = TextSecondaryDark)
                }

                OutlinedButton(
                  onClick = onClearChat,
                  shape = RoundedCornerShape(8.dp),
                  border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f))
                ) {
                  Text("Clear History", fontSize = 11.sp, color = Color(0xFFEF4444))
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              items(chatMessages) { msg ->
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(ObsidianCardBg)
                    .border(1.dp, ObsidianCardBorder, RoundedCornerShape(10.dp))
                    .padding(10.dp)
                ) {
                  Column {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                      Text(msg.senderName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletSecondary)
                      Text(msg.timestamp, fontSize = 10.sp, color = TextMutedDark)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(msg.text, fontSize = 12.sp, color = Color.White)
                  }
                }
              }
            }
          }
        }

        HistoryTab.MATCHES -> {
          // Saved Peer Matches
          LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(peers) { peer ->
              GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text(peer.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("${peer.university} • ${peer.major}", fontSize = 11.sp, color = TextSecondaryDark)
                  }
                  GlowingPill(text = "${peer.matchScore}% Match", color = NeonCyan, fontSize = 10)
                }
              }
            }
          }
        }
      }
    }
  }
}
