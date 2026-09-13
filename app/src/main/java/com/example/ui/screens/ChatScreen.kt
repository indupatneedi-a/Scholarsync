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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.model.ChatMessage
import com.example.model.MessageType
import com.example.model.Peer
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ChatScreen(
  activePeer: Peer,
  messages: List<ChatMessage>,
  onSendMessage: (text: String, type: MessageType, fileName: String?, fileSize: String?, linkUrl: String?, voiceDurationSec: Int?) -> Unit,
  onLaunchVoiceCall: () -> Unit,
  onRateHelper: (Peer) -> Unit,
  onClearChatHistory: () -> Unit,
  onBack: () -> Unit
) {
  var inputDraft by remember { mutableStateOf("") }
  var isRecordingVoice by remember { mutableStateOf(false) }
  var showShareLinkDialog by remember { mutableStateOf(false) }
  var linkInput by remember { mutableStateOf("https://docs.google.com/document/d/cs170-study-notes") }
  var showAttachFileDialog by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
    ) {
      // Chat Top App Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(ObsidianDarkSurface)
          .border(0.5.dp, ObsidianCardBorder)
          .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          UserAvatar(name = activePeer.name, size = 40.dp)
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = activePeer.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(6.dp))
              StatusBadge(isOnline = activePeer.isOnline)
            }
            Text(
              text = "${activePeer.university} • ${activePeer.major.take(20)}...",
              fontSize = 11.sp,
              color = TextSecondaryDark
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Voice Call CTA
          IconButton(
            onClick = onLaunchVoiceCall,
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(VioletPrimary.copy(alpha = 0.2f))
              .border(1.dp, VioletPrimary, CircleShape)
          ) {
            Icon(Icons.Default.Call, contentDescription = "Voice Call", tint = VioletSecondary, modifier = Modifier.size(18.dp))
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Rate Helper CTA (Learner feedback)
          IconButton(
            onClick = { onRateHelper(activePeer) },
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(AmberRating.copy(alpha = 0.15f))
              .border(1.dp, AmberRating.copy(alpha = 0.4f), CircleShape)
          ) {
            Icon(Icons.Default.Star, contentDescription = "Rate Helper", tint = AmberRating, modifier = Modifier.size(18.dp))
          }
        }
      }

      // AI Memory Banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFF110822))
          .padding(horizontal = 16.dp, vertical = 6.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Memory, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "AI Study Memory active: Prior chats & syllabus retained",
              fontSize = 11.sp,
              color = NeonCyan
            )
          }
          Text(
            text = "Clear Memory",
            fontSize = 10.sp,
            color = TextMutedDark,
            modifier = Modifier.clickable { onClearChatHistory() }
          )
        }
      }

      // Messages List
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 14.dp),
        reverseLayout = false,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
      ) {
        items(messages) { msg ->
          MessageBubble(msg)
        }
      }

      // Communication Modes Input Bar
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(ObsidianDarkSurface)
          .border(0.5.dp, ObsidianCardBorder)
          .padding(horizontal = 12.dp, vertical = 8.dp)
      ) {
        // Quick attach buttons row: Voice Note, File, Link
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Voice Note Mode Toggle
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (isRecordingVoice) Color(0xFFEF4444).copy(alpha = 0.2f) else ObsidianCardBg)
              .border(1.dp, if (isRecordingVoice) Color(0xFFEF4444) else ObsidianCardBorder, RoundedCornerShape(8.dp))
              .clickable { isRecordingVoice = !isRecordingVoice }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Mic, contentDescription = null, tint = if (isRecordingVoice) Color(0xFFEF4444) else VioletSecondary, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isRecordingVoice) "Recording..." else "Voice Note",
                fontSize = 11.sp,
                color = if (isRecordingVoice) Color(0xFFEF4444) else TextSecondaryDark
              )
            }
          }

          // Attach File
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(ObsidianCardBg)
              .border(1.dp, ObsidianCardBorder, RoundedCornerShape(8.dp))
              .clickable { showAttachFileDialog = true }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AttachFile, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Upload File", fontSize = 11.sp, color = TextSecondaryDark)
            }
          }

          // Share Link
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(ObsidianCardBg)
              .border(1.dp, ObsidianCardBorder, RoundedCornerShape(8.dp))
              .clickable { showShareLinkDialog = true }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Link, contentDescription = null, tint = AmberRating, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Share Link", fontSize = 11.sp, color = TextSecondaryDark)
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isRecordingVoice) {
          // Voice Recording Bar with Simulated Waveform
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFF1E0A1A))
              .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
              .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEF4444)))
              Spacer(modifier = Modifier.width(8.dp))
              Text("00:14 • Recording audio waveform...", fontSize = 12.sp, color = Color.White)
            }
            Button(
              onClick = {
                onSendMessage("Recorded voice note (34s)", MessageType.VOICE_NOTE, null, null, null, 34)
                isRecordingVoice = false
              },
              colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
              Text("Send Audio", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        } else {
          // Text Input Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = inputDraft,
              onValueChange = { inputDraft = it },
              placeholder = { Text("Ask a concept, e.g., 'How does raft leader election work?'", fontSize = 12.sp) },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VioletPrimary,
                unfocusedBorderColor = ObsidianCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              ),
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(20.dp),
              singleLine = false,
              maxLines = 3
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
              onClick = {
                if (inputDraft.isNotBlank()) {
                  onSendMessage(inputDraft, MessageType.TEXT, null, null, null, null)
                  inputDraft = ""
                }
              },
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(VioletPrimary)
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }
    }

    // Attach File Dialog
    if (showAttachFileDialog) {
      AlertDialog(
        onDismissRequest = { showAttachFileDialog = false },
        title = { Text("Attach File to Peer Discussion", color = Color.White) },
        text = {
          Column {
            Text("Select file from your private vault to share securely with ${activePeer.name}:", fontSize = 13.sp, color = TextSecondaryDark)
            Spacer(modifier = Modifier.height(10.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(ObsidianCardBg)
                .border(1.dp, ObsidianCardBorder, RoundedCornerShape(10.dp))
                .clickable {
                  onSendMessage("Shared problem set notes", MessageType.FILE, "CS170_Midterm2_StudyGuide.pdf", "4.2 MB", null, null)
                  showAttachFileDialog = false
                }
                .padding(12.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = null, tint = VioletSecondary)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text("CS170_Midterm2_StudyGuide.pdf", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                  Text("4.2 MB • End-to-end encrypted sharing", fontSize = 10.sp, color = TextSecondaryDark)
                }
              }
            }
          }
        },
        confirmButton = {
          TextButton(onClick = { showAttachFileDialog = false }) {
            Text("Cancel", color = VioletSecondary)
          }
        },
        containerColor = ObsidianDarkSurface
      )
    }

    // Share Link Dialog
    if (showShareLinkDialog) {
      AlertDialog(
        onDismissRequest = { showShareLinkDialog = false },
        title = { Text("Share Study Link", color = Color.White) },
        text = {
          Column {
            Text("Share a collaborative whiteboard, Google Doc, or GitHub repo:", fontSize = 13.sp, color = TextSecondaryDark)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = linkInput,
              onValueChange = { linkInput = it },
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VioletPrimary,
                unfocusedBorderColor = ObsidianCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              )
            )
          }
        },
        confirmButton = {
          Button(
            onClick = {
              onSendMessage("Collaborative Study Resource", MessageType.LINK, null, null, linkInput, null)
              showShareLinkDialog = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
          ) {
            Text("Share Link")
          }
        },
        dismissButton = {
          TextButton(onClick = { showShareLinkDialog = false }) {
            Text("Cancel", color = TextSecondaryDark)
          }
        },
        containerColor = ObsidianDarkSurface
      )
    }
  }
}

