package com.novatech.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // variables
    var startTime = 0.0
    var finalTime = 0.0
    var forwardTime = 10000
    var backwardTime = 10000
    var oneTimeOnly = 0

    // Handler
    var handler: Handler = Handler()

    var mediaPlayer = MediaPlayer()
    lateinit var time_txt: TextView
    lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val play_btn : Button = findViewById(R.id.play_btn)
        val stop_btn : Button = findViewById(R.id.pause_btn)
        val forward_btn: Button = findViewById(R.id.forward_btn)
        val back_btn : Button = findViewById(R.id.back_btn)

        val tvTitle = findViewById<TextView>(R.id.tvSongTitle)
        time_txt = findViewById<TextView>(R.id.time_left_text)
        seekBar = findViewById<SeekBar>(R.id.seek_bar)

        //Media Player
        mediaPlayer = MediaPlayer.create(this, R.raw.astronaut)

        seekBar.isClickable = false

        //adding functin
        play_btn.setOnClickListener {
            mediaPlayer.start()

            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()

            if(oneTimeOnly == 0){
                seekBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            time_txt.text = startTime.toString()
            seekBar.setProgress(startTime.toInt())

            handler.postDelayed(UpdateSongTime, 100)
        }

    }

    // creating the runnable
    // handlers allows to post runnable on ui thread safely
    val UpdateSongTime : Runnable = object : Runnable {
        override fun run() {
            // updating the playing song time
            startTime = mediaPlayer.currentPosition.toDouble()
            time_txt.text = "" + String.format("%d min , %d sec",
                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(startTime.toLong() -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()))))

            seekBar.progress = startTime.toInt()
            handler.postDelayed(this, 100)

        }
    }

}