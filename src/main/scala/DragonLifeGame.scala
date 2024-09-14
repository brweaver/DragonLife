import org.cosplay.*
import org.cosplay.games.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.prefabs.scenes.CPFadeShimmerLogoScene

val BLUE_BLACK = CPColor("0x00000F")
val BG_PX = '.'&&(C_GRAY18, BLUE_BLACK)
//var audioOn = true // By default, the audio is ON.

object DragonLifeGame:
  /**
   * Entry point for JVM runtime.
   *
   * @param args Ignored.
   */
  def main(args: Array[String]): Unit =
    val gameInfo = CPGameInfo(name = "Dragon Life")

    // Initialize the engine.
    CPEngine.init(gameInfo, System.console() == null || args.contains("emuterm"))

    // Start the game & wait for exit.
    try
      CPEngine.startGame(
        new CPFadeShimmerLogoScene("logo", None, BG_PX, CS, "title"),
        //DragonLifePlayScene,
      )
    finally CPEngine.dispose()

    sys.exit(0)
