package background

import org.cosplay.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*

object PigData {

  private val imgsWalkRight = new CPArrayImage(
    prepSeq(
    """
      |xxx__,xx
      |x໒(xx‘x]
      |   V V
      |------
      |xxx__,xx
      |x໒(xx‘x]
      |   W W
      |------
      """
  ).filter(!_.endsWith("------")),
  (ch, _, _) => ch match
    case '‘' | '-' => ch & C_BLUE
    case _ => ch & C_PINK1
  ).split(8,3)

  val imgsWalkLeft = imgsWalkRight.map(_.horFlip())

  private val imgsIdle = new CPArrayImage(
    prepSeq(
      """
        |xxx__,xx
        |x໒(xx‘x]
        |   V V
        |------
        |xxx__,xx
        |x໒(xx‘x]
        |   W W
        |------
        """
    ).filter(!_.endsWith("------")),
    (ch, _, _) => ch match
      case '‘' | '-' => ch & C_BLUE
      case _ => ch & C_PINK1
  ).split(8, 3)

  private val imgsJump = new CPArrayImage(
    prepSeq(
      """
        |xxx__,xx
        |x໒(xx‘x]
        |   V V
        |------
        |xxx__,xx
        |x໒(xx‘x]
        |   W W
        |------
          """
    ).filter(!_.endsWith("------")),
    (ch, _, _) => ch match
      case '‘' | '-' => ch & C_BLUE
      case _ => ch & C_PINK1
  ).split(8, 3)

}
