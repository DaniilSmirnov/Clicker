package daniilsmirnov.game.clicker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.widget.*
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import android.widget.Toast
import android.widget.TextView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import android.app.Activity
import java.io.IOException
import java.util.*
import android.content.SharedPreferences
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {

    //private val mContext: Context = applicationContext

    var money: Int = 0
    var diamonds: Int = 0
    var power: Int = 100

    var energy_max: Int = 100
    var energy: Int = 10

    var location = 1.1

    var i: Int = 0
    val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val context: Context = applicationContext

        val sPref: SharedPreferences

        val pipe = findViewById<ImageButton>(R.id.pipe)
        val money_view = findViewById<TextView>(R.id.money)
        val diamonds_view = findViewById<TextView>(R.id.diamonds)
        val energy_view = findViewById<ProgressBar>(R.id.energybar)
        val energy_view_text = findViewById<TextView>(R.id.energytext)
        val event_card = findViewById<CardView>(R.id.event_card)
        val mouse_info = findViewById<TextView>(R.id.mouse_info)
        val mouse_icon_view = findViewById<ImageView>(R.id.mouse_icon_view)
        val mouse_cost = findViewById<TextView>(R.id.mouse_cost)
        val move_button = findViewById<ImageButton>(R.id.move_button)
        val shop_button = findViewById<ImageButton>(R.id.shop_button)


        var mice_name: String = ""
        var mice_cost: String = ""

        lateinit var mAdView: AdView

        MobileAds.initialize(this, "ca-app-pub-7531773367714486~4707428901")

        var this_location_mice: IntArray
        var this_location_mice_cost: IntArray

        fun update_ui() {

            energy_view.progress = energy
            energy_view_text.text = "$energy"
            diamonds_view.text = "$diamonds"
            money_view.text = "$money"
            mouse_info.text = mice_name
            mouse_cost.text = mice_cost

        }

        fun add_event() {

            val myThread = async(CommonPool) {
                // Запустить сопрограмму и присвоить её переменной myThread.
                mice_name = getfromXML(this@MainActivity,1)
                mice_cost = getfromXML(this@MainActivity,0)

            }

            launch(UI) {
                var myResult = myThread.await()
            }


        }

        fun energy_threads() {

            val EnergyCheckThread = async(CommonPool) {

                if (energy < energy_max) {

                    val EnergyThread = async(CommonPool) {
                        // Запустить сопрограмму и присвоить её переменной myThread.
                        Thread.sleep(1000)
                        energy++
                    }

                    launch(UI) {
                        // Запустить и забыть.
                        var myResult = EnergyThread.await()          // Подождём результата
                        update_ui()
                    }

                }

            }

            launch(UI) {
                var Result = EnergyCheckThread.await()
            }
        }

        fun init() {

            energy_threads()
            update_ui()

        }

        init()

        pipe.setOnClickListener {

            if (energy > 0) {

                energy -= 1
                add_event()
                money += mice_cost.toInt()

            } else {

                Toast.makeText(this@MainActivity, "Not enough energy", Toast.LENGTH_SHORT).show()

            }

            update_ui()

        }

        event_card.setOnClickListener {

            val intent_events = Intent(this, ScrollingActivity::class.java)
            startActivity(intent_events)
        }

        move_button.setOnClickListener {

            val intent_events = Intent(this, MoveActivity::class.java)
            startActivity(intent_events)
        }

        shop_button.setOnClickListener {

            val intent_events = Intent(this, ShopActivity::class.java)
            startActivity(intent_events)
        }

    }

    private fun rand(from: Int, to: Int): Int {
        return random.nextInt(to - from) + from
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun getfromXML(activity: Activity, index: Int): String {
        i = rand(1, 6)
        val stringBuilder = StringBuilder()
        val res = activity.resources
        val parser = resources.getXml(R.xml.locations)

        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                    && parser.getName().equals("mice" + "$i")) {
                stringBuilder.append(parser.getAttributeValue(index))
            }
            parser.next()
        }
        return stringBuilder.toString()
    }

}
