import play.api.libs.json.{JsObject, JsValue, Json}
import scalaj.http._

object Main {

  def main(args: Array[String]): Unit = {

    val dcConCrawler = new DCConCrawler()
    //    val dcConData: JsValue = dcConCrawler.getDCConData()

    // 현재 존재하는 DCCon 수 MAX
    val MAX = 35729
    val KEJANG_OFFICIAL_1 = 6233
    val KEJANG_OFFICIAL_1_TAGS = List("케장", "케장콘", "카연갤")

    val dcConData: JsValue = dcConCrawler.getDCConData(KEJANG_OFFICIAL_1)

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

    val tags = dcConCrawler.getTags(dcConData)
    val tagList = dcConCrawler.getTagList(dcConData)
    val detail = dcConCrawler.getDetail(dcConData)
    dcConCrawler.downloadDcCon(KEJANG_OFFICIAL_1, KEJANG_OFFICIAL_1_TAGS)

  }
}
