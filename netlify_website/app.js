// KnowConnect Web Application Engine
const STATE = {
  activeTab: 'dashboard',
  theme: localStorage.getItem('kc_theme') || 'dark',
  user: JSON.parse(localStorage.getItem('kc_user')) || {
    isLoggedIn: true,
    name: 'Alex Rivera',
    email: 'alex.rivera@berkeley.edu',
    university: 'UC Berkeley',
    karma: 1480,
    role: 'Verified Scholar'
  },
  studySessions: [
    { id: 1, title: 'CS 61B: Graphs & Dijkstra Algorithm', peer: 'Maya Patel', time: 'Today at 4:30 PM', mode: 'Virtual Room #4' },
    { id: 2, title: 'MATH 53: Surface Integrals Review', peer: 'Elena Rostova', time: 'Tomorrow at 2:00 PM', mode: 'Main Library 2nd Floor' }
  ],
  peers: [
    { id: 'p1', name: 'Maya Patel', subject: 'Computer Science', courses: ['CS 61B', 'EECS 127'], match: '98%', karma: 1850, verified: true },
    { id: 'p2', name: 'Marcus Chen', subject: 'Data Science', courses: ['DATA 100', 'STAT 140'], match: '95%', karma: 1420, verified: true },
    { id: 'p3', name: 'Elena Rostova', subject: 'Mathematics & Stats', courses: ['MATH 53', 'MATH 54'], match: '92%', karma: 1690, verified: true },
    { id: 'p4', name: 'David Kim', subject: 'Electrical Eng', courses: ['EECS 16A', 'CS 61C'], match: '89%', karma: 1120, verified: false }
  ],
  materials: [
    { id: 'm1', title: 'Midterm 2 Comprehensive Summary', course: 'CS 61B', author: 'Maya P.', category: 'Exam Review', downloads: 342 },
    { id: 'm2', title: 'Gradient Descent & Convexity Cheatsheet', course: 'EECS 127', author: 'Alex R.', category: 'Cheatsheet', downloads: 218 },
    { id: 'm3', title: 'Stokes Theorem Visualized Problem Sets', course: 'MATH 53', author: 'Elena R.', category: 'Lecture Notes', downloads: 184 }
  ],
  requests: [
    { id: 'r1', from: 'Jordan Smith', course: 'CS 61B', topic: 'Red-Black Tree balance cases', time: '20 mins ago' },
    { id: 'r2', from: 'Chloe Vance', course: 'EECS 127', topic: 'Dual problem formulation', time: '2 hours ago' }
  ],
  guilds: [
    { id: 'g1', name: 'Algorithms & LeetCode Guild', members: 428, desc: 'Weekly coding challenge reviews and technical interview preps.' },
    { id: 'g2', name: 'Deep Learning Research Guild', members: 310, desc: 'Paper reviews on LLM reasoning and transformer architectures.' },
    { id: 'g3', name: 'Pre-Med & Organic Chem Collective', members: 265, desc: 'Study flashcards, lab quiz preps, and MCAT sprint groups.' },
    { id: 'g4', name: 'Calculus & Proofs Circle', members: 195, desc: 'Rigorous theorem proofs, vector calculus, and problem brainstorming.' }
  ],
  leaderboard: [
    { rank: 1, name: 'Maya Patel', uni: 'UC Berkeley', karma: 2350, reviews: 48 },
    { rank: 2, name: 'Lucas Meyer', uni: 'Stanford', karma: 2120, reviews: 42 },
    { rank: 3, name: 'Elena Rostova', uni: 'MIT', karma: 1890, reviews: 36 },
    { rank: 4, name: 'Alex Rivera (You)', uni: 'UC Berkeley', karma: 1480, reviews: 29 },
    { rank: 5, name: 'Marcus Chen', uni: 'Georgia Tech', karma: 1420, reviews: 25 }
  ]
};

// Initialize Application
document.addEventListener('DOMContentLoaded', () => {
  applyTheme(STATE.theme);
  renderAll();
  lucide.createIcons();
});

// Theme Management
function toggleTheme() {
  const newTheme = STATE.theme === 'dark' ? 'light' : 'dark';
  STATE.theme = newTheme;
  localStorage.setItem('kc_theme', newTheme);
  applyTheme(newTheme);
}

