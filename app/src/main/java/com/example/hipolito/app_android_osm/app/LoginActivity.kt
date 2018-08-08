package com.example.hipolito.app_android_osm.app

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.hipolito.app_android_osm.R
import com.example.hipolito.app_android_osm.R.id.*
import com.example.hipolito.app_android_osm.infra.api.APIService
import com.example.hipolito.app_android_osm.model.TokenAPIModel
import com.example.hipolito.app_android_osm.model.Usuario
import com.example.hipolito.app_android_osm.utils.RefeitorioConstants
import com.example.hipolito.app_android_osm.utils.SecurityPreferences
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var apiService: APIService
    private lateinit var progressDialog: ProgressDialog
    private lateinit var securityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initComponents()
    }

    fun initComponents(){
        apiService = APIService("")
        securityPreferences = SecurityPreferences(this)
        progressDialog = initProgressDialog()

        if(estaLogado()){
            initProxActivity()
        }

        btnLogin.setOnClickListener {
            progressDialog.show()
            realizarLogin(criarUsuario())
        }

    }

    private fun realizarLogin(usuario: Usuario) {

        val loginAPI = apiService.loginEndPoint.logarAPI(usuario)

        loginAPI.enqueue(object: Callback<TokenAPIModel> {
            override fun onFailure(call: Call<TokenAPIModel>?, t: Throwable?) {
                progressDialog.hide()
                Toast.makeText(this@LoginActivity, "Falha", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<TokenAPIModel>?, response: Response<TokenAPIModel>?) {
                if(response!!.isSuccessful){
                    progressDialog.hide()
                    logarUsuario(response.body()!!, usuario)
                }else{
                    Toast.makeText(this@LoginActivity, "Error " + response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun criarUsuario(): Usuario{
        val username = editUsuario.text.toString()
        val password = editSenha.text.toString()

        val usuario = Usuario(username, password)
        return usuario
    }

    private fun logarUsuario(token: TokenAPIModel, usuario: Usuario){
        securityPreferences.saveString(RefeitorioConstants.KEY.TOKEN_LOGADO, token.token)
        securityPreferences.saveLong(RefeitorioConstants.KEY.ID_USUARIO_LOGADO,usuario.id)

        initProxActivity()
    }

    private fun estaLogado(): Boolean{
        var token = securityPreferences.getSavedString(RefeitorioConstants.KEY.TOKEN_LOGADO)
        return if (token == "") false else true
    }

    private fun initProxActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initProgressDialog(): ProgressDialog {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Aguarde...")
        return progressDialog
    }
}
