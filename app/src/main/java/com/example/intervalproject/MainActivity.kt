package com.example.intervalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.intervalproject.Dialog.CustomDialog
import com.example.intervalproject.databinding.ActivityMainBinding
import com.example.intervalproject.databinding.ActivtyDialogBinding
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
    var restTime = 0L   //휴식 타이머 시간
    var restTempTime = 0L   //휴식 타이머 임시 시간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //시간정지, 일시정지
        binding.timeStop.setOnClickListener {
            Running()
        }

        //시작버튼
        binding.timerBtn.setOnClickListener {
            timerSetting("start")
            startTimer()
        }
    }   //onCreate

    fun timerSetting(mode: String) {
        firstState = true
        if (mode == "start") {
            binding.settingLayout.visibility = View.GONE
            binding.workingLayout.visibility = View.VISIBLE
        } else {   //설정 모드
            binding.settingLayout.visibility = View.VISIBLE
            binding.workingLayout.visibility = View.GONE
        }
    }

    private fun Running() {
        if (timerRunning) {  //실행중일시 정지하기
            stopTimer()

        } else {                 //정지일때 실행
            startTimer()
        }
    }

    private fun startTimer() {
        //처음 실행 했을 때 기본 값 실행
        if (firstState) {
            val workingMin = binding.workingMin.text.toString()
            val workingSec = binding.workingSec.text.toString()

            time = (workingMin.toLong() * 60000) + (workingSec.toLong() * 1000) + 1000
        } else {      //정지 후 이어서 시작이면 기존값 사용
            time = tempTime
        }

        //타이머 실행
        countDownTimer = object : CountDownTimer(time, 1000) {
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
        binding.timeStop.setText("일시정지")
        Toast.makeText(this, "일시정자", Toast.LENGTH_SHORT).show()
    }

    private fun stopTimer() {
        countDownTimer.cancel()     //타이머 취소
        timerRunning = false          //정지 상태
        binding.timeStop.setText("시작")
        Toast.makeText(this, "시작", Toast.LENGTH_SHORT).show()

    }

    private fun updateTime() {
        val min = tempTime % 3600000 / 60000
        val sec = tempTime % 3600000 % 60000 / 1000
        var workTimeLeftText = ""
        var restTimeLeftText = ""

        //분이 10보다 작으면 0 붙이기
        if (min < 10) workTimeLeftText += "0"

        //분 추가
        workTimeLeftText += "$min:"

        if (sec < 10) workTimeLeftText += "0"

        //초 추가
        workTimeLeftText += sec

        //타이머 텍스트 보여주기
        binding.workTimerText.text  = workTimeLeftText
    }

    override fun onBackPressed() {
        val customDialog = CustomDialog(this)
        customDialog.show()
    }
}