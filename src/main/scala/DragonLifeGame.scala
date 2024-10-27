import org.cosplay._
import org.cosplay.CPColor._


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
        //new CPFadeShimmerLogoScene("logo", None, BG_PX, CS, "title"),
        DragonLifePlayScene,
      )
    finally CPEngine.dispose()

    sys.exit(0)
