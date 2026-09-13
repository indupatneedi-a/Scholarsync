package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
  onComplete: () -> Unit
) {
  val scrollState = rememberScrollState()

  var selectedCollege by remember { mutableStateOf("UC Berkeley") }
  var selectedMajor by remember { mutableStateOf("Computer Science & Cognitive AI") }

  val popularColleges = listOf("UC Berkeley", "Stanford University", "MIT", "Carnegie Mellon", "Harvard University", "Georgia Tech")
  val topicsCatalog = listOf(
    "Graph Algorithms", "Linear Algebra", "Dynamic Programming",
    "Distributed Systems", "Quantum Gates", "Organic Synthesis",
    "Macroeconomics", "Biochemistry", "Operating Systems", "React / Compose UI"
  )

  val canExplainTopics = remember { mutableStateListOf("Graph Algorithms", "Linear Algebra", "Dynamic Programming") }
  val needHelpTopics = remember { mutableStateListOf("Quantum Gates", "Distributed Systems", "Organic Synthesis") }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 680.dp)
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp)
        .padding(top = 44.dp, bottom = 32.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      GlowingPill(
        text = "STUDENT PROFILE SYNCHRONIZATION",
        color = NeonCyan,
        fontSize = 10
      )

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Personalize Your Peer Hub",
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
      Text(
        text = "KnowConnect AI matches you with campus peers based on exact course overlap.",
        fontSize = 13.sp,
        color = TextSecondaryDark,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
      )

      Spacer(modifier = Modifier.height(20.dp))

      // University Selection
      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.School, contentDescription = null, tint = VioletSecondary)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Select Your College / University", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          popularColleges.forEach { college ->
            val isSelected = selectedCollege == college
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) VioletPrimary.copy(alpha = 0.25f) else ObsidianDarkSurface)
                .border(1.dp, if (isSelected) VioletPrimary else ObsidianCardBorder, RoundedCornerShape(10.dp))
                .clickable { selectedCollege = college }
                .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(college, color = if (isSelected) Color.White else TextSecondaryDark, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                if (isSelected) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = VioletSecondary, modifier = Modifier.size(16.dp))
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Topics I Can Explain (Helper side)
      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Text("Topics I Can Explain (Earn Karma & Ratings)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Select concepts you feel confident breaking down for peers:", fontSize = 12.sp, color = TextSecondaryDark, modifier = Modifier.padding(vertical = 4.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          topicsCatalog.take(5).forEach { topic ->
            val isSelected = canExplainTopics.contains(topic)
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) NeonEmerald.copy(alpha = 0.2f) else ObsidianDarkSurface)
                .border(1.dp, if (isSelected) NeonEmerald else ObsidianCardBorder, RoundedCornerShape(10.dp))
                .clickable {
                  if (isSelected) canExplainTopics.remove(topic) else canExplainTopics.add(topic)
                }
                .padding(horizontal = 14.dp, vertical = 9.dp)
            ) {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(topic, color = if (isSelected) Color.White else TextSecondaryDark, fontSize = 13.sp)
                if (isSelected) Text("Helper Ready ✓", color = NeonEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Topics I Need Help With (Learner side)
      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Text("Topics I Need Help Understanding", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("AI will instantly prioritize matching with verified mentors in these fields:", fontSize = 12.sp, color = TextSecondaryDark, modifier = Modifier.padding(vertical = 4.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          topicsCatalog.takeLast(5).forEach { topic ->
            val isSelected = needHelpTopics.contains(topic)
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) VioletPrimary.copy(alpha = 0.25f) else ObsidianDarkSurface)
                .border(1.dp, if (isSelected) VioletPrimary else ObsidianCardBorder, RoundedCornerShape(10.dp))
                .clickable {
                  if (isSelected) needHelpTopics.remove(topic) else needHelpTopics.add(topic)
                }
                .padding(horizontal = 14.dp, vertical = 9.dp)
            ) {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(topic, color = if (isSelected) Color.White else TextSecondaryDark, fontSize = 13.sp)
                if (isSelected) Text("Priority Match ★", color = VioletSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = onComplete,
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
        shape = RoundedCornerShape(14.dp)
      ) {
        Text("Launch My Campus Dashboard", fontSize = 15.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}
