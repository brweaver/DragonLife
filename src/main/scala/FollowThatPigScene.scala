import background.{BrickImage, PalmTreeImage, PigAnimation}
import objects.Pig
import org.cosplay.CPKeyboardKey.*
import org.cosplay.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.prefabs.shaders.{CPFadeInShader, CPFadeOutShader}

val bgW = 200
val bgH = 40
val dim = CPDim(bgW, bgH)

object FollowThatPigScene extends CPScene("play", Some(dim), BG_PX):

  val bgPx = ' ' && (C_BLACK, C_GRAY1)



  // Background Stars & Mountains Sprite
  val bgCanv = CPCanvas(dim, bgPx)
  Engine.paintStars(bgCanv)
  Engine.paintMountains(bgCanv)
  val fiShdr = new CPFadeInShader(entireFrame = true, durMs = 500.ms, BG_PX)
  val foShdr = new CPFadeOutShader(true, 300, BG_PX, _.exitGame())

  val bgSpr = new CPImageSprite("bg", img = bgCanv.capture(), false, Seq(fiShdr, foShdr)):
    private var lastCamFrame: CPRect = _
    private var xf = initX.toFloat

    override def getX: Int = xf.round

    override def render(ctx: CPSceneObjectContext): Unit =
      val camFrame = ctx.getCameraFrame
      // Produce parallax effect for background sprite.
      if lastCamFrame != null && lastCamFrame.w == camFrame.w then
        xf -= (lastCamFrame.x - camFrame.x).sign * 0.7f
      lastCamFrame = ctx.getCameraFrame
      super.render(ctx)

  // Brick Sprite (Floor)
  val brickCanv = CPCanvas(CPDim(bgW, 3), bgPx)
  for i <- 0 until bgW / BrickImage.getWidth do brickCanv.drawImage(BrickImage, x = i * 5, y = 0, z = 2)
  val brickY = bgH - BrickImage.getHeight
  val brickSpr = new CPStaticImageSprite("bricks", 0, brickY, 2, brickCanv.capture())

  // Palm Trees (Foreground Sprite Sequence)
  val palmY = bgH - BrickImage.getHeight - PalmTreeImage.getHeight
  val palmSeq = for i <- 0 until 6 yield
    new CPStaticImageSprite(s"palm$i", CPRand.randInt(10, bgW - 10), palmY, 3, PalmTreeImage.trimBg())

  // Player
  private val animation = Seq(CPAnimation.filmStrip("ani", 100.ms, imgs = PigAnimation.replaceBg(_.char == 'x', CPPixel.XRAY).split(8,3)))
  //def sprite: CPAnimationSprite = new CPAnimationSprite(NAME, anis = animation, x = 15, y = 5, z = 10, "ani", false):
  val pigSpr = new CPAnimationSprite(id = "pig", anis = animation, x = 15, y = bgH - BrickImage.getHeight - 3, z = 4, "ani", false):
    private var x = initX.toFloat

    override def getX: Int = x.round

    override def update(ctx: CPSceneObjectContext): Unit =
      // NOTE: we don't need to override 'render(...)' method - it just stays default.
      super.update(ctx)
      ctx.getKbEvent match
        case Some(evt) => evt.key match
          // NOTE: if keyboard event is repeated (same key pressed in consecutive frames) -
          // we use smaller movement amount to smooth out the movement. If key press
          // is "new" we move the entire character position to avoid an initial "dead keystroke" feel.
          case KEY_LO_A | KEY_LEFT => x -= (if evt.isRepeated then 0.8f else 1.0f)
          case KEY_LO_D | KEY_RIGHT => x += (if evt.isRepeated then 0.8f else 1.0f)
          case _ => ()
        case None => ()


  // Other Objects
  var debugLabelSprite = new CPLabelSprite(5, 2, 0, "Testing", C_DARK_CYAN)

  // Set Camera Tracking
  getCamera.setFocusTrackId(Some("pig"))
  getCamera.setFocusFrameInsets(new CPInsets(hor = 40, vert = 0))

  var objs = List(
    bgSpr,
    brickSpr,
    pigSpr,
    debugLabelSprite,
    // On 'Q' press kick off fade out shader that will exit the game once it is finished.
    CPKeyboardSprite(_ => foShdr.start(), KEY_LO_Q, KEY_UP_Q),
  )
  objs ++= palmSeq

  addObjects(objs:_*)





