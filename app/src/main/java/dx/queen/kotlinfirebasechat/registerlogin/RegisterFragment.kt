package dx.queen.kotlinfirebasechat.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.message.BaseFragmentSwitch
import dx.queen.kotlinfirebasechat.model.User
import kotlinx.android.synthetic.main.register_fragment.*
import java.util.*

class RegisterFragment : BaseFragmentSwitch() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment , container , false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bt_registartion.setOnClickListener {

            performRegister()

        }

        tv_have_an_account.setOnClickListener {
            callback!!.switchTo(5)
        }

        bt_select_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, selectedPhotoUri)

            selected_image_view.setImageBitmap(bitmap)
            bt_select_image.alpha = 0f
        }
    }

    private fun performRegister() {
        val email = et_email_registration.text.toString()
        val password = et_password_registration.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            makeText(context, "Please enter text ", Toast.LENGTH_LONG)
                .show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                makeText(
                    context,
                    " User was successfully created! User uid : ${it.result!!.user!!.uid} ",
                    Toast.LENGTH_LONG
                )
                    .show()

                uploadImageToFirebaseStorage()
            }

            .addOnFailureListener {
                makeText(context, "Failed to create user : ${it.message} ", Toast.LENGTH_LONG)
                    .show()

            }

    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        saveUserToFirebaseDatabase(it.toString())
                    }
            }
    }

    private fun saveUserToFirebaseDatabase(imageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val refDataBase = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            et_username_registration.text.toString(),
            imageUrl
        )

        refDataBase.setValue(user)
            .addOnSuccessListener {

                callback!!.switchTo(1)
                activity!!.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE )
            }
    }


}