@Composable
fun MessageBubble(msg: ChatMessage) {
  val isUser = msg.isUser
  val isAi = msg.type == MessageType.AI_SUMMARY

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = if (isAi) Alignment.CenterHorizontally else if (isUser) Alignment.End else Alignment.Start
  ) {
    if (isAi) {
      // AI Study Buddy Callout Card
      Box(
        modifier = Modifier
          .fillMaxWidth(0.95f)
          .clip(RoundedCornerShape(16.dp))
          .background(
            Brush.linearGradient(
              colors = listOf(
                Color(0xFF1B0B36),
                Color(0xFF100722)
              )
            )
          )
          .border(1.dp, VioletPrimary.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
          .padding(14.dp)
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = VioletSecondary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("AI STUDY BUDDY INSIGHT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VioletSecondary, letterSpacing = 1.sp)
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(msg.text, fontSize = 13.sp, color = Color.White, lineHeight = 18.sp)
          msg.aiInsight?.let {
            Spacer(modifier = Modifier.height(6.dp))
            Text(it, fontSize = 11.sp, color = NeonCyan)
          }
        }
      }
    } else {
      Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
      ) {
        if (!isUser) {
          UserAvatar(name = msg.senderName, size = 28.dp)
          Spacer(modifier = Modifier.width(6.dp))
        }

        Box(
          modifier = Modifier
            .widthIn(max = 280.dp)
            .clip(
              RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
              )
            )
            .background(if (isUser) VioletPrimary else ObsidianCardBg)
            .border(1.dp, if (isUser) VioletSecondary.copy(alpha = 0.4f) else ObsidianCardBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
        ) {
          Column {
            when (msg.type) {
              MessageType.VOICE_NOTE -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.PlayCircleFilled, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(28.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text("Voice Note (${msg.voiceDurationSec ?: 30}s)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("||| | |||| | ||||| | ||", fontSize = 10.sp, color = if (isUser) VioletTertiary else NeonCyan)
                  }
                }
              }
              MessageType.FILE -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.InsertDriveFile, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(msg.fileName ?: "Vault Document", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(msg.fileSize ?: "Encrypted File", fontSize = 10.sp, color = VioletTertiary)
                  }
                }
              }
              MessageType.LINK -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Link, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(msg.linkUrl ?: "Shared Link", fontSize = 12.sp, color = Color.White, textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)
                }
              }
              else -> {
                Text(msg.text, fontSize = 13.sp, color = Color.White, lineHeight = 18.sp)
              }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = msg.timestamp,
              fontSize = 10.sp,
              color = if (isUser) VioletTertiary else TextMutedDark,
              modifier = Modifier.align(Alignment.End)
            )
          }
        }
      }
    }
  }
}
