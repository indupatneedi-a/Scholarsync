package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AuthResult
import com.example.data.AuthService
import com.example.data.AuthUserState
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPill
import com.example.ui.theme.*
import kotlinx.coroutines.launch

enum class AuthTab {
  SIGN_IN,
  SIGN_UP,
  MANAGE_ACCOUNT
}

@Composable
fun AuthScreen(
  authService: AuthService,
  currentThemeMode: AppThemeMode,
  onThemeChanged: (AppThemeMode) -> Unit,
  onAuthComplete: () -> Unit,
  onBackToApp: (() -> Unit)? = null
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val currentUserState by authService.currentUserState.collectAsState()

  var selectedTab by remember {
    mutableStateOf(if (currentUserState.isLoggedIn && currentUserState.email != null) AuthTab.MANAGE_ACCOUNT else AuthTab.SIGN_IN)
  }

  var email by remember { mutableStateOf(currentUserState.email ?: "alex.rivera@berkeley.edu") }
  var password by remember { mutableStateOf("ScholarPass123!") }
  var displayName by remember { mutableStateOf(currentUserState.displayName ?: "Alex Rivera") }
  var passwordVisible by remember { mutableStateOf(false) }

  var isLoading by remember { mutableStateOf(false) }
  var statusMessage by remember { mutableStateOf<String?>(null) }
  var isError by remember { mutableStateOf(false) }

  // Forgot Password Dialog state
  var showResetDialog by remember { mutableStateOf(false) }
  var resetEmail by remember { mutableStateOf("") }

  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 500.dp)
        .verticalScroll(scrollState)
        .systemBarsPadding()
        .padding(vertical = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top back button if navigating from authenticated view
      if (onBackToApp != null) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Start
        ) {
          TextButton(onClick = onBackToApp) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back to workspace", tint = VioletSecondary)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Back to Workspace", color = VioletSecondary, fontSize = 13.sp)
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
      }

      // Header Branding
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
              Brush.linearGradient(listOf(VioletPrimary, VioletDeep))
            )
            .border(1.dp, VioletSecondary, RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AllInclusive,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "KnowConnect",
              fontSize = 24.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            GlowingPill(text = "AUTH", color = NeonCyan, fontSize = 9)
          }
          Text(
            text = "Firebase Authentication & Account Security",
            fontSize = 11.sp,
            color = TextSecondaryDark
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Navigation Segmented Control
      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianBlack)
            .padding(4.dp)
        ) {
          listOf(
            AuthTab.SIGN_IN to "Sign In",
            AuthTab.SIGN_UP to "Create Account",
            AuthTab.MANAGE_ACCOUNT to "Account"
          ).forEach { (tab, label) ->
            val isSelected = selectedTab == tab
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) VioletPrimary else Color.Transparent)
                .clickable {
                  selectedTab = tab
                  statusMessage = null
                }
                .padding(vertical = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = label,
                color = if (isSelected) Color.White else TextSecondaryDark,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Status Feedback Banner
        AnimatedVisibility(visible = statusMessage != null) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isError) Color(0xFF441B1B) else Color(0xFF1B3D2B),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isError) Color(0xFFFF5252) else NeonEmerald
            ),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 16.dp)
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (isError) Icons.Default.ErrorOutline else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (isError) Color(0xFFFF5252) else NeonEmerald,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = statusMessage ?: "",
                fontSize = 12.sp,
                color = Color.White
              )
            }
          }
        }

        // TAB 1: SIGN IN
        if (selectedTab == AuthTab.SIGN_IN) {
          Text(
            text = "Welcome Back to Campus",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Sign in using your collegiate Firebase credentials or Google Account.",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Google Sign-In button
          OutlinedButton(
            onClick = {
              isLoading = true
              statusMessage = null
              coroutineScope.launch {
                val result = authService.signInWithGoogleCredential(context)
                isLoading = false
                when (result) {
                  is AuthResult.Success -> {
                    isError = false
                    statusMessage = result.message
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    onAuthComplete()
                  }
                  is AuthResult.Error -> {
                    isError = true
                    statusMessage = result.message
                  }
                }
              }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianCardBorder)
          ) {
            Icon(
              imageVector = Icons.Default.AccountCircle,
              contentDescription = "Google",
              tint = NeonCyan,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text("Continue with Google", color = Color.White, fontSize = 13.sp)
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = ObsidianCardBorder)
            Text(" OR EMAIL ", fontSize = 10.sp, color = TextMutedDark, modifier = Modifier.padding(horizontal = 8.dp))
            HorizontalDivider(modifier = Modifier.weight(1f), color = ObsidianCardBorder)
          }

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("College or Personal Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = VioletSecondary) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = VioletPrimary,
              unfocusedBorderColor = ObsidianCardBorder,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = VioletSecondary) },
            trailingIcon = {
              IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                  imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                  contentDescription = "Toggle password visibility",
                  tint = TextSecondaryDark
                )
              }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = VioletPrimary,
              unfocusedBorderColor = ObsidianCardBorder,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            TextButton(
              onClick = {
                resetEmail = email
                showResetDialog = true
              }
            ) {
              Text("Forgot Password?", color = VioletSecondary, fontSize = 12.sp)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Button(
            onClick = {
              if (email.isBlank() || password.isBlank()) {
                isError = true
                statusMessage = "Please enter both email and password."
                return@Button
              }
              isLoading = true
              statusMessage = null
              coroutineScope.launch {
                val result = authService.signInWithEmail(email.trim(), password)
                isLoading = false
                when (result) {
                  is AuthResult.Success -> {
                    isError = false
                    statusMessage = result.message
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    onAuthComplete()
                  }
                  is AuthResult.Error -> {
                    isError = true
                    statusMessage = result.message
                  }
                }
              }
            },
            enabled = !isLoading,
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
            shape = RoundedCornerShape(12.dp)
          ) {
            if (isLoading) {
              CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
              Text("Sign In with Firebase", fontWeight = FontWeight.Bold)
            }
          }
        }

        // TAB 2: SIGN UP
        if (selectedTab == AuthTab.SIGN_UP) {
          Text(
            text = "Create Scholar Account",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Register with Firebase Auth to join peer study networks.",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )

          Spacer(modifier = Modifier.height(16.dp))

          OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Full Name / Scholar Alias") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = VioletSecondary) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = VioletPrimary,
              unfocusedBorderColor = ObsidianCardBorder,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("University / Collegiate Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = VioletSecondary) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = VioletPrimary,
              unfocusedBorderColor = ObsidianCardBorder,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Create Password (min 6 chars)") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = VioletSecondary) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = VioletPrimary,
              unfocusedBorderColor = ObsidianCardBorder,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(18.dp))

          Button(
            onClick = {
              if (email.isBlank() || password.length < 6) {
                isError = true
                statusMessage = "Please enter a valid email and password (minimum 6 characters)."
                return@Button
              }
              isLoading = true
              statusMessage = null
              coroutineScope.launch {
                val result = authService.signUpWithEmail(email.trim(), password, displayName.trim())
                isLoading = false
                when (result) {
                  is AuthResult.Success -> {
                    isError = false
                    statusMessage = result.message
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    onAuthComplete()
                  }
                  is AuthResult.Error -> {
                    isError = true
                    statusMessage = result.message
                  }
                }
              }
            },
            enabled = !isLoading,
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
            shape = RoundedCornerShape(12.dp)
          ) {
            if (isLoading) {
              CircularProgressIndicator(color = ObsidianBlack, modifier = Modifier.size(20.dp))
            } else {
              Text("Create Account", color = ObsidianBlack, fontWeight = FontWeight.Bold)
            }
          }
        }

        // TAB 3: MANAGE ACCOUNT
        if (selectedTab == AuthTab.MANAGE_ACCOUNT) {
          Text(
            text = "Active Account Session",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Review security details, connected provider, and manage authentication.",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )

          Spacer(modifier = Modifier.height(16.dp))

          Surface(
            shape = RoundedCornerShape(14.dp),
            color = ObsidianDarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianCardBorder),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(VioletPrimary.copy(alpha = 0.3f))
                    .border(1.dp, VioletSecondary, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                  Text(
                    text = currentUserState.displayName ?: "Scholar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                  Text(
                    text = currentUserState.email ?: "No email registered",
                    fontSize = 12.sp,
                    color = TextSecondaryDark
                  )
                }
              }

              Spacer(modifier = Modifier.height(16.dp))
              HorizontalDivider(color = ObsidianCardBorder)
              Spacer(modifier = Modifier.height(16.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Authentication Provider", fontSize = 12.sp, color = TextSecondaryDark)
                GlowingPill(
                  text = currentUserState.providerId?.uppercase() ?: "FIREBASE_AUTH",
                  color = VioletSecondary,
                  fontSize = 9
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Student Trust Status", fontSize = 12.sp, color = TextSecondaryDark)
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Verified, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Verified Scholar", fontSize = 12.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("User ID", fontSize = 12.sp, color = TextSecondaryDark)
                Text(
                  text = currentUserState.uid?.take(16) ?: "local_guest",
                  fontSize = 11.sp,
                  color = TextMutedDark
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Update Display Name section
          OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Edit Display Name") },
            singleLine = true,
            trailingIcon = {
              IconButton(
                onClick = {
                  coroutineScope.launch {
                    val res = authService.updateDisplayName(displayName.trim())
                    when (res) {
                      is AuthResult.Success -> {
                        isError = false
                        statusMessage = res.message
                      }
                      is AuthResult.Error -> {
                        isError = true
                        statusMessage = res.message
                      }
                    }
                  }
                }
              ) {
                Icon(Icons.Default.Save, contentDescription = "Save Name", tint = VioletSecondary)
              }
            },
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

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = {
                authService.signOut()
                selectedTab = AuthTab.SIGN_IN
                statusMessage = "Signed out of current session."
                isError = false
              },
              modifier = Modifier
                .weight(1f)
                .height(46.dp),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.6f))
            ) {
              Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Sign Out", color = Color(0xFFFF5252), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
              onClick = onAuthComplete,
              modifier = Modifier
                .weight(1f)
                .height(46.dp),
              colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("Enter App", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Theme toggle footer
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text("Display Theme:", fontSize = 12.sp, color = TextSecondaryDark)
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
          selected = currentThemeMode == AppThemeMode.DARK,
          onClick = { onThemeChanged(AppThemeMode.DARK) },
          label = { Text("Dark Obsidian", fontSize = 11.sp) }
        )
        Spacer(modifier = Modifier.width(6.dp))
        FilterChip(
          selected = currentThemeMode == AppThemeMode.LIGHT,
          onClick = { onThemeChanged(AppThemeMode.LIGHT) },
          label = { Text("Light Studio", fontSize = 11.sp) }
        )
      }
    }
  }

  // Forgot password dialog
  if (showResetDialog) {
    AlertDialog(
      onDismissRequest = { showResetDialog = false },
      title = { Text("Reset Password", fontWeight = FontWeight.Bold, color = Color.White) },
      text = {
        Column {
          Text(
            "Enter your registered email address. We will send a Firebase password recovery link to your inbox.",
            fontSize = 12.sp,
            color = TextSecondaryDark
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = resetEmail,
            onValueChange = { resetEmail = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = VioletPrimary,
              unfocusedBorderColor = ObsidianCardBorder,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            )
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (resetEmail.isNotBlank()) {
              coroutineScope.launch {
                val res = authService.sendPasswordReset(resetEmail.trim())
                showResetDialog = false
                when (res) {
                  is AuthResult.Success -> {
                    isError = false
                    statusMessage = res.message
                  }
                  is AuthResult.Error -> {
                    isError = true
                    statusMessage = res.message
                  }
                }
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
        ) {
          Text("Send Reset Link")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetDialog = false }) {
          Text("Cancel", color = TextSecondaryDark)
        }
      },
      containerColor = ObsidianDarkSurface
    )
  }
}
