package com.example.corona_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        "https://coronavirus-19-api.herokuapp.com/countries"
            .httpGet().responseObject(Post.Deserializer()) { _, _, result ->
                val coronaData = result.component1()
                if (coronaData != null) {
                    val countries = resources.getStringArray(R.array.Countries)
                    val spinner = findViewById<Spinner>(R.id.spinner1)

                    if (spinner != null) {
                        val adapter = ArrayAdapter(
                            this,
                            android.R.layout.simple_spinner_item, countries
                        )
                        spinner.adapter = adapter

                        spinner.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                for(i in coronaData.indices){
                                    if(coronaData[i].country == "Poland"){
                                        todayCases.text = "Today cases: " + coronaData[i].todayCases
                                        todayDeaths.text = "Today deaths: " + coronaData[i].todayDeaths
                                        cases.text = "Total cases: " + coronaData[i].cases
                                        deaths.text = "Total deaths: " + coronaData[i].deaths
                                        recovered.text = "Recovered: " + coronaData[i].recovered
                                        break
                                    }
                                }
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                            ) {
                                Toast.makeText(
                                    this@MainActivity,
                                    getString(R.string.selected_item) + " " +
                                            "" + countries[position], Toast.LENGTH_SHORT
                                ).show()

                                //////
                                for(i in coronaData.indices){
                                    if(coronaData[i].country == countries[position]){
                                        todayCases.text = "Today cases: " + coronaData[i].todayCases
                                        todayDeaths.text = "Today deaths: " + coronaData[i].todayDeaths
                                        cases.text = "Total cases: " + coronaData[i].cases
                                        deaths.text = "Total deaths: " + coronaData[i].deaths
                                        recovered.text = "Recovered: " + coronaData[i].recovered
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }

}

data class Post(var country:String,
                var cases:String,
                var todayCases:String,
                var deaths:String,
                var todayDeaths:String,
                var recovered:String,
                var active:String,
                var critical:String,
                var casesPerOneMillion:String,
                var deathsPerOneMillion:String,
                var totalTestes:String,
                var testsPerOneMillion:String){

    class Deserializer : ResponseDeserializable<Array<Post>> {
        override fun deserialize(content: String): Array<Post>
                = Gson().fromJson(content, Array<Post>::class.java)
    }
}
