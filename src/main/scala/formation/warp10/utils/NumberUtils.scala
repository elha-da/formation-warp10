package formation.warp10.utils

object NumberUtils {
  def isDouble(string: String): Boolean = {
    try {
      string.toDouble
      true
    } catch {
      case _: Throwable => false
    }
  }

  def toDoubleOpt(string: String): Option[Double] = {
    try {
      Some(string.toDouble)
    } catch {
      case _: Throwable => None
    }
  }

  def isInt(string: String): Boolean = {
    try {
      string.toInt
      true
    } catch {
      case _: Throwable => false
    }
  }

  def toIntOpt(string: String): Option[Int] = {
    try {
      Some(string.toInt)
    } catch {
      case _: Throwable => None
    }
  }
}
