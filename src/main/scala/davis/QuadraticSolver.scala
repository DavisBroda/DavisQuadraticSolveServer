package davis

object QuadraticSolver {

  /**
   * Solves the quadratic equation, returning all real solutions, rounded to 3 decimal places of precision.
   * If no solutions exist in the real numbers, and empty set will be returned
   *
   * @return Right[Set[Double]] containing all solutions to the quadratic equation,
   *         with a maximum of two elements. Elements rounded to 3 decimal places of percision<br>
   *         Left[Exception] if a is exactly zero, as this does not define a valid quadratic equation
   * */
  def solve(a: Double, b: Double, c: Double): Either[Exception, Set[Double]] = {

    if (a == 0) {
      return Left(new Exception("a cannot be zero is a valid quadratic equation"))
    }
    val bsq = math.pow(b, 2)
    val fourAC = 4 * a * c

    val sqrtContent = bsq - fourAC

    if (sqrtContent < 0) {
      return Right(Set())
    }

    val s1 = (-b + math.sqrt(sqrtContent)) / (2 * a)
    val s2 = (-b - math.sqrt(sqrtContent)) / (2 * a)

    val preRoundResults = Set(s1, s2)
    val postRound = preRoundResults.map(scala.math.BigDecimal(_).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble)

    Right(postRound)
  }

}
