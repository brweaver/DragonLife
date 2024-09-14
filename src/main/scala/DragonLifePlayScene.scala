import org.cosplay.CPKeyboardKey.*
import org.cosplay.games.*
import org.cosplay.games.pong.CPPongPlayScene.addObjects
import org.cosplay.prefabs.shaders.*
import org.cosplay.*

object DragonLifePlayScene extends CPScene("play", None, BG_PX):
  addObjects(
    // Exit the game on 'Q' press.
    CPKeyboardSprite(_.exitGame(), KEY_LO_Q, KEY_UP_Q),
    // Toggle audio on 'Ctrl+A' press.
    //CPKeyboardSprite(_ => toggleAudio(), KEY_CTRL_A),
    // Scene-wide shader holder.
    new CPOffScreenSprite(shaders = CPFadeInShader(true, 1000.ms, BG_PX).seq)
  )







