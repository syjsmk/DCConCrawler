import java.io._

import play.api.libs.json.{JsValue, Json}
import scalaj.http.{Http, HttpOptions}

class DCConCrawler {

  /*

    http://mall.dcinside.com/?from=A08#6312
    http://dccon.dcinside.com/index/package_detail
    http://dccon.dcinside.com/#6312

    참고

    http://dccon.dcinside.com/hot/5
    http://dccon.dcinside.com/new/1#35139

    http://~~~/
    hot : 인기 디시콘
    new : 신상 디시콘
    #뒤 번호 : 디시콘 번호

    순서대로 검색하고자 한다면 new를 쓰는 것이 나을듯

    url/new/1 로 GET요쳥을 보냈을 때 해당 html의 package_idx값이 해당 디시콘의 url이 됨.


    // 아래의 주소로 POST를 보낼 때
    http://dccon.dcinside.com/index/package_detail

    curl --data "ci_t=75960e76856b01dc69a224891f4874bd&package_idx=15276&dcConResponse=" http://dccon.dcinside.com/index/package_detail
    위의 인자와 함께 POST를 보낼 경우 반응이 있긴 하지만 허락되지 않은 POST라는 결과가 나옴. cookie 문제인듯.

    curl -X GET "http://dcimg5.dcinside.com/dccon.php?no=62b5df2be09d3ca567b1c5bc12d46b394aa3b1058c6e4d0ca41648b65ceb246e13df9546348593b9b03a53cb28363e94746ccdd8268733d87a9837c6e3cdbd8dab5749" -H 'Referer: http://dccon.dcinside.com/' > t.png
    curl -X GET "http://dcimg5.dcinside.com/dccon.php?no=62b5df2be09d3ca567b1c5bc12d46b394aa3b1058c6e4d0ca41648b65ceb246e13df9546348593b9b03a53cb2a363e94d9db0c94f2afb697e1b3a7490d11b82224a3b77b" -H 'Referer: http://dccon.dcinside.com/' > t.png


    이 URI로 curl 요청하면 해당 idx의 DC콘 정보가 json으로 들어옴
    // curl 'http://dccon.dcinside.com/index/package_detail' -H 'Cookie: ci_c=61e286cd35e229c36c8d24d17e4289fe' -H 'X-Requested-With: XMLHttpRequest' --data 'ci_t=61e286cd35e229c36c8d24d17e4289fe&package_idx=15276&dcConResponse=' --compressed
    // curl 'http://dccon.dcinside.com/index/package_detail' -H 'Cookie: ci_c=61e286cd35e229c36c8d24d17e4289fe' -H 'X-Requested-With: XMLHttpRequest' --data 'ci_t=61e286cd35e229c36c8d24d17e4289fe&package_idx=15276&dcConResponse='

   */

  private val TAGS = "tags"
  private val TAG = "tag"
  private val DETAIL = "detail"
  private val DIRECTORY_PATH = "./DCCon" + File.separator
  private val CONN_TIMEOUT_MS = 50000
  private val READ_TIMEOUT_MS = 50000

  def getDCConData(packageIdx: Int): JsValue = {

    val DCConPackageUrl = "http://dccon.dcinside.com/index/package_detail"

    // ci_c랑 ci_t를 새로 안만들고 저거 계속 써도 되는지 모르겠음
    //    val data = "ci_t=61e286cd35e229c36c8d24d17e4289fe&package_idx=15276&dcConResponse="
    val data = "ci_t=61e286cd35e229c36c8d24d17e4289fe&package_idx=" + packageIdx + "&dcConResponse="


    val dcConResponse = Http(DCConPackageUrl)
        .timeout(connTimeoutMs = CONN_TIMEOUT_MS, readTimeoutMs = READ_TIMEOUT_MS)
      .postData(data)
      .header("Cookie", "ci_c=61e286cd35e229c36c8d24d17e4289fe")
      .header("X-Requested-With", "XMLHttpRequest").asString

    println("======================================")
    println(dcConResponse)

    var dcConJson: JsValue = Json.obj()
    dcConResponse.body match {
      case "error" =>
        println("body is error")
        dcConJson = Json.obj("" -> "")
      case _ =>
        dcConJson = Json.parse(dcConResponse.body)
    }

//    val dcConJson: JsValue = Json.parse(dcConResponse.body)
//
    dcConJson
  }

  // 해당 DCCon에 달린 tag들을 다 읽어옴
  def getTags(dcConData: JsValue): Seq[JsValue] = {

    val tags = dcConData \\ TAGS
//    println(tags)

    tags
  }

  def getTagList(dcConData: JsValue): Seq[JsValue] = {

    val tagList = dcConData \\ TAG
//    println(tagList)

    tagList
  }

  def getDetail(dcConData: JsValue): Seq[JsValue] = {

    val detail = dcConData \\ DETAIL
//    println(detail)

    detail
  }


  def getDCConInfo(dcConData: JsValue): Option[JsValue] = {
    val dcConInfo = dcConData \\ "info"

    dcConInfo.headOption
  }

