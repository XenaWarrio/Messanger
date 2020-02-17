package dx.queen.kotlinfirebasechat.message

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.model.User
import dx.queen.kotlinfirebasechat.registerlogin.LoginFragment
import dx.queen.kotlinfirebasechat.mvvm_test.RegisterFragment

class MainActivity : AppCompatActivity(), FragmentsSwitch {


    companion object {
        var user: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SharedPreferencesIsUserRegister.init(this)



        if (SharedPreferencesIsUserRegister.read(true)) {
            switchTo(1)
        } else {
            switchTo(4)
        }


    }

    override fun switchTo(item: Int) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        when (item) {
            1 -> {
                SharedPreferencesIsUserRegister.write(true)
                transaction.replace(R.id.container, ChatsRow())
            }
            2 -> {

                transaction.replace(R.id.container, NewChat())
            }

            3 ->
                if (user != null) {

                    transaction.replace(R.id.container, SpecificChat.newInstance(user!!))
                        //.addToBackStack("stack")
                }
            4 -> {
                SharedPreferencesIsUserRegister.write(true)
                transaction.replace(R.id.container,
                    RegisterFragment()
                )
            }

            5 ->
                transaction.replace(R.id.container, LoginFragment())


        }

        transaction.commit()
    }


    override fun onBackPressed() {
        switchTo(1)
        super.onBackPressed()
    }
}

