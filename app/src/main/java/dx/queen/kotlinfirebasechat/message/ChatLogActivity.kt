package dx.queen.kotlinfirebasechat.message

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.model.User
import kotlinx.android.synthetic.main.activity_new_message.*

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)


        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = user.username


        val adapter = GroupAdapter<GroupieViewHolder>()

        new_message_recycler.adapter = adapter
    }
}

class ChatToItem: Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.pattern_message_companion
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

}

class ChatFromItem: Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.message_pattern_user
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

}