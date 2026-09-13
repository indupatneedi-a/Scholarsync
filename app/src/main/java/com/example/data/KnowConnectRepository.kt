package com.example.data

import com.example.model.*
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class KnowConnectRepository {

  private val _themeMode = MutableStateFlow(AppThemeMode.DARK)
  val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

  fun setThemeMode(mode: AppThemeMode) {
    _themeMode.value = mode
  }

  val authService = AuthService()

  private val _currentUser = MutableStateFlow(
    UserProfile(
      id = "u_alex",
      name = "Alex Rivera",
      email = "alex.rivera@berkeley.edu",
      university = "UC Berkeley",
      major = "Computer Science & Cognitive AI",
      karmaPoints = 1680,
      level = 8,
      streakDays = 14,
      rating = 4.94f,
      isVerified = true
    )
  )
  val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()
  val currentUserProfile: StateFlow<UserProfile> = _currentUser.asStateFlow()

  fun syncUserProfileWithAuth(state: AuthUserState) {
    if (state.isLoggedIn && state.email != null) {
      _currentUser.value = _currentUser.value.copy(
        id = state.uid ?: _currentUser.value.id,
        name = state.displayName ?: _currentUser.value.name,
        email = state.email,
        isVerified = state.isEmailVerified || state.email.endsWith(".edu")
      )
    }
  }

  // Peers dataset
  private val _peers = MutableStateFlow(
    listOf(
      Peer(
        id = "p_1",
        name = "Sophia Chen",
        university = "Stanford University",
        major = "Applied Mathematics & CS",
        rating = 4.98f,
        reviewCount = 64,
        matchScore = 98,
        avatarLetter = "S",
        isOnline = true,
        topicsCanExplain = listOf("Graph Algorithms", "Distributed Raft Protocol", "Machine Learning Math", "Combinatorics"),
        badges = listOf("Peer Champion", "Top Math Explainer", "21-Day Streak"),
        bio = "Undergrad researcher. Love explaining complex algorithmic concepts with simple intuitive diagrams and zero jargon.",
        hourlyRate = "Free Peer Help"
      ),
      Peer(
        id = "p_2",
        name = "Marcus Vance",
        university = "MIT",
        major = "Electrical Engineering & Physics",
        rating = 4.92f,
        reviewCount = 47,
        matchScore = 94,
        avatarLetter = "M",
        isOnline = true,
        topicsCanExplain = listOf("Quantum Gates", "Linear Algebra", "Electromagnetism", "Circuit Theory"),
        badges = listOf("Quantum Guru", "Fast Responder"),
        bio = "Junior at MIT. Happy to hop on voice calls and break down quantum mechanics and linear transforms step by step.",
        hourlyRate = "Free Peer Help"
      ),
      Peer(
        id = "p_3",
        name = "Elena Rostova",
        university = "UC Berkeley",
        major = "Chemical Biology & Pre-Med",
        rating = 4.96f,
        reviewCount = 89,
        matchScore = 91,
        avatarLetter = "E",
        isOnline = false,
        topicsCanExplain = listOf("Organic Synthesis", "Biochemistry", "Molecular Dynamics", "Reaction Mechanisms"),
        badges = listOf("Diamond Mentor", "Patient Explainer", "Exam Lifesaver"),
        bio = "Teaching Assistant for Chem 3A. I specialize in reaction mechanisms and arrow-pushing logic!",
        hourlyRate = "Free Peer Help"
      ),
      Peer(
        id = "p_4",
        name = "Devon Patel",
        university = "Carnegie Mellon",
        major = "Computer Science",
        rating = 4.88f,
        reviewCount = 38,
        matchScore = 87,
        avatarLetter = "D",
        isOnline = true,
        topicsCanExplain = listOf("Distributed Systems", "Operating Systems", "Rust", "Computer Architecture"),
        badges = listOf("Systems Whiz", "Code Reviewer"),
        bio = "CS Senior. Let's conquer distributed consensus algorithms and memory management.",
        hourlyRate = "Free Peer Help"
      ),
      Peer(
        id = "p_5",
        name = "Aisha Al-Mansoor",
        university = "Harvard University",
        major = "Economics & Data Science",
        rating = 4.95f,
        reviewCount = 52,
        matchScore = 85,
        avatarLetter = "A",
        isOnline = true,
        topicsCanExplain = listOf("Macroeconomics", "Econometrics", "Game Theory", "Stochastic Calculus"),
        badges = listOf("Top Econ Peer", "Super Clear"),
        bio = "Passionate about making econometrics and mathematical economics completely intuitive.",
        hourlyRate = "Free Peer Help"
      )
    )
  )
  val peers: StateFlow<List<Peer>> = _peers.asStateFlow()

  // Help Requests
  private val _requests = MutableStateFlow(
    listOf(
      HelpRequest(
        id = "req_1",
        requesterId = "u_student_9",
        requesterName = "Jordan Hayes",
        requesterUniversity = "UC Berkeley",
        topic = "Dijkstra & A* Shortest Path proof",
        courseCode = "CS 170",
        description = "Need someone to explain why Dijkstra's algorithm fails with negative edge weights and how Bellman-Ford fixes it.",
        urgency = UrgencyLevel.EXAM_PREP,
        status = RequestStatus.OPEN,
        repliesCount = 3,
        timestamp = "12 min ago"
      ),
      HelpRequest(
        id = "req_2",
        requesterId = "u_alex",
        requesterName = "Alex Rivera (You)",
        requesterUniversity = "UC Berkeley",
        topic = "Raft Consensus Log Compaction",
        courseCode = "CS 162",
        description = "Looking for a quick voice call or whiteboarding session to walk through state machine snapshots in Raft.",
        urgency = UrgencyLevel.HIGH,
        status = RequestStatus.IN_PROGRESS,
        repliesCount = 2,
        timestamp = "45 min ago",
        acceptedByPeerName = "Sophia Chen"
      ),
      HelpRequest(
        id = "req_3",
        requesterId = "u_student_12",
        requesterName = "Mia Tanaka",
        requesterUniversity = "Stanford University",
        topic = "SN1 vs SN2 Steric Hindrance",
        courseCode = "CHEM 33",
        description = "Struggling with tertiary carbocation stability versus nucleophile strength in polar protic solvents.",
        urgency = UrgencyLevel.MEDIUM,
        status = RequestStatus.OPEN,
        repliesCount = 5,
        timestamp = "2 hours ago"
      ),
      HelpRequest(
        id = "req_4",
        requesterId = "u_student_14",
        requesterName = "Kofi Mensah",
        requesterUniversity = "MIT",
        topic = "Quantum Teleportation Circuit Analysis",
        courseCode = "PHYS 8.04",
        description = "Looking to review Bell state measurement steps and classical bit communication for quantum teleportation.",
        urgency = UrgencyLevel.HIGH,
        status = RequestStatus.OPEN,
        repliesCount = 1,
        timestamp = "3 hours ago"
      )
    )
  )
  val requests: StateFlow<List<HelpRequest>> = _requests.asStateFlow()
  val helpRequests: StateFlow<List<HelpRequest>> = _requests.asStateFlow()

  // Uploaded Files
  private val _uploadedFiles = MutableStateFlow(
    listOf(
      UploadedFileItem(
        id = "f_1",
        name = "CS170_Midterm2_StudyGuide.pdf",
        size = "4.2 MB",
        type = "PDF Document",
        uploadedAt = "Today, 10:14 AM",
        isPrivate = true,
        extractedTopics = listOf("Dynamic Programming", "Dijkstra vs Bellman-Ford", "Max Flow Min Cut", "Linear Programming Duality"),
        aiConceptSummary = "Document covers optimization algorithms, graph flow networks, and reduction proof strategies for NP-completeness.",
        sharedPeersCount = 1
      ),
      UploadedFileItem(
        id = "f_2",
        name = "Chem33_Carbonyl_Additions.pdf",
        size = "2.8 MB",
        type = "Lecture Notes",
        uploadedAt = "Yesterday",
        isPrivate = true,
        extractedTopics = listOf("Grignard Reagents", "Acetal Hydrolysis", "Nucleophilic Carbonyl Addition"),
        aiConceptSummary = "Mechanisms of aldehyde and ketone reactions with strong nucleophiles, reversible hemiacetal formations.",
        sharedPeersCount = 2
      ),
      UploadedFileItem(
        id = "f_3",
        name = "Quantum_Gates_Matrix_Transformations.ipynb",
        size = "1.1 MB",
        type = "Jupyter Notebook",
        uploadedAt = "3 days ago",
        isPrivate = true,
        extractedTopics = listOf("Hadamard Transform", "Pauli Matrices", "CNOT Entanglement", "Bloch Sphere Rotation"),
        aiConceptSummary = "Interactive Python matrix demonstrations of unitary gates operating on pure 2-qubit states.",
        sharedPeersCount = 0
      )
    )
  )
  val uploadedFiles: StateFlow<List<UploadedFileItem>> = _uploadedFiles.asStateFlow()

  // Communities
  private val _communities = MutableStateFlow(
    listOf(
      CommunityGroup(
        id = "c_1",
        name = "UC Berkeley EECS Guild",
        university = "UC Berkeley",
        category = "Computer Science & Engineering",
        description = "Peer study hub for CS 61A/B/C, CS 70, CS 170, and CS 162. Homework debugging, midterm prep, and concept sprints.",
        memberCount = 1420,
        isJoined = true,
        tags = listOf("Algorithms", "Systems", "AI/ML")
      ),
      CommunityGroup(
        id = "c_2",
        name = "Stanford AI & Theory Circle",
        university = "Stanford University",
        category = "Artificial Intelligence",
        description = "Deep dive into machine learning theory, transformer architectures, reinforcement learning, and mathematical foundations.",
        memberCount = 980,
        isJoined = true,
        tags = listOf("Deep Learning", "Optimization", "Linear Algebra")
      ),
      CommunityGroup(
        id = "c_3",
        name = "MIT Quantum & Condensed Matter",
        university = "MIT",
        category = "Physics",
        description = "Collaborative peer explanations on quantum computing circuits, Hamiltonian dynamics, and solid state physics.",
        memberCount = 650,
        isJoined = false,
        tags = listOf("Quantum", "Physics", "Linear Algebra")
      ),
      CommunityGroup(
        id = "c_4",
        name = "Pre-Med & Organic Chem Collaborative",
        university = "National Inter-Collegiate",
        category = "Chemistry & Biology",
        description = "Non-location-bound community breaking down organic chemistry reaction mechanisms, MCAT prep, and biochemistry pathways.",
        memberCount = 2100,
        isJoined = true,
        tags = listOf("O-Chem", "Biochem", "Pre-Med")
      ),
      CommunityGroup(
        id = "c_5",
        name = "University Economics & Finance Alliance",
        university = "Multi-Campus",
        category = "Economics",
        description = "Peer problem sets, econometric modeling in R/Python, game theory strategies, and macro policy debates.",
        memberCount = 840,
        isJoined = false,
        tags = listOf("Econometrics", "Microeconomics", "Finance")
      )
    )
  )
  val communities: StateFlow<List<CommunityGroup>> = _communities.asStateFlow()

  // Chat Messages
  private val _chatMessages = MutableStateFlow(
    listOf(
      ChatMessage(
        id = "m_1",
        senderId = "p_1",
        senderName = "Sophia Chen",
        text = "Hey Alex! I saw your request on Raft Log Compaction and Dijkstra proofs. How can I help break this down?",
        isUser = false,
        timestamp = "10:30 AM",
        type = MessageType.TEXT
      ),
      ChatMessage(
        id = "m_2",
        senderId = "u_alex",
        senderName = "Alex Rivera",
        text = "Thanks Sophia! I'm trying to wrap my head around state machine snapshots in Raft. When does the leader trigger compaction?",
        isUser = true,
        timestamp = "10:32 AM",
        type = MessageType.TEXT
      ),
      ChatMessage(
        id = "m_3",
        senderId = "p_1",
        senderName = "Sophia Chen",
        text = "Great question! Each server takes snapshots independently once the log hits a byte threshold. Here is a diagram breakdown of the snapshot header: lastIncludedIndex & lastIncludedTerm.",
        isUser = false,
        timestamp = "10:33 AM",
        type = MessageType.TEXT
      ),
      ChatMessage(
        id = "m_4",
        senderId = "p_1",
        senderName = "Sophia Chen",
        text = "Audio explanation of the InstallSnapshot RPC fallback for slow followers:",
        isUser = false,
        timestamp = "10:34 AM",
        type = MessageType.VOICE_NOTE,
        voiceDurationSec = 42
      ),
      ChatMessage(
        id = "m_5",
        senderId = "u_alex",
        senderName = "Alex Rivera",
        text = "Shared assignment notes:",
        isUser = true,
        timestamp = "10:35 AM",
        type = MessageType.FILE,
        fileName = "CS170_Midterm2_StudyGuide.pdf",
        fileSize = "4.2 MB"
      ),
      ChatMessage(
        id = "m_6",
        senderId = "ai_buddy",
        senderName = "KnowConnect AI Study Buddy",
        text = "Smart Summary: Raft snapshots discard already committed log entries up to lastIncludedIndex. Followers falling behind receive an InstallSnapshot RPC from the leader to catch up immediately.",
        isUser = false,
        timestamp = "10:35 AM",
        type = MessageType.AI_SUMMARY,
        aiInsight = "Key concept: Linearizability is preserved because snapshots only cover state machine states that have already reached quorum consensus."
      )
    )
  )
  val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

  // Notifications
  private val _notifications = MutableStateFlow(
    listOf(
      NotificationItem(
        id = "n_1",
        title = "Peer Match Found!",
        message = "Sophia Chen from Stanford is 98% match for your Raft Consensus request.",
        timestamp = "15m ago",
        isRead = false,
        type = "MATCH"
      ),
      NotificationItem(
        id = "n_2",
        title = "Request Accepted",
        message = "Sophia Chen accepted your help request on Raft Consensus.",
        timestamp = "30m ago",
        isRead = false,
        type = "REQUEST"
      ),
      NotificationItem(
        id = "n_3",
        title = "+50 Karma & 5★ Rating!",
        message = "Jordan gave you 5 stars: 'Alex explained Dijkstra with crystal clarity!'",
        timestamp = "2h ago",
        isRead = true,
        type = "RATING"
      ),
      NotificationItem(
        id = "n_4",
        title = "New Discussion in UC Berkeley EECS",
        message = "14 peers are preparing for CS 170 Midterm 2 right now.",
        timestamp = "4h ago",
        isRead = true,
        type = "COMMUNITY"
      )
    )
  )
  val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

  // Leaderboard
  val leaderboard = listOf(
    LeaderboardStudent(1, "Sophia Chen", "Stanford University", 4820, 142, 4.98f, 21, "🏆 Peer Champion"),
    LeaderboardStudent(2, "Elena Rostova", "UC Berkeley", 4150, 118, 4.96f, 18, "💎 Diamond Mentor"),
    LeaderboardStudent(3, "Marcus Vance", "MIT", 3890, 95, 4.92f, 16, "⚡ Quantum Sage"),
    LeaderboardStudent(4, "Alex Rivera (You)", "UC Berkeley", 1680, 48, 4.94f, 14, "🌟 Level 8 Scholar"),
    LeaderboardStudent(5, "Aisha Al-Mansoor", "Harvard University", 1540, 41, 4.95f, 12, "📊 Econ Master"),
    LeaderboardStudent(6, "Devon Patel", "Carnegie Mellon", 1320, 36, 4.88f, 9, "💻 Systems Whiz")
  )

  // Learning Progress
  private val _learningProgress = MutableStateFlow(
    listOf(
      LearningSubjectProgress("Distributed Systems & Raft", 0.85f, 18, 6),
      LearningSubjectProgress("Graph Algorithms & Flow Networks", 0.92f, 24, 8),
      LearningSubjectProgress("Organic Synthesis Mechanisms", 0.64f, 12, 4),
      LearningSubjectProgress("Quantum Gates & Bell States", 0.48f, 9, 3)
    )
  )
  val learningProgress: StateFlow<List<LearningSubjectProgress>> = _learningProgress.asStateFlow()

  // Reputation Badges
  val badges = listOf(
    ReputationBadge("b_1", "Top 1% Explainer", "Earned 4.9+ rating across 30+ peer explanations", true, "👑", 500),
    ReputationBadge("b_2", "14-Day Study Streak", "Helped or learned with peers 14 days in a row", true, "🔥", 250),
    ReputationBadge("b_3", "Code Mentor", "Solved 25 complex algorithmic challenges", true, "⚡", 300),
    ReputationBadge("b_4", "Concept Architect", "Generated 10 AI-assisted concept breakdowns", true, "🧠", 200),
    ReputationBadge("b_5", "Exam Lifesaver", "Helped 5 peers pass urgent exam-prep requests", false, "🛡️", 400),
    ReputationBadge("b_6", "Grand Scholar", "Reach Level 10 and 3,000 Karma Points", false, "🌌", 1000)
  )

  // Actions
  fun sendMessage(
    text: String,
    isUser: Boolean = true,
    type: MessageType = MessageType.TEXT,
    fileName: String? = null,
    fileSize: String? = null,
    linkUrl: String? = null,
    voiceDurationSec: Int? = null
  ) {
    sendChatMessage(text, type, fileName, fileSize, linkUrl, voiceDurationSec)
  }

  fun sendChatMessage(
    text: String,
    type: MessageType = MessageType.TEXT,
    fileName: String? = null,
    fileSize: String? = null,
    linkUrl: String? = null,
    voiceDurationSec: Int? = null
  ) {
    val newMsg = ChatMessage(
      id = UUID.randomUUID().toString(),
      senderId = "u_alex",
      senderName = _currentUser.value.name,
      text = text,
      isUser = true,
      timestamp = "Just now",
      type = type,
      fileName = fileName,
      fileSize = fileSize,
      linkUrl = linkUrl,
      voiceDurationSec = voiceDurationSec
    )
    _chatMessages.value = _chatMessages.value + newMsg

    // Simulate AI Study Buddy response if applicable
    if (type == MessageType.FILE || text.contains("explain", ignoreCase = true) || text.contains("how", ignoreCase = true)) {
      val aiReply = ChatMessage(
        id = UUID.randomUUID().toString(),
        senderId = "ai_buddy",
        senderName = "KnowConnect AI Study Buddy",
        text = "I've analyzed your concept request: '${text.take(60)}...'. Breaking it down into 3 foundational steps with peer recommendations!",
        isUser = false,
        timestamp = "Just now",
        type = MessageType.AI_SUMMARY,
        aiInsight = "Recommended peer: Sophia Chen has a 98% match for this exact topic."
      )
      _chatMessages.value = _chatMessages.value + aiReply
    }
  }

  fun postHelpRequest(
    topic: String,
    courseCode: String,
    description: String,
    urgency: UrgencyLevel
  ) {
    createHelpRequest(topic, courseCode, description, urgency)
  }

  fun createHelpRequest(
    topic: String,
    courseCode: String,
    description: String,
    urgency: UrgencyLevel
  ) {
    val req = HelpRequest(
      id = UUID.randomUUID().toString(),
      requesterId = _currentUser.value.id,
      requesterName = "${_currentUser.value.name} (You)",
      requesterUniversity = _currentUser.value.university,
      topic = topic,
      courseCode = courseCode.ifBlank { "GENERAL" },
      description = description,
      urgency = urgency,
      status = RequestStatus.OPEN,
      repliesCount = 0,
      timestamp = "Just now"
    )
    _requests.value = listOf(req) + _requests.value
  }

  fun acceptHelpRequest(requestId: String, helperName: String) {
    _requests.value = _requests.value.map { req ->
      if (req.id == requestId) {
        req.copy(
          status = RequestStatus.IN_PROGRESS,
          acceptedByPeerName = helperName,
          repliesCount = req.repliesCount + 1
        )
      } else req
    }
  }

  fun acceptRequest(requestId: String) {
    acceptHelpRequest(requestId, _currentUser.value.name)
  }

  fun resolveHelpRequest(requestId: String) {
    resolveRequest(requestId)
  }

  fun resolveRequest(requestId: String) {
    _requests.value = _requests.value.map { req ->
      if (req.id == requestId) req.copy(status = RequestStatus.RESOLVED) else req
    }
  }

  fun uploadFile(name: String, size: String, type: String, isPrivate: Boolean, extractedTopics: List<String>, summary: String) {
    uploadNewFile(name, size, type, isPrivate, extractedTopics, summary)
  }

  fun uploadNewFile(name: String, size: String, type: String, isPrivate: Boolean, extractedTopics: List<String>, summary: String) {
    val newFile = UploadedFileItem(
      id = UUID.randomUUID().toString(),
      name = name,
      size = size,
      type = type,
      uploadedAt = "Just now",
      isPrivate = isPrivate,
      extractedTopics = extractedTopics,
      aiConceptSummary = summary,
      sharedPeersCount = if (isPrivate) 0 else 1
    )
    _uploadedFiles.value = listOf(newFile) + _uploadedFiles.value
  }

  fun deleteFile(fileId: String) {
    _uploadedFiles.value = _uploadedFiles.value.filter { it.id != fileId }
  }

  fun toggleFilePrivacy(fileId: String) {
    _uploadedFiles.value = _uploadedFiles.value.map { f ->
      if (f.id == fileId) f.copy(isPrivate = !f.isPrivate) else f
    }
  }

  fun toggleJoinCommunity(communityId: String) {
    _communities.value = _communities.value.map { c ->
      if (c.id == communityId) {
        val newJoined = !c.isJoined
        c.copy(isJoined = newJoined, memberCount = if (newJoined) c.memberCount + 1 else c.memberCount - 1)
      } else c
    }
  }

  fun createCommunity(name: String, university: String, category: String, description: String, tags: List<String>) {
    val newComm = CommunityGroup(
      id = UUID.randomUUID().toString(),
      name = name,
      university = university,
      category = category,
      description = description,
      memberCount = 1,
      isJoined = true,
      tags = tags
    )
    _communities.value = listOf(newComm) + _communities.value
  }

  fun submitLearnerRating(peerId: String, stars: Int, tags: List<String>, comment: String) {
    ratePeer(peerId, stars, tags, comment)
  }

  fun ratePeer(peerId: String, stars: Int, tags: List<String>, comment: String) {
    val bonusKarma = stars * 25
    _currentUser.value = _currentUser.value.copy(
      karmaPoints = _currentUser.value.karmaPoints + bonusKarma
    )
  }

  fun clearChatHistory() {
    _chatMessages.value = emptyList()
  }

  fun markAllNotificationsRead() {
    _notifications.value = _notifications.value.map { it.copy(isRead = true) }
  }
}
