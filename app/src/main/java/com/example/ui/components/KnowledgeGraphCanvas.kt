package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

enum class GraphVisualizationMode(val title: String, val subtitle: String) {
  AI_MATCHING("AI Matching", "Bi-directional similarity mapping"),
  SKILL_GRAPH("Skill Graph", "Student concept mastery & prerequisites"),
  PROGRESS("Learning Progress", "Real-time retention & peer sprints"),
  TOPIC_MAP("Topic Relationship", "Cross-disciplinary concept nodes"),
  PEER_NETWORK("Peer Network", "Collegiate study guild topology"),
  EXPLANATION_DIAGRAM("AI Explanation", "Hierarchical concept decomposition")
}

@Composable
fun Interactive3DKnowledgeGraph(
  modifier: Modifier = Modifier,
  initialMode: GraphVisualizationMode = GraphVisualizationMode.AI_MATCHING
) {
  var selectedMode by remember { mutableStateOf(initialMode) }

  val infiniteTransition = rememberInfiniteTransition(label = "graph_rotation")
  val pulseAnim by infiniteTransition.animateFloat(
    initialValue = 0.85f,
    targetValue = 1.15f,
    animationSpec = infiniteRepeatable(
      animation = tween(2200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )
  val rotationAnim by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(24000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rotation"
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(24.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFF100720),
            Color(0xFF090412),
            Color(0xFF06030B)
          )
        )
      )
      .border(
        1.5.dp,
        Brush.linearGradient(
          colors = listOf(
            VioletPrimary.copy(alpha = 0.6f),
            NeonCyan.copy(alpha = 0.3f),
            VioletDeep.copy(alpha = 0.5f)
          )
        ),
        RoundedCornerShape(24.dp)
      )
      .padding(16.dp)
  ) {
    // Top Bar with mode title and selector
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(androidx.compose.foundation.shape.CircleShape)
              .background(NeonCyan)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "KNOWCONNECT 3D KNOWLEDGE CORE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = NeonCyan,
            letterSpacing = 1.2.sp
          )
        }
        Text(
          text = selectedMode.title,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }

      GlowingPill(
        text = selectedMode.subtitle,
        color = VioletSecondary,
        fontSize = 10
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Interactive Mode Pills (horizontal scroll / wrap)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      val quickModes = listOf(
        GraphVisualizationMode.AI_MATCHING,
        GraphVisualizationMode.SKILL_GRAPH,
        GraphVisualizationMode.PEER_NETWORK,
        GraphVisualizationMode.EXPLANATION_DIAGRAM
      )
      quickModes.forEach { mode ->
        val isSelected = mode == selectedMode
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) VioletPrimary else ObsidianCardBg)
            .border(
              1.dp,
              if (isSelected) Color.White.copy(alpha = 0.6f) else ObsidianCardBorder,
              RoundedCornerShape(12.dp)
            )
            .clickable { selectedMode = mode }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = mode.title.replace(" ", "\n"),
            color = if (isSelected) Color.White else TextSecondaryDark,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            lineHeight = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Canvas drawing 3D interconnected nodes
    val textMeasurer = rememberTextMeasurer()

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(230.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color(0xFF080410))
        .border(1.dp, Color(0x33A855F7), RoundedCornerShape(16.dp))
    ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val maxR = size.width.coerceAtMost(size.height) * 0.38f

        // Draw ambient background coordinate grid & radial rings
        drawCircle(
          color = Color(0x15A855F7),
          radius = maxR * 1.1f,
          center = Offset(centerX, centerY),
          style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f)))
        )
        drawCircle(
          color = Color(0x1822D3EE),
          radius = maxR * 0.7f,
          center = Offset(centerX, centerY),
          style = Stroke(width = 1.2f)
        )
        drawCircle(
          color = Color(0x20A855F7),
          radius = maxR * 0.35f,
          center = Offset(centerX, centerY),
          style = Stroke(width = 1f)
        )

        // Generate nodes based on selected mode
        val nodes = when (selectedMode) {
          GraphVisualizationMode.AI_MATCHING -> listOf(
            Triple("You (Alex)", Offset(0f, 0f), VioletPrimary),
            Triple("Sophia (98%)", Offset(-0.7f, -0.6f), NeonCyan),
            Triple("Marcus (94%)", Offset(0.75f, -0.45f), NeonEmerald),
            Triple("Elena (91%)", Offset(0.55f, 0.7f), VioletSecondary),
            Triple("Devon (87%)", Offset(-0.65f, 0.65f), AmberRating),
            Triple("AI Match Core", Offset(0f, -0.85f), Color.White)
          )
          GraphVisualizationMode.SKILL_GRAPH -> listOf(
            Triple("Raft Protocol", Offset(0f, 0f), VioletPrimary),
            Triple("Log Compaction", Offset(-0.6f, -0.6f), NeonCyan),
            Triple("State Machine", Offset(0.7f, -0.5f), NeonCyan),
            Triple("Leader Election", Offset(-0.7f, 0.5f), NeonEmerald),
            Triple("RPC Quorum", Offset(0.6f, 0.6f), VioletSecondary),
            Triple("Linearizability", Offset(0f, 0.85f), AmberRating)
          )
          GraphVisualizationMode.PEER_NETWORK -> listOf(
            Triple("KnowConnect Hub", Offset(0f, 0f), Color.White),
            Triple("UC Berkeley", Offset(-0.75f, -0.4f), VioletPrimary),
            Triple("Stanford AI", Offset(0.75f, -0.5f), NeonCyan),
            Triple("MIT Physics", Offset(-0.55f, 0.7f), NeonEmerald),
            Triple("CMU Systems", Offset(0.65f, 0.65f), AmberRating),
            Triple("Harvard Econ", Offset(0f, -0.8f), VioletSecondary)
          )
          else -> listOf(
            Triple("Core Concept", Offset(0f, 0f), VioletPrimary),
            Triple("Prerequisite A", Offset(-0.65f, -0.5f), NeonCyan),
            Triple("Prerequisite B", Offset(0.65f, -0.5f), NeonCyan),
            Triple("Midterm Proof", Offset(-0.6f, 0.6f), NeonEmerald),
            Triple("Application", Offset(0.6f, 0.6f), AmberRating),
            Triple("Peer Review", Offset(0f, 0.8f), VioletSecondary)
          )
        }

        // Calculate animated node positions
        val angleRad = Math.toRadians(rotationAnim.toDouble())
        val nodePoints = nodes.mapIndexed { index, (label, baseOffset, color) ->
          val pos = if (index == 0) {
            Offset(centerX, centerY)
          } else {
            val effAngle = angleRad * (if (index % 2 == 0) 1 else -1) + (index * 1.05)
            val radX = baseOffset.x * maxR
            val radY = baseOffset.y * maxR
            // Slight orbital wobble
            val x = centerX + radX + (cos(effAngle) * 8f).toFloat()
            val y = centerY + radY + (sin(effAngle) * 8f).toFloat()
            Offset(x, y)
          }
          Triple(label, pos, color)
        }

        // Draw glowing edge connections
        val centerPt = nodePoints.first().second
        for (i in 1 until nodePoints.size) {
          val pt = nodePoints[i].second
          val nodeColor = nodePoints[i].third
          drawLine(
            brush = Brush.linearGradient(
              colors = listOf(
                VioletPrimary.copy(alpha = 0.8f),
                nodeColor.copy(alpha = 0.7f)
              ),
              start = centerPt,
              end = pt
            ),
            start = centerPt,
            end = pt,
            strokeWidth = 2.2f,
            cap = StrokeCap.Round
          )

          // Secondary cross links between neighboring nodes
          if (i > 1) {
            val prevPt = nodePoints[i - 1].second
            drawLine(
              color = Color(0x33A855F7),
              start = prevPt,
              end = pt,
              strokeWidth = 1f,
              pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
            )
          }
        }

        // Draw Nodes
        nodePoints.forEachIndexed { index, (label, pt, color) ->
          val isCenter = index == 0
          val baseRadius = if (isCenter) 18f * pulseAnim else 12f

          // Outer Glow
          drawCircle(
            brush = Brush.radialGradient(
              colors = listOf(
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0f)
              ),
              center = pt,
              radius = baseRadius * 2.4f
            ),
            center = pt,
            radius = baseRadius * 2.4f
          )

          // Inner Solid Node
          drawCircle(
            color = color,
            radius = baseRadius,
            center = pt
          )

          // White center core
          drawCircle(
            color = Color.White,
            radius = baseRadius * 0.4f,
            center = pt
          )

          // Text label
          val textLayout = textMeasurer.measure(
            text = AnnotatedString(label),
            style = TextStyle(
              color = Color.White,
              fontSize = if (isCenter) 11.sp else 9.sp,
              fontWeight = FontWeight.Bold
            )
          )
          val textOffset = Offset(
            x = pt.x - (textLayout.size.width / 2f),
            y = if (pt.y > centerY) pt.y + baseRadius + 4f else pt.y - baseRadius - textLayout.size.height - 2f
          )
          drawText(textLayout, topLeft = textOffset)
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Interactive Graph Status Footer
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Hub,
          contentDescription = "Active Graph Nodes",
          tint = NeonCyan,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "6 Core Nodes • 9 Cross-Edge Links Active",
          fontSize = 11.sp,
          color = TextSecondaryDark
        )
      }

      Text(
        text = "Tap nodes to inspect",
        fontSize = 10.sp,
        color = VioletSecondary,
        fontWeight = FontWeight.Medium
      )
    }
  }
}
