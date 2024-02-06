package com.example.a7_minutes_app
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7_minutes_app.databinding.ActivityMainBinding
import org.w3c.dom.Text
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityMainBinding?=null
    private var restTime :CountDownTimer?=null
    private var restProgress  = 0
    private var restTime_exercise:CountDownTimer?=null
    private var restProgress_exercise = 0
    private var exerciseList:ArrayList<ExerciseModel>?=null
    private var currentExercisePosition = -1
    private var tts:TextToSpeech?=null
    private var player:MediaPlayer?=null
    private var exerciserAdapter:ExerciseNumberAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setSupportActionBar(binding?.toolBar)
        binding?.toolBar?.setNavigationOnClickListener {
            onBackPressed()
        }
        exerciseList=Constants.defaultExerciseList()
        binding?.progressBarExercise?.visibility= View.VISIBLE

        tts = TextToSpeech(this,this)

        setRestView()
        setExerciswNumberRecycleView()
    }

    private fun setExerciswNumberRecycleView() {
        binding?.rvExerciseNumber?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciserAdapter = ExerciseNumberAdapter(exerciseList!!)
        binding?.rvExerciseNumber?.adapter=exerciserAdapter
    }


    private fun setRestView(){
        try {
            val soundURI= Uri.parse("android.resource//com.example/a7_minutes_app"+R.raw.start_sound)
            player= MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping=false
            player?.start()
        }catch (e:Exception){
            e.printStackTrace()
        }


        binding?.flProgressBar?.visibility=View.VISIBLE
        binding?.tvStart?.visibility=View.VISIBLE
        binding?.excerciseImg?.visibility=View.INVISIBLE
        binding?.tvExerciseName?.visibility=View.INVISIBLE
        binding?.flExerciseView?.visibility=View.INVISIBLE
        binding?.tvUpcomingExercise?.visibility=View.VISIBLE
        binding?.tvNextExercise?.visibility=View.VISIBLE

        onSpeak("Take rest")


        binding?.tvNextExercise?.text=exerciseList!![currentExercisePosition+1].getNmae()


        if(restTime!=null){
            restTime?.cancel()
            restProgress=0
        }
        setRestProgressBar()
    }
    private fun setExerciseView(){
        binding?.flProgressBar?.visibility=View.INVISIBLE
        binding?.tvStart?.visibility=View.INVISIBLE
        binding?.excerciseImg?.visibility=View.VISIBLE
        binding?.tvExerciseName?.visibility=View.VISIBLE
        binding?.flExerciseView?.visibility=View.VISIBLE
        binding?.tvUpcomingExercise?.visibility=View.INVISIBLE
        binding?.tvNextExercise?.visibility=View.INVISIBLE
        if(restTime_exercise!=null){
            restTime_exercise?.cancel()
            restProgress_exercise=0
        }

        onSpeak(exerciseList!![currentExercisePosition].getNmae())
        binding?.excerciseImg?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text= exerciseList!![currentExercisePosition].getNmae()
        setProgressBarExercise()
    }



    private fun setRestProgressBar(){
        binding?.progressBar?.progress=restProgress

        restTime = object :CountDownTimer(10000,1000){
            override fun onTick(p0: Long) {
               restProgress++
                binding?.progressBar?.progress=10-restProgress
                binding?.tvProgressBarTimer?.text=(10-restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciserAdapter!!.notifyDataSetChanged()
                setExerciseView()
            }
        }.start()
    }

    private fun setProgressBarExercise(){
        binding?.progressBarExercise?.progress=restProgress_exercise

        restTime_exercise = object :CountDownTimer(30000,1000) {
            override fun onTick(p0: Long) {
                restProgress_exercise++
                binding?.progressBarExercise?.progress = 30 - restProgress_exercise
                binding?.tvProgressBarTimerExercise?.text = (30 - restProgress_exercise).toString()
            }

            override fun onFinish() {
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciserAdapter!!.notifyDataSetChanged()


                if (currentExercisePosition < exerciseList!!.size - 1) {
                    setRestView()
                } else {
                    Toast.makeText(this@MainActivity, "You reach at end of the Excercise!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }.start()
    }


    override fun onDestroy(){
        super.onDestroy()
        if(restTime!=null){
            restTime?.cancel()
            restProgress=0
        }
        if(restTime_exercise!=null){
            restTime_exercise?.cancel()
            restProgress_exercise=0
        }

        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.ENGLISH)
            if(result == TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS","The language specified is not supported")
            }
            else{
                Log.e("TTS","Initiation Failed!!")
            }
        }
    }
    private fun onSpeak(text:String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

}