import org.cosplay._
import org.cosplay.CPColor._
import org.cosplay.CPPixel.&&

val BLUE_BLACK: CPColor = CPColor("0x00015F")
val BG_PX: CPPixel = ' ' && (C_GRAY18, BLUE_BLACK)

object DragonLifeGame:
  /**
   * Entry point for JVM runtime.
   *
   * @param args Ignored.
   */
  def main(args: Array[String]): Unit =
    val gameInfo = CPGameInfo(name = "Dragon Life", initDim = Some(new CPDim(80,40)))

    // Initialize the engine.
    CPEngine.init(gameInfo, System.console() == null || args.contains("emuterm"))

    // Start the game & wait for exit.
    try
      CPEngine.startGame(
        //new CPFadeShimmerLogoScene("logo", None, BG_PX, CS, "title"),
        //DragonLifePlayScene,
        FollowThatPigScene
      )
    finally CPEngine.dispose()

    sys.exit(0)
