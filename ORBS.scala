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

        val originalWriter = new PrintWriter(new File("Original.scala"))
        originalWriter.println("import java.io._")
        var lineCounter = 0
        Source.fromFile(program).getLines.foreach{
            (s: String) => {
                originalWriter.println(s)
                lineCounter = lineCounter + 1
                if(lineCounter == lineNumber) {
                    originalWriter.println("val resultWriter = new PrintWriter(new File(\"result.txt\"))")
                    originalWriter.println("resultWriter.write(" + vName+ ".toString)")
                    originalWriter.println("resultWriter.close()")
                }
            }
        }
        originalWriter.close()

        // input에 대한 result를 역순으로 저장
        var resultList: List[String] = Nil
        "scalac Original.scala".!
        inputList.foreach{
            (s: String) => {
                ("scala Target " + s).!         //여기 Target을 어떻게 하지... 프로그램 시작을 찾아야하는건데
                                                //노상관이네 어짜피 인풋으로 이 프로그램을 실행 시킬 방법, 
                                                //그리고 라인을 지우는 방법을 줘야하는 거니까
                resultList = Source.fromFile("result.txt").mkString :: resultList
            }
        }

        var subProgram: List[String] = Source.fromFile("Original.scala").getLines.toList.reverse

        // deleting
        var deleted: Boolean = false
        //while(!deleted){
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
                    "cat SubProgram.scala".!
                    println(s"i: $i")
                    println(s"j: $j")
                    // if well builded "".! returns 0 else 1
                    if(("scalac SubProgram.scala".!) == 0){
                        var resultListTemp: List[String] = Nil
                        inputList.foreach{
                            (s: String) => {
                                ("scala Target " + s).! 
                                resultListTemp = Source.fromFile("result.txt").mkString :: resultListTemp
                            }
                        }
                        println("debug1")
                        if(resultList == resultListTemp){
                            println("debug12")
                            subProgram = subProgramTemp
                            erased = true
                        } 
                    }
                }
                if (!erased){
                    i = i + 1 
                }
            }
        }

    //}
}