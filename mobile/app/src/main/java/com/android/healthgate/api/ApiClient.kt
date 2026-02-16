package com.android.healthgate.api

import com.android.healthgate.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object ApiClient {
    // Use 10.0.2.2 for Android emulator to reach host machine's localhost
    private const val BASE_URL = "http://10.0.2.2:8080/api"
    private val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()
    private val client = OkHttpClient()
    private val gson = Gson()

    interface ApiCallback<T> {
        fun onSuccess(result: T)
        fun onError(error: String)
    }

    fun login(email: String, password: String, callback: ApiCallback<LoginResponse>) {
        val body = gson.toJson(LoginRequest(email, password))
            .toRequestBody(JSON_MEDIA)

        val request = Request.Builder()
            .url("$BASE_URL/auth/login")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError("Network error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    try {
                        val loginResponse = gson.fromJson(responseBody, LoginResponse::class.java)
                        callback.onSuccess(loginResponse)
                    } catch (e: Exception) {
                        callback.onError("Failed to parse response")
                    }
                } else {
                    val errorMsg = try {
                        val jsonObj = gson.fromJson(responseBody, JsonObject::class.java)
                        jsonObj.get("message")?.asString ?: "Login failed (${response.code})"
                    } catch (e: Exception) {
                        "Login failed (${response.code})"
                    }
                    callback.onError(errorMsg)
                }
            }
        })
    }

    fun register(
        firstName: String, lastName: String,
        email: String, password: String, confirmPassword: String,
        callback: ApiCallback<ApiResponse>
    ) {
        val body = gson.toJson(RegisterRequest(firstName, lastName, email, password, confirmPassword))
            .toRequestBody(JSON_MEDIA)

        val request = Request.Builder()
            .url("$BASE_URL/auth/register")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError("Network error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    try {
                        val apiResponse = gson.fromJson(responseBody, ApiResponse::class.java)
                        callback.onSuccess(apiResponse)
                    } catch (e: Exception) {
                        callback.onError("Failed to parse response")
                    }
                } else {
                    val errorMsg = try {
                        val jsonObj = gson.fromJson(responseBody, JsonObject::class.java)
                        jsonObj.get("message")?.asString ?: "Registration failed (${response.code})"
                    } catch (e: Exception) {
                        "Registration failed (${response.code})"
                    }
                    callback.onError(errorMsg)
                }
            }
        })
    }

    fun getProfile(token: String, callback: ApiCallback<User>) {
        val request = Request.Builder()
            .url("$BASE_URL/user/me")
            .addHeader("Authorization", "Bearer $token")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError("Network error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    try {
                        // Backend returns ApiResponse { success, message, data: { user fields } }
                        val jsonObj = gson.fromJson(responseBody, JsonObject::class.java)
                        val dataObj = jsonObj.getAsJsonObject("data")
                        val user = gson.fromJson(dataObj, User::class.java)
                        callback.onSuccess(user)
                    } catch (e: Exception) {
                        callback.onError("Failed to parse profile")
                    }
                } else {
                    callback.onError("Session expired. Please log in again.")
                }
            }
        })
    }

    fun logout(token: String, callback: ApiCallback<String>) {
        val request = Request.Builder()
            .url("$BASE_URL/auth/logout")
            .addHeader("Authorization", "Bearer $token")
            .post("".toRequestBody(JSON_MEDIA))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Still clear session locally even if network fails
                callback.onSuccess("Logged out")
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess("Logged out")
            }
        })
    }
}
