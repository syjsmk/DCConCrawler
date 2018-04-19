import play.api.libs.json.{JsObject, JsValue, Json}
import scalaj.http._


//-i=6233 -t=(케장,케장콘,카연갤)
// -i=6220 -r=6230-6235 -t=(케장,케장콘,카연갤)
object Main {

  // 현재 존재하는 DCCon 수 MAX
  private val MAX = 35729
  private val KEJANG_OFFICIAL_1 = 6233
  private val KEJANG_OFFICIAL_1_TAGS = List("케장", "케장콘", "카연갤")

  def main(args: Array[String]): Unit = {

    val dcConCrawler = new DCConCrawler()
    val PACKAGE_INDEX_PREFIX = "-i="
    val TAGS_PREFIX = "-t="
    val RANGE_PREFIX = "-r="

    var packageIdx = 0
    var tags = List[String]()
    var range = List[Int]()


    args.foreach(arg => arg match {
      case i if i.startsWith("-i") =>
        println("case i")
        println(arg)
        packageIdx = Integer.parseInt(i.replace(PACKAGE_INDEX_PREFIX, ""))

      case r if r.startsWith("-r") =>
        println("case r")
        println(arg)
        range = r.replace(RANGE_PREFIX, "").split("-").map(value => Integer.parseInt(value)).toList

      case t if t.startsWith("-t") =>
        println("case t")
        println(arg)
        tags = t.replace(TAGS_PREFIX, "").replaceAll("[()]", "").split(",").toList
    })

//    dcConCrawler.downloadDcCon(7777)
//    dcConCrawler.downloadDcCon(KEJANG_OFFICIAL_1)
//    dcConCrawler.downloadDcCon(KEJANG_OFFICIAL_1, tags)


//    if(packageIdx != 0) {
//      for(i <- 6230 to 6240) {
//        dcConCrawler.downloadDcCon(i, tags)
//      }
//    }

//    if(packageIdx != 0) {
//      for(i <- 6231 to 6235) {
//        dcConCrawler.downloadDcCon(i, tags)
//        Thread.sleep(1)
//      }
//    }

    if(tags.isEmpty) {

      if(packageIdx != 0) {
        dcConCrawler.downloadDcCon(packageIdx)
      }

      for(i <- range(0) to range(1)) {
        dcConCrawler.downloadDcCon(i)
      }

    } else {

      if(packageIdx != 0) {
        dcConCrawler.downloadDcCon(packageIdx, tags)
      }

      for(i <- range(0) to range(1)) {
        dcConCrawler.downloadDcCon(i, tags)
      }

    }


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


  }
}
