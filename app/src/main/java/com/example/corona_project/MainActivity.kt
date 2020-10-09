package com.example.corona_project

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupHyperlink()

        var state = false
        val btn_click_me = findViewById(R.id.button) as Button
        btn_click_me.setOnClickListener {
            spinner1.isVisible = state
            textView.isVisible = state
            todayCases.isVisible = state
            todayDeaths.isVisible = state
            cases.isVisible = state
            deaths.isVisible = state
            recovered.isVisible = state
            search.isVisible = state
            credits1.isVisible = !state
            credits2.isVisible = !state
            state = !state

            if(state == true) button.text = "< BACK"
            else button.text = "CREATOR"
        }

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
                                        todayCases.text = "Today cases:" + coronaData[i].todayCases
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
                    search.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                        if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                            loop@ for(i in coronaData.indices){
                                if(search.text.toString().toLowerCase() == coronaData[i].country.toLowerCase()){
                                    todayCases.text = "Today cases: " + coronaData[i].todayCases
                                    todayDeaths.text = "Today deaths: " + coronaData[i].todayDeaths
                                    cases.text = "Total cases: " + coronaData[i].cases
                                    deaths.text = "Total deaths: " + coronaData[i].deaths
                                    recovered.text = "Recovered: " + coronaData[i].recovered

                                    Toast.makeText(
                                        this@MainActivity,
                                        getString(R.string.found_item) + " " +
                                                "" + coronaData[i].country, Toast.LENGTH_SHORT
                                    ).show()
                                    deaths.hideKeyboard()
                                    break@loop
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        getString(R.string.not_found), Toast.LENGTH_SHORT
                                    ).show()

                                }


                            }
                            return@OnKeyListener true
                        }
                        false
                    })
                }
            }
    }

    fun setupHyperlink() {
        val linkTextView = findViewById<TextView>(R.id.credits2)
        linkTextView.movementMethod = LinkMovementMethod.getInstance();
        linkTextView.setLinkTextColor(Color.WHITE)

        val linkTextView2 = findViewById<TextView>(R.id.credits1)
        linkTextView2.movementMethod = LinkMovementMethod.getInstance();
        linkTextView2.setLinkTextColor(Color.WHITE)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
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
