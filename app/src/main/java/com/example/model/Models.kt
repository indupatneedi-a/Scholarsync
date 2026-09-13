package com.example.model

enum class UrgencyLevel(val label: String) {
  LOW("Low Priority"),
  MEDIUM("Standard"),
  HIGH("Urgent"),
  EXAM_PREP("Exam Tomorrow")
}

enum class RequestStatus(val label: String) {
  OPEN("Open"),
  IN_PROGRESS("In Progress"),
  RESOLVED("Resolved")
}

enum class MessageType {
  TEXT,
  VOICE_NOTE,
  FILE,
  LINK,
  AI_SUMMARY
}

data class ScholarBadgeItem(
  val title: String,
  val icon: String
)

data class LearnerReview(
  val learnerName: String,
  val courseTopic: String,
  val stars: Int,
  val comment: String,
  val tags: List<String>
)

data class UserProfile(
  val id: String = "u_alex",
  val name: String = "Alex Rivera",
  val email: String = "alex.rivera@berkeley.edu",
  val university: String = "UC Berkeley",
  val major: String = "Computer Science & Cognitive AI",
  val karmaPoints: Int = 1680,
  val level: Int = 8,
  val streakDays: Int = 14,
  val rating: Float = 4.94f,
  val isVerified: Boolean = true,
  val bio: String = "Passionate about algorithms, distributed systems, and collaborative peer learning. Teaching Assistant for CS 61B.",
  val topicsCanExplain: List<String> = listOf("Graph Algorithms", "Linear Algebra", "Compose UI & Kotlin", "Dynamic Programming", "Bayesian Stats"),
  val topicsNeedHelp: List<String> = listOf("Quantum Gates", "Organic Synthesis", "Distributed Raft Protocol"),
  val topicsNeedingHelp: List<String> = listOf("Quantum Gates", "Organic Synthesis", "Distributed Raft Protocol"),
  val badges: List<ScholarBadgeItem> = listOf(
    ScholarBadgeItem("Top 1% Explainer", "👑"),
    ScholarBadgeItem("14-Day Streak", "🔥"),
    ScholarBadgeItem("Code Mentor", "⚡"),
    ScholarBadgeItem("Exam Lifesaver", "🛡️")
  ),
  val reviewsFromLearners: List<LearnerReview> = listOf(
    LearnerReview(
      learnerName = "Jordan Hayes",
      courseTopic = "CS 170 Dijkstra Proof",
      stars = 5,
      comment = "Alex took 10 minutes to walk me through why negative edge weights fail in greedy search. Completely saved my midterm!",
      tags = listOf("Crystal Clear", "Super Patient")
    ),
    LearnerReview(
      learnerName = "Mia Tanaka",
      courseTopic = "Dynamic Programming",
      stars = 5,
      comment = "Great whiteboard explanation of memoization tables and base cases. 10/10 mentor.",
      tags = listOf("Genius Explainer", "Fast Responder")
    )
  )
)

data class Peer(
  val id: String,
  val name: String,
  val university: String,
  val major: String,
  val rating: Float,
  val reviewCount: Int,
  val matchScore: Int, // e.g. 98%
  val avatarLetter: String,
  val isOnline: Boolean,
  val topicsCanExplain: List<String>,
  val badges: List<String>,
  val bio: String,
  val hourlyRate: String = "Free Peer Help"
)

data class HelpRequest(
  val id: String,
  val requesterId: String,
  val requesterName: String,
  val requesterUniversity: String,
  val topic: String,
  val courseCode: String,
  val description: String,
  val urgency: UrgencyLevel,
  val status: RequestStatus,
  val repliesCount: Int,
  val timestamp: String,
  val acceptedByPeerName: String? = null
)

data class ChatMessage(
  val id: String,
  val senderId: String,
  val senderName: String,
  val text: String,
  val isUser: Boolean,
  val timestamp: String,
  val type: MessageType = MessageType.TEXT,
  val fileName: String? = null,
  val fileSize: String? = null,
  val linkUrl: String? = null,
  val voiceDurationSec: Int? = null,
  val aiInsight: String? = null
)

data class CommunityGroup(
  val id: String,
  val name: String,
  val university: String,
  val category: String,
  val description: String,
  val memberCount: Int,
  val isJoined: Boolean = false,
  val tags: List<String> = emptyList()
)

data class UploadedFileItem(
  val id: String,
  val name: String,
  val size: String,
  val type: String,
  val uploadedAt: String,
  val isPrivate: Boolean = true,
  val extractedTopics: List<String>,
  val aiConceptSummary: String,
  val sharedPeersCount: Int = 0
) {
  val uploadDate: String get() = uploadedAt
}

data class ReputationBadge(
  val id: String,
  val title: String,
  val description: String,
  val isUnlocked: Boolean,
  val iconEmoji: String,
  val xpReward: Int
)

data class LeaderboardStudent(
  val rank: Int,
  val name: String,
  val university: String,
  val points: Int,
  val explanationsGiven: Int,
  val rating: Float,
  val streak: Int,
  val badge: String
)

data class LearningSubjectProgress(
  val subject: String,
  val progressPercentage: Float, // 0.0 to 1.0
  val conceptsUnderstood: Int,
  val peerSessionsCount: Int
)

typealias AppNotification = NotificationItem

data class NotificationItem(
  val id: String,
  val title: String,
  val message: String,
  val timestamp: String,
  val isRead: Boolean = false,
  val type: String = "MATCH" // MATCH, REQUEST, RATING, COMMUNITY
)
