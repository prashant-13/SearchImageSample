package com.example.sampletestraman

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sampletestraman.Utilities.Utils
import com.example.sampletestraman.database.Person
import com.example.sampletestraman.database.SampleDatabase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_person_info.*
import java.util.*

class PersonInfoActivity : AppCompatActivity() {
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var person: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)

        initIntentData();
        saveButton.setOnClickListener {
            validateData()
        }
    }

    private fun initIntentData() {
        if (intent.hasExtra("person_data")) {
            person = intent.getSerializableExtra("person_data") as Person

            person?.let {
                firstName.setText(person!!.firstName)
                lastName.setText(person!!.lastName)
                phone.setText(person!!.phoneNumber)
                city.setText(person!!.city)
                state.setText(person!!.state)
                pincode.setText(person!!.pincode)
            }
        }
    }

    private fun validateData() {
        val firstName = firstName.text.toString().trim()
        val lastName = lastName.text.toString().trim()
        val phoneNumber = phone.text.toString().trim()
        val city = city.text.toString().trim()
        val state = state.text.toString().trim()
        val pincode = pincode.text.toString().trim()

        var msg: String? = null

        if (Utils.isNullOrBlank(firstName))
            msg = getString(R.string.please_enter_first_name)
        else if (Utils.isNullOrBlank(lastName))
            msg = getString(R.string.please_enter_last_name)
        else if (Utils.isNullOrBlank(phoneNumber))
            msg = getString(R.string.please_enter_mobile)
        else if (!Utils.isNullOrBlank(phoneNumber) and !Utils.isValidMobileNo(phoneNumber))
            msg = getString(R.string.please_enter_valid_mobile)
        else if (Utils.isValidMobileNo(city))
            msg = getString(R.string.please_enter_city)
        else if (Utils.isNullOrBlank(state))
            msg = getString(R.string.please_enter_state)
        else if (Utils.isNullOrBlank(pincode))
            msg = getString(R.string.please_enter_Pincode)

        if (!Utils.isNullOrBlank(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        } else {
            var time = Date().time
            person?.let {
                time = person!!.personId
            }

            var newPerson = Person(time, firstName, lastName, phoneNumber, city, state, pincode)

            compositeDisposable.add(
                    Completable.fromAction {
                        SampleDatabase.getDatabase(applicationContext).personDao().addPerson(newPerson)
                    }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                finish()
                            },
                                    {
                                        Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show()
                                        it.printStackTrace()
                                    })
            )
        }
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