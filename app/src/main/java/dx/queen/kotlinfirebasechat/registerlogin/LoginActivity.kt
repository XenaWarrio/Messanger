package dx.queen.kotlinfirebasechat.registerlogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dx.queen.kotlinfirebasechat.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = et_login_email.text.toString()
        val password = et_login_password.text.toString()

        bt_login.setOnClickListener {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
        }

        tv_to_registration.setOnClickListener {
            finish()
        }
    }
}