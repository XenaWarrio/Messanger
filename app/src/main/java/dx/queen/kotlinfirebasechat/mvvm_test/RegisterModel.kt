package dx.queen.kotlinfirebasechat.mvvm_test

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dx.queen.kotlinfirebasechat.model.User
import java.util.*

class RegisterModel(emailForSaving: String, passwordForSaving: String, username: String, uri: Uri) {

    private val email = emailForSaving
    private val password = passwordForSaving
    private val userName = username
    private val uriImage = uri

    private var success = 0


    fun performRegister(): Int {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                success = uploadImageToFirebaseStorage()
            }

            .addOnFailureListener {

                success = 0
            }

        return success
    }

    private fun uploadImageToFirebaseStorage(): Int {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(uriImage)
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        success = saveUserToFirebaseDatabase(it.toString())
                    }
            }
            .addOnFailureListener {
                success = 0
            }


        return success
    }

    private fun saveUserToFirebaseDatabase(imageUrl: String): Int {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val refDataBase = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            userName,
            imageUrl
        )

        refDataBase.setValue(user)
            .addOnSuccessListener {
                success = 1
            }
            .addOnFailureListener {
                success = 0
            }

        return success
    }


}