package advxml.syntax

import org.scalatest.FunSuite
import scala.xml.Text

/**
  * Advxml
  * Created by geirolad on 03/07/2019.
  *
  * @author geirolad
  */
class TextConverterSyntaxTest extends FunSuite {

  import advxml.instances.convert._
  import advxml.syntax.convert._
  import cats.instances.option._

  test("String to Text") {

    val value: Option[String] = Some("TEST")
    val res = <Test Value={value.mapAs[Text]}/>

    assert(res \@ "Value" == value.get)
  }

  test("Byte to Text") {
    val value: Option[Byte] = Some(0x001)
    val res = <Test Value={value.mapAs[Text]}/>

    assert(res \@ "Value" == value.get.toString)
  }

  test("Char to Text") {
    val value: Option[Char] = Some('A')
    val res = <Test Value={value.mapAs[Text]}/>

    assert(res \@ "Value" == value.get.toString)
  }

  test("Short to Text") {
    val value: Option[Short] = Some(1)
    val res = <Test Value={value.mapAs[Text]}/>

    assert(res \@ "Value" == value.get.toString)
  }

  test("Int to Text") {
    val value: Option[Int] = Some(100)
    val res = <Test Value={value.mapAs[Text]}/>

    assert(res \@ "Value" == value.get.toString)
  }

  test("Long to Text") {
    val value: Option[Long] = Some(100L)
    val res = <Test Value={value.mapAs[Text]}/>

    assert(res \@ "Value" == value.get.toString)
  }

  test("Float to Text") {
    val value: Option[Float] = Some(100.55373f)
    val res = <Test Value={value.mapAs[Text]}/>

    assert(res \@ "Value" == value.get.toString)
  }

  test("Double to Text") {
    val value: Option[Double] = Some(100.55275373d)
    val res = <Test Value={value.mapAs[Text]}/>

    assert(res \@ "Value" == value.get.toString)
  }

  test("BigInt to Text") {
    val value: Option[BigInt] = Some(BigInt(1453472357))
    val res = <Test Value={value.mapAs[Text]}/>

    assert(res \@ "Value" == value.get.toString)
  }

  test("BigDecimal to Text") {
    val value: Option[BigDecimal] = Some(BigDecimal(100.5527537255633))
    val res = <Test Value={value.mapAs[Text]}/>

    assert(res \@ "Value" == value.get.toString)
  }
}
