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

     */


//    val DCConPackageUrl = "http://dccon.dcinside.com/new/1#35139"
    val DCConPackageUrl = "http://dccon.dcinside.com/new/1#35148"

    println(Http(DCConPackageUrl).asString)


  }
}
