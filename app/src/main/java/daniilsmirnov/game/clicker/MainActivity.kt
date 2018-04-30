package daniilsmirnov.game.clicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    var points: Int  = 0
    var power: Int = 100
    var power_upgrade_need: Int = 500
    var power_upgrade_value: Int = 100
    var enemy_health: Int = 100
    var health_const: Int = 100
    var level_lenght: Int = 10
    var enemy_counter: Int = 10
    var enemy_const: Int = 10
    val numbers: IntArray = intArrayOf(R.drawable.sword1, R.drawable.sword2, R.drawable.sword3)
    var i: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lateinit var mAdView : AdView

        MobileAds.initialize(this,"ca-app-pub-7531773367714486~4707428901")

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val click_on_enemy = findViewById<ImageButton>(R.id.enemy_button)
        val points_view = findViewById<TextView>(R.id.points)
        val click_on_upgrade_power = findViewById<ImageButton>(R.id.upgrade_power)
        val need_to_upgrade_power_view = findViewById<TextView>(R.id.need_to_upgrade_power_text)
        val health_view = findViewById<ProgressBar>(R.id.healthbar)
        val health_view_text = findViewById<TextView>(R.id.healthtext)
        val level_view= findViewById<TextView>(R.id.level_counter)
        val layout = findViewById<ConstraintLayout>(R.id.layout)

        fun update_ui(){

            health_view.progress = enemy_health
            health_view_text.text = "$enemy_health"
            need_to_upgrade_power_view.text = "$power_upgrade_need"
            points_view.text = "$points"
            level_view.text= "$level_lenght" + "/" + "$enemy_counter"

            click_on_enemy.setBackgroundResource(R.drawable.enemy1)

        }

        update_ui()

        click_on_enemy.setOnClickListener {
            // enemy need an animation

            //Timer().schedule(100) {
            click_on_enemy.setBackgroundResource(R.drawable.enemy1)

            if(enemy_health > 0){

                enemy_health-=power
                health_view_text.text = "$enemy_health"
                health_view.progress = enemy_health

            }
            else
            {
                points += power
                health_const*=2
                enemy_health=health_const
                enemy_counter-=1

            }

            if( enemy_counter == 0){


                enemy_const*=2
                enemy_counter=enemy_const
                level_lenght=enemy_const

            }

            update_ui()

        }

        click_on_upgrade_power.setOnClickListener{

            if(points >= power_upgrade_need){

                power+=power_upgrade_value

                points-=power_upgrade_need
                power_upgrade_value+=1000
                power_upgrade_need*=5

                click_on_upgrade_power.setBackgroundResource(numbers[i])

                if(i == 3){

                    i =  0

                }
                else {
                    i++
                }
                if (points <= 0){

                    points = 0

                }

                need_to_upgrade_power_view.text = "$power_upgrade_need"

            }

            update_ui()
        }

    }
}
