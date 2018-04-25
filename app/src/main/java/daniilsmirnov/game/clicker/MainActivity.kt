package daniilsmirnov.game.clicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    var points: Int  = 0
    var power: Int = 10
    var power_upgrade_need: Int = 500
    var power_upgrade_value: Int = 100
    var enemy_health: Int = 100
    var health_const: Int = 50
    var level_lenght: Int = 10
    var enemy_counter: Int = 10
    var enemy_const: Int = 10

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
        val health_view = findViewById<ProgressBar>(R.id.healtbar)
        val health_view_text = findViewById<TextView>(R.id.healthtext)
        val level_view= findViewById<TextView>(R.id.level_counter)

        fun update_ui(){

            health_view.progress = enemy_health
            health_view_text.text = "$enemy_health"
            need_to_upgrade_power_view.text = "$power_upgrade_need"
            points_view.text = "$points"
            level_view.text= "$level_lenght" + "/" + "$enemy_counter"

        }

        update_ui()

        click_on_enemy.setOnClickListener {
            // enemy need an animation

            points += power

            if(enemy_health > 0){

                enemy_health-=power
                health_view_text.text = "$enemy_health"
                health_view.progress = enemy_health

            }
            else
            {

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

                power_upgrade_value+=1000
                power_upgrade_need*=5
                points-=power_upgrade_need

                if (points <= 0){

                    points = 0

                }

                need_to_upgrade_power_view.text = "$power_upgrade_need"

            }

            update_ui()
        }

    }
}