function applyTheme(theme) {
  document.documentElement.setAttribute('data-theme', theme);
  const themeIcon = document.getElementById('theme-icon');
  if (themeIcon) {
    themeIcon.setAttribute('data-lucide', theme === 'dark' ? 'sun' : 'moon');
    lucide.createIcons();
  }
}

// Navigation Engine
function navigateTo(tabId) {
  STATE.activeTab = tabId;

  // Update Sections
  document.querySelectorAll('.screen-section').forEach(sec => {
    sec.classList.remove('active');
  });
  const targetSection = document.getElementById(`screen-${tabId}`);
  if (targetSection) targetSection.classList.add('active');

  // Update Header Nav Buttons
  document.querySelectorAll('.nav-btn').forEach(btn => {
    btn.classList.toggle('active', btn.dataset.tab === tabId);
  });

  // Update Mobile Bottom Nav Buttons
  document.querySelectorAll('.mobile-nav-btn').forEach(btn => {
    btn.classList.toggle('active', btn.dataset.tab === tabId);
  });

  window.scrollTo({ top: 0, behavior: 'smooth' });
  lucide.createIcons();
}

// Render All UI Dynamic Components
function renderAll() {
  renderDashboardSessions();
  renderDashboardPeers();
  renderDashboardMaterials();
  renderAiMatches();
  renderRequests();
  renderGuilds();
  renderLeaderboard();
  renderUserAuthUI();
}

function renderDashboardSessions() {
  const container = document.getElementById('dashboard-sessions');
  if (!container) return;
  container.innerHTML = STATE.studySessions.map(s => `
    <div class="session-item">
      <div class="peer-header">
        <div class="peer-info">
          <div class="avatar-circle">${s.peer[0]}</div>
          <div class="peer-details">
            <h4>${s.title}</h4>
            <span>With <strong>${s.peer}</strong> • ${s.time}</span>
          </div>
        </div>
        <span class="badge badge-cyan">${s.mode}</span>
      </div>
    </div>
  `).join('');
}

function renderDashboardPeers() {
  const container = document.getElementById('dashboard-peers');
  if (!container) return;
  container.innerHTML = STATE.peers.slice(0, 3).map(p => `
    <div class="peer-item">
      <div class="peer-header">
        <div class="peer-info">
          <div class="avatar-circle">${p.name[0]}</div>
          <div class="peer-details">
            <h4>${p.name}</h4>
            <span>${p.subject} • ${p.courses.join(', ')}</span>
          </div>
        </div>
        <button class="btn btn-sm btn-primary" onclick="sendStudyInvite('${p.name}')">
          Match ${p.match}
        </button>
      </div>
    </div>
  `).join('');
}

function renderDashboardMaterials() {
  const container = document.getElementById('dashboard-materials');
  if (!container) return;
  container.innerHTML = STATE.materials.map(m => `
    <div class="material-card">
      <div>
        <span class="material-tag">${m.course} • ${m.category}</span>
        <h4>${m.title}</h4>
        <p>Verified study notes uploaded by ${m.author}. High peer rating.</p>
      </div>
      <div class="material-footer">
        <span><i data-lucide="download"></i> ${m.downloads} downloads</span>
        <button class="text-btn" onclick="downloadMaterial('${m.title}')">Read PDF</button>
      </div>
    </div>
  `).join('');
}

function renderAiMatches(filter = 'ALL') {
  const container = document.getElementById('aimatch-results');
  if (!container) return;
  const list = filter === 'ALL' ? STATE.peers : STATE.peers.filter(p => p.courses.some(c => c.replace(/\s+/g, '') === filter));
  container.innerHTML = list.map(p => `
    <div class="peer-item" style="padding: 18px;">
      <div class="peer-header mb-3">
        <div class="peer-info">
          <div class="avatar-circle" style="width: 46px; height: 46px;">${p.name[0]}</div>
          <div class="peer-details">
            <h4>${p.name} ${p.verified ? '<span class="badge badge-emerald">Verified</span>' : ''}</h4>
            <span>${p.subject}</span>
          </div>
        </div>
        <span class="match-score">${p.match} Match</span>
      </div>
      <p style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 14px;">
        Enrolled in <strong>${p.courses.join(', ')}</strong>. High collaborative activity on campus.
      </p>
      <button class="btn btn-primary btn-block" onclick="sendStudyInvite('${p.name}')">
        Request Study Pairing
      </button>
    </div>
  `).join('');
  lucide.createIcons();
}

