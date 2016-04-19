import scala.io.Source
object Target{
    def main(args: Array[String]) {
        var alphabets: Int = 0
        Source.fromFile(args(0)).foreach{ 
            (s: Char) => {s match {
                case it if 97 to 122 contains it => alphabets = alphabets + 1
                case it if 65 to 90 contains it => alphabets = alphabets + 1
                case _ => ()
            }}
        }
val resultWriter = new PrintWriter(new File("result.txt"))
resultWriter.write(alphabets.toString)
resultWriter.close()
    }
}
