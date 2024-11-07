import background.{BrickImage, PalmTreeImage, PigAnimation}
import objects.Pig
import org.cosplay.CPKeyboardKey.*
import org.cosplay.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.prefabs.shaders.{CPFadeInShader, CPFadeOutShader}
import java.lang.System.currentTimeMillis

object FollowThatPigScene extends CPScene("play", Some(DragonLifeGame.LEVEL_DIMENSIONS), DragonLifeGame.BG_PX):

  private val bgPx = ' ' && (C_BLACK, C_GRAY1)
  private val bgW = DragonLifeGame.LEVEL_WIDTH
  private val bgH = DragonLifeGame.LEVEL_HEIGHT

  // Background Stars & Mountains Sprite
  private val bgCanv = CPCanvas(DragonLifeGame.LEVEL_DIMENSIONS, bgPx)
  Engine.paintStars(bgCanv)
  Engine.paintMountains(bgCanv)
  private val fiShdr = new CPFadeInShader(entireFrame = true, durMs = 500.ms, DragonLifeGame.BG_PX)
  private val foShdr = new CPFadeOutShader(true, 300, DragonLifeGame.BG_PX, _.exitGame())

  private val bgSpr = new CPImageSprite("bg", img = bgCanv.capture(), false, Seq(fiShdr, foShdr)):
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
  private val brickCanv = CPCanvas(CPDim(bgW, 3), bgPx)
  for i <- 0 until bgW / BrickImage.getWidth do brickCanv.drawImage(BrickImage, x = i * 5, y = 0, z = 2)
  private val brickY = bgH - BrickImage.getHeight
  private val brickSpr = new CPStaticImageSprite("bricks", 0, brickY, 2, brickCanv.capture())

  // Palm Trees (Foreground Sprite Sequence)
  private val palmY = bgH - BrickImage.getHeight - PalmTreeImage.getHeight
  private val palmSeq = for i <- 0 until 6 yield
    new CPStaticImageSprite(s"palm$i", CPRand.randInt(10, bgW - 10), palmY, 3, PalmTreeImage.trimBg())

  // Player
  private val animation = Seq(CPAnimation.filmStrip("ani", 100.ms, imgs = PigAnimation.replaceBg(_.char == 'x', CPPixel.XRAY).split(8,3)))

  // Player Constants
  private val floor = bgH - BrickImage.getHeight - 3
  private val initJumpVelocity = 22.72f
  private val gravity = 51.65f
  private var friction = 3.5f
  private val maxVelocityY = 2*initJumpVelocity
  private val maxVelocityX = 10f
  private val initWalkVelocity = 2f
  private val incrementWalkVelocity = 0.8f

  private val pigSpr = new CPAnimationSprite(id = "pig", anis = animation, x = 15, y = floor, z = 4, "ani", false):
    private var x = initX.toFloat
    private var y = floor.toFloat
    private var xVelocity = 0f
    private var yVelocity = 0f
    private var wantsToJump = false
    private var isHoldingJump = false
    private var isGrounded = true
    private var isJumping = false
    private var isJumpPressed = false
    private var jumpElapsedTime = 0.toLong
    private var lastJumpTime = 0.toLong


    private var lastTime = currentTimeMillis()

    override def getX: Int = x.round
    override def getY: Int = y.round

    override def update(ctx: CPSceneObjectContext): Unit =
      // NOTE: we don't need to override 'render(...)' method - it just stays default.
      super.update(ctx)
      ctx.getKbEvent match
        case Some(evt) => evt.key match

          // NOTE: if keyboard event is repeated (same key pressed in consecutive frames) -
          // we use smaller movement amount to smooth out the movement. If key press
          // is "new" we move the entire character position to avoid an initial "dead keystroke" feel.
          case KEY_LO_A | KEY_LEFT => xVelocity -= (if evt.isRepeated then incrementWalkVelocity else initWalkVelocity)
          case KEY_LO_D | KEY_RIGHT => xVelocity += (if evt.isRepeated then incrementWalkVelocity else initWalkVelocity)
          case KEY_LO_W | KEY_UP => if evt.isRepeated && isJumping then isHoldingJump = true else isJumpPressed = true
          case _ => ()
        case None => ()

      // Set up the time elapsed since the last moment we were here.
      val currentTime = currentTimeMillis()
      val dt = (currentTime - lastTime) / 1000.0f
      lastTime = currentTime

      // Update velocities
      yVelocity = yVelocity - gravity * dt

      // Note, damping doesn't work with cosplay, because you can't hold buttons down well
      // However, consider adding this for sharp turning
      // if (isGrounded) then // friction only applies on the ground
      //  xVelocity = xVelocity / (1 + friction*dt)

      // Update positions
      y = y - yVelocity * dt // Note, jumping "negative" in this coordinate system i.e, zero is at the top.
      x = x + xVelocity * dt

      // Adjust for Max Velocity values
//      val velocityMagnitude = Math.sqrt(xVelocity * xVelocity * yVelocity * yVelocity).toFloat
//      if velocityMagnitude > maxVelocityMagnitude then
//        val velocityAdjustment = maxVelocityMagnitude / velocityMagnitude
//        xVelocity = xVelocity * velocityAdjustment
//        yVelocity = yVelocity * velocityAdjustment

      if Math.abs(xVelocity) > maxVelocityX then
        xVelocity = xVelocity * maxVelocityX / Math.abs(xVelocity)
      if Math.abs(yVelocity) > maxVelocityY then
        yVelocity = yVelocity * maxVelocityY / Math.abs(yVelocity)

      println(s"vx=$xVelocity vy=$yVelocity")

      // Hard Stop
      if y >= floor.toFloat then
        y = floor
        yVelocity = 0

      if isJumping then
        if y == floor then // check for landing
          isGrounded = true
          isJumping = false

      if !isJumping && isJumpPressed && isGrounded then
        isJumping = true
        yVelocity = yVelocity + initJumpVelocity
        println("Jump!")

      // Working on changing height of jump depending on how long jump is held.
      //if (isJumping && isHoldingJump) then

      // Clear jump button push
      isJumpPressed = false

  // Other Objects
  var debugLabelSprite = new CPLabelSprite(5, 2, 0, s"Jump Testing: y=${pigSpr.getY}", C_DARK_CYAN)

  // Set Camera Tracking
  getCamera.setFocusTrackId(Some("pig"))
  getCamera.setFocusFrameInsets(new CPInsets(hor = 40, vert = 0))

  private var objs = List(
    bgSpr,
    brickSpr,
    pigSpr,
    debugLabelSprite,
    // On 'Q' press kick off fade out shader that will exit the game once it is finished.
    CPKeyboardSprite(_ => foShdr.start(), KEY_LO_Q, KEY_UP_Q),
  )
  objs ++= palmSeq

  addObjects(objs:_*)





