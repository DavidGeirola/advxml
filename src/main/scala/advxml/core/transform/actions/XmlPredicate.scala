package advxml.core.transform.actions

import scala.xml.NodeSeq

object XmlPredicate {

  type XmlPredicate = NodeSeq => Boolean

  def apply(f: NodeSeq => Boolean): XmlPredicate = f(_)
}
