package dx.queen.kotlinfirebasechat.message

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dx.queen.kotlinfirebasechat.R
import dx.queen.kotlinfirebasechat.groupieViewHolder.LatestMessage
import dx.queen.kotlinfirebasechat.model.Message
import kotlinx.android.synthetic.main.chats_row_fragment.*

class ChatsRow : BaseFragmentSwitch() {

   private val adapter = GroupAdapter<GroupieViewHolder>()
    val latestMessagesMap = HashMap<String, Message>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chats_row_fragment, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Chats"

        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_latest_chats.adapter = adapter
        rv_latest_chats.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        setHasOptionsMenu(true)

        listenForLatestMessage()


        adapter.setOnItemClickListener { item, _ ->
            val row  = item as LatestMessage
            MainActivity.user = row.chatPartnerUser
            switchToFragment(3)

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


    private fun switchToFragment(number : Int) {
        callback!!.switchTo(number)
       // activity!!.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE )
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate((R.menu.nav_menu), menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {

            R.id.menu_new_message -> {
              switchToFragment(2)

            }

            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                switchToFragment(4)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
