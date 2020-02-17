package dx.queen.kotlinfirebasechat.mvvm_test

import android.net.Uri
import androidx.lifecycle.ViewModel

class RegisterViewModel( email : String , password : String , username : String, uri : Uri) : ViewModel() {

    var emailViewModel = email
    var passwordViewModel = password
    var usernameViewModel = username
    var uriViewModel = uri

    fun performRegistration() : Int{
        if (emailViewModel.isEmpty() || passwordViewModel.isEmpty()) {
            // check correct
            // make toast
            return 2
        }

        val repository = Repository()

        return repository.doRegistration(emailViewModel , passwordViewModel, usernameViewModel, uriViewModel)


    }

}