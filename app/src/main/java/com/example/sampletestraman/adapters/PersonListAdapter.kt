package com.example.sampletestraman.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sampletestraman.PersonInfoActivity
import com.example.sampletestraman.R
import com.example.sampletestraman.database.Person
import kotlinx.android.synthetic.main.list_item.view.*

class PersonListAdapter(val context: Context, private val list: List<Person>) : RecyclerView.Adapter<PersonListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val personList = list[position]
        holder.setData(personList, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var person: Person? = null
        var currentPosition: Int = 0

        init {
            itemView.imgEdit.setOnClickListener {

                person?.let {
                    val intent = Intent(context, PersonInfoActivity::class.java)
                    intent.putExtra("person_data", person)
                    context.startActivity(intent)
                }
            }
        }

        fun setData(person1: Person?, pos: Int) {
            person1?.let {
                itemView.firstName.text = person1.firstName
                itemView.lastName.text = person1.lastName
                itemView.phone.text = person1.phoneNumber
                itemView.state.text = person1.state
                itemView.city.text = person1.city
                itemView.pincode.text = person1.pincode
            }
            this.person = person1
            this.currentPosition = pos
        }
    }
}
