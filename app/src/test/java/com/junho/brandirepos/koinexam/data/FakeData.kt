package com.junho.brandirepos.koinexam.data

import io.reactivex.Single
import io.reactivex.SingleObserver
import okhttp3.MediaType
import okhttp3.ResponseBody

class FakeData {

    fun makeFakeData(): ResponseBody {

        return  ResponseBody.create(MediaType.parse("application/json"),"{\"documents\":[{\"collection\":\"blog\",\"datetime\":\"2015-03-27T14:58:00.000+09:00\",\"display_sitename\":\"네이버블로그\",\"doc_url\":\"http://blog.naver.com/kljg28/220312740752\",\"height\":200,\"image_url\":\"http://postfiles5.naver.net/20150327_260/kljg28_1427435731217jPkDX_JPEG/img_0327_9.jpg?type=w1\",\"thumbnail_url\":\"https://search4.kakaocdn.net/argon/130x130_85_c/GfdDqt3OWxZ\",\"width\":600},{\"collection\":\"blog\",\"datetime\":\"2015-03-27T14:58:00.000+09:00\",\"display_sitename\":\"네이버블로그\",\"doc_url\":\"http://blog.naver.com/kljg28/220312740752\",\"height\":470,\"image_url\":\"http://postfiles11.naver.net/20150327_298/kljg28_1427439261166hBltK_JPEG/img_0327_10.jpg?type=w1\",\"thumbnail_url\":\"https://search2.kakaocdn.net/argon/130x130_85_c/coao68Rh1L\",\"width\":635},{\"collection\":\"cafe\",\"datetime\":\"2014-04-02T22:56:56.000+09:00\",\"display_sitename\":\"Daum카페\",\"doc_url\":\"http://cafe.daum.net/ninaano/icbd/44\",\"height\":266,\"image_url\":\"http://cfile297.uf.daum.net/image/214500465336EECA2CD527\",\"thumbnail_url\":\"https://search1.kakaocdn.net/argon/130x130_85_c/BgoAASw4Kpo\",\"width\":580}],\"meta\":{\"is_end\":true,\"pageable_count\":3,\"total_count\":6}}")
    }
}