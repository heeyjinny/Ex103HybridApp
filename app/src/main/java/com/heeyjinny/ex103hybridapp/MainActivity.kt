package com.heeyjinny.ex103hybridapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.heeyjinny.ex103hybridapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //설정들 -필수속성
        binding.wv.settings.javaScriptEnabled=true //js사용 허용
        binding.wv.settings.allowFileAccess=true //클라이언트의 js에서 AJAX기술 동작을 허용
        binding.wv.webViewClient= WebViewClient()
        binding.wv.webChromeClient= WebChromeClient()

        //웹뷰가 보여줄 뷰를 설정
        binding.wv.loadUrl("file:///android_asset/index.html")

        //1) 네이티브안드로이드 쪽에서 웹뷰의 UI제어
        binding.btn.setOnClickListener {
            //웹뷰에서 보여주는 index.html안에 있는 특정함수를 호출하여 웹UI제어
            //네이티브에서 직접 html의 dom요소는 제어불가

            var msg:String=binding.et.text.toString()
            //내가 지금 보여주고있는 페이지에 연결된 자바스크립트의 명령어실행
            binding.wv.loadUrl("javascript:setMessage('$msg')")

            binding.et.setText("")
        }

        //2) 웹뷰에서 네이티브안드로이드를 제어하기 위해 중계인 역할을
        //수행하는 객체 생성하며 웹에서 이 객체를 지칭할 별명 설정
        binding.wv.addJavascriptInterface(WebViewConnector(),"Droid")

    }//onCreate

    //1) 웹뷰의 자바스크립트와 통신을 담당한 중계인 객체의 클래스 정의
    inner class WebViewConnector{

        //자바스크립트에서 호출할 수 있는 메소드는 반드시
        //어노테이션 @JavascriptInterface 가 '반드시' 지정되어 있어야함
        @JavascriptInterface
        fun setTextView(msg:String){
            binding.tv.text="웹뷰로부터 받은 메시지 : $msg"
        }

        //디바이스의 고유기능인 사진(갤러리)앱을 열어주는 기능
        @JavascriptInterface
        fun openGalleryApp(){
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivity(intent)
        }
    }

}//MainActivity