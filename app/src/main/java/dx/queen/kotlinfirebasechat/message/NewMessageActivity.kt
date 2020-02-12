package dx.queen.kotlinfirebasechat.message

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.model.User
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.chat_item.view.*

class NewMessageActivity : AppCompatActivity() {


    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)


        supportActionBar?.title = "Select User"

        new_message_recycler.adapter = adapter

        fetchUser()
    }

    companion object {
        val USER_KEY = "user key"
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
                        if (user != null && FirebaseAuth.getInstance().uid != null) {
                            if (user.uid != FirebaseAuth.getInstance().uid) {
                                add(UserItem(user))
                            }
                        }
                    }
                }
                adapter.setOnItemClickListener { item, view ->

                    val user = item as UserItem

                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY, item.user)
                    startActivity(intent)

                    finish() // removing this activity from the backstack
                }

            }

        })



    }

}

class UserItem(val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.apply {
            itemView.username.text = user.username
            Picasso.get().load(user.imageUrl).into(itemView.imageView)

        }

    }

}

