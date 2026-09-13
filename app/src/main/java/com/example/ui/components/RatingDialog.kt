package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
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
import com.example.ui.theme.*

@Composable
fun LearnerRatingDialog(
  helperName: String,
  onDismiss: () -> Unit,
  onSubmitRating: (stars: Int, tags: List<String>, comment: String) -> Unit
) {
  var stars by remember { mutableIntStateOf(5) }
  val availableTags = listOf(
    "Crystal Clear",
    "Super Patient",
    "Genius Explainer",
    "Exam Lifesaver",
    "Zero Jargon",
    "Fast Responder"
  )
  val selectedTags = remember { mutableStateListOf("Crystal Clear", "Super Patient") }
  var comment by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .background(ObsidianDarkSurface)
        .border(1.dp, ObsidianCardBorder, RoundedCornerShape(24.dp))
        .padding(24.dp)
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        GlowingPill(
          text = "LEARNER FEEDBACK ONLY",
          color = NeonCyan,
          fontSize = 10
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Rate $helperName",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = "Learners grade peer helpers to foster verified collegiate trust.",
          fontSize = 12.sp,
          color = TextSecondaryDark,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Star Rating Row
        Row(
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          for (i in 1..5) {
            val isSelected = i <= stars
            IconButton(
              onClick = { stars = i },
              modifier = Modifier.size(44.dp)
            ) {
              Icon(
                imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "$i Stars",
                tint = if (isSelected) AmberRating else Color.Gray,
                modifier = Modifier.size(34.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Badge / Feedback Tag chips
        Text(
          text = "Choose peer merit endorsements:",
          fontSize = 12.sp,
          color = TextSecondaryDark,
          modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Wrap tag chips
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          availableTags.chunked(2).forEach { rowTags ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              rowTags.forEach { tag ->
                val isSelected = selectedTags.contains(tag)
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) VioletPrimary.copy(alpha = 0.25f) else ObsidianCardBg)
                    .border(
                      1.dp,
                      if (isSelected) VioletPrimary else ObsidianCardBorder,
                      RoundedCornerShape(20.dp)
                    )
                    .clickable {
                      if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                    }
                    .padding(vertical = 8.dp, horizontal = 10.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = tag,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else TextSecondaryDark
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = comment,
          onValueChange = { comment = it },
          placeholder = { Text("What made this explanation helpful? (Optional)", fontSize = 12.sp) },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VioletPrimary,
            unfocusedBorderColor = ObsidianCardBorder,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          ),
          modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondaryDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianCardBorder)
          ) {
            Text("Cancel")
          }

          Button(
            onClick = {
              onSubmitRating(stars, selectedTags.toList(), comment)
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
          ) {
            Text("Submit +XP", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
