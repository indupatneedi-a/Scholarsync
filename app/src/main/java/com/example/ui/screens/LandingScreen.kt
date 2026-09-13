package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.components.Interactive3DKnowledgeGraph
import com.example.ui.theme.*

@Composable
fun LandingScreen(
  onStartLearning: () -> Unit,
  onFindPeer: () -> Unit,
  onLoginClick: () -> Unit
) {
  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 1100.dp)
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp)
        .padding(top = 32.dp, bottom = 40.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Navigation Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.linearGradient(
                  colors = listOf(VioletPrimary, VioletDeep)
                )
              )
              .border(1.dp, VioletSecondary, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AllInclusive,
              contentDescription = "KnowConnect Logo",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "know connect",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            letterSpacing = (-0.5).sp
          )
        }

        OutlinedButton(
          onClick = onLoginClick,
          colors = ButtonDefaults.outlinedButtonColors(contentColor = VioletSecondary),
          border = androidx.compose.foundation.BorderStroke(1.dp, VioletPrimary.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(50.dp),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
        ) {
          Text(
            text = "Sign In",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      Spacer(modifier = Modifier.height(36.dp))

      // Hero Tagline Badge
      GlowingPill(
        text = "THE NEXT-GEN PEER INTEL PLATFORM",
        color = NeonCyan,
        fontSize = 10
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Hero Headline
      Text(
        text = "Free peer learning\n+ premium AI\n+ optional paid tutoring.",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center,
        lineHeight = 38.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Hero Subheadline
      Text(
        text = "Friendly study buddy, adaptive matching, and smart help for college students.",
        fontSize = 15.sp,
        color = TextSecondaryDark,
        textAlign = TextAlign.Center,
        lineHeight = 22.sp,
        modifier = Modifier.padding(horizontal = 16.dp)
      )

      Spacer(modifier = Modifier.height(28.dp))

      // Hero CTAs
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Button(
          onClick = onStartLearning,
          modifier = Modifier
            .weight(1f)
            .height(52.dp),
          colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
          shape = RoundedCornerShape(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Start Learning",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = Color.White
            )
          }
        }

        OutlinedButton(
          onClick = onFindPeer,
          modifier = Modifier
            .weight(1f)
            .height(52.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, VioletSecondary.copy(alpha = 0.7f)),
          shape = RoundedCornerShape(16.dp)
        ) {
          Text(
            text = "Find a Peer",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Hero 3D Graphic Banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, VioletPrimary.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
      ) {
        Image(
          painter = painterResource(id = R.drawable.hero_knowledge_graph),
          contentDescription = "3D Knowledge Graph Visualization",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Transparent,
                  Color(0xCC07040D)
                )
              )
            )
            .padding(14.dp),
          contentAlignment = Alignment.BottomStart
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(NeonCyan)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Active Peer Neural Graph • 1,420+ College Guilds",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Interactive 3D Knowledge Core (Combining AI matching, skill graph, learning progress, topic relationship map, peer network, AI explanation diagram)
      Interactive3DKnowledgeGraph()

      Spacer(modifier = Modifier.height(32.dp))

      // Feature Highlights Glass Cards
      Text(
        text = "WHY STUDENTS LOVE KNOWCONNECT",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = VioletSecondary,
        letterSpacing = 1.2.sp,
        modifier = Modifier.align(Alignment.Start)
      )

      Spacer(modifier = Modifier.height(12.dp))

      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(VioletPrimary.copy(alpha = 0.2f))
              .border(1.dp, VioletPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Psychology,
              contentDescription = null,
              tint = VioletSecondary,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column {
            Text(
              text = "Intelligent AI Peer Matching",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Bi-directional algorithms match students by exact course syllabus, uploaded notes, and learning styles.",
              fontSize = 12.sp,
              color = TextSecondaryDark,
              lineHeight = 18.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(NeonCyan.copy(alpha = 0.15f))
              .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = NeonCyan,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column {
            Text(
              text = "Private-by-Default File Vault",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Upload problem sets, lecture PDFs, and code. Encrypted and shared only with peers you approve.",
              fontSize = 12.sp,
              color = TextSecondaryDark,
              lineHeight = 18.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(AmberRating.copy(alpha = 0.15f))
              .border(1.dp, AmberRating.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Grade,
              contentDescription = null,
              tint = AmberRating,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column {
            Text(
              text = "Learner-Only Reputation System",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Only students receiving help can rate mentors. Earn badges, study streaks, and collegiate leaderboard status.",
              fontSize = 12.sp,
              color = TextSecondaryDark,
              lineHeight = 18.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Bottom CTA Banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(
            Brush.linearGradient(
              colors = listOf(
                VioletDeep,
                Color(0xFF1B0C38)
              )
            )
          )
          .border(1.dp, VioletPrimary, RoundedCornerShape(20.dp))
          .padding(20.dp)
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "Join 48,000+ university peers today",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Free to join with college email or instant guest access.",
            fontSize = 13.sp,
            color = VioletTertiary,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = onStartLearning,
            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Enter Campus Hub", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
