package davis

import scala.util.parsing.combinator.RegexParsers


/**
 * Parses Strings in the format [-]ax&#94;2 +|- bx +|- c = 0 into a 3-tuple of numbers.
 **/
object StringParser {

  /**
   * @return A Right[(Double, Double, Double)] if the input string parsed correctly. The first double is the one
   *         that is assicated with the x&#94;2 term. The second is associated with the x term. The third is associated
   *         with the constant term.
   *         <br>
   *         A Left[Exception] if the String was not parsable. Exception error message details the reason why.
   * */
  def parseQuadratic(in: String) : Either[Exception,(Double, Double, Double)] = {

    val result = SimpleParsers.parse(SimpleParsers.fullParser, in).map(Right(_))
      .getOrElse(Left(new Exception("could not parse string as quadratic. Expected format like ax^2 [+|-] bx [+|-] c = 0")))

    result
  }

}

/**
 * Component sub-parsers that are used to make up the full parser used to identify quadratics, and extract their values.
 * */
object SimpleParsers extends RegexParsers{
  val doubleRegexNoSign = "(0|([1-9][0-9]*))(\\.[0-9]+)?"
  val xSquare: Parser[Double] = s"(-?)(${doubleRegexNoSign})?x\\^2".r ^^ {s =>
    if(s == "x^2"){
      1
    }else if(s == "-x^2"){
      -1
    }else{
      s.dropRight(3).toDouble
    }
  }
  val bx: Parser[Double] = s"(${doubleRegexNoSign})?x".r ^^ { s =>
    if(s == "x"){
      1.toDouble
    }else{
      s.dropRight(1).toDouble
    }
  }
  val cParse: Parser[Double] = s"(-?)$doubleRegexNoSign".r ^^ {_.toDouble}
  val plusMinus: Parser[Int] = "[+|-]".r ^^ {s => if(s == "+") 1 else -1}
  val equals: Parser[String] = "=".r
  val zero: Parser[String] = "0".r

  val bxSigned: Parser[Double] = plusMinus ~ bx ^^ { case sign ~ b => sign * b}
  val cSigned: Parser[Double] = plusMinus ~ cParse ^^ { case sign ~ cVal => sign * cVal}


  def fullParser: Parser[(Double, Double, Double)] = xSquare ~ bxSigned.? ~ cSigned.? <~ equals <~ zero ^^ {
    case a ~ b ~ c =>
      (a,b.getOrElse(0),c.getOrElse(0))
  }



}