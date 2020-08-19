package com.example.sampletestraman

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampletestraman.adapters.PersonListAdapter
import com.example.sampletestraman.database.Person
import com.example.sampletestraman.database.SampleDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_person_list.*

class PersonListActivity : AppCompatActivity() {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var personList = listOf<Person>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_list)

        fab_add.setOnClickListener {
            openAddPersonDetailsActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        getPersonFromLocal()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        val adapter = PersonListAdapter(this, personList)
        recyclerView.adapter = adapter
    }

    private fun getPersonFromLocal() {
        compositeDisposable.add(
                SampleDatabase.getDatabase(applicationContext).personDao().getPersonList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let {
                                personList = it
                                setupRecyclerView()
                            }
                        },
                                {
                                    Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show()
                                    it.printStackTrace()
                                })
        )
    }

    private fun openAddPersonDetailsActivity() {
        var intent = Intent(this, PersonInfoActivity::class.java)
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
