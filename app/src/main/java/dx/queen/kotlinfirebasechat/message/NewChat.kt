package dx.queen.kotlinfirebasechat.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.groupieViewHolder.UserItem
import dx.queen.kotlinfirebasechat.model.User
import kotlinx.android.synthetic.main.new_chat_fragment.*

class NewChat : BaseFragmentSwitch() {

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.new_chat_fragment, container,false)

        (activity as AppCompatActivity).supportActionBar?.title = "Select User"

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new_message_recycler.adapter = adapter

        fetchUser()
    }


    private fun fetchUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/")
        val uid = FirebaseAuth.getInstance().uid


        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                adapter.apply {
                    p0.children.forEach {
                        val user = it.getValue(User::class.java)
                        if (user != null && uid != null) {
                            if (user.uid != uid) {
                                add(UserItem(user))
                            }
                        }
                    }
                }
                adapter.setOnItemClickListener { item, _ ->

                    val user = item as UserItem

                    MainActivity.user = item.user
                    callback!!.switchTo(3)
                }

            }

        })



    }

}



