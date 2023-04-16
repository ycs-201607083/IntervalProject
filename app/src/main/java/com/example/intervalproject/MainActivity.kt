package com.example.intervalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.intervalproject.Dialog.CustomDialog
import com.example.intervalproject.databinding.ActivityMainBinding
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var countDownTimer: CountDownTimer
    var timerRunning = false        //타이머 실행상태
    var firstState = false      //타이머 실행 처음인지
    var time = 0L   //타이머 시간
    var tempTime = 0L   //타이머 임시 시간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //시작버튼
        binding.timerBtn.setOnClickListener {
            timerSetting("start")
            startTimer()
        }
    }   //onCreate

    fun timerSetting(mode : String){
        firstState = true
        if (mode == "start"){
            binding.settingLayout.visibility = View.GONE
            binding.workingLayout.visibility = View.VISIBLE
        }
        else{   //설정 모드
            binding.settingLayout.visibility = View.VISIBLE
            binding.workingLayout.visibility = View.GONE
        }
    }
    private fun Running(){
        if (timerRunning){  //실행중일시 정지하기
            stopTimer()

        }
        else{                 //정지일때 실행
            startTimer()
        }
    }

    private fun startTimer(){
        //처음 실행 했을 때 기본 값 실행
        if (firstState){
            val workingMin= binding.workingMin.text.toString()
            val workingSec = binding.workingSec.text.toString()

            time = (workingMin.toLong() * 60000) + (workingSec.toLong() * 1000) + 1000
        } else{      //정지 후 이어서 시작이면 기존값 사용
            time = tempTime
        }

        //타이머 실행
        countDownTimer = object : CountDownTimer(time, 1000){
            override fun onTick(millisUntilFinished: Long) {
                tempTime = millisUntilFinished
                //타이머 업데이트
                updateTime()
            }

            override fun onFinish() {

            }
        }.start()

        //타이머 상태 = 실행
        timerRunning = true
        //처음실행 아님
        firstState = false
    }

    private fun stopTimer(){
        //타이머 취소
        countDownTimer.cancel()
        //타이머 정지상태
        timerRunning = false
    }

    private fun updateTime(){
        val min = tempTime % 3600000 / 60000
        val sec = tempTime % 3600000 % 60000 / 1000
        var timeLeftText  = ""

        //분이 10보다 작으면 0 붙이기
        if (min < 10) timeLeftText += "0"

        //분 추가
        timeLeftText += "$min:"

        if(sec < 10) timeLeftText += "0"

        //초 추가
        timeLeftText += sec

        //타이머 텍스트 보여주기
        binding.timerText.text = timeLeftText
    }

    override fun onBackPressed() {
       CustomDialog(this).show()
    }

}