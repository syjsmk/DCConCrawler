import play.api.libs.json.{JsObject, JsValue, Json}
import scalaj.http._

object Main {

  def main(args: Array[String]): Unit = {

    val dcConCrawler = new DCConCrawler()
    val PACKAGE_INDEX_PREFIX = "-i="
    val TAGS_PREFIX = "-t="

    var packageIdx = 0
    var tags = List[String]()


    args.foreach(arg => arg match {
      case i if i.startsWith("-i") =>
        println("case i")
        println(arg)
        packageIdx = Integer.parseInt(i.replace(PACKAGE_INDEX_PREFIX, ""))

      case t if t.startsWith("-t") =>
        println("case t")
        println(arg)
        tags = t.replace(TAGS_PREFIX, "").replaceAll("[()]", "").split(",").toList
    })

    dcConCrawler.downloadDcCon(packageIdx, tags)


    //    val dcConData: JsValue = dcConCrawler.getDCConData()

    // 현재 존재하는 DCCon 수 MAX
    val MAX = 35729
    val KEJANG_OFFICIAL_1 = 6233
    val KEJANG_OFFICIAL_1_TAGS = List("케장", "케장콘", "카연갤")

//    // \ 로 현재 노드 바로 아래 단계를 탐색함
//    println(((dcConData \ "info") \ "package_idx").as[String])
//
//    // \\로 JsValue 내에서 find 기능 수행
//    (dcConData \\ "package_idx").foreach(packageIdx => {
//      println(packageIdx)
//    })
//
//    (dcConData \\ "idx").foreach(idx => {
//      println("idx : " + idx)
//    })

//    dcConCrawler.downloadDcCon(KEJANG_OFFICIAL_1, KEJANG_OFFICIAL_1_TAGS)


  }
}
