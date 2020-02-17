package dx.queen.kotlinfirebasechat.groupieViewHolder

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.model.User
import kotlinx.android.synthetic.main.chat_item.view.*

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