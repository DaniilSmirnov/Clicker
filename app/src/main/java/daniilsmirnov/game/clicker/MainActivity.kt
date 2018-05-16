package daniilsmirnov.game.clicker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.widget.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.view.*
import android.widget.TextView
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.widget.LinearLayout.LayoutParams


class MainActivity : AppCompatActivity() {

    //private val mContext: Context = applicationContext

    var money: Int  = 0
    var diamonds: Int = 0
    var power: Int = 100
    var energy: Int = 10
    var location = 1.1

    var i: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val context: Context = applicationContext

        lateinit var mAdView : AdView

        MobileAds.initialize(this,"ca-app-pub-7531773367714486~4707428901")

        val pipe = findViewById<ImageButton>(R.id.pipe)
        val money_view = findViewById<TextView>(R.id.money)
        val diamonds_view = findViewById<TextView>(R.id.diamonds)
        val energy_view = findViewById<ProgressBar>(R.id.energybar)
        val energy_view_text = findViewById<TextView>(R.id.energytext)
        val event_card = findViewById<CardView>(R.id.event_card)
        val mouse_info = findViewById<TextView>(R.id.mouse_info)
        val mouse_icon_view = findViewById<ImageView>(R.id.mouse_icon_view)

        var this_location_mice: IntArray
        var this_location_mice_cost: IntArray

        val energy_runnable = Runnable {
            energy+=1
            Toast.makeText(this@MainActivity, "Thread runned", Toast.LENGTH_SHORT).show()
            Thread.sleep(60000)
        }

        fun update_ui(){

            energy_view.progress = energy
            energy_view_text.text = "$energy"
            diamonds_view.text = "$diamonds"
            money_view.text = "$money"


        }

        fun add_event(){

            mouse_info.setText("Test")
            mouse_icon_view.setImageResource(R.drawable.enemy1)

        }


        fun init(){

        //    val t = Thread(energy_runnable)
          //  t.start()
            update_ui()

        }

        init()

        pipe.setOnClickListener {

            if(energy > 0){

                energy-=1
                energy_view_text.text = "$energy"
                energy_view.progress = energy

                add_event()

            }
            else
            {

                Toast.makeText(this@MainActivity, "Not enough energy", Toast.LENGTH_SHORT).show()

            }

            update_ui()

        }

        event_card.setOnClickListener{

            val intent_events = Intent( this, ScrollingActivity::class.java)
            startActivity(intent_events)
        }

    }
}
