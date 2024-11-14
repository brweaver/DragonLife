package background

import org.cosplay.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*

object PigData {

  val imgsWalkRight: Seq[CPImage] = new CPArrayImage(
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
  ).replaceBg(_.char == 'x', CPPixel.XRAY).split(8,3)

  val imgsWalkLeft: Seq[CPImage]  = imgsWalkRight.map(_.horFlip())

  val imgsIdle: Seq[CPImage]  = new CPArrayImage(
    prepSeq(
      """
        |xxx__,xx
        |x໒(xx‘x]
        |   P |
        |------
        |xxx__,xx
        |x໒(xx‘x]
        |   P >
        |------
        """
    ).filter(!_.endsWith("------")),
    (ch, _, _) => ch match
      case '‘' | '-' => ch & C_BLUE
      case _ => ch & C_PINK1
  ).replaceBg(_.char == 'x', CPPixel.XRAY).split(8, 3)

  val imgsJump: Seq[CPImage]  = new CPArrayImage(
    prepSeq(
      """
        |xxx__,xx
        |x໒(xx‘x]
        |   / >
        |------
        |xxx__,xx
        |x໒(xx‘x]
        |   > \
        |------
          """
    ).filter(!_.endsWith("------")),
    (ch, _, _) => ch match
      case '‘' | '-' => ch & C_BLUE
      case _ => ch & C_PINK1
  ).replaceBg(_.char == 'x', CPPixel.XRAY).split(8, 3)

}
