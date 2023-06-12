package com.emre.currencyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.emre.currencyapp.databinding.ActivityMainBinding
import com.emre.currencyapp.model.CurrencyModels
import com.emre.currencyapp.service.CurrencyAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.schedulers.Schedulers
import java.text.DecimalFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val BASE_URL = "https://api.freecurrencyapi.com/v1/"
    private var compositeDisposable = CompositeDisposable()
    private lateinit var currencyName: ArrayList<String>
    private lateinit var currencyValue: ArrayList<Double>
    private lateinit var spCurrencyFirst: Spinner
    private lateinit var spCurrencySecond: Spinner
    private var selectedCurrencyFirstValue: Double = 0.00
    private var selectedCurrencyFirstName = ""
    private var selectedCurrencySecondValue: Double = 0.00
    private var selectedCurrencySecondName = ""
    private var year = 0
    private var month = 0
    private var monthName = ""
    private var day = 0
    private var firstCurrency: Double = 0.00


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize
        currencyName = ArrayList()
        currencyValue = ArrayList()
        spCurrencyFirst = binding.spCurrencyFirst
        spCurrencySecond = binding.spCurrencySecond

        binding.currencyTextView.visibility = View.GONE
        binding.dateTextView.visibility = View.GONE

        // Getting data
        loadData()

        // Action for spinners choices
        spinnerListener()

    }

    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CurrencyAPI::class.java)

        compositeDisposable.add(
            retrofit.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )

    }

    fun changeValue(view: View) {

        val value = binding.firstCurrencyValue.text.toString()
        if (value.isNotEmpty()) {
            firstCurrency = binding.firstCurrencyValue.text.toString().toDouble()

            dateAndHour()


            val format = DecimalFormat("#.####")
            val convertedValue = firstCurrency / selectedCurrencyFirstValue
            val result = format.format(selectedCurrencySecondValue * convertedValue)

            binding.secondCurrencyValue.setText(result.toString())
            val currencyRatio = format.format((selectedCurrencySecondValue * convertedValue) / firstCurrency)

            binding.dateTextView.text = "$day $monthName $year"
            binding.currencyTextView.text = "1 $selectedCurrencyFirstName = $currencyRatio $selectedCurrencySecondName"


            binding.currencyTextView.visibility = View.VISIBLE
            binding.dateTextView.visibility = View.VISIBLE


        } else {
            Toast.makeText(this@MainActivity, "Enter currency value", Toast.LENGTH_LONG).show()
        }




    }

    private fun handleResponse(currency: CurrencyModels) {

        val data = currency.data

        for ((key, value) in data) {
            currencyName.add(key)
            currencyValue.add(value)
        }

        val spinnerAdapter = ArrayAdapter(this@MainActivity,android.R.layout.simple_spinner_item, currencyName)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCurrencyFirst.adapter = spinnerAdapter
        spCurrencySecond.adapter = spinnerAdapter

    }

    // Action for spinners choices
    private fun spinnerListener() {
        spCurrencyFirst.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCurrencyFirstValue = currencyValue[position]
                selectedCurrencyFirstName = currencyName[position]
                binding.firstCurrencyName.text = currencyName[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedCurrencyFirstValue = currencyValue[0]
                selectedCurrencyFirstName = currencyName[0]
                binding.firstCurrencyName.text = currencyName[0]
            }

        }
        spCurrencySecond.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCurrencySecondValue = currencyValue[position]
                selectedCurrencySecondName = currencyName[position]
                binding.secondCurrencyName.text = currencyName[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedCurrencySecondValue = currencyValue[0]
                selectedCurrencySecondName = currencyName[0]
                binding.secondCurrencyName.text = currencyName[0]
            }

        }
    }

    // Get current date and hour
    private fun dateAndHour() {

        val calendar = Calendar.getInstance()

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH) + 1
        day = calendar.get(Calendar.DAY_OF_MONTH)

        when(month) {
            1 ->
                monthName = "January"
            2 ->
                monthName = "February"
            3 ->
                monthName = "March"
            4 ->
                monthName = "April"
            5 ->
                monthName = "May"
            6 ->
                monthName = "June"
            7 ->
                monthName = "July"
            8 ->
                monthName = "August"
            9 ->
                monthName = "September"
            10 ->
                monthName = "October"
            11 ->
                monthName = "September"
            12 ->
                monthName = "December"
            else ->
                println("Error")
        }
    }


}