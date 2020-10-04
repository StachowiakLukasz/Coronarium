package com.example.corona_project

public class Data {
    lateinit var country:String
    lateinit var cases:String
    lateinit var todayCases:String
    lateinit var deaths:String
    lateinit var todayDeaths:String
    lateinit var recovered:String
    lateinit var active:String
    lateinit var critical:String
    lateinit var casesPerOneMillion:String
    lateinit var deathsPerOneMillion:String
    lateinit var totalTests:String
    lateinit var testsPerOneMillion:String

    constructor(country:String,
                cases:String,
                todayCases:String,
                deaths:String,
                todayDeaths:String,
                recovered:String,
                active:String,
                critical:String,
                casesPerOneMillion:String,
                deathsPerOneMillion:String,
                totalTests:String,
                testsPerOneMillion:String){
        
        this.country = country
        this.cases = cases
        this.todayCases = todayCases
        this.deaths = deaths
        this.todayDeaths = todayDeaths
        this.recovered = recovered
        this.active = active
        this.critical = critical
        this.casesPerOneMillion = casesPerOneMillion
        this.deathsPerOneMillion = deathsPerOneMillion
        this.totalTests = totalTests
        this.testsPerOneMillion = testsPerOneMillion
    }

    constructor()
}