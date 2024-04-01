package com.example.pruebamusica

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebamusica.ui.theme.PruebaMusicaTheme

class MainActivity : ComponentActivity() {

    val fd by lazy {
        assets.openFd(cancionActual)
    }

    val mp by lazy {
        val m = MediaPlayer()
        m.setDataSource(
            fd.fileDescriptor,
            fd.startOffset,
            fd.length
        )
        fd.close()
        m.prepare()
        m
    }

    val controllers by lazy {
        listOf(R.id.prev, R.id.stop, R.id.play, R.id.next).map {
            findViewById<Button>(it)
        }
    }

    object ci {
        val prev = 0
        val stop = 1
        val play = 2
        val next = 3
    }
    val nombreCancion by lazy {
        findViewById<TextView>(R.id.nombreCancion)
    }

    val canciones by lazy {
        val nombreFicheros = assets.list("")?.toList() ?: listOf()
        nombreFicheros.filter { it.contains(".mp3") }
    }

    var cancionActualIndex = 0
        set(value){
            var v = if(value==-1){
                canciones.size-1
            }
            else{
                value%canciones.size
            }
            field = v
            cancionActual = canciones[v]
        }

    lateinit var cancionActual:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        controllers[ci.play].setOnClickListener(this::playClicked)
        controllers[ci.stop].setOnClickListener(this::stopClicked)
        controllers[ci.prev].setOnClickListener(this::prevClicked)
        controllers[ci.next].setOnClickListener(this::nextClicked)
        cancionActual = canciones[cancionActualIndex]
        nombreCancion.text = cancionActual

        val marqueeid = findViewById<TextView>(R.id.nombreCancion)
        nombreCancion.isSelected = true
    }

    override fun onStart() {
        super.onStart()
        findViewById<RecyclerView>(R.id.rv).apply {
            adapter = AdaptadorCanciones(canciones, this@MainActivity)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    fun playClicked(v:View){
        if(!mp.isPlaying){
            mp.start()
            controllers[ci.play].setBackgroundResource(R.drawable.baseline_pause_48)
            nombreCancion.visibility = View.VISIBLE
        }
        else{
            mp.pause()
            controllers[ci.play].setBackgroundResource(R.drawable.baseline_play_arrow_48)
        }
    }

    fun stopClicked(v:View){
        if(mp.isPlaying){
            mp.pause()
            controllers[ci.play].setBackgroundResource(R.drawable.baseline_play_arrow_48)
            nombreCancion.visibility = View.INVISIBLE
        }
        mp.seekTo(0)
        nombreCancion.visibility = View.INVISIBLE
    }

    fun nextClicked(v:View){
        cancionActualIndex++
        refreshSong()
    }

    fun prevClicked(v:View){
        cancionActualIndex--
        refreshSong()
    }

    fun refreshSong(){
        mp.reset()
        val fd = assets.openFd(cancionActual)
        mp.setDataSource(
            fd.fileDescriptor,
            fd.startOffset,
            fd.length
        )
        mp.prepare()
        playClicked(controllers[ci.play])
        nombreCancion.text = cancionActual
    }
}

