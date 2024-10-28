import objects.Pig
import org.cosplay.CPKeyboardKey._
import org.cosplay._
import org.cosplay.CPArrayImage._
import org.cosplay.CPColor.{C_DARK_CYAN, C_GRAY18}
import org.cosplay.CPPixel._

object DragonLifePlayScene extends CPScene("play", None, BG_PX):

  private val BIRD_COLOR: CPColor = CPColor("0x0000ff")
  private val BIRD_EYE_COLOR: CPColor = CPColor("0x00ff00")
  private val BIRD_BEAK_COLOR: CPColor = CPColor("0xff0000")

  private var xLocation = 10
  private var yLocation = 10
  private var xSpeed = 2
  private var ySpeed = 2
  private var dead = false


  private var debugLabelSprite = new CPLabelSprite(5, 2, 0, "Testing", C_DARK_CYAN)

  private val birdImgs = new CPArrayImage(
    prepSeq(
      """
         |          ,  ,
         |          \\ \\
         |         ) \\ \\     _p_
         |          )^\))\))  /  *\
         |           \_|| || / /^`-'
         |  __       -\ \\--/ /
         |<'  \\___/   ___. )'
         |     `====\ )___/\\
         |           //    //
         |          `"     `"
      """
    ).filter(!_.endsWith("------")),
    (ch, _, _) => ch match
      case '^' | '-' => ch & BIRD_EYE_COLOR
      case '>' => ch & BIRD_BEAK_COLOR
      case _ => ch & BIRD_COLOR
  ).trimBg().split(26, 10)

  private val birdAnis = Seq(CPAnimation.filmStrip("ani", 100.ms, imgs = birdImgs))
  private val birdSpr = new CPAnimationSprite("bird", anis = birdAnis, x = 15, y = 5, z = 10, "ani", false):
    private var cnt = 0L

    override def update(ctx: CPSceneObjectContext): Unit =
      super.update(ctx)

      cnt += 1

      if !dead then
        val canv = ctx.getCanvas

        if cnt > CPEngine.fps then // Give it a second...
          ctx.getKbEvent match
            case Some(evt) =>
              evt.key match
                case KEY_UP =>
                  yLocation -= ySpeed
                case KEY_DOWN =>
                  if (yLocation < 30) yLocation += ySpeed
                case KEY_RIGHT =>
                  xLocation += xSpeed
                case KEY_LEFT =>
                  xLocation -= xSpeed
                case KEY_LO_K | KEY_UP_K =>
                  dead = true
                case _ => ()
            case None => ()
        debugLabelSprite.setText("Location: " + xLocation + ", " + yLocation)
        setXY(xLocation, yLocation)


  addObjects(
    // Exit the game on 'Q' press.
    //CPKeyboardSprite(_.exitGame(), KEY_LO_Q, KEY_UP_Q),
    birdSpr,
    debugLabelSprite,
    new Pig().sprite
    // Toggle audio on 'Ctrl+A' press.
    //CPKeyboardSprite(_ => toggleAudio(), KEY_CTRL_A),
    // Scene-wide shader holder.
    //new CPOffScreenSprite(shaders = CPFadeInShader(true, 1000.ms, BG_PX).seq)
  )









