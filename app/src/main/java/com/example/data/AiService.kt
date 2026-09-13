package com.example.data

import com.example.model.Peer

data class AiConceptExplanation(
  val topic: String,
  val simpleExplanation: String,
  val keyTakeaways: List<String>,
  val commonPitfalls: List<String>,
  val recommendedNextSteps: List<String>,
  val suggestedPeerMatches: List<String>
)

data class FileAnalysisResult(
  val fileName: String,
  val detectedSubject: String,
  val extractedKeyTopics: List<String>,
  val estimatedDifficulty: String, // Beginner, Intermediate, Advanced
  val summary: String,
  val keyFormulaeOrRules: List<String>,
  val recommendedPeers: List<String>
)

object AiService {

  fun explainConcept(topic: String): AiConceptExplanation {
    val cleanTopic = topic.trim()
    return when {
      cleanTopic.contains("raft", ignoreCase = true) || cleanTopic.contains("consensus", ignoreCase = true) -> {
        AiConceptExplanation(
          topic = "Raft Distributed Consensus",
          simpleExplanation = "Think of Raft as a democratic parliamentary system for computer servers. Instead of chaotic simultaneous updates, servers elect a single trusted Leader. The Leader receives all commands, logs them, and only applies them once a majority of followers acknowledge receipt.",
          keyTakeaways = listOf(
            "Leader Election: Random heartbeat timers prevent split votes.",
            "Log Replication: The leader's log is the single source of truth.",
            "Safety Invariant: Only servers with the most up-to-date logs can be elected leader."
          ),
          commonPitfalls = listOf(
            "Confusing commit index with applied index.",
            "Forgetting that non-majority partitions cannot make progress."
          ),
          recommendedNextSteps = listOf(
            "Study Log Compaction & Snapshot RPCs.",
            "Simulate a network split with 5 nodes in the interactive visualizer.",
            "Connect with Sophia Chen (Stanford) for an architecture walkthrough."
          ),
          suggestedPeerMatches = listOf("Sophia Chen (Stanford - 98% Match)", "Devon Patel (CMU - 87% Match)")
        )
      }
      cleanTopic.contains("dijkstra", ignoreCase = true) || cleanTopic.contains("graph", ignoreCase = true) -> {
        AiConceptExplanation(
          topic = "Dijkstra's Shortest Path Algorithm",
          simpleExplanation = "Dijkstra is like a wave of water expanding outward from a source pebble on a graph. At each step, it greedily selects the closest unvisited node using a min-priority queue, guaranteeing the shortest path as long as all edge costs are non-negative.",
          keyTakeaways = listOf(
            "Greedy Choice: Always finalize the smallest distance node.",
            "Time Complexity: O((V + E) log V) with a Min-Heap / PriorityQueue.",
            "Critical Constraint: Fails on negative edge cycles (use Bellman-Ford instead)."
          ),
          commonPitfalls = listOf(
            "Attempting to add a fixed constant to negative edges (this changes paths with different hop counts!).",
            "Not updating distance values in the priority queue properly."
          ),
          recommendedNextSteps = listOf(
            "Compare Dijkstra vs A* heuristic pruning.",
            "Implement Bellman-Ford algorithm for negative edge detection.",
            "Request a peer code review from Alex Rivera or Sophia Chen."
          ),
          suggestedPeerMatches = listOf("Sophia Chen (Stanford - 98% Match)", "Jordan Hayes (UC Berkeley - 89% Match)")
        )
      }
      cleanTopic.contains("quantum", ignoreCase = true) -> {
        AiConceptExplanation(
          topic = "Quantum Teleportation & Bell States",
          simpleExplanation = "Quantum teleportation does not physically move matter. Instead, it transmits the exact unknown quantum state of a particle to another distant particle by consuming a shared entangled Bell state and transmitting two classical bits.",
          keyTakeaways = listOf(
            "No-Cloning Theorem: The original state is destroyed during Bell-state measurement.",
            "Classical Speed Limit: Cannot transmit information faster than light because 2 classical bits are required.",
            "Entanglement: Einstein's 'spooky action at a distance' serves as the quantum channel."
          ),
          commonPitfalls = listOf(
            "Thinking matter is moved (only information is transferred).",
            "Believing this enables Faster-Than-Light (FTL) communication."
          ),
          recommendedNextSteps = listOf(
            "Write the unitary 4x4 matrix representation of the CNOT gate.",
            "Review density matrices and mixed states.",
            "Hop on a voice call with Marcus Vance (MIT)."
          ),
          suggestedPeerMatches = listOf("Marcus Vance (MIT - 94% Match)", "Kofi Mensah (MIT - 85% Match)")
        )
      }
      else -> {
        AiConceptExplanation(
          topic = cleanTopic.ifBlank { "Concept Overview" },
          simpleExplanation = "$cleanTopic is a foundational college-level topic. The core insight revolves around breaking down the primary mechanism into first principles: identifying state invariants, constraint boundaries, and real-world analogous behaviors.",
          keyTakeaways = listOf(
            "Establish the baseline definitions and mathematical/conceptual axioms.",
            "Trace boundary edge cases and failure modes.",
            "Synthesize theory with problem set application."
          ),
          commonPitfalls = listOf(
            "Memorizing steps without understanding the underlying invariant.",
            "Overlooking prerequisite assumptions."
          ),
          recommendedNextSteps = listOf(
            "Review foundational course notes and related slides.",
            "Post an open peer help request in the Requests tab.",
            "Connect with top-rated peer mentors in KnowConnect."
          ),
          suggestedPeerMatches = listOf("Sophia Chen (98% Match)", "Marcus Vance (94% Match)", "Elena Rostova (91% Match)")
        )
      }
    }
  }

