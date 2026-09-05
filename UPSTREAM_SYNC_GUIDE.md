# BitChord (Apple Music Edition) — Upstream Sync & Maintenance Guide

This document is a step-by-step instruction protocol for developers and AI agents (such as Antigravity / Claude / Cursor) to pull the latest upstream updates from the original BitChord repository while **strictly preserving all Apple Music UI design, icons, red theme (#FA2D48), splash screen, and custom layout enhancements**.

---

## 1. Architecture & Git Remote Setup

| Remote Name | Repository URL | Purpose |
| :--- | :--- | :--- |
| **`origin`** | `https://github.com/febin52/BitChord.git` | Your personal fork & Apple Music Edition repository |
| **`upstream`** | `https://github.com/kushagrasinghx/BitChord.git` | Official BitChord open-source repository |

### Ensure Remotes are Configured
```bash
# Verify existing remotes
git remote -v

# If upstream is missing:
git remote add upstream https://github.com/kushagrasinghx/BitChord.git
```

---

## 2. Core Apple Music UI Assets & Checklist (Must NEVER Be Overwritten)

When resolving conflicts or reviewing merges, ensure these exact components remain intact:

### A. Theme & Red Accent Color
- **File**: `app/src/main/java/com/music/bitchord/ui/theme/Theme.kt`
- **Rule**: `primary` in both `DarkColors` and `LightColors` MUST be `AccentRed = Color(0xFFFA2D48)`.
- **Rule**: `BitChordTheme` alias must wrap `AppleMusicTheme`.

### B. Navigation Bar & Tabs
- **File**: `app/src/main/java/com/music/bitchord/ui/components/FloatingBottomBar.kt` & `MainActivity.kt`
- **Tabs**: Exactly 5 tabs in this order:
  1. `Home` (`TAB_HOME = 0`)
  2. `New / Browse` (`TAB_EXPLORE = 1`) -> `ExploreScreen`
  3. `Radio` (`TAB_RADIO = 2`) -> `RadioScreen`
  4. `Library` (`TAB_LIBRARY = 3`) -> `LibraryScreen`
  5. `Search` (`TAB_SEARCH = 4`) -> `SearchScreen`
- **Drawables**:
  - `ic_tab_home.xml` & `ic_tab_home_selected.xml`
  - `ic_tab_new.xml` & `ic_tab_new_selected.xml`
  - `ic_tab_radio.xml` & `ic_tab_radio_selected.xml`
  - `ic_tab_library.xml` & `ic_tab_library_selected.xml`
  - `ic_tab_search.xml` & `ic_tab_search_selected.xml`

### C. Library Screen Categories
- **File**: `app/src/main/java/com/music/bitchord/ui/screens/LibraryScreen.kt`
- **Drawables**:
  - `ic_library_playlists.xml`
  - `ic_library_artists.xml`
  - `ic_library_albums.xml`
  - `ic_library_songs.xml`
  - `ic_library_tv.xml`
  - `ic_library_downloaded.xml`

### D. Splash Screen & Launcher Icons
- **File**: `app/src/main/java/com/music/bitchord/ui/splash/SplashScreen.kt` using `apple_music_logo.png`
- **Launcher Icons**: `app/src/main/res/mipmap-*/ic_launcher_foreground.png`

### E. MiniPlayer & Navigation Transport Icons
- **Files**: `app/src/main/java/com/music/bitchord/ui/components/MiniPlayer.kt` and `GlassNavBar.kt`
- **Next Button**: Must use double-triangle `Icons.Rounded.FastForward` (not `SkipNext`).
- **Play/Pause Button**: `Icons.Rounded.PlayArrow` / `Icons.Rounded.Pause`.
- **Dimensions**: Glyph size `30.dp`, button spacing `2.dp`.

### F. Now Playing Screen
- **File**: `app/src/main/java/com/music/bitchord/ui/player/NowPlayingScreen.kt`
- **Top Bar**: Drag handle pill removed (clean strip).
- **Loader**: Circular progress spinner set to `34.dp` (stroke `2.5.dp`) inside fixed `74.dp` Box (`62.dp + 12.dp` matching the play button footprint so the progress bar never jumps).
- **Seekbar**: No "Lossless", "High Quality", or "Upgrading Quality" badges underneath the progress bar — only the timestamps.

### G. In-App Updates Disabled
- **Files**: `MainActivity.kt` and `MainViewModel.kt`
- **Rule**: `UpdateAvailableDialog`, top-bar update icon, and `AppUpdateChecker.check()` are removed so no popup or badges appear.

---

## 3. Step-by-Step AI Agent Sync Protocol

Instruct your AI agent to follow these exact steps when pulling upstream updates:

```text
Prompt to give AI Agent:
"Follow the instructions in UPSTREAM_SYNC_GUIDE.md to fetch the latest changes from upstream/main, merge them into main while preserving all Apple Music UI changes, build the optimized prodRelease APK, and install it on the connected device."
```

### Step 1: Pre-flight Safety Backup
```bash
# Ensure working tree is clean
git status

# Create a timestamped safety backup branch
git branch backup/pre-sync-$(date +%Y%m%d)
```

### Step 2: Fetch & Merge Upstream
```bash
git fetch upstream
git checkout main
git merge upstream/main
```

### Step 3: Conflict Resolution Strategy
If Git reports merge conflicts in any files:
1. **Backend & Services** (`playback/`, `data/`, `download/`, `smart/`): Accept upstream changes and bug fixes.
2. **UI Files** (`MainActivity.kt`, `Theme.kt`, `FloatingBottomBar.kt`, `LibraryScreen.kt`, `MiniPlayer.kt`, `GlassNavBar.kt`, `NowPlayingScreen.kt`, `SettingsSheet.kt`):
   - Keep Apple Music red color (`#FA2D48`), 5 custom tabs, custom icons, and refined layouts.
   - Integrate new ViewModel states or upstream features into the existing UI without breaking layout structure.

### Step 4: Build & Compile Verification
```bash
# Compile and build the optimized production release APK
./gradlew assembleProdRelease
```

### Step 5: Install & Test on Device
```bash
# Sideload and launch on connected Android device via ADB
adb install -r app/build/outputs/apk/prod/release/app-prod-arm64-v8a-release.apk
adb shell am start -n com.music.bitchord/.MainActivity

# Rename v8a release binary to AppleMusic.apk
cp app/build/outputs/apk/prod/release/app-prod-arm64-v8a-release.apk AppleMusic.apk

# Optionally push APK to device storage for manual installation
adb push AppleMusic.apk /sdcard/
adb push AppleMusic.apk /sdcard/Download/
```

### Step 6: Commit, Push, and Update GitHub Release
```bash
# Push merged main branch to your personal repo
git push origin main

# Update / create GitHub Release with the single AppleMusic.apk asset
gh release create <NEW_TAG> \
  AppleMusic.apk \
  --title "BitChord <NEW_TAG> (Apple Music Edition)" \
  --notes "Merged upstream updates with Apple Music UI enhancements." \
  -R febin52/BitChord
```
