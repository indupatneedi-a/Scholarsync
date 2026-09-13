package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.data.KnowConnectRepository
import com.example.model.Peer
import com.example.ui.components.LearnerRatingDialog
import com.example.ui.components.VoiceCallDialog
import com.example.ui.screens.*
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianDarkSurface
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.VioletDeep
import com.example.ui.theme.VioletPrimary
import com.example.ui.theme.VioletSecondary

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
  object Landing : Screen("landing", "Landing")
  object Auth : Screen("auth", "Auth")
  object Onboarding : Screen("onboarding", "Onboarding")
  object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
  object Matching : Screen("matching", "AI Match", Icons.Default.AutoAwesome)
  object Upload : Screen("upload", "Upload", Icons.Default.CloudUpload)
  object Requests : Screen("requests", "Requests", Icons.Default.Handshake)
  object Chat : Screen("chat", "Chat", Icons.Default.Chat)
  object Communities : Screen("communities", "Guilds", Icons.Default.Groups)
  object Leaderboard : Screen("leaderboard", "Ranks", Icons.Default.EmojiEvents)
  object Profile : Screen("profile", "Profile", Icons.Default.Person)
  object History : Screen("history", "History", Icons.Default.History)
  object Notifications : Screen("notifications", "Alerts", Icons.Default.Notifications)
  object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun KnowConnectNavHost(
  repository: KnowConnectRepository,
  currentThemeMode: AppThemeMode,
  onThemeChanged: (AppThemeMode) -> Unit
) {
  val navController = rememberNavController()

  val userProfile by repository.currentUserProfile.collectAsState()
  val peers by repository.peers.collectAsState()
  val requests by repository.helpRequests.collectAsState()
  val files by repository.uploadedFiles.collectAsState()
  val communities by repository.communities.collectAsState()
  val chatMessages by repository.chatMessages.collectAsState()
  val notifications by repository.notifications.collectAsState()
  val learningProgress by repository.learningProgress.collectAsState()

  var activeVoiceCallPeer by remember { mutableStateOf<Peer?>(null) }
  var ratingPeerTarget by remember { mutableStateOf<Peer?>(null) }
  var selectedChatPeer by remember { mutableStateOf(peers.first()) }

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  // Show bottom navigation bar only for core authenticated screens on mobile
  val bottomBarRoutes = listOf(
    Screen.Dashboard.route,
    Screen.Matching.route,
    Screen.Upload.route,
    Screen.Requests.route,
    Screen.Communities.route,
    Screen.Leaderboard.route
  )

  val authenticatedRoutes = listOf(
    Screen.Dashboard.route,
    Screen.Matching.route,
    Screen.Upload.route,
    Screen.Requests.route,
    Screen.Communities.route,
    Screen.Leaderboard.route,
    Screen.Profile.route,
    Screen.History.route,
    Screen.Notifications.route,
    Screen.Settings.route,
    Screen.Chat.route
  )

  BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    val isDesktopWeb = maxWidth >= 768.dp
    val showDesktopHeader = isDesktopWeb && currentRoute in authenticatedRoutes
    val showBottomBar = !isDesktopWeb && currentRoute in bottomBarRoutes

    Scaffold(
      topBar = {
        if (showDesktopHeader) {
          KnowConnectDesktopHeader(
            navController = navController,
            currentRoute = currentRoute,
            unreadNotificationCount = notifications.count { !it.isRead },
            currentThemeMode = currentThemeMode,
            onThemeChanged = onThemeChanged
          )
        }
      },
      bottomBar = {
        if (showBottomBar) {
          KnowConnectBottomBar(navController = navController, currentRoute = currentRoute)
        }
      },
      containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues),
        contentAlignment = Alignment.TopCenter
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .widthIn(max = if (isDesktopWeb) 1160.dp else androidx.compose.ui.unit.Dp.Unspecified)
        ) {
          NavHost(
            navController = navController,
            startDestination = Screen.Landing.route
          ) {
        composable(Screen.Landing.route) {
          LandingScreen(
            onStartLearning = {
              navController.navigate(Screen.Auth.route)
            },
            onFindPeer = {
              navController.navigate(Screen.Matching.route)
            },
            onLoginClick = {
              navController.navigate(Screen.Auth.route)
            }
          )
        }

        composable(Screen.Auth.route) {
          AuthScreen(
            authService = repository.authService,
            currentThemeMode = currentThemeMode,
            onThemeChanged = onThemeChanged,
            onAuthComplete = {
              repository.syncUserProfileWithAuth(repository.authService.currentUserState.value)
              navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Landing.route) { inclusive = false }
              }
            },
            onBackToApp = if (navController.previousBackStackEntry != null) {
              { navController.popBackStack() }
            } else null
          )
        }

        composable(Screen.Onboarding.route) {
          OnboardingScreen(
            onComplete = {
              navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Auth.route) { inclusive = true }
              }
            }
          )
        }

        composable(Screen.Dashboard.route) {
          DashboardScreen(
            user = userProfile,
            peers = peers,
            requests = requests,
            uploadedFiles = files,
            communities = communities,
            chatMessages = chatMessages,
            learningProgress = learningProgress,
            unreadNotificationCount = notifications.count { !it.isRead },
            onNavigateToMatching = { navController.navigate(Screen.Matching.route) },
            onNavigateToUpload = { navController.navigate(Screen.Upload.route) },
            onNavigateToRequests = { navController.navigate(Screen.Requests.route) },
            onNavigateToChat = {
              selectedChatPeer = peers.first()
              navController.navigate(Screen.Chat.route)
            },
            onNavigateToCommunities = { navController.navigate(Screen.Communities.route) },
            onNavigateToHistory = { navController.navigate(Screen.History.route) },
            onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
            onNavigateToLeaderboard = { navController.navigate(Screen.Leaderboard.route) },
            onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
          )
        }

        composable(Screen.Matching.route) {
          AiMatchingScreen(
            peers = peers,
            onStartChatWithPeer = { peer ->
              selectedChatPeer = peer
              navController.navigate(Screen.Chat.route)
            },
            onLaunchVoiceCallWithPeer = { peer ->
              activeVoiceCallPeer = peer
            }
          )
        }

        composable(Screen.Upload.route) {
          UploadTopicScreen(
            onFindPeerForTopic = { topic ->
              navController.navigate(Screen.Matching.route)
            },
            onFileSaved = { name, size, type, isPriv, topics, summary ->
              repository.uploadFile(name, size, type, isPriv, topics, summary)
            }
          )
        }

        composable(Screen.Chat.route) {
          ChatScreen(
            activePeer = selectedChatPeer,
            messages = chatMessages,
            onSendMessage = { text, type, fileName, fileSize, linkUrl, voiceSec ->
              repository.sendMessage(
                text = text,
                isUser = true,
                type = type,
                fileName = fileName,
                fileSize = fileSize,
                linkUrl = linkUrl,
                voiceDurationSec = voiceSec
              )
            },
            onLaunchVoiceCall = {
              activeVoiceCallPeer = selectedChatPeer
            },
            onRateHelper = { peer ->
              ratingPeerTarget = peer
            },
            onClearChatHistory = {
              repository.clearChatHistory()
            },
            onBack = {
              navController.popBackStack()
            }
          )
        }

        composable(Screen.Requests.route) {
          RequestsScreen(
            requests = requests,
            onCreateRequest = { topic, course, desc, urgency ->
              repository.postHelpRequest(topic, course, desc, urgency)
            },
            onAcceptRequest = { reqId ->
              repository.acceptHelpRequest(reqId, userProfile.name)
            },
            onResolveRequest = { reqId ->
              repository.resolveHelpRequest(reqId)
            },
            onOpenChatForRequest = { req ->
              val match = peers.find { it.name == req.requesterName } ?: peers.first()
              selectedChatPeer = match
              navController.navigate(Screen.Chat.route)
            }
          )
        }

        composable(Screen.Communities.route) {
          CommunitiesScreen(
            communities = communities,
            onToggleJoin = { commId ->
              repository.toggleJoinCommunity(commId)
            },
            onCreateCommunity = { name, uni, cat, desc, tags ->
              repository.createCommunity(name, uni, cat, desc, tags)
            },
            onEnterCommunityChat = { comm ->
              selectedChatPeer = peers.first()
              navController.navigate(Screen.Chat.route)
            }
          )
        }

        composable(Screen.Leaderboard.route) {
          LeaderboardScreen(
            peers = peers,
            onRateHelper = { peer ->
              ratingPeerTarget = peer
            },
            onOpenPeerProfile = { peer ->
              selectedChatPeer = peer
              navController.navigate(Screen.Chat.route)
            }
          )
        }

        composable(Screen.Profile.route) {
          ProfileScreen(
            user = userProfile,
            onOpenSettings = {
              navController.navigate(Screen.Settings.route)
            },
            onManageAccount = {
              navController.navigate(Screen.Auth.route)
            }
          )
        }

        composable(Screen.History.route) {
          HistoryScreen(
            files = files,
            requests = requests,
            peers = peers,
            chatMessages = chatMessages,
            onDeleteFile = { fileId ->
              repository.deleteFile(fileId)
            },
            onClearChat = {
              repository.clearChatHistory()
            },
            onOpenPeerChat = { peer ->
              selectedChatPeer = peer
              navController.navigate(Screen.Chat.route)
            }
          )
        }

        composable(Screen.Notifications.route) {
          NotificationsScreen(
            notifications = notifications,
            onMarkAllAsRead = {
              repository.markAllNotificationsRead()
            }
          )
        }

        composable(Screen.Settings.route) {
          SettingsScreen(
            currentTheme = currentThemeMode,
            onThemeChanged = onThemeChanged,
            onManageAccount = {
              navController.navigate(Screen.Auth.route)
            }
          )
        }
      }

      // Voice Call Overlay Dialog
      activeVoiceCallPeer?.let { peer ->
        VoiceCallDialog(
          peer = peer,
          onEndCall = { activeVoiceCallPeer = null }
        )
      }

      // Learner Rating Dialog (5-star rating, tags, comment, karma boost)
      ratingPeerTarget?.let { peer ->
        LearnerRatingDialog(
          helperName = peer.name,
          onDismiss = { ratingPeerTarget = null },
          onSubmitRating = { stars, tags, comment ->
            repository.submitLearnerRating(peer.id, stars, tags, comment)
            ratingPeerTarget = null
          }
        )
      }
        }
      }
    }
  }
}

