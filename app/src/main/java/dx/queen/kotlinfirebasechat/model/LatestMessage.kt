package dx.queen.kotlinfirebasechat.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import dx.queen.kotlinfirebasechat.R
import kotlinx.android.synthetic.main.latest_message_item.view.*

class LatestMessage (val chatMessage : Message ): Item<GroupieViewHolder>() {
    var chatPartnerUser : User? = null

    override fun getLayout(): Int {
        return R.layout.latest_message_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_latest_message_latest_messages.text = chatMessage.text

        val chatPartnerId : String
        if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatPartnerId = chatMessage.toId
        }else{
            chatPartnerId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(User::class.java)
                    viewHolder.itemView.tv_username_latest_messages.text = chatPartnerUser?.username

                    Picasso.get().load(chatPartnerUser?.imageUrl).into(viewHolder.itemView.iv_profile_users_photo)


            }

        })

    }

}