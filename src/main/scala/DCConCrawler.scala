object DCConCrawler {

  def get(url: String) = {
    io.Source.fromURL(url).mkString
  }

  def main(args: Array[String]): Unit = {

//    val url = "http://naver.com"
//    val url = "http://www.dcinside.com/"
    val url = "http://mall.dcinside.com/?from=A08"

    println(get(url))


  }
}
