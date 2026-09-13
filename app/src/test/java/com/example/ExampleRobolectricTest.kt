package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AiService
import com.example.data.KnowConnectRepository
import com.example.model.UrgencyLevel
import com.example.ui.theme.AppThemeMode
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("KnowConnect", appName)
  }

  @Test
  fun `verify ai service concept explanation and file extraction`() {
    val explanation = AiService.explainConcept("Distributed Raft Consensus")
    assertNotNull(explanation)
    assertTrue(explanation.topic.contains("Raft", ignoreCase = true))
    assertTrue(explanation.keyTakeaways.isNotEmpty())

    val fileAnalysis = AiService.analyzeFile("CS170_Midterm2_StudyGuide.pdf")
    assertTrue(fileAnalysis.extractedKeyTopics.contains("Dynamic Programming"))
    assertTrue(fileAnalysis.summary.isNotBlank())
  }

  @Test
  fun `verify repository request creation and learner rating`() {
    val repo = KnowConnectRepository()
    val initialRequestsCount = repo.requests.value.size

    repo.postHelpRequest("Graph Flows", "CS 170", "Need help on Edmonds-Karp", UrgencyLevel.HIGH)
    assertEquals(initialRequestsCount + 1, repo.requests.value.size)

    val initialKarma = repo.currentUser.value.karmaPoints
    repo.submitLearnerRating("p_1", 5, listOf("Crystal Clear"), "Awesome explanation!")
    assertTrue(repo.currentUser.value.karmaPoints > initialKarma)
  }

  @Test
  fun `verify repository theme switching`() {
    val repo = KnowConnectRepository()
    assertEquals(AppThemeMode.DARK, repo.themeMode.value)
    repo.setThemeMode(AppThemeMode.LIGHT)
    assertEquals(AppThemeMode.LIGHT, repo.themeMode.value)
  }
}

