import java.io._
import scala.io.Source

object Target{
    def main(args: Array[String]) {
        println("Following is the content read:" )

        var alphabets: Int = 0
        var spaces: Int = 0
        var newlines: Int = 0

        Source.fromFile(args(0)).foreach{ 
            (s: Char) => {s match {
                case ' ' => spaces = spaces + 1
                case 10 => newlines = newlines + 1
                case it if 97 to 122 contains it => alphabets = alphabets + 1
                case it if 65 to 90 contains it => alphabets = alphabets + 1
                case _ => ()
            }}
        }
        //alphabets = alphabets + 27
        println(s"alphabets #: $alphabets")
val resultWriter = new PrintWriter(new File("result.txt"))
resultWriter.write(alphabets.toString)
resultWriter.close()
        println(s"spaces #: $spaces")
        println(s"newlines #: $newlines")

    }
}
