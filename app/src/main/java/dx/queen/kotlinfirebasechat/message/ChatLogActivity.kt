package dx.queen.kotlinfirebasechat.message

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.model.Message
import dx.queen.kotlinfirebasechat.model.User
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.message_pattern_user.view.*
import kotlinx.android.synthetic.main.pattern_message_companion.view.*

class ChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)


        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = user.username


        new_message_recycler.adapter = adapter

        val fromId = FirebaseAuth.getInstance().uid
        val toId = user.uid

        if (fromId == null) return


        listenForMessages(user, fromId, toId)

        bt_send_message.setOnClickListener {
            sendMessage( fromId, toId)
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
                val currentUser = LatestMessagesActivity.currentUser

                if (chatMessage != null) {

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        if (currentUser != null) {
                            adapter.add(ChatFromItem(chatMessage.text, currentUser))
                        }

                    } else {
                        adapter.add(ChatToItem(chatMessage.text, user))
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })

        rv_chat.scrollToPosition(adapter.itemCount -1)
    }

    private fun sendMessage(fromId: String, toId: String) {

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val latestMessageRow = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        val latestMessageToRow = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")

        val text = et_message.text.toString()
        val message = Message(ref.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)

        ref.setValue(message)
            .addOnSuccessListener {
                et_message.text.clear()
                rv_chat.scrollToPosition(adapter.itemCount -1)
            }
        toRef.setValue(message)

        latestMessageRow.setValue(message)
        latestMessageToRow.setValue(message)

    }
}

class ChatToItem(val text: String, val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.pattern_message_companion
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_single_message.text = text
        Picasso.get().load(user.imageUrl).into(viewHolder.itemView.iv_companion_photo)

    }

}

class ChatFromItem(val text: String, val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.message_pattern_user
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_single_message_user.text = text
        Picasso.get().load(user.imageUrl).into(viewHolder.itemView.iv_user_photo)
    }

}