  fun analyzeFile(fileName: String): FileAnalysisResult {
    return when {
      fileName.contains("cs", ignoreCase = true) || fileName.contains("algorithm", ignoreCase = true) || fileName.contains("170", ignoreCase = true) -> {
        FileAnalysisResult(
          fileName = fileName,
          detectedSubject = "Computer Science: Algorithms & Optimization",
          extractedKeyTopics = listOf("Dynamic Programming", "Dijkstra vs Bellman-Ford", "Max Flow Min Cut", "Linear Programming Duality"),
          estimatedDifficulty = "Advanced (Upper-Division)",
          summary = "Rigorous proof-oriented curriculum covering graph theory, network flows, and asymptotic reductions. High probability of midterm questions on Ford-Fulkerson and DP memoization tables.",
          keyFormulaeOrRules = listOf(
            "Bellman Equation: OPT(i, v) = min(OPT(i-1, v), min_w(OPT(i-1, w) + c_wv))",
            "Max-Flow Min-Cut Theorem: Capacity of minimum cut equals value of maximum flow.",
            "Duality Gap: Primal feasible <= Dual feasible."
          ),
          recommendedPeers = listOf("Sophia Chen (Stanford)", "Devon Patel (CMU)")
        )
      }
      fileName.contains("chem", ignoreCase = true) -> {
        FileAnalysisResult(
          fileName = fileName,
          detectedSubject = "Organic Chemistry: Synthesis & Mechanisms",
          extractedKeyTopics = listOf("Grignard Reagents", "Acetal Hydrolysis", "Nucleophilic Carbonyl Addition", "Aldol Condensation"),
          estimatedDifficulty = "Intermediate",
          summary = "Systematic analysis of carbonyl electrophilicity, nucleophile attack trajectories, and acid/base catalysis mechanisms in synthesis pathways.",
          keyFormulaeOrRules = listOf(
            "Nucleophilic addition to carbonyl creates tetrahedral intermediate.",
            "Acetals are stable in basic conditions but hydrolyze rapidly in aqueous acid.",
            "Steric hindrance slows down nucleophilic attack rate."
          ),
          recommendedPeers = listOf("Elena Rostova (UC Berkeley)", "Mia Tanaka (Stanford)")
        )
      }
      else -> {
        FileAnalysisResult(
          fileName = fileName,
          detectedSubject = "Academic Course Material",
          extractedKeyTopics = listOf("Core Theory", "Analytical Framework", "Problem Solving", "Empirical Proofs"),
          estimatedDifficulty = "College / University Standard",
          summary = "Document analyzed successfully. Found 4 core conceptual modules suitable for peer review and collaborative study sessions.",
          keyFormulaeOrRules = listOf(
            "Primary Axiom & Invariant Check",
            "Boundary Condition Verification",
            "Step-by-step Reduction Method"
          ),
          recommendedPeers = listOf("Sophia Chen", "Marcus Vance", "Elena Rostova")
        )
      }
    }
  }

  fun calculateMatchScore(peer: Peer, requestedTopic: String): Int {
    if (requestedTopic.isBlank()) return peer.matchScore
    val lower = requestedTopic.lowercase()
    val matchesTopic = peer.topicsCanExplain.any { it.lowercase().contains(lower) || lower.contains(it.lowercase()) }
    return if (matchesTopic) 98 else (peer.matchScore - 12).coerceAtLeast(65)
  }
}
