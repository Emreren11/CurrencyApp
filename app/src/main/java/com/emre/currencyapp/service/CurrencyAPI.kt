package com.emre.currencyapp.service


import com.emre.currencyapp.model.CurrencyModels
import io.reactivex.Observable
import retrofit2.http.GET

interface CurrencyAPI {

    // https://api.freecurrencyapi.com/v1/latest?apikey=wEr4ceQtdJXDHO4oeqFYpTpXngCe2ekfCSBW8vVc
    @GET("latest?apikey=wEr4ceQtdJXDHO4oeqFYpTpXngCe2ekfCSBW8vVc")
    fun getData(): Observable<CurrencyModels>
}