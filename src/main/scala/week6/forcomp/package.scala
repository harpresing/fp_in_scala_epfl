package week6

import java.io.File

package object forcomp {
  val dictionaryPath = List("forcomp", "linuxwords.txt")

  def loadDictionary = {
    val wordstream = Option {
      getClass.getResourceAsStream(dictionaryPath.mkString("/"))
    } orElse {
      resourceAsStreamFromSrc
    } getOrElse {
      sys.error("Could not load word list, dictionary file not found")
    }
    try {
      val s = scala.io.Source.fromInputStream(wordstream)
      s.getLines.toList
    } catch {
      case e: Exception =>
        println("Could not load word list: " + e)
        throw e
    } finally {
      wordstream.close()
    }
  }

  def resourceAsStreamFromSrc: Option[java.io.InputStream] = {

    val resource = this.getClass.getClassLoader.getResource("forcomp/linuxwords.txt")
    val resourceFile = new File(resource.toURI)
    if (resourceFile.exists)
      Some(new java.io.FileInputStream(resourceFile))
    else
      None
  }

}
