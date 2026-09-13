package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import com.example.model.HelpRequest
import com.example.model.RequestStatus
import com.example.model.UrgencyLevel
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.theme.*

@Composable
fun RequestsScreen(
  requests: List<HelpRequest>,
  onCreateRequest: (topic: String, courseCode: String, desc: String, urgency: UrgencyLevel) -> Unit,
  onAcceptRequest: (String) -> Unit,
  onResolveRequest: (String) -> Unit,
  onOpenChatForRequest: (HelpRequest) -> Unit
) {
  var selectedStatusFilter by remember { mutableStateOf<RequestStatus?>(null) }
  var showNewRequestDialog by remember { mutableStateOf(false) }

  // New Request Form State
  var newTopic by remember { mutableStateOf("") }
  var newCourseCode by remember { mutableStateOf("") }
  var newDescription by remember { mutableStateOf("") }
  var newUrgency by remember { mutableStateOf(UrgencyLevel.MEDIUM) }

  val filteredRequests = requests.filter { req ->
    selectedStatusFilter == null || req.status == selectedStatusFilter
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
            text = "Peer Help Requests",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Request explanations or step up as a helper",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )
        }

        Button(
          onClick = { showNewRequestDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Post Request", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Status Filter Pills
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        val filterOptions = listOf(
          null to "All (${requests.size})",
          RequestStatus.OPEN to "Open",
          RequestStatus.IN_PROGRESS to "In Progress",
          RequestStatus.RESOLVED to "Resolved"
        )

        filterOptions.forEach { (status, label) ->
          val isSelected = selectedStatusFilter == status
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(if (isSelected) VioletPrimary.copy(alpha = 0.25f) else ObsidianCardBg)
              .border(1.dp, if (isSelected) VioletPrimary else ObsidianCardBorder, RoundedCornerShape(20.dp))
              .clickable { selectedStatusFilter = status }
              .padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text(
              text = label,
              fontSize = 11.sp,
              color = if (isSelected) Color.White else TextSecondaryDark,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Request Feed
      LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(filteredRequests) { req ->
          GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                GlowingPill(
                  text = req.courseCode,
                  color = VioletSecondary,
                  fontSize = 10
                )
                Spacer(modifier = Modifier.width(6.dp))
                GlowingPill(
                  text = req.urgency.label,
                  color = when (req.urgency) {
                    UrgencyLevel.EXAM_PREP -> Color(0xFFEF4444)
                    UrgencyLevel.HIGH -> AmberRating
                    else -> NeonCyan
                  },
                  fontSize = 10
                )
              }

              // Status Tag
              GlowingPill(
                text = req.status.label,
                color = when (req.status) {
                  RequestStatus.OPEN -> NeonEmerald
                  RequestStatus.IN_PROGRESS -> AmberRating
                  RequestStatus.RESOLVED -> Color.Gray
                },
                fontSize = 10
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = req.topic,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = req.description,
              fontSize = 13.sp,
              color = TextSecondaryDark,
              lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${req.requesterName} • ${req.requesterUniversity} • ${req.timestamp}",
                fontSize = 11.sp,
                color = TextMutedDark
              )

              req.acceptedByPeerName?.let { peerName ->
                Text(
                  text = "Assigned: $peerName",
                  fontSize = 11.sp,
                  color = NeonCyan,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              if (req.status == RequestStatus.OPEN) {
                Button(
                  onClick = {
                    onAcceptRequest(req.id)
                    onOpenChatForRequest(req)
                  },
                  modifier = Modifier.weight(1f),
                  colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Icon(Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Accept & Explain (+50 XP)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              } else if (req.status == RequestStatus.IN_PROGRESS) {
                Button(
                  onClick = { onOpenChatForRequest(req) },
                  modifier = Modifier.weight(1f),
                  colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Icon(Icons.Default.Chat, contentDescription = null, tint = ObsidianBlack, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Continue Chat", color = ObsidianBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                  onClick = { onResolveRequest(req.id) },
                  shape = RoundedCornerShape(10.dp),
                  border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianCardBorder)
                ) {
                  Text("Resolve ✓", fontSize = 12.sp, color = TextSecondaryDark)
                }
              } else {
                Text(
                  text = "Concept successfully resolved by peer session",
                  fontSize = 12.sp,
                  color = NeonEmerald
                )
              }
            }
          }
        }
      }
    }

    // New Help Request Dialog
    if (showNewRequestDialog) {
      AlertDialog(
        onDismissRequest = { showNewRequestDialog = false },
        title = { Text("Request Concept Explanation", color = Color.White) },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
              value = newTopic,
              onValueChange = { newTopic = it },
              label = { Text("Topic or Concept") },
              placeholder = { Text("e.g., Dijkstra negative weights proof") },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VioletPrimary,
                unfocusedBorderColor = ObsidianCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              ),
              modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
              value = newCourseCode,
              onValueChange = { newCourseCode = it },
              label = { Text("Course Code (Optional)") },
              placeholder = { Text("e.g., CS 170, CHEM 33") },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VioletPrimary,
                unfocusedBorderColor = ObsidianCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              ),
              modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
              value = newDescription,
              onValueChange = { newDescription = it },
              label = { Text("What specifically do you not understand?") },
              placeholder = { Text("Describe where you get stuck in the problem or mechanism...") },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VioletPrimary,
                unfocusedBorderColor = ObsidianCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              ),
              modifier = Modifier.fillMaxWidth()
            )

            Text("Urgency Level:", fontSize = 12.sp, color = TextSecondaryDark)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              UrgencyLevel.values().forEach { urg ->
                val isSelected = newUrgency == urg
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) VioletPrimary else ObsidianCardBg)
                    .clickable { newUrgency = urg }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                  Text(urg.label, fontSize = 10.sp, color = if (isSelected) Color.White else TextSecondaryDark)
                }
              }
            }
          }
        },
        confirmButton = {
          Button(
            onClick = {
              if (newTopic.isNotBlank()) {
                onCreateRequest(newTopic, newCourseCode, newDescription, newUrgency)
                showNewRequestDialog = false
                newTopic = ""
                newCourseCode = ""
                newDescription = ""
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
          ) {
            Text("Publish Request")
          }
        },
        dismissButton = {
          TextButton(onClick = { showNewRequestDialog = false }) {
            Text("Cancel", color = TextSecondaryDark)
          }
        },
        containerColor = ObsidianDarkSurface
      )
    }
  }
}
