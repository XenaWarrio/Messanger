package dx.queen.kotlinfirebasechat.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.groupieViewHolder.MessagesBetweenUsers
import dx.queen.kotlinfirebasechat.model.Message
import dx.queen.kotlinfirebasechat.model.User
import kotlinx.android.synthetic.main.specific_chat_fragment.*

class SpecificChat : BaseFragmentSwitch() {

    private lateinit var user: User


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.specific_chat_fragment, container, false)
    }


    companion object {
        const val USER_KEY = "user key"

        fun newInstance(user: User): SpecificChat {
            val fragment = SpecificChat()
            val args = Bundle()
            args.putParcelable(USER_KEY, user)
            fragment.arguments = args
            return fragment
        }
    }
    val adapter = GroupAdapter<GroupieViewHolder>()
    var currentuser: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null ) {
            user = arguments!!.getParcelable(USER_KEY)
        }


        rv_chat.adapter = adapter


        (activity as AppCompatActivity).supportActionBar?.title = user.username


        fetchCurrentUser()


        val fromId = FirebaseAuth.getInstance().uid
        val toId = user.uid

        if (fromId == null) return


        listenForMessages(user, fromId, toId)

        bt_send_message.setOnClickListener {
            sendMessage(fromId, toId)
        }
    }


    private fun listenForMessages(user: User, fromId: String, toId: String) {

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")


        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}


            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val chatMessage = p0.getValue(Message::class.java)

                if (chatMessage != null) {

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        if (currentuser != null) {
                            adapter.add(
                                MessagesBetweenUsers.ChatFromItem(
                                    chatMessage.text,
                                    currentuser!!
                                )
                            )
                        }

                    } else {
                        adapter.add(MessagesBetweenUsers.ChatToItem(chatMessage.text, user))
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })

        rv_chat.scrollToPosition(adapter.itemCount - 1)
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")


        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                currentuser = p0.getValue(User::class.java)
            }

        })

    }

    private fun sendMessage(fromId: String, toId: String) {

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toRef =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val latestMessageRow =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        val latestMessageToRow =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")

        val text = et_message.text.toString()

        val message = Message(ref.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)



        ref.setValue(message)
            .addOnSuccessListener {

                et_message.text.clear()
                rv_chat.scrollToPosition(adapter.itemCount - 1)


            }
        toRef.setValue(message)
            .addOnSuccessListener {

            }

        latestMessageRow.setValue(message)
            .addOnSuccessListener {

            }

        latestMessageToRow.setValue(message)
            .addOnSuccessListener {

            }

    }

}
