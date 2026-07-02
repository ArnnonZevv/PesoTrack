# PesoTracker — Android (Kotlin)

Talks to the **same Spring Boot backend** from Part 1 (`/api/auth/register`, `/api/auth/login`) — no new backend work needed.

## 1. Open in Android Studio
File → Open → select the `PesoTrackerMobile` folder → let Gradle sync (first sync downloads dependencies, takes a few minutes).

## 2. Make sure your backend is running
Same as before — run `HellosummerApplication` in IntelliJ with your four environment variables set (DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET). It needs to be listening on `localhost:8080` **before** you test the app.

## 3. Network address — read this carefully
`RetrofitClient.kt` points to:
```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/"
```
`10.0.2.2` is a special address that **only works from the Android Emulator** — it's the emulator's alias for "your computer's localhost." If you run the app that way, no changes needed.

**If you test on a real physical phone instead:**
1. Find your computer's local IP (Windows: `ipconfig`, look for IPv4 Address, e.g. `192.168.1.23`)
2. Your phone and computer must be on the **same Wi-Fi network**
3. Change `BASE_URL` to `"http://192.168.1.23:8080/"` (your actual IP)
4. Your backend's `SecurityConfig` CORS settings may need to allow this origin — if login/register calls fail with a network error, check that first

## 4. Run it
Select an emulator (or plug in your phone with USB debugging enabled) → click the green ▶ Run button in Android Studio. First screen shown is the Login screen (it's set as the launcher activity).

## 5. Test the flow
1. Tap "Don't have an account? Create one" → fill in the registration form → submit
2. Should land on the Dashboard showing your name — **screenshot this**
3. Tap "Log out" → log back in with the same credentials → **screenshot the dashboard again**
4. Check Supabase's Table Editor — the same `users` table from Part 1 should now also contain any users you registered from the phone/emulator — **screenshot this** as your database/API connection proof

## 6. If registration/login calls fail with a network error
- Confirm the backend is actually running and reachable at `http://localhost:8080` in a normal browser first
- Double check `BASE_URL` matches emulator (`10.0.2.2`) vs real device (your LAN IP) as described above
- Check Logcat in Android Studio (View → Tool Windows → Logcat) — the `OkHttp` logging interceptor included in this project prints the full request/response, which will show you exactly what's failing

## Project structure
```
app/src/main/java/edu/cit/pangan/pesotracker/
  data/       — RegisterRequest, LoginRequest, AuthResponse, SessionManager (JWT storage)
  network/    — ApiService (Retrofit interface), RetrofitClient (singleton)
  ui/         — LoginActivity, RegisterActivity, DashboardActivity
app/src/main/res/layout/
  activity_login.xml, activity_register.xml, activity_dashboard.xml
```

## GitHub
Same idea as Part 1 — commit as you go rather than all at once, to show real development progress:
```bash
cd PesoTrackerMobile
git init
git add .
git commit -m "Initial commit: Android project setup"
git commit -am "Add login and register screens"
git commit -am "Wire up Retrofit API calls to backend"
git branch -M main
git remote add origin https://github.com/<your-username>/pesotracker-mobile.git
git push -u origin main
```
