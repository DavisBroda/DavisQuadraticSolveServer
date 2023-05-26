package davis

import org.scalatest.flatspec.AnyFlatSpec

class StringParserTests extends AnyFlatSpec{


  it should "parse valid quadratic with all parameters" in {
    val in = "2x^2 - 3x + 2 = 0"
    val expected = Right(2,-3,2)
    assert(StringParser.parseQuadratic(in) == expected)

  }

  it should "parse when x^2 has no accompanying number, and return number 1" in {

    val in = "x^2 - 3x + 2 = 0"
    val expected = Right(1, -3, 2)
    assert(StringParser.parseQuadratic(in) == expected)

  }

  it should "parse when x^2 has no accompanying number but has negative sign, and return number -1" in {

    val in = "-x^2 - 3x + 2 = 0"
    val expected = Right(-1, -3, 2)
    assert(StringParser.parseQuadratic(in) == expected)

  }

  it should "parse when x has no accompanying number, and return number +/-1" in {

    val in = "2x^2 - x + 2 = 0"
    val expected = Right(2, -1, 2)
    assert(StringParser.parseQuadratic(in) == expected)

  }


  it should "parse valid quadratic with missing b node" in {

    val in = "2x^2 + 2 = 0"
    val expected = Right(2, 0, 2)
    assert(StringParser.parseQuadratic(in) == expected)

  }

  it should "parse valid quadratic with missing c node" in {

    val in = "2x^2 - 3x = 0"
    val expected = Right(2, -3, 0)
    assert(StringParser.parseQuadratic(in) == expected)

  }

  it should "parse valid quadratic with missing b and c nodes" in {

    val in = "2x^2 = 0"
    val expected = Right(2, 0, 0)
    assert(StringParser.parseQuadratic(in) == expected)

  }

  it should "fail to parse when a node is missing" in {

    val in = "3x + 2 = 0"
    val actual = StringParser.parseQuadratic(in)
    assert(actual.isLeft)

  }

  it should "fail to parse when = node is missing" in {

    val in = "2x^2 - 3x + 2 0"
    val actual = StringParser.parseQuadratic(in)
    assert(actual.isLeft)

  }

  it should "fail to parse when 0 node is missing" in {
    val in = "2x^2 - 3x + 2 ="
    val actual = StringParser.parseQuadratic(in)
    assert(actual.isLeft)

  }

  it should "fail to parse if first sign is missing" in {

    val in = "2x^2 3x + 2 = 0"
    val actual = StringParser.parseQuadratic(in)
    assert(actual.isLeft)

  }

  it should "fail to parse if second sign is missing" in {

    val in = "2x^2 - 3x 2 = 0"
    val actual = StringParser.parseQuadratic(in)
    assert(actual.isLeft)

  }


}
