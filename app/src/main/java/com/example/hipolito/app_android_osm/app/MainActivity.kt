package com.example.hipolito.app_android_osm.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import com.example.hipolito.app_android_osm.R
import com.example.hipolito.app_android_osm.infra.api.APIService
import com.example.hipolito.app_android_osm.model.Refeicao
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        apiService = APIService("")
        capturarDiaSelecionado()
    }

    private fun buscarCardapio(data: String) {
        val call = apiService.cardapioEndPoint.getRefeicoes(data)

        call.enqueue(object: Callback<Refeicao>{
            override fun onResponse(call: Call<Refeicao>?, response: Response<Refeicao>?) {
                if (response!!.isSuccessful){
                    exibirCardapio(response.body()!!)
                }else{
                    Toast.makeText(this@MainActivity, "Erro " + response!!.code(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Refeicao>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Falha na conexão", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun exibirCardapio(refeicao: Refeicao) {
        Toast.makeText(this@MainActivity, refeicao.tipo, Toast.LENGTH_SHORT).show()
    }

    private fun capturarDiaSelecionado() {
        calendarCardapios.setOnDateChangeListener { calendarView, year, month, day ->
            var data = Date(year, month, day)

            buscarCardapio(converterData(Date(calendarView.date)))
        }
    }

    private fun converterData(date: Date): String {
        val formatBra = SimpleDateFormat("dd/MM/2018")
        return formatBra.format(date)
    }

}
