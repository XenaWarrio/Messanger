package dx.queen.kotlinfirebasechat.message

import android.content.Context
import androidx.fragment.app.Fragment

open class BaseFragmentSwitch : Fragment() {

      var callback: FragmentsSwitch? = null


        override fun onAttach(context: Context) {
            super.onAttach(context)
            callback = context as FragmentsSwitch

        }

        override fun onDetach() {
            super.onDetach()
            callback = null
        }
}