package com.tiktokbiz

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.facebook.react.module.annotations.ReactModule
import com.tiktok.TikTokBusinessSdk
import com.tiktok.appevents.base.TTBaseEvent

@ReactModule(name = TiktokBizModule.NAME)
class TiktokBizModule(reactContext: ReactApplicationContext) :
  NativeTiktokBizSpec(reactContext) {

  override fun getName(): String {
    return NAME
  }


  private val mainHandler = Handler(Looper.getMainLooper())
  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  override fun initializeTikTokSDK(appId: String, secret: String, promise: Promise) {
     try {
       mainHandler.post {
            val lifecycleOwner = currentActivity as? LifecycleOwner
            lifecycleOwner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
                override fun onResume(owner: LifecycleOwner) {
                     val ttConfig = TikTokBusinessSdk.TTConfig(reactApplicationContext)
                    .setAppId(appId) // Android package name or iOS listing ID, eg: com.sample.app (from Play Store) or 9876543 (from App Store)
                    .setTTAppId(secret) // TikTok App ID from TikTok Events Manager

                  TikTokBusinessSdk.initializeSdk(
                    ttConfig
                  )

                  TikTokBusinessSdk.startTrack()

                  promise.resolve(true)
                }
            })
        }


    } catch (e: Exception) {
      promise.reject(
        "TIKTOK_INIT_EXCEPTION",
        "Exception while initializing TikTok SDK",
        e
      )
    }
  }


  override fun trackEvent(eventName: String?, properties: ReadableMap?, promise: Promise?) {
    val eventInfo = TTBaseEvent.newBuilder(eventName)

    val iterator = properties?.keySetIterator()
    while (iterator != null && iterator.hasNextKey()) {
        val key = iterator.nextKey()
        when (properties.getType(key)) {
            ReadableType.Boolean -> {
                val value = properties.getBoolean(key)
                eventInfo.addProperty(key, value)
            }
            ReadableType.Number -> {
                val value = properties.getDouble(key)
                eventInfo.addProperty(key, value)
            }
            ReadableType.String -> {
                val value = properties.getString(key)
                eventInfo.addProperty(key, value)
            }
            ReadableType.Null -> {
                eventInfo.addProperty(key, null)
            }
            ReadableType.Map -> {
                // Handle map if needed
            }
            ReadableType.Array -> {
                // Handle array if needed
            }
        }
    }
    try {
      TikTokBusinessSdk.trackTTEvent(eventInfo.build())
      TikTokBusinessSdk.flush();
      promise?.resolve(true)
    } catch (e: Exception) {
      promise?.reject("TIKTOK_TRACK_ERROR", e.message, e)
    }
  }

  companion object {
    const val NAME = "TiktokBiz"
  }
}
