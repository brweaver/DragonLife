package background

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPPixel.*

object BrickImage extends CPArrayImage(
  // 5x3
  prepSeq(
    """
      |^^"^^
      |___[_
      |_[___
    """
  ),
  (ch, _, _) => ch match
    case '^' => '^' && (C_DARK_GREEN, C_GREEN_YELLOW)
    case '"' => '@' && (C_GRAY3, C_GREEN_YELLOW)
    case '{' => '[' && (C_SANDY_BROWN, C_DARK_ORANGE3)
    case '-' => '_' && (C_DARK_ORANGE3, C_DARK_ORANGE3)
    case c => c && (C_MAROON, C_DARK_ORANGE3)
)
