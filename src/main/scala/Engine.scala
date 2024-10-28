import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.*
import org.cosplay.CPCanvas.ArtLineStyle.ART_SMOOTH

import scala.collection.mutable

object Engine {

  private def paintObjects(canvas: CPCanvas): Unit = {

  }

  def paintStars(canvas: CPCanvas): Unit = {
    // Paint the starry sky.
    val starClrs = CS_X11_CYANS ++ CS_X11_ORANGES ++ CS_X11_REDS ++ CS_X11_WHITES
    canvas.fillRect(canvas.rect, -1, (x, y) =>
      if y < 7 && CPRand.randFloat() < 0.02 then
        val ch = if CPRand.randFloat() < 0.5 then '+' else '*'
        ch & starClrs.rand
      else
        XRAY
    )
  }

  def paintMountains(canvas: CPCanvas): Unit = {
    // Mountains polyline.
    val width = canvas.dim.width
    val height = canvas.dim.height

    val mntPts = mutable.ArrayBuffer.empty[(Int, Int)]
    mntPts += 0 -> 39 // Initial point.
    var x = 0
    var i = 0
    val lastX = width - 20
    while x < lastX do
      x += CPRand.randInt(10, 20)
      val y = if i % 2 == 0 then CPRand.randInt(5, 20) else CPRand.randInt(25, 38)
      mntPts += x -> y
      i += 1
    mntPts += width - 1 -> 39
    mntPts += 0 -> 39 // Close in the polyline.

    // Paint the mountains with white peaks.
    canvas.drawArtPolyline(
      mntPts.toSeq,
      0,
      // Use 'tag=1' to mark mountains outline for filling later.
      ppx => ppx.px.withFg(C_GRAY1.lighter(1 - ppx.y.toFloat / 40)).withTag(1),
      ART_SMOOTH
    )
    val blank = ' ' & C_BLACK
    // NOTE: we are using 'tag=1' to detect the outline of the mountains.
    canvas.fill(11, 37, {
      _.px.tag == 1
    }, (x, y) =>
      if y < 5 then 'X' & C_WHITE
      else if y < 20 then 'X' & C_GRAY1.lighter(1 - (y - 5).toFloat / 15)
      else blank
    )
  }
}

class Engine {
  val bgPx = ' ' && (C_BLACK, C_GRAY1)
  val bgW = 200
  val bgH = 40
  val dim = CPDim(bgW, bgH)
  val bgCanv = CPCanvas(dim, bgPx)

  // +===================>--START--<======================+
  // | Procedural generation of the terrain & background. |
  // +====================================================+

  Engine.paintStars(bgCanv)
  Engine.paintMountains(bgCanv)


}
