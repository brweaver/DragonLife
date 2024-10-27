package objects

import org.cosplay.CPArrayImage.prepSeq
import org.cosplay.{CPAnimation, CPAnimationSprite, CPArrayImage, CPColor, CPSceneObjectContext}
import org.cosplay.CPPixel.*
import org.cosplay.*

class Pig extends GeneralObject {
  
  private val NAME: String = "Pig"
  private val COLOR: CPColor = CPColor("0xff7777")
  private val EYE_COLOR: CPColor = CPColor("0x00ff00")

  private val imgs = new CPArrayImage(
    prepSeq(
      """
        |   __
        | ໒(  ‘ ]
        |   V V
        |------
        |   __
        | ໒(  ‘ ]
        |   W W
        |------
          """
    ).filter(!_.endsWith("------")),
    (ch, _, _) => ch match
      case '‘' | '-' => ch & EYE_COLOR
      case _ => ch & COLOR
  ).split(8, 3) // Width, Height

  private val animation = Seq(CPAnimation.filmStrip("ani", 100.ms, imgs = imgs))
  def sprite: CPAnimationSprite = new CPAnimationSprite(NAME, anis = animation, x = 15, y = 5, z = 10, "ani", false):
    private var cnt = 0L

    override def update(ctx: CPSceneObjectContext): Unit =
      super.update(ctx)
      // pig logic
}
