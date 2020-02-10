package dx.queen.kotlinfirebasechat.message

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.model.LatestMessage
import dx.queen.kotlinfirebasechat.model.Message
import dx.queen.kotlinfirebasechat.model.User
import dx.queen.kotlinfirebasechat.registerlogin.RegisterActivity
import kotlinx.android.synthetic.main.activity_latest_messages.*

class LatestMessagesActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    val latestMessagesMap = HashMap<String, Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        val adapter = GroupAdapter<GroupieViewHolder>()
        rv_latest_massages.adapter = adapter
        rv_latest_massages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        listenForLatestMessage()

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)
            val row  = item as LatestMessage
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

        val uid = FirebaseAuth.getInstance().uid


        if (uid == null) {
            moveToRegisterActivity()
        } else {
            fetchCurrentUser(uid)

        }
    }

    private fun listenForLatestMessage() {

        val fromId = FirebaseAuth.getInstance().uid

        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(Message::class.java) ?: return
                latestMessagesMap[p0.key!!] = message
                refreshRV()

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(Message::class.java) ?: return
                latestMessagesMap[p0.key!!] = message
                refreshRV()
            }

            override fun onChildRemoved(p0: DataSnapshot) {}

        })
    }

    private fun refreshRV(){
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessage(it))
        }
    }
    private fun fetchCurrentUser(uid: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/user/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
            }

        })
    }

    private fun moveToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)

            }

            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                moveToRegisterActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
