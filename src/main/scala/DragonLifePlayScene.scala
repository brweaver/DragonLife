import org.cosplay.CPKeyboardKey.*
import org.cosplay.games.*
import org.cosplay.games.pong.CPPongPlayScene.addObjects
import org.cosplay.prefabs.shaders.*
import org.cosplay.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPPixel.*

object DragonLifePlayScene extends CPScene("play", None, BG_PX):

  private val BIRD_COLOR: CPColor = CPColor("0x0000ff")
  private val BIRD_EYE_COLOR: CPColor = CPColor("0x00ff00")
  private val BIRD_BEAK_COLOR: CPColor = CPColor("0xff0000")

  private var xLocation = 10
  private var yLocation = 10
  private var dead = false

  private val birdImgs = new CPArrayImage(
    prepSeq(
      """
        | \\
        |( ^)>
        | //
        |------
        | //
        |( -)>
        | \\
        |------
        | }}
        |( o)>
        | }}
        |------
              """
    ).filter(!_.endsWith("------")),
    (ch, _, _) => ch match
      case '^' | '-' => ch & BIRD_EYE_COLOR
      case '>' => ch & BIRD_BEAK_COLOR
      case _ => ch & BIRD_COLOR
  ).split(5, 3)

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
                  yLocation -= 1
                case KEY_DOWN =>
                  yLocation += 1
                case KEY_RIGHT =>
                  xLocation += 1
                case KEY_LEFT =>
                  xLocation -= 1
                case KEY_LO_K | KEY_UP_K =>
                  dead = true
                case _ => ()
            case None => ()
        setXY(xLocation, yLocation)


  addObjects(
    // Exit the game on 'Q' press.
    CPKeyboardSprite(_.exitGame(), KEY_LO_Q, KEY_UP_Q),
    birdSpr,
    // Toggle audio on 'Ctrl+A' press.
    //CPKeyboardSprite(_ => toggleAudio(), KEY_CTRL_A),
    // Scene-wide shader holder.
    new CPOffScreenSprite(shaders = CPFadeInShader(true, 1000.ms, BG_PX).seq)
  )









