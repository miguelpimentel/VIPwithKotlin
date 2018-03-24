package com.example.miguelpimentel.aux

import android.content.res.ColorStateList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

interface  LoginDisplayLogic {

    fun displayLoginStatus(viewModel: Login.ViewModel)
}

class MainActivity : AppCompatActivity(), LoginDisplayLogic {

    var interactor: LoginBusinessLogic? = null

    val messageTextView: TextView? = null
    val requestForLoginButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setup()

        val textView: TextView = findViewById<TextView>(R.id.text1)
        textView.setText("Without a request")

        val button: Button = findViewById<Button>(R.id.button821)
        button.setOnClickListener {

            textView.setText("Botão apertado")

            val request = Login.Request("Miguel", "123456")
            interactor?.requireForAuth(request)
        }
    }

    override fun displayLoginStatus(viewModel: Login.ViewModel) {
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.text1)
        textView.setText(viewModel.message)
    }


    private fun setup() {

        val viewController = this
        val interactor = LoginInteractor()
        val presenter = LoginPresenter()

        viewController.interactor = interactor
        interactor.presenter = presenter
        presenter.viewController = viewController
    }
}




// Scene Model

class Login {

    class Response {

        var isAuth: Boolean = false

        constructor(isAuth: Boolean) {

            this.isAuth = isAuth
        }
        var isAuth: Boolean = false

        init { this.isAuth = isAuth }
    }

    class Request(name: String, passwd: String) {

        var name: String = ""
        var passwd: String = ""

        init {
            this.name = name
            this.passwd = passwd
        }
    }

    class ViewModel(message: String) {

        var message: String = ""

        init {
            this.message = message
        }
    }
}

// Worker
class LoginWorker {

    fun authUser(request: Login.Request, completion: (Login.Response) -> Unit) {

        val passwd: String = request.passwd
        val name: String = request.name
        var isValid: Boolean = false

        if (name.equals("admin") && passwd.equals("admin")) {
            isValid = true
        } else {
            isValid = false
        }

        val response = Login.Response(isValid)
        completion(response)
    }
}


// Interactor

interface LoginBusinessLogic {

    fun requireForAuth(request: Login.Request)
}

class LoginInteractor: LoginBusinessLogic {

    var presenter: LoginPresentationLogic? = null
    var worker = LoginWorker()

    override fun requireForAuth(request: Login.Request) {

        worker.authUser(request) { response ->
            this.presenter?.presentLogin(response)
        }
    }
}

// Presenter

interface LoginPresentationLogic {

    fun presentLogin(response: Login.Response)
}

class LoginPresenter: LoginPresentationLogic {

    var viewController: LoginDisplayLogic? = null

    override fun presentLogin(response: Login.Response)  {

        if (response.isAuth) {
            val viewModel = Login.ViewModel("Ohh Great!!!")
            this.viewController?.displayLoginStatus(viewModel)
        } else {
            val viewModel = Login.ViewModel("Something is wrong! Try Again")
            this.viewController?.displayLoginStatus(viewModel)
        }
    }
}

