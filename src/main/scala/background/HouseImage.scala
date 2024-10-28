package background

import org.cosplay.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*

object HouseImage extends CPArrayImage(
  prepSeq(
    """
      |  []___
      | /    /\
      |/____/__\
      ||[][]||||
             """),
  (ch, _, _) => ch match
    case 'x' => '\\' & C_ORANGE1
    case 'z' => '/' & C_ORANGE1
    case '@' => '_' & C_ORANGE1
    case c => c & C_GREEN1
)

