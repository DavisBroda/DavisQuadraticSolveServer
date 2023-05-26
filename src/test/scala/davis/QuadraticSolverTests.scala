package davis

import org.scalatest.flatspec.AnyFlatSpec

class QuadraticSolverTests extends AnyFlatSpec {

  it should "solve simple quadratic" in {
    // x^2 -3x +2 = 0  gives roots x=1, x=2
    val expected: Either[Exception, Set[Double]] = Right(Set(1,2))
    assert(QuadraticSolver.solve(1,-3,2) == expected)
  }

  it should "solve with non-integer coefficients and outputs to 3 decimal places of accuracy" in {
    // x^2 - 3.8x + 3.45 = 0 -> x=1.5, x=2.3
    val expected: Either[Exception, Set[Double]] = Right(Set(1.500, 2.300))

    assert(QuadraticSolver.solve(1, -3.8, 3.45) == expected)
  }

  it should "solve quadratic with missing b" in {
    // x^2 - 4 = 0 -> x=2, x=-2
    val expected: Either[Exception, Set[Double]] = Right(Set(2,-2))
    assert(QuadraticSolver.solve(1, 0, -4) == expected)

  }

  it should "solve quadratic with missing c" in {
    // x^2 - 4x = 0 -> x=4, x=0
    val expected: Either[Exception, Set[Double]] = Right(Set(4, 0))
    assert(QuadraticSolver.solve(1, -4, 0) == expected)

  }

  it should "solve quadratic with missing b and c" in {
    // 2x^2 = 0 -> x = 0

    val expected: Either[Exception, Set[Double]] = Right(Set(0))
    assert(QuadraticSolver.solve(2, 0, 0) == expected)

  }

  it should "error if a == 0" in {
    val expected: Either[Exception, Set[Double]] = Right(Set(0))
    val actual = QuadraticSolver.solve(0, 1, 1)
    assert(actual.isLeft)
    assert(actual.left.get.getMessage.contains("a cannot be zero"))
  }

  it should "empty set returned if equation would have imaginary solutions" in {
    // x^2  + 1 = 0 -> x = i, x=-i

    val expected: Either[Exception, Set[Double]] = Right(Set())
    assert(QuadraticSolver.solve(1, 0, 1) == expected)
  }

}
