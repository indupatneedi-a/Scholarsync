package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.data.AiConceptExplanation
import com.example.data.AiService
import com.example.data.FileAnalysisResult
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.theme.*

enum class InputMode {
  MANUAL_TYPE,
  PREDEFINED_TOPICS,
  FILE_UPLOAD
}

@Composable
fun UploadTopicScreen(
  onFindPeerForTopic: (String) -> Unit,
  onFileSaved: (name: String, size: String, type: String, isPrivate: Boolean, topics: List<String>, summary: String) -> Unit
) {
  val scrollState = rememberScrollState()
  var inputMode by remember { mutableStateOf(InputMode.FILE_UPLOAD) }

  // Manual Type
  var manualTopicText by remember { mutableStateOf("Raft Protocol Log Compaction") }
  var isExplaining by remember { mutableStateOf(false) }
  var explanationResult by remember { mutableStateOf<AiConceptExplanation?>(null) }

  // Predefined Topics
  val predefinedCategories = mapOf(
    "Computer Science" to listOf("Dijkstra's Algorithm", "Dynamic Programming", "Raft Consensus", "Operating System Paging"),
    "Chemistry & Pre-Med" to listOf("SN1 vs SN2 Mechanisms", "Carbonyl Addition", "Biochemical Pathways"),
    "Physics & Math" to listOf("Quantum Gates & Bell States", "Eigenvalues & Eigenvectors", "Maxwell's Equations"),
    "Economics & Business" to listOf("Game Theory Nash Equilibrium", "Econometric Regression", "IS-LM Model")
  )
  var selectedCategory by remember { mutableStateOf("Computer Science") }

  // File Upload State
  var uploadedFileName by remember { mutableStateOf("CS170_Midterm2_StudyGuide.pdf") }
  var isPrivateByDefault by remember { mutableStateOf(true) }
  var fileAnalysis by remember { mutableStateOf<FileAnalysisResult?>(null) }

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
            text = "Learning Input Hub",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "3 input modes • AI topic extraction",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )
        }
        GlowingPill(text = "STUDY BUDDY AI", color = VioletSecondary, fontSize = 10)
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 3 Input Mode Tabs
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(ObsidianCardBg)
          .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        listOf(
          Triple(InputMode.FILE_UPLOAD, "Upload File", Icons.Default.CloudUpload),
          Triple(InputMode.MANUAL_TYPE, "Type Topic", Icons.Default.EditNote),
          Triple(InputMode.PREDEFINED_TOPICS, "Catalog", Icons.Default.Category)
        ).forEach { (mode, title, icon) ->
          val isSelected = inputMode == mode
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (isSelected) VioletPrimary else Color.Transparent)
              .clickable { inputMode = mode }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else TextSecondaryDark,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else TextSecondaryDark
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      when (inputMode) {
        InputMode.FILE_UPLOAD -> {
          // File Upload Dropzone
          GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Upload Course Files & Notes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Private by Default", fontSize = 11.sp, color = NeonCyan)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dropzone Area
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ObsidianDarkSurface)
                .border(
                  1.5.dp,
                  androidx.compose.ui.graphics.Brush.linearGradient(
                    listOf(VioletPrimary.copy(alpha = 0.6f), NeonCyan.copy(alpha = 0.3f))
                  ),
                  RoundedCornerShape(16.dp)
                )
                .clickable {
                  // Simulate file selection and AI extraction
                  fileAnalysis = AiService.analyzeFile(uploadedFileName)
                },
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.UploadFile, contentDescription = null, tint = VioletSecondary, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(uploadedFileName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Any file type supported • PDF, Jupyter, Slides, Code, DOCX", fontSize = 11.sp, color = TextSecondaryDark)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Tap to simulate instant AI file extraction", fontSize = 11.sp, color = NeonCyan)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Privacy Switch
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text("Keep file private in personal vault", fontSize = 13.sp, color = Color.White)
                Text("Only shared with peers you explicitly grant during study sessions.", fontSize = 11.sp, color = TextSecondaryDark)
              }
              Switch(
                checked = isPrivateByDefault,
                onCheckedChange = { isPrivateByDefault = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = VioletPrimary)
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = {
                val analysis = AiService.analyzeFile(uploadedFileName)
                fileAnalysis = analysis
                onFileSaved(
                  uploadedFileName,
                  "4.2 MB",
                  "PDF Document",
                  isPrivateByDefault,
                  analysis.extractedKeyTopics,
                  analysis.summary
                )
              },
              colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Extract Concepts with AI", fontWeight = FontWeight.Bold)
            }
          }

          // File Analysis Result
          fileAnalysis?.let { analysis ->
            Spacer(modifier = Modifier.height(16.dp))
            GlassCard(modifier = Modifier.fillMaxWidth()) {
              GlowingPill(text = "AI TOPIC EXTRACTION COMPLETE", color = NeonEmerald, fontSize = 10)
              Spacer(modifier = Modifier.height(10.dp))
              Text(analysis.detectedSubject, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
              Text("Difficulty: ${analysis.estimatedDifficulty}", fontSize = 12.sp, color = AmberRating)
              Spacer(modifier = Modifier.height(8.dp))
              Text(analysis.summary, fontSize = 12.sp, color = TextSecondaryDark, lineHeight = 17.sp)

              Spacer(modifier = Modifier.height(12.dp))
              Text("Extracted Key Topics:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletSecondary)
              Spacer(modifier = Modifier.height(6.dp))

              Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                analysis.extractedKeyTopics.forEach { topic ->
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(5.dp).clip(androidx.compose.foundation.shape.CircleShape).background(NeonCyan))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(topic, fontSize = 12.sp, color = Color.White)
                  }
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Button(
                onClick = { onFindPeerForTopic(analysis.extractedKeyTopics.firstOrNull() ?: "") },
                colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
              ) {
                Text("Find Matching Peers for This File", color = ObsidianBlack, fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        InputMode.MANUAL_TYPE -> {
          // Manual Type Mode
          GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text("Type Any College Topic or Concept", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Our friendly AI study buddy will synthesize key takeaways and find peers.", fontSize = 12.sp, color = TextSecondaryDark)

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
              value = manualTopicText,
              onValueChange = { manualTopicText = it },
              placeholder = { Text("e.g., Dynamic Programming memoization, SN2 steric hindrance") },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VioletPrimary,
                unfocusedBorderColor = ObsidianCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              ),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
              onClick = {
                explanationResult = AiService.explainConcept(manualTopicText)
              },
              colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Explain Concept & Find Peers", fontWeight = FontWeight.Bold)
            }
          }

          explanationResult?.let { exp ->
            Spacer(modifier = Modifier.height(16.dp))
            GlassCard(modifier = Modifier.fillMaxWidth()) {
              GlowingPill(text = "AI STUDY BUDDY EXPLANATION", color = VioletSecondary, fontSize = 10)
              Spacer(modifier = Modifier.height(10.dp))
              Text(exp.topic, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
              Spacer(modifier = Modifier.height(6.dp))
              Text(exp.simpleExplanation, fontSize = 13.sp, color = TextPrimaryDark, lineHeight = 19.sp)

              Spacer(modifier = Modifier.height(14.dp))
              Text("Key Takeaways:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
              Spacer(modifier = Modifier.height(6.dp))
              exp.keyTakeaways.forEach { takeaway ->
                Text("• $takeaway", fontSize = 12.sp, color = TextSecondaryDark, lineHeight = 16.sp, modifier = Modifier.padding(vertical = 2.dp))
              }

              Spacer(modifier = Modifier.height(14.dp))
              Text("Recommended Next Steps:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberRating)
              Spacer(modifier = Modifier.height(6.dp))
              exp.recommendedNextSteps.forEach { step ->
                Text("→ $step", fontSize = 12.sp, color = TextSecondaryDark, lineHeight = 16.sp, modifier = Modifier.padding(vertical = 2.dp))
              }

              Spacer(modifier = Modifier.height(16.dp))
              Button(
                onClick = { onFindPeerForTopic(exp.topic) },
                colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
              ) {
                Text("Connect with Explainer Peers", fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        InputMode.PREDEFINED_TOPICS -> {
          // Predefined Topic Catalog
          GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text("Select from University Course Catalog", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Browse verified topics with active collegiate explainer mentors.", fontSize = 12.sp, color = TextSecondaryDark)

            Spacer(modifier = Modifier.height(14.dp))

            // Category Chips
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              predefinedCategories.keys.forEach { cat ->
                val isSelected = selectedCategory == cat
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) VioletPrimary.copy(alpha = 0.25f) else ObsidianDarkSurface)
                    .border(1.dp, if (isSelected) VioletPrimary else ObsidianCardBorder, RoundedCornerShape(20.dp))
                    .clickable { selectedCategory = cat }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = cat.split(" ").first(),
                    fontSize = 11.sp,
                    color = if (isSelected) Color.White else TextSecondaryDark,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              predefinedCategories[selectedCategory]?.forEach { topic ->
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ObsidianDarkSurface)
                    .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                    .clickable { onFindPeerForTopic(topic) }
                    .padding(14.dp)
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text(topic, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                      Text("Tap to discover active peer explainers", fontSize = 11.sp, color = TextSecondaryDark)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = VioletSecondary)
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
