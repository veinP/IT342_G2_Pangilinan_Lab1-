package com.android.healthgate

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.android.healthgate.api.ApiClient
import com.android.healthgate.auth.SessionManager
import com.android.healthgate.model.User

class DashboardActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Profile fields
        val tvAvatarInitials = findViewById<TextView>(R.id.tv_avatar_initials)
        val tvFirstName = findViewById<TextView>(R.id.tv_first_name)
        val tvLastName = findViewById<TextView>(R.id.tv_last_name)
        val tvEmail = findViewById<TextView>(R.id.tv_email)
        val tvMemberSince = findViewById<TextView>(R.id.tv_member_since)
        val tvLastUpdated = findViewById<TextView>(R.id.tv_last_updated)
        val progressDashboard = findViewById<ProgressBar>(R.id.progress_dashboard)

        // Navbar: Welcome text + Logout (matching web exactly)
        val navWelcome = findViewById<TextView>(R.id.nav_welcome)
        val navLogout = findViewById<TextView>(R.id.nav_logout)

        navLogout.setOnClickListener { showLogoutDialog() }

        // Load profile data
        loadProfile(tvAvatarInitials, tvFirstName, tvLastName, tvEmail,
            tvMemberSince, tvLastUpdated, navWelcome, progressDashboard)
    }

    private fun loadProfile(
        tvInitials: TextView, tvFirstName: TextView, tvLastName: TextView,
        tvEmail: TextView, tvMemberSince: TextView, tvLastUpdated: TextView,
        navWelcome: TextView, progress: ProgressBar
    ) {
        val token = SessionManager.getToken(this)
        if (token == null) {
            goToLogin()
            return
        }

        progress.visibility = View.VISIBLE

        ApiClient.getProfile(token, object : ApiClient.ApiCallback<User> {
            override fun onSuccess(result: User) {
                runOnUiThread {
                    progress.visibility = View.GONE

                    // Avatar initials (matching web .profile-avatar)
                    val initials = "${result.firstName.firstOrNull() ?: ""}${result.lastName.firstOrNull() ?: ""}"
                    tvInitials.text = initials.uppercase()

                    // Navbar welcome text (matching web .welcome-text)
                    navWelcome.text = "Welcome, ${result.firstName}!"

                    // Profile fields
                    tvFirstName.text = result.firstName
                    tvLastName.text = result.lastName
                    tvEmail.text = result.email
                    tvMemberSince.text = formatDate(result.createdAt)
                    tvLastUpdated.text = formatDate(result.updatedAt)
                }
            }

            override fun onError(error: String) {
                runOnUiThread {
                    progress.visibility = View.GONE
                    SessionManager.clearSession(this@DashboardActivity)
                    goToLogin()
                }
            }
        })
    }

    private fun showLogoutDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setDimAmount(0.5f)

        // Dialog card: bg #D9D9D7 matching web .logout-card
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(64, 48, 64, 48)
            background = resources.getDrawable(R.drawable.bg_card, null)
            elevation = 8f
        }

        // Title: "Confirm Logout" matching web .logout-title
        val title = TextView(this).apply {
            text = getString(R.string.confirm_logout)
            textSize = 20f
            setTextColor(resources.getColor(R.color.hg_navy, null))
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        }

        // Message: matching web .logout-message
        val message = TextView(this).apply {
            text = getString(R.string.logout_message)
            textSize = 14f
            setTextColor(resources.getColor(R.color.hg_navy, null))
            gravity = Gravity.CENTER
            setPadding(0, 16, 0, 40)
        }

        val buttonRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        // Cancel button: bg #B1BBC7, text #2A4D87 matching web .btn-cancel
        val btnCancel = Button(this).apply {
            text = getString(R.string.cancel)
            textSize = 14f
            setTextColor(resources.getColor(R.color.hg_navy, null))
            background = resources.getDrawable(R.drawable.bg_button_cancel, null)
            isAllCaps = false
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            params.marginEnd = 12
            layoutParams = params
            setPadding(0, 24, 0, 24)
            setOnClickListener { dialog.dismiss() }
        }

        // Logout button: bg #7C94B8, white text matching web .btn-logout
        val btnLogout = Button(this).apply {
            text = getString(R.string.logout)
            textSize = 14f
            setTextColor(Color.WHITE)
            background = resources.getDrawable(R.drawable.bg_button_primary, null)
            isAllCaps = false
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            params.marginStart = 12
            layoutParams = params
            setPadding(0, 24, 0, 24)
            setOnClickListener {
                dialog.dismiss()
                performLogout()
            }
        }

        buttonRow.addView(btnCancel)
        buttonRow.addView(btnLogout)
        layout.addView(title)
        layout.addView(message)
        layout.addView(buttonRow)

        dialog.setContentView(layout)
        dialog.show()
    }

    private fun performLogout() {
        val token = SessionManager.getToken(this)
        if (token != null) {
            ApiClient.logout(token, object : ApiClient.ApiCallback<String> {
                override fun onSuccess(result: String) {
                    runOnUiThread {
                        SessionManager.clearSession(this@DashboardActivity)
                        goToLogin()
                    }
                }
                override fun onError(error: String) {
                    runOnUiThread {
                        SessionManager.clearSession(this@DashboardActivity)
                        goToLogin()
                    }
                }
            })
        } else {
            SessionManager.clearSession(this)
            goToLogin()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun formatDate(raw: String): String {
        return try {
            if (raw.contains("T")) {
                val parts = raw.split("T")[0].split("-")
                val months = arrayOf(
                    "", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                )
                val month = months[parts[1].toInt()]
                val day = parts[2].toInt()
                val year = parts[0]
                "$month $day, $year"
            } else raw
        } catch (e: Exception) {
            raw
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishAffinity()
    }
}