  def getDCConDetails(dcConData: JsValue): Option[JsValue] = {
    val dcConDetails = dcConData \\ "detail"

    dcConDetails.headOption
  }

  def getExts(dcConDetails: JsValue): Seq[JsValue] = {
    val dcConExts = dcConDetails \\ "ext"

    dcConExts
  }

  def getDCConImageTitles(dcConDetails: JsValue): Seq[JsValue] = {
    val dcConImageTitles = dcConDetails \\ "title"
    dcConImageTitles
  }

  def getDCConPathes(dcConData: JsValue): Seq[JsValue] = {
    val pathes = dcConData \\ "path"

    pathes
  }

  def downloadDcConImage(directoryPath: String, title: String, path: String, ext: String): Unit = {

    // DC콘 사이즈가 119인 경우는 아무것도 없는 흰 DC콘인 경우임
    // DC콘이 지워진 경우에도 DC콘 정보가 삭제되지는 않고 남아있기 때문에 이미지 크기로 판별해서 안받게 처리함.
    // 이미지가 있는 경우인데 사이즈가 119인 경우도 있을지는 모르겠지만 그 경우는 어쩔 수 없는 것으로.
    val EMPTY_IMAGE_SIZE = 119
    val fileName = directoryPath + File.separator + title + "." + ext

    val imageUrl = "http://dcimg5.dcinside.com/dccon.php?no="

    val result = Http(imageUrl + path)
      .header("Referer", "http://dccon.dcinside.com/")
      .asBytes

//    println(result.body)

    if(result.body.length != EMPTY_IMAGE_SIZE) {
      val bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileName))
      Stream.continually(bufferedOutputStream.write(result.body))
      bufferedOutputStream.close()
    }

  }

  def downloadDcCon(args: Array[String]): Unit = {

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

    if(tags.isEmpty) {

      if(packageIdx != 0) {
        downloadDcCon(packageIdx)
      }

      if(range.nonEmpty) {
        for(i <- range.head to range.last) {
          downloadDcCon(i)
        }
      }

    } else {

      if(packageIdx != 0) {
        downloadDcCon(packageIdx, tags)
      }

      if(range.nonEmpty) {
        for(i <- range.head to range.last) {
          downloadDcCon(i, tags)
        }
      }

    }

  }


  // 패키지 인덱스만 주면 무조건 다운받음
  def downloadDcCon(packageIdx: Int): Unit = {

    val (dcConInfo: Option[JsValue], dcConDetails: Option[JsValue], dcConTagList: Seq[JsValue]) = extract(packageIdx)

    this.downloadDcCon(dcConInfo, dcConDetails)

  }

  // tagList에 들어있는 tag를 하나라도 포함하고 있으면 받음
  def downloadDcCon(packageIdx: Int, tagList: List[String]): Unit = {

    val (dcConInfo: Option[JsValue], dcConDetails: Option[JsValue], dcConTagList: Seq[JsValue]) = extract(packageIdx)

    for(
      dcConTagValue <- dcConTagList;
      tagValue <- tagList
    ) {
//      if(tagValue.contentEquals(dcConTagValue.as[String])) {
      if(tagValue.contains(dcConTagValue.as[String])) {
        println("equal")
//        this.downloadDcCon(packageIdx)
        this.downloadDcCon(dcConInfo, dcConDetails)
      }
    }

  }

  private def extract(packageIdx: Int) = {
    val dcConData = getDCConData(packageIdx)

    val dcConInfo = getDCConInfo(dcConData)
    println("dcConInfo")
    println(dcConInfo)
    val dcConDetails = getDCConDetails(dcConData)
    println("dcConDetails")
    println(dcConDetails)
    val dcConTagList = getTagList(dcConData)
    println("dcConTagList")
    println(dcConTagList)
    (dcConInfo, dcConDetails, dcConTagList)
  }

//  private def downloadDcCon(dcConInfo: JsValue, dcConDetails: JsValue): Unit = {
  private def downloadDcCon(dcConInfo: Option[JsValue], dcConDetails: Option[JsValue]): Unit = {

    //    val directoryPath = "./target" + File.separator

    val title = (dcConInfo.get \\ "title").headOption


    new File(DIRECTORY_PATH).mkdir()
    if(title.isDefined) { // title != None

      val DCCON_PATH = DIRECTORY_PATH + title.get.as[String]

      val dcConDirectory = new File(DCCON_PATH)
      dcConDirectory.mkdir()

      val dcConImageTitles = getDCConImageTitles(dcConDetails.get)
      val dcConPathes = getDCConPathes(dcConDetails.get)
      val dcConExts = getExts(dcConDetails.get)

      for(((title, path), ext) <- dcConImageTitles zip dcConPathes zip dcConExts) yield {
        downloadDcConImage(dcConDirectory.getPath, title.as[String], path.as[String], ext.as[String])

      }

      if(dcConDirectory.list().length == 0) {
        dcConDirectory.delete()
      }
    }

  }


}
