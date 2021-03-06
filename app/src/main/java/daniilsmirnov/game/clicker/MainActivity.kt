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
import android.R.attr.data
import android.content.Context
import android.util.Xml
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.AccessController.getContext


class MainActivity : AppCompatActivity() {

    //private val mContext: Context = applicationContext

    var money: Int = 0
    var diamonds: Int = 0
    var power: Int = 100

    var energy_max: Int = 1
    var energy: Int = 1

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
        val location_view = findViewById<TextView>(R.id.Location_view)


        var mice_name: String = ""
        var mice_cost: String = ""
        var location: String=getfromXML(this@MainActivity,0,1,"location")

        lateinit var mAdView: AdView

        MobileAds.initialize(this, "ca-app-pub-7531773367714486~4707428901")

        var this_location_mice: IntArray
        var this_location_mice_cost: IntArray

        fun update_ui() {

            energy_view.progress = energy
            energy_view_text.text = "$energy"
            diamonds_view.text = "$diamonds"
            money_view.text = "$money"
            location_view.text = location

            WriteFile(money, energy, diamonds)
        }

        fun add_event() {

            i = rand(1, 11)

            val myThread = async(CommonPool) {
                // Запустить сопрограмму и присвоить её переменной myThread.
                mice_name = getfromXML(this@MainActivity,1,i,"mice")
                mice_cost = getfromXML(this@MainActivity,0,i,"mice")

            }

            launch(UI) {
                var myResult = myThread.await()
                money += mice_cost.toInt()
                mouse_info.text = mice_name
                mouse_cost.text = mice_cost
                update_ui()
            }


        }

        fun energy_threads()= async(UI) {
            try {
                val job = async(CommonPool) {
                    while(true) {
                        if (energy < energy_max) {
                            Thread.sleep(1000)
                            energy++
                        }
                    }
                }

                job.await()

            }
            catch (e: Exception) {
            }
            finally {
                //Turn off busy indicator.
            }
        }

        fun safe_energy(){

            val myThread = async(CommonPool) {
                // Запустить сопрограмму и присвоить её переменной myThread.
                Thread.sleep(1000)
                energy++
            }

            launch(UI) {
                var myResult = myThread.await()
                update_ui()
            }

        }

        fun init() {

            //energy_threads()
            /*money = ReadFile("money").toInt()
            energy = ReadFile("energy").toInt()
            diamonds = ReadFile("diamonds").toInt()*/
            update_ui()

        }

        init()

        pipe.setOnClickListener {

            if (energy > 0) {

                energy -= 1
                add_event()

            } else {

                Toast.makeText(this@MainActivity, "Not enough energy", Toast.LENGTH_SHORT).show()

            }

            safe_energy()
            update_ui()

        }

        event_card.setOnClickListener {

            val intent_events = Intent(this, ScrollingActivity::class.java)
            startActivity(intent_events)
        }

        shop_button.setOnClickListener {

            val intent_events = Intent(this, ShopActivity::class.java)
            startActivity(intent_events)
        }

        move_button.setOnClickListener {

            val intent_events = Intent(this, MoveActivity::class.java)
            startActivity(intent_events)
        }

    }

    private fun rand(from: Int, to: Int): Int {
        return random.nextInt(to - from) + from
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun getfromXML(activity: Activity, index: Int, random: Int, tag: String): String {
        val stringBuilder = StringBuilder()
        val res = activity.resources
        val parser = resources.getXml(R.xml.locations)

        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                    && parser.getName().equals(tag + "$random")) {
                stringBuilder.append(parser.getAttributeValue(index))
            }
            parser.next()
        }
        return stringBuilder.toString()
    }

    private fun ReadFile(v: String): String {
        val stringBuilder = StringBuilder()
       try {
           /*val parser = resources.getXml(R.xml.userdata)

            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("$v")) {
                    stringBuilder.append(parser.getAttributeValue(0))
            }
            parser.next()*/
        }
        catch (e: NumberFormatException){

       }
        return stringBuilder.toString()
    }

    private fun WriteFile(money: Int, energy: Int, diamonds: Int){

        val filename = "userdata.xml"

        val fos: FileOutputStream

        fos = openFileOutput(filename, Context.MODE_APPEND)


        val serializer = Xml.newSerializer()
        serializer.setOutput(fos, "UTF-8")
        serializer.startDocument(null, java.lang.Boolean.valueOf(true))
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true)

        serializer.startTag(null, "user")

            serializer.startTag(null, "money")

                serializer.text("$money")

            serializer.endTag(null, "money")

            serializer.startTag(null, "energy")

                serializer.text("$energy")

            serializer.endTag(null, "energy")

            serializer.startTag(null, "diamonds")

                serializer.text("$diamonds")

            serializer.endTag(null, "diamonds")

        serializer.endTag(null, "user")

        serializer.endDocument()

        serializer.flush()

        fos.close()

    }
}