package com.lox.action
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import org.json.JSONObject
import java.io.InputStream

// Action - because XML animations could not
class Action (
    private val context: Context,
    private val imageView: ImageView,
    private val configFilePath: String = "animation/config.json")
{
    private val handler = Handler(Looper.getMainLooper())
    private val frames = mutableListOf<Drawable>()
    private var frameDurations = mutableListOf<Int>()
    private var repeatAnimation = false
    private var currentFrame = 0
    private var isAnimating = false


    init {
        loadConfig()
    }

    private fun loadConfig() {
        try {
            val configStream: InputStream = context.assets.open(configFilePath)
            val configString = configStream.bufferedReader().use { it.readText() }
            val jsonConfig = JSONObject(configString)

            // Load background image (or not)
            if (jsonConfig.getBoolean("useBackground")) {
                val backgroundImage = jsonConfig.getString("backgroundImage")
                val backgroundDrawable = loadDrawableFromAssets("animation/$backgroundImage")
                imageView.background = backgroundDrawable
            }

            // Load frames
            val jsonFrames = jsonConfig.getJSONArray("frames")
            for (i in 0 until jsonFrames.length()) {
                val frame = jsonFrames.getJSONObject(i)
                val frameFile = frame.getString("file")
                val frameDuration = frame.getInt("duration")

                loadDrawableFromAssets("animation/$frameFile")?.let { frames.add(it) }

                frameDurations.add(frameDuration)
            }

            // Set repeat option
            repeatAnimation = jsonConfig.getBoolean("repeat")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadDrawableFromAssets(assetPath: String): Drawable? {
        val inputStream = context.assets.open(assetPath)
        return Drawable.createFromStream(inputStream, null)
    }

    fun start() {
        isAnimating = true
        animateFrame()
    }
    fun stop() {
        isAnimating = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun animateFrame() {
        if (!isAnimating || frames.isEmpty()) return

        // Set current frame
        imageView.setImageDrawable(frames[currentFrame])

        // Schedule next frame
        handler.postDelayed({
            currentFrame = (currentFrame + 1) % frames.size
            if (currentFrame == 0 && !repeatAnimation) {
                stop()
                imageView.setImageDrawable(loadDrawableFromAssets("animation/well-done/no-animation/0.jpg"))
            } else {
                animateFrame()
            }
        }, frameDurations[currentFrame].toLong())
    }
}