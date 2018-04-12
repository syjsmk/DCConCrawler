import play.api.libs.json.{JsObject, JsValue, Json}
import scalaj.http._

object DCConCrawler {

  def main(args: Array[String]): Unit = {

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


      이 URI로 curl 요청하면 해당 idx의 DC콘 정보가 json으로 들어옴
      // curl 'http://dccon.dcinside.com/index/package_detail' -H 'Cookie: ci_c=61e286cd35e229c36c8d24d17e4289fe' -H 'X-Requested-With: XMLHttpRequest' --data 'ci_t=61e286cd35e229c36c8d24d17e4289fe&package_idx=15276&dcConResponse=' --compressed
      // curl 'http://dccon.dcinside.com/index/package_detail' -H 'Cookie: ci_c=61e286cd35e229c36c8d24d17e4289fe' -H 'X-Requested-With: XMLHttpRequest' --data 'ci_t=61e286cd35e229c36c8d24d17e4289fe&package_idx=15276&dcConResponse='

     */


    val DCConPackageUrl = "http://dccon.dcinside.com/index/package_detail"

    // ci_c랑 ci_t를 새로 안만들고 저거 계속 써도 되는지 모르겠음
    val data = "ci_t=61e286cd35e229c36c8d24d17e4289fe&package_idx=15276&dcConResponse="

    val dcConResponse = Http(DCConPackageUrl)
      .postData(data)
      .header("Cookie", "ci_c=61e286cd35e229c36c8d24d17e4289fe")
      .header("X-Requested-With", "XMLHttpRequest").asString

    //    println(Http(DCConPackageUrl).asString)
    println(dcConResponse)
    println(dcConResponse.body)

    val dcConJson: JsValue = Json.parse(dcConResponse.body)

//    println(dcConJson)

    // \ 로 현재 노드 바로 아래 단계를 탐색함
    println(((dcConJson \ "info") \ "package_idx").as[String])

    // \\로 JsValue 내에서 find 기능 수행
    (dcConJson \\ "package_idx").foreach(packageIdx => {
      println(packageIdx)
    })


    println(Json.toJson(dcConResponse.body))

  }
}
