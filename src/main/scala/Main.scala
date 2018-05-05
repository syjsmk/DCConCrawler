import play.api.libs.json.{JsObject, JsValue, Json}
import scalaj.http._


// -r=1-10000 -t=(케장,케장콘,카연갤)
// -i=6220 -r=6230-6235 -t=(케장,케장콘,카연갤)
// 22번에서 에러
object Main {

  // 현재 존재하는 DCCon 수 MAX
  private val MAX = 35729
  private val KEJANG_OFFICIAL_1 = 6233
  private val KEJANG_OFFICIAL_1_TAGS = List("케장", "케장콘", "카연갤")

  def main(args: Array[String]): Unit = {

    val dcConCrawler = new DCConCrawler()

    dcConCrawler.downloadDcCon(args: Array[String])


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
