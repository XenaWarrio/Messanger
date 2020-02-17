package dx.queen.kotlinfirebasechat.mvvm_test

import android.net.Uri

class Repository {

     fun doRegistration(emailForSaving: String, passwordForSaving: String, username: String, uri: Uri) : Int{
        val registerModel = RegisterModel(emailForSaving, passwordForSaving, username , uri)

        return registerModel.performRegister()
    }
}