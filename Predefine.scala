object Write {
    val originalFileName: String = "Original.scala"
    val preImport: String = "import java.io._"
    val outputVariableName: String = "resultWriter"
    val outputFileName: String = "result.txt"
    def writeLine(variableName: String): String = {
        ("val " + outputVariableName + " = new PrintWriter(new File(\"" + outputFileName + "\"))\n"
            + outputVariableName + ".write(" + variableName + ".toString)\n"
            + outputVariableName + ".close()")
    }
}

object Run {
    def compileCmd(originalFileName: String): String = "scalac " + originalFileName
    def runCmd(input: String): String = "scala Target " + input
}
