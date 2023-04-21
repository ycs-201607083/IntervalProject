package com.example.intervalproject

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.intervalproject.Dialog.CustomDialog
import com.example.intervalproject.Interface.Interface
import com.example.intervalproject.databinding.ActivityMainBinding
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var countDownTimer: CountDownTimer
    var timerRunning = false        //타이머 실행상태
    val timerResting = false
    var firstState = false      //타이머 실행 처음인지
    var time = 0L   //타이머 시간
    var tempTime = 0L   //타이머 임시 시간
    var restTime = 0L   //휴식 타이머 시간
    var restTempTime = 0L   //휴식 타이머 임시 시간
    var keyBoardManager: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        keyBoardManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        //시간정지, 일시정지
        binding.timeStop.setOnClickListener {
            Running()
        }

        //시작버튼
        binding.timerBtn.setOnClickListener {
            var count = 0

            timerSetting("start")
            startTimer("w")
            startTimer("r")

            count++
            keyBordHide()
        }

        binding.backTimer.setOnClickListener {
            timerSetting("back")
        }
    }   //onCreate

    fun keyBordHide() {
        WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.ime())
    }

    fun timerSetting(mode: String) {
        firstState = true
        if (mode == "start") {
            binding.settingLayout.visibility = View.GONE
            binding.workingLayout.visibility = View.VISIBLE
        } else if (mode == "back"){   //설정 모드
            binding.settingLayout.visibility = View.VISIBLE
            binding.workingLayout.visibility = View.GONE
        }
    }

    private fun Running() {
        if (timerRunning) {  //실행중일시 정지하기
            stopTimer()

        } else {                 //정지일때 실행
            startTimer("w")
        }
    }

    private fun startTimer(name: String) {
        //처음 실행 했을 때 기본 값 실행

        when (name) {
            "w" -> {
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
                        updateTime("w")
                    }

                    override fun onFinish() {}
                }.start()

                //타이머 상태 실행상태?
                timerRunning = true     //false 일때 타이머 2개 작동되지 왜?
                //처음실행 아님
                firstState = false
                binding.timeStop.setText("일시정지")
                Toast.makeText(this, "일시정지", Toast.LENGTH_SHORT).show()
            }

            "r" -> {
                //휴식타이머 실행
                if (!timerRunning) {
                    val restingMin = binding.restMin.text.toString()
                    val restingSec = binding.restSec.text.toString()

                    restTime = (restingMin.toLong() * 60000) + (restingSec.toLong() * 1000) + 1000


                    countDownTimer = object : CountDownTimer(restTime, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            restTempTime = millisUntilFinished
                            updateTime("r")

                        }

                        override fun onFinish() {}
                    }.start()
                }
            }
        }
    }

    private fun stopTimer() {
        countDownTimer.cancel()     //타이머 취소
        timerRunning = false          //정지 상태
        binding.timeStop.setText("시작")
        Toast.makeText(this, "시작", Toast.LENGTH_SHORT).show()

    }

    private fun updateTime(name: String) {
        val workngMin = tempTime % 3600000 / 60000
        val workingSec = tempTime % 3600000 % 60000 / 1000
        val restingMin = restTempTime % 3600000 / 60000
        val restingSec = restTempTime % 3600000 % 60000 / 1000
        var workTimeLeftText = ""
        var restTimeLeftText = ""

        if (name == "w") {
            //분이 10보다 작으면 0 붙이기
            if (workngMin < 10) {
                workTimeLeftText += "0"
            }
            //분 추가
            workTimeLeftText += "$workngMin:"

            //초가 10보다 작으면 0 붙임
            if (workingSec < 10) {
                workTimeLeftText += "0"
            }
            //초 추가
            workTimeLeftText += workingSec

            //타이머 텍스트 보여주기
            binding.workTimerText.text = workTimeLeftText

            if (workngMin.toInt() == 0 && workingSec.toInt() == 0) {
                val beep = ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME)

                //시작타이머 종료 시 비프음
                Thread(Runnable {
                    var count = 0
                    while (count < 3) {
                        count++
                        try {
                            beep.startTone(ToneGenerator.TONE_DTMF_S, 300)
                            Thread.sleep(1000)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }).start()
            }
        } else if (name == "r") {

            //분이 10보다 작으면 0 붙이기
            if (restingMin < 10) {
                restTimeLeftText += "0"
            }
            //분 추가
            restTimeLeftText += "$restingMin:"

            //초가 10보다 작으면 0 붙임
            if (restingSec < 10) {
                restTimeLeftText += "0"
            }
            //초 추가
            restTimeLeftText += restingSec

            //타이머 텍스트 보여주기
            binding.restTimer.text = restTimeLeftText


            if (restingMin.toInt() == 0 && restingSec.toInt() == 0) {
                val beep = ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME)

                //시작타이머 종료 시 비프음
                /* Thread(Runnable {
                     var count = 0
                     while (count < 2) {
                         count++
                         try {
                             beep.startTone(ToneGenerator.TONE_DTMF_S, 300)
                             Thread.sleep(1000)
                         } catch (e: Exception) {
                             e.printStackTrace()
                         }
                     }
                 }).start()*/
            }
        }

    }

    override fun onBackPressed() {
        val customDialog = CustomDialog(this)
        customDialog.show()
    }
}