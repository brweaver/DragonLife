package background

import org.cosplay.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*

object PalmTreeImage extends CPArrayImage(
    prepSeq(
      """
        |    __ _.--..--._ _
        | .-' _/   _/\_   \_'-.
        ||__ /   _/x@@z\_   \__|
        |   |___/\_x@@z  \___|
        |          x@@z
        |          x@@z
        |           x@@z
        |            x@@z
        |             x@@z
             """),
    (ch, _, _) => ch match
      case 'x' => '\\' & C_ORANGE1
      case 'z' => '/' & C_ORANGE1
      case '@' => '_' & C_ORANGE1
      case c => c & C_GREEN1
  )

