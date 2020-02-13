package dx.queen.kotlinfirebasechat.registerlogin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.message.LatestMessagesActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        bt_login.setOnClickListener {
            val email = et_login_email.text.toString()
            val password = et_login_password.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnSuccessListener {

                    val intent  = Intent(this, LatestMessagesActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {

                    makeText(this , "Wrong password or email. Maybe you aren`t registered? ", LENGTH_LONG).show()
                }
        }

        tv_to_registration.setOnClickListener {
            finish()
        }
    }
}