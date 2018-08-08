package com.example.hipolito.app_android_osm.app

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import com.example.hipolito.app_android_osm.R
import com.example.hipolito.app_android_osm.infra.api.APIService
import com.example.hipolito.app_android_osm.model.MensagemResponse
import com.example.hipolito.app_android_osm.model.Refeicao
import com.example.hipolito.app_android_osm.utils.RefeitorioConstants
import com.example.hipolito.app_android_osm.utils.SecurityPreferences
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: APIService
    private lateinit var securityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        securityPreferences = SecurityPreferences(this@MainActivity)
        apiService = APIService(securityPreferences.getSavedString(RefeitorioConstants.KEY.TOKEN_LOGADO))

        capturarDiaSelecionado()
        registraConfirmaInteresse()
        buscarCardapio(converterData(Date()))
    }

    private fun registraConfirmaInteresse() {
        btnConfirmarRefeicao.setOnClickListener({l ->
            confirmaInteresse(securityPreferences.getSavedLong(RefeitorioConstants.KEY.REFEICAO_SELECIONADA))
        })
    }

    private fun confirmaInteresse(refeicao: Long) {
        val call = apiService.cardapioEndPoint.confirmarInteresse(refeicao)

        call.enqueue(object: Callback<MensagemResponse>{
            override fun onFailure(call: Call<MensagemResponse>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Falha na conexão", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MensagemResponse>?, response: Response<MensagemResponse>?) {
                if (response!!.isSuccessful){
                    Toast.makeText(this@MainActivity, "Confirmado", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "Erro " + response!!.code(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun buscarCardapio(data: String) {
        val call = apiService.cardapioEndPoint.getRefeicoes(data)

        call.enqueue(object: Callback<MutableList<Refeicao>>{
            override fun onFailure(call: Call<MutableList<Refeicao>>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Falha na conexão " + t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MutableList<Refeicao>>?, response: Response<MutableList<Refeicao>>?) {
                if (response!!.isSuccessful){
                    if (response.body()!!.isNotEmpty())
                        exibirCardapio(response.body()!![0])
                    else
                        txtCardapioDia.setText("Prato ainda não informado!")
                }else{
                    Toast.makeText(this@MainActivity, "Erro " + response!!.code(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    @SuppressLint("ResourceAsColor")
    private fun exibirCardapio(refeicao: Refeicao) {
        securityPreferences.saveLong(RefeitorioConstants.KEY.REFEICAO_SELECIONADA, refeicao.id)
        txtCardapioDia.setText(refeicao.prato.descricao)

        if (refeicao.interessados.contains(securityPreferences.getSavedLong(RefeitorioConstants.KEY.ID_USUARIO_LOGADO))) {
            btnConfirmarRefeicao.isActivated = false
            btnConfirmarRefeicao.setBackgroundColor(R.color.accent_material_dark)
        }
        Toast.makeText(this@MainActivity, "qtd" + refeicao.interessados + " id" + securityPreferences.getSavedLong(RefeitorioConstants.KEY.ID_USUARIO_LOGADO), Toast.LENGTH_SHORT).show()
    }

    private fun capturarDiaSelecionado() {
        calendarCardapios.setOnDateChangeListener { calendarView, year, month, day ->
            var data = Date(year, month, day)

            buscarCardapio(converterData(data))
        }
    }

    private fun converterData(date: Date): String {
        val formatBra = SimpleDateFormat("dd-MM-2018")
        return formatBra.format(date)
    }



}
