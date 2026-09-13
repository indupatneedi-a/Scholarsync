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
import com.example.model.CommunityGroup
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.theme.*

@Composable
fun CommunitiesScreen(
  communities: List<CommunityGroup>,
  onToggleJoin: (String) -> Unit,
  onCreateCommunity: (name: String, university: String, category: String, desc: String, tags: List<String>) -> Unit,
  onEnterCommunityChat: (CommunityGroup) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var showCreateDialog by remember { mutableStateOf(false) }

  // New Group Dialog State
  var newName by remember { mutableStateOf("") }
  var newUniversity by remember { mutableStateOf("UC Berkeley") }
  var newCategory by remember { mutableStateOf("Computer Science") }
  var newDescription by remember { mutableStateOf("") }

  val filteredCommunities = communities.filter {
    searchQuery.isBlank() ||
      it.name.contains(searchQuery, ignoreCase = true) ||
      it.university.contains(searchQuery, ignoreCase = true) ||
      it.category.contains(searchQuery, ignoreCase = true) ||
      it.tags.any { tag -> tag.contains(searchQuery, ignoreCase = true) }
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
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Campus Study Guilds",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "College-specific & global student communities",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )
        }

        Button(
          onClick = { showCreateDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Create Guild", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Search Bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search colleges, subjects, or tags (e.g. EECS, Pre-Med)", fontSize = 13.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = VioletSecondary) },
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

      Spacer(modifier = Modifier.height(16.dp))

      // Communities List
      LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(filteredCommunities) { guild ->
          GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              GlowingPill(text = guild.university, color = VioletSecondary, fontSize = 10)
              GlowingPill(text = "${guild.memberCount} Members", color = NeonCyan, fontSize = 10)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = guild.name,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )

            Text(
              text = guild.category,
              fontSize = 12.sp,
              color = VioletSecondary,
              fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = guild.description,
              fontSize = 12.sp,
              color = TextSecondaryDark,
              lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tags
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              guild.tags.forEach { tag ->
                GlowingPill(text = "#$tag", color = VioletPrimary, fontSize = 9)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Row: Join toggle & Enter Lounge
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                onClick = { onToggleJoin(guild.id) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                  contentColor = if (guild.isJoined) NeonEmerald else Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (guild.isJoined) NeonEmerald else ObsidianCardBorder
                ),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(
                  imageVector = if (guild.isJoined) Icons.Default.Check else Icons.Default.GroupAdd,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (guild.isJoined) "Joined ✓" else "Join Guild", fontSize = 12.sp)
              }

              Button(
                onClick = { onEnterCommunityChat(guild) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                shape = RoundedCornerShape(10.dp)
              ) {
                Text("Open Lounge", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }

    // Create Community Dialog
    if (showCreateDialog) {
      AlertDialog(
        onDismissRequest = { showCreateDialog = false },
        title = { Text("Found a New Study Guild", color = Color.White) },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
              value = newName,
              onValueChange = { newName = it },
              label = { Text("Guild Name") },
              placeholder = { Text("e.g., Stanford Theory Circle") },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VioletPrimary,
                unfocusedBorderColor = ObsidianCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              ),
              modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
              value = newUniversity,
              onValueChange = { newUniversity = it },
              label = { Text("University or Collegiate Scope") },
              placeholder = { Text("e.g. UC Berkeley, Multi-Campus") },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VioletPrimary,
                unfocusedBorderColor = ObsidianCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              ),
              modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
              value = newCategory,
              onValueChange = { newCategory = it },
              label = { Text("Academic Field") },
              placeholder = { Text("e.g., Computer Science, Biochemistry") },
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
              label = { Text("Purpose & Study Goals") },
              placeholder = { Text("Weekly problem set reviews, exam prep...") },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VioletPrimary,
                unfocusedBorderColor = ObsidianCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              ),
              modifier = Modifier.fillMaxWidth()
            )
          }
        },
        confirmButton = {
          Button(
            onClick = {
              if (newName.isNotBlank()) {
                onCreateCommunity(newName, newUniversity, newCategory, newDescription, listOf("PeerStudy", "Exams"))
                showCreateDialog = false
                newName = ""
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
          ) {
            Text("Create Guild")
          }
        },
        dismissButton = {
          TextButton(onClick = { showCreateDialog = false }) {
            Text("Cancel", color = TextSecondaryDark)
          }
        },
        containerColor = ObsidianDarkSurface
      )
    }
  }
}
