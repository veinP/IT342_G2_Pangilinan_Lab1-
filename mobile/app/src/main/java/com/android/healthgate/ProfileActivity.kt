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

class ProfileActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvInitials = findViewById<TextView>(R.id.tv_avatar_initials)
        val tvName = findViewById<TextView>(R.id.tv_profile_name)
        val tvEmail = findViewById<TextView>(R.id.tv_profile_email)
        val tvDetailId = findViewById<TextView>(R.id.tv_detail_id)
        val tvDetailEmail = findViewById<TextView>(R.id.tv_detail_email)
        val tvDetailFirstName = findViewById<TextView>(R.id.tv_detail_first_name)
        val tvDetailLastName = findViewById<TextView>(R.id.tv_detail_last_name)
        val tvDetailCreatedAt = findViewById<TextView>(R.id.tv_detail_created_at)
        val progressProfile = findViewById<ProgressBar>(R.id.progress_profile)

        // Nav bar
        val navWelcome = findViewById<TextView>(R.id.nav_welcome)
        val navLogout = findViewById<TextView>(R.id.nav_logout)

        navLogout.setOnClickListener { showLogoutDialog() }

        // Load profile
        loadProfile(
            tvInitials, tvName, tvEmail,
            tvDetailId, tvDetailEmail, tvDetailFirstName, tvDetailLastName, tvDetailCreatedAt,
            navWelcome, progressProfile
        )
    }

    private fun loadProfile(
        tvInitials: TextView, tvName: TextView, tvEmail: TextView,
        tvDetailId: TextView, tvDetailEmail: TextView,
        tvDetailFirstName: TextView, tvDetailLastName: TextView,
        tvDetailCreatedAt: TextView, navWelcome: TextView, progress: ProgressBar
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

                    // Avatar initials
                    val initials = "${result.firstName.firstOrNull() ?: ""}${result.lastName.firstOrNull() ?: ""}"
                    tvInitials.text = initials.uppercase()

                    tvName.text = "${result.firstName} ${result.lastName}"
                    tvEmail.text = result.email

                    navWelcome.text = "Welcome, ${result.firstName}!"

                    tvDetailId.text = result.id.toString()
                    tvDetailEmail.text = result.email
                    tvDetailFirstName.text = result.firstName
                    tvDetailLastName.text = result.lastName
                    tvDetailCreatedAt.text = formatDate(result.createdAt)
                }
            }

            override fun onError(error: String) {
                runOnUiThread {
                    progress.visibility = View.GONE
                    SessionManager.clearSession(this@ProfileActivity)
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

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(64, 48, 64, 48)
            background = resources.getDrawable(R.drawable.bg_card, null)
            elevation = 8f
        }

        val title = TextView(this).apply {
            text = getString(R.string.confirm_logout)
            textSize = 20f
            setTextColor(resources.getColor(R.color.hg_navy, null))
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        }

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
                        SessionManager.clearSession(this@ProfileActivity)
                        goToLogin()
                    }
                }
                override fun onError(error: String) {
                    runOnUiThread {
                        SessionManager.clearSession(this@ProfileActivity)
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
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
