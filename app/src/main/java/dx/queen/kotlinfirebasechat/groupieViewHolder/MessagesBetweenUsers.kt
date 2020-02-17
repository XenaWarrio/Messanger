package dx.queen.kotlinfirebasechat.groupieViewHolder

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.model.User
import kotlinx.android.synthetic.main.message_pattern_user.view.*
import kotlinx.android.synthetic.main.pattern_message_companion.view.*

class MessagesBetweenUsers {
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
}