@Composable
fun KnowConnectDesktopHeader(
  navController: NavController,
  currentRoute: String?,
  unreadNotificationCount: Int,
  currentThemeMode: AppThemeMode,
  onThemeChanged: (AppThemeMode) -> Unit
) {
  Surface(
    color = ObsidianDarkSurface,
    tonalElevation = 6.dp,
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp)
      .border(0.5.dp, ObsidianCardBorder)
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Left: Logo & Brand
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
          navController.navigate(Screen.Dashboard.route)
        }
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
              Brush.linearGradient(listOf(VioletPrimary, VioletDeep))
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
        Spacer(modifier = Modifier.width(12.dp))
        Text(
          text = "KnowConnect",
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White
        )
      }

      // Middle: Nav links
      val navItems = listOf(
        Screen.Dashboard,
        Screen.Matching,
        Screen.Upload,
        Screen.Requests,
        Screen.Communities,
        Screen.Leaderboard
      )

      Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        navItems.forEach { screen ->
          val isSelected = currentRoute == screen.route
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isSelected) VioletPrimary.copy(alpha = 0.25f) else Color.Transparent,
            modifier = Modifier.clickable {
              if (currentRoute != screen.route) {
                navController.navigate(screen.route) {
                  popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                  }
                  launchSingleTop = true
                  restoreState = true
                }
              }
            }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              screen.icon?.let { icon ->
                Icon(
                  imageVector = icon,
                  contentDescription = screen.title,
                  tint = if (isSelected) VioletSecondary else TextSecondaryDark,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
              }
              Text(
                text = screen.title,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else TextSecondaryDark
              )
            }
          }
        }
      }

      // Right: Notifications, Theme, Profile
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Notification bell
        IconButton(
          onClick = { navController.navigate(Screen.Notifications.route) }
        ) {
          BadgedBox(
            badge = {
              if (unreadNotificationCount > 0) {
                Badge(containerColor = VioletSecondary) {
                  Text(unreadNotificationCount.toString(), color = Color.White)
                }
              }
            }
          ) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = "Notifications",
              tint = if (currentRoute == Screen.Notifications.route) VioletSecondary else TextSecondaryDark
            )
          }
        }

        // Theme toggle
        IconButton(
          onClick = {
            val nextTheme = when (currentThemeMode) {
              AppThemeMode.DARK -> AppThemeMode.LIGHT
              AppThemeMode.LIGHT -> AppThemeMode.SYSTEM
              AppThemeMode.SYSTEM -> AppThemeMode.DARK
            }
            onThemeChanged(nextTheme)
          }
        ) {
          Icon(
            imageVector = when (currentThemeMode) {
              AppThemeMode.DARK -> Icons.Default.DarkMode
              AppThemeMode.LIGHT -> Icons.Default.LightMode
              AppThemeMode.SYSTEM -> Icons.Default.SettingsBrightness
            },
            contentDescription = "Toggle Theme",
            tint = TextSecondaryDark
          )
        }

        // Account / Firebase Auth button
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = VioletPrimary.copy(alpha = 0.2f),
          border = androidx.compose.foundation.BorderStroke(1.dp, VioletSecondary.copy(alpha = 0.4f)),
          modifier = Modifier
            .clickable { navController.navigate(Screen.Auth.route) }
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.AccountCircle, contentDescription = "Account", tint = VioletSecondary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Account", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
          }
        }

        // Profile button
        Surface(
          shape = CircleShape,
          color = VioletPrimary.copy(alpha = 0.3f),
          border = androidx.compose.foundation.BorderStroke(1.dp, VioletSecondary.copy(alpha = 0.6f)),
          modifier = Modifier
            .size(36.dp)
            .clickable { navController.navigate(Screen.Profile.route) }
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = "Profile",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun KnowConnectBottomBar(
  navController: NavController,
  currentRoute: String?
) {
  val items = listOf(
    Screen.Dashboard,
    Screen.Matching,
    Screen.Upload,
    Screen.Requests,
    Screen.Communities,
    Screen.Leaderboard
  )

  NavigationBar(
    containerColor = ObsidianDarkSurface,
    tonalElevation = 8.dp,
    modifier = Modifier
      .fillMaxWidth()
      .border(0.5.dp, ObsidianCardBorder)
  ) {
    items.forEach { screen ->
      val isSelected = currentRoute == screen.route
      NavigationBarItem(
        icon = {
          screen.icon?.let { icon ->
            Icon(
              imageVector = icon,
              contentDescription = screen.title,
              tint = if (isSelected) VioletSecondary else TextSecondaryDark
            )
          }
        },
        label = {
          Text(
            text = screen.title,
            fontSize = 10.sp,
            fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
            color = if (isSelected) Color.White else TextSecondaryDark
          )
        },
        selected = isSelected,
        onClick = {
          if (currentRoute != screen.route) {
            navController.navigate(screen.route) {
              popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
              }
              launchSingleTop = true
              restoreState = true
            }
          }
        },
        colors = NavigationBarItemDefaults.colors(
          indicatorColor = VioletPrimary.copy(alpha = 0.25f)
        )
      )
    }
  }
}
