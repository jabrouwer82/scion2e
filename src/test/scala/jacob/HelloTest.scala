package jacob

import utest._

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.ext._

object HelloTest extends TestSuite {

  // Initialize App
  Hello.setupUI()

  override def tests = Tests {
    test("HelloWorld") {
      assert(document.querySelectorAll("p").count(_.textContent == "Hello World") == 1)
    }
    test("ButtonClick") {
      def messageCount =
        document.querySelectorAll("p").count(_.textContent == "You clicked the button!")

      document.querySelector("button") match {
        case button: dom.html.Button =>
          assert(button != null && button.textContent == "Click me!")
          assert(messageCount == 0)

          for (c <- 1 to 5) {
            button.click()
            assert(messageCount == c)
          }
        case _ => assert(false)
      }
    }
  }
}