function filterAiMatches() {
  const val = document.getElementById('course-filter').value;
  renderAiMatches(val);
}

function renderRequests() {
  const container = document.getElementById('requests-list-container');
  if (!container) return;
  const badge = document.getElementById('badge-requests');
  if (badge) badge.innerText = STATE.requests.length;

  if (STATE.requests.length === 0) {
    container.innerHTML = `<p style="color: var(--text-muted); text-align: center; padding: 24px;">No pending study requests right now.</p>`;
    return;
  }
  container.innerHTML = STATE.requests.map(r => `
    <div class="request-card" id="req-${r.id}">
      <div class="peer-header">
        <div class="peer-info">
          <div class="avatar-circle">${r.from[0]}</div>
          <div class="peer-details">
            <h4>${r.from} requests review for <strong>${r.course}</strong></h4>
            <span>Topic: "${r.topic}" • ${r.time}</span>
          </div>
        </div>
        <div style="display: flex; gap: 8px;">
          <button class="btn btn-sm btn-success" onclick="acceptRequest('${r.id}', '${r.from}')">Accept</button>
          <button class="btn btn-sm btn-danger-outline" onclick="declineRequest('${r.id}')">Decline</button>
        </div>
      </div>
    </div>
  `).join('');
}

function renderGuilds() {
  const container = document.getElementById('guilds-container');
  if (!container) return;
  container.innerHTML = STATE.guilds.map(g => `
    <div class="guild-card">
      <div class="peer-header mb-3">
        <h4>${g.name}</h4>
        <span class="pill pill-violet">${g.members} Scholars</span>
      </div>
      <p style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 16px;">
        ${g.desc}
      </p>
      <button class="btn btn-outline btn-block" onclick="joinGuild('${g.name}')">
        Join Circle
      </button>
    </div>
  `).join('');
}

function renderLeaderboard() {
  const container = document.getElementById('leaderboard-container');
  if (!container) return;
  container.innerHTML = STATE.leaderboard.map(item => `
    <div class="rank-row">
      <div style="display: flex; align-items: center; gap: 14px;">
        <span class="rank-number ${item.rank <= 3 ? 'rank-top' : ''}">#${item.rank}</span>
        <div>
          <strong style="display: block; font-size: 0.95rem;">${item.name}</strong>
          <span style="font-size: 0.75rem; color: var(--text-muted);">${item.uni}</span>
        </div>
      </div>
      <div style="display: flex; align-items: center; gap: 16px;">
        <span style="font-size: 0.8rem; color: var(--text-secondary);">${item.reviews} Reviews</span>
        <span class="badge badge-cyan" style="font-size: 0.85rem; padding: 4px 10px;">${item.karma} pts</span>
      </div>
    </div>
  `).join('');
}

function renderUserAuthUI() {
  const userLabel = document.getElementById('nav-user-label');
  if (userLabel) userLabel.innerText = STATE.user.isLoggedIn ? STATE.user.name.split(' ')[0] : 'Sign In';

  const nameEl = document.getElementById('profile-display-name');
  const emailEl = document.getElementById('profile-email');
  if (nameEl) nameEl.innerText = STATE.user.name;
  if (emailEl) emailEl.innerText = STATE.user.email;
}

// User Actions
function sendStudyInvite(peerName) {
  alert(`Study invitation sent to ${peerName}! They will receive a notification in their collegiate feed.`);
}

function downloadMaterial(title) {
  alert(`Opening "${title}" in web reader.`);
}

function acceptRequest(id, fromName) {
  STATE.requests = STATE.requests.filter(r => r.id !== id);
  renderRequests();
  alert(`Accepted session with ${fromName}! A calendar invite has been scheduled.`);
}

function declineRequest(id) {
  STATE.requests = STATE.requests.filter(r => r.id !== id);
  renderRequests();
}

