package com.dg.advxml.transform

import org.scalatest.FeatureSpec

import scala.xml.Elem

class XmlTrasformerTest extends FeatureSpec  {

  import com.dg.advxml.AdvXml._

  feature("Xml manipulation: Filters") {
    scenario("Filter By Attribute") {
      val elem: Elem = <Order>
        <OrderLines>
          <OrderLine PrimeLineNo="1" />
          <OrderLine PrimeLineNo="2" />
          <OrderLine PrimeLineNo="3" />
        </OrderLines>
      </Order>

      val result = elem \ "OrderLines" \ "OrderLine" filter attrs("PrimeLineNo" -> "1")

      assert(result \@ "PrimeLineNo" == "1")
    }
  }

  feature("Xml manipulation: Nodes") {

    scenario("AppendNode") {
      val elem: Elem = <Order>
        <OrderLines>
          <OrderLine PrimeLineNo="1" />
        </OrderLines>
      </Order>

      val result = elem.transform(
          $(_ \ "OrderLines")
            ==> append(<OrderLine PrimeLineNo="2" />)
            ==> append(<OrderLine PrimeLineNo="3" />)
            ==> append(<OrderLine PrimeLineNo="4" />)
      )

      assert(result \ "OrderLines" \ "OrderLine"
        exists attrs("PrimeLineNo" -> "1"))
      assert(result \ "OrderLines" \ "OrderLine"
        exists attrs("PrimeLineNo" -> "2"))
      assert(result \ "OrderLines" \ "OrderLine"
        exists attrs("PrimeLineNo" -> "3"))
      assert(result \ "OrderLines" \ "OrderLine"
        exists attrs("PrimeLineNo" -> "4"))
    }

    scenario("ReplaceNode") {
      val elem: Elem = <Order>
        <OrderLines>
          <OrderLine PrimeLineNo="1" />
        </OrderLines>
      </Order>

      val result = elem.transform(
        $(_ \ "OrderLines" \ "OrderLine" filter attrs("PrimeLineNo" -> "1"))
          ==> replace(<OrderLine PrimeLineNo="4" />)
      )

      assert((result \ "OrderLines" \ "OrderLine"
        filter attrs("PrimeLineNo" -> "1")).length == 0)
      assert(result \ "OrderLines" \ "OrderLine"
        exists attrs("PrimeLineNo" -> "4"))
    }

    scenario("RemoveNode") {
      val elem: Elem = <Order>
        <OrderLines>
          <OrderLine PrimeLineNo="1" />
          <OrderLine PrimeLineNo="2" />
        </OrderLines>
      </Order>

      val result = elem.transform(
        $(_ \ "OrderLines" \ "OrderLine" filter attrs("PrimeLineNo" -> "1")) ==> remove
      )

      assert((result \ "OrderLines" \ "OrderLine"
        filter attrs("PrimeLineNo" -> "1")).length == 0)
    }

    scenario("AppendNode to Root"){
      val elem: Elem = <OrderLines />
      val result = elem.transform(
        append(<OrderLine PrimeLineNo="1" />)
      )

      assert((result \ "OrderLine").length == 1)
      assert(result \ "OrderLine" \@ "PrimeLineNo" == "1")
    }
  }

  feature("Xml manipulation: Attributes") {

    scenario("SetAttribute") {
      val elem: Elem = <Order><OrderLines /></Order>

      val result = elem.transform(
        $(_ \ "OrderLines") ==> setAttrs("A1" -> "1", "A2" -> "2", "A3" -> "3")
      )

      assert(result \ "OrderLines" \@ "A1" == "1")
      assert(result \ "OrderLines" \@ "A2" == "2")
      assert(result \ "OrderLines" \@ "A3" == "3")
    }

    scenario("SetAttribute to root") {
      val elem: Elem = <Order />

      val result = elem.transform(
        setAttrs("A1" -> "1", "A2" -> "2", "A3" -> "3")
      )

      assert(result \@ "A1" == "1")
      assert(result \@ "A2" == "2")
      assert(result \@ "A3" == "3")
    }


    scenario("ReplaceAttribute") {
      val elem: Elem = <Order>
        <OrderLines T1="1">
          <OrderLine PrimeLineNo="1"></OrderLine>
          <OrderLine PrimeLineNo="2"></OrderLine>
          <OrderLine PrimeLineNo="3"></OrderLine>
        </OrderLines>
      </Order>

      val result = elem.transform(
        $(_ \ "OrderLines") ==> setAttrs("T1" -> "EDITED")
      )

      assert(result \ "OrderLines" \@ "T1" == "EDITED")
    }

    scenario("RemoveAttribute") {
      val elem: Elem = <Order>
        <OrderLines T1="1">
          <OrderLine PrimeLineNo="1"></OrderLine>
          <OrderLine PrimeLineNo="2"></OrderLine>
          <OrderLine PrimeLineNo="3"></OrderLine>
        </OrderLines>
      </Order>

      val result = elem.transform(
        $(_ \ "OrderLines") ==> removeAttrs("T1")
      )

      assert(result \ "OrderLines" \@ "T1" == "")
    }
  }
}
