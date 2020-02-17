package dx.queen.kotlinfirebasechat.mvvm_test

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import dx.queen.kotlinfirebasechat.message.BaseFragmentSwitch
import kotlinx.android.synthetic.main.register_fragment.*


class RegisterFragment : BaseFragmentSwitch() {
    var selectedPhotoUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(dx.queen.kotlinfirebasechat.R.layout.register_fragment , container , false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bt_select_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        bt_registartion.setOnClickListener {
            val email = et_email_registration.text.toString()
            val password = et_password_registration.text.toString()
            val userName = et_username_registration.text.toString()

            if(selectedPhotoUri == null){
                selectedPhotoUri = Uri.parse("android.resource://dx.queen.kotlinfirebasechat/drawable/voldemort")
            }

            val viewModel = RegisterViewModel(email,password ,userName, selectedPhotoUri!!)

            val num  = viewModel.performRegistration()

            when(num){
                0 -> makeText(context , "Error", LENGTH_LONG).show()

                1 -> callback!!.switchTo(1)

                2 -> makeText(context , "Email  or Password CAN`T be empty", LENGTH_LONG).show()
            }



        }

        tv_have_an_account.setOnClickListener {
            callback!!.switchTo(5)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, selectedPhotoUri)

            selected_image_view.setImageBitmap(bitmap)
            bt_select_image.alpha = 0f
        }
    }





}