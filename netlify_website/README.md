# 🌐 KnowConnect - Ready-to-Publish Netlify Website

This directory contains the **complete standalone website version** of **KnowConnect**. It is pre-configured and ready to be deployed to **Netlify** with zero command-line tools or app store requirements.

---

## 🚀 How to Publish on Netlify in 60 Seconds (Free & Instant)

### Option 1: Netlify Drop (Instant Drag & Drop)
1. **Download / Export your project:**
   - In Google AI Studio Build, click the **Settings / Menu (top right)** and select **Export as ZIP** (or download the `netlify_website` folder).
   - Unzip the downloaded file on your computer and locate the `netlify_website` folder (containing `index.html`, `styles.css`, `app.js`, and `netlify.toml`).

2. **Open Netlify Drop:**
   - Go to **[https://app.netlify.com/drop](https://app.netlify.com/drop)** in your web browser.
   - Log in or sign up for your free Netlify account.

3. **Drag & Drop:**
   - Drag the `netlify_website` folder into the Netlify Drop area.
   - In 5 to 10 seconds, Netlify will build your site and give you a public URL (e.g., `https://knowconnect-scholars.netlify.app`).

---

### Option 2: Connect via GitHub (Automatic Updates)
1. In AI Studio Build, click **Push to GitHub** from the top-right settings.
2. In your [Netlify Dashboard](https://app.netlify.com), click **Add new site** → **Import an existing project**.
3. Select your GitHub repository.
4. Set:
   - **Base directory:** `netlify_website`
   - **Publish directory:** `netlify_website`
5. Click **Deploy KnowConnect**. Whenever you make updates, Netlify will automatically redeploy!

---

## ✨ Features in this Website Version
- **Full-Screen Responsive Layout**: Runs natively in Chrome, Safari, Edge, and Firefox on PC, Mac, iPhone, and Android.
- **Top Bar & Mobile Navigation**: Desktop header with navigation tabs + native bottom touch bar on mobile phones.
- **AI Peer Matcher**: Course-based algorithm matching peers across STEM & Humanities.
- **Upload Materials**: Share lecture summaries, exam reviews, and cheat sheets with file upload support.
- **Study Guilds & Requests**: Accept or decline peer study invites, join academic circles.
- **Leaderboard**: Real-time collegiate karma point system.
- **Firebase Auth Ready**: Pre-wired with Firebase Auth & Google Sign-In interface for easy credential connection.
- **Obsidian Dark & Clean Light Themes**: Instant theme toggle with localStorage persistence.
