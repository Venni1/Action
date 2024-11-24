# Action
because XML animations could not
## Installation
It's easy. Just copy Action.kt to the "java/com/lox/action" folder.
## Usage
```kotlin
import com.lox.action.Action
animationImageView = AppCompatImageView(this).apply( { scaleType = ImageView.ScaleType.CENTER_CROP } )
action = Action(context = this, imageView = animationImageView, configFilePath = "animation/anim_file.json")
action.start()
setContent {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(Modifier.align(Alignment.Center).fillMaxSize().zIndex(0f)) {
            AndroidView(
                factory = { animationImageView },
                modifier = Modifier.fillMaxSize()
            )
```
You must also have an animation file. Here's an example of one.
```json
{
  "frames": [
    {
      "duration": 75,
      "file": "0.jpg"
    },
    {
      "duration": 75,
      "file": "1.jpg"
    },
    {
      "duration": 75,
      "file": "2.jpg"
    },
    {
      "duration": 75,
      "file": "3.jpg"
    }
  ],
  "repeat": true,
  "useBackground": false
}
```