function joinGuild(name) {
  alert(`You have joined "${name}"! Check the guilds channel for updates.`);
}

function handleFileSelect(e) {
  const file = e.target.files[0];
  if (file) {
    document.getElementById('selected-file-name').innerText = `Selected: ${file.name} (${(file.size / (1024*1024)).toFixed(2)} MB)`;
  }
}

function handleUploadNote(e) {
  e.preventDefault();
  const course = document.getElementById('note-course').value;
  const title = document.getElementById('note-title').value;
  const category = document.getElementById('note-category').value;

  STATE.materials.unshift({
    id: `m_${Date.now()}`,
    title,
    course,
    author: STATE.user.name,
    category,
    downloads: 1
  });

  STATE.user.karma += 50;
  localStorage.setItem('kc_user', JSON.stringify(STATE.user));
  document.getElementById('stat-points').innerText = STATE.user.karma;

  alert(`Published! +50 Scholar Karma earned.`);
  e.target.reset();
  document.getElementById('selected-file-name').innerText = '';
  renderDashboardMaterials();
  navigateTo('dashboard');
}

// Auth Handlers
function switchAuthTab(tab) {
  document.querySelectorAll('.auth-tab-btn').forEach(b => b.classList.remove('active'));
  document.getElementById(`tab-btn-${tab}`).classList.add('active');

  document.getElementById('form-signin').style.display = tab === 'signin' ? 'block' : 'none';
  document.getElementById('form-signup').style.display = tab === 'signup' ? 'block' : 'none';
  document.getElementById('view-account').style.display = tab === 'account' ? 'block' : 'none';
  document.getElementById('auth-alert').style.display = 'none';
  lucide.createIcons();
}

function showAlert(message, isError = false) {
  const el = document.getElementById('auth-alert');
  el.className = `alert-box ${isError ? 'alert-error' : 'alert-success'}`;
  el.innerText = message;
  el.style.display = 'block';
}

function handleSignIn(e) {
  e.preventDefault();
  const email = document.getElementById('signin-email').value;
  STATE.user = {
    isLoggedIn: true,
    name: email.split('@')[0].replace('.', ' ').toUpperCase(),
    email,
    university: 'Collegiate Member',
    karma: 1520,
    role: 'Verified Scholar'
  };
  localStorage.setItem('kc_user', JSON.stringify(STATE.user));
  showAlert(`Signed in successfully as ${email}!`);
  renderUserAuthUI();
  setTimeout(() => navigateTo('dashboard'), 800);
}

function handleSignUp(e) {
  e.preventDefault();
  const name = document.getElementById('signup-name').value;
  const email = document.getElementById('signup-email').value;
  STATE.user = {
    isLoggedIn: true,
    name,
    email,
    university: 'Collegiate Member',
    karma: 1000,
    role: 'New Scholar'
  };
  localStorage.setItem('kc_user', JSON.stringify(STATE.user));
  showAlert(`Account created successfully for ${email}! Welcome to KnowConnect.`);
  renderUserAuthUI();
  setTimeout(() => navigateTo('dashboard'), 800);
}

function handleGoogleSignIn() {
  STATE.user = {
    isLoggedIn: true,
    name: 'Alex Rivera (Google)',
    email: 'alex.rivera@berkeley.edu',
    university: 'UC Berkeley',
    karma: 1600,
    role: 'Verified Scholar'
  };
  localStorage.setItem('kc_user', JSON.stringify(STATE.user));
  showAlert(`Signed in with Google Account!`);
  renderUserAuthUI();
  setTimeout(() => navigateTo('dashboard'), 800);
}

function handleForgotPassword() {
  const email = prompt('Enter your university email to send password recovery link:');
  if (email) {
    showAlert(`Firebase password recovery link sent to ${email}. Check your inbox!`);
  }
}

function handleSignOut() {
  STATE.user = {
    isLoggedIn: false,
    name: 'Guest Scholar',
    email: 'guest@campus.edu',
    university: 'Guest',
    karma: 0,
    role: 'Guest'
  };
  localStorage.setItem('kc_user', JSON.stringify(STATE.user));
  renderUserAuthUI();
  switchAuthTab('signin');
  showAlert('Signed out of active session.');
}
