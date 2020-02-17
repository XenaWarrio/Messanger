package dx.queen.kotlinfirebasechat.registerlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.message.BaseFragmentSwitch
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseFragmentSwitch() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate( R.layout.login_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        bt_login.setOnClickListener {
            val email = et_login_email.text.toString()
            val password = et_login_password.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnSuccessListener {

                    callback!!.switchTo(1)
                }
                .addOnFailureListener {

                    makeText(
                        context,
                        "Wrong password or email. Maybe you aren`t registered? ",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

        tv_to_registration.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE )
        }
    }
}