// input
// P: *.scala, source program
// v: variable name
// l: line #
// d: delta, maximum deltion window size
// I1, ... In: inputs

import java.io._
import scala.io.Source
import scala.sys.process._

object ORBS {
    def main(args: Array[String]) {
        val program: String = args(0)
        val vName: String = args(1)
        val lineNumber: Int = args(2).toInt
        val delta: Int = args(3).toInt
        var inputList: List[String] = args.takeRight(args.length-4).toList

        val originalWriter = new PrintWriter(new File(Write.originalFileName))
        originalWriter.println(Write.preImport)
        var lineCounter = 0
        Source.fromFile(program).getLines.foreach{
            (s: String) => {
                originalWriter.println(s)
                lineCounter = lineCounter + 1
                if(lineCounter == lineNumber) {
                    originalWriter.println(Write.writeLine(vName))
                }
            }
        }
        originalWriter.close()

        // input에 대한 result를 역순으로 저장
        var originAlresultList: List[String] = Nil
        (Run.compileCmd(Write.originalFileName)).!
        inputList.foreach{
            (s: String) => {
                (Run.runCmd(s)).!
                originAlresultList = Source.fromFile(Write.outputFileName).mkString :: originAlresultList
            }
        }

        var subProgram: List[String] = Source.fromFile(Write.originalFileName).getLines.toList.reverse

        // deleting
        var deleted: Boolean = false
        var i: Int = 0
        while(i < subProgram.length){
            println(s"debug, $i, " + subProgram.length)
            var erased: Boolean = false
            var j = 0
            var subProgramTemp: List[String] = Nil
            while(j < 3 && !erased) {
                j = j + 1
                subProgramTemp = (subProgram.dropRight(subProgram.length - i) ::: 
                    (if (i+j > subProgram.length) Nil else subProgram.drop(i+j)))
                val subProgramWriter = new PrintWriter(new File("SubProgram.scala"))
                subProgramTemp.reverse.foreach{
                    (s: String) => subProgramWriter.write(s + "\n")
                }
                subProgramWriter.close()
                // if well builded "".! returns 0 else 1
                if(("scalac SubProgram.scala".!) == 0){
                    var newResultList: List[String] = Nil
                    inputList.foreach{
                        (s: String) => {
                            ("scala Target " + s).! 
                            newResultList = Source.fromFile("result.txt").mkString :: newResultList
                        }
                    }
                    if(originAlresultList == newResultList){
                        subProgram = subProgramTemp
                        erased = true
                    } 
                }
            }
            if (!erased){
                i = i + 1 
            }
        }

        val subProgramWriter = new PrintWriter(new File("SubProgram.scala"))
        subProgram.reverse.foreach{
            (s: String) => subProgramWriter.write(s + "\n")
        }
        subProgramWriter.close()
    }
}