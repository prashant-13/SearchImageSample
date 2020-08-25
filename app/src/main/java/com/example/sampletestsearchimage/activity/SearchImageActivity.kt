package com.example.sampletestsearchimage.activity

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sampletestsearchimage.R
import com.example.sampletestsearchimage.Utilities.NetworkHelper
import com.example.sampletestsearchimage.Utilities.Toaster
import com.example.sampletestsearchimage.Utilities.Utils
import com.example.sampletestsearchimage.Utilities.hideKeyboard
import com.example.sampletestsearchimage.adapters.ImageListAdapter
import com.example.sampletestsearchimage.database.entity.ImageResponse
import com.example.sampletestsearchimage.network.CustomResponse
import com.google.gson.Gson
import com.healthcoco.safeplace.network.Networking
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_image.*

class SearchImageActivity : AppCompatActivity() {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var imageList = listOf<ImageResponse>()
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_image)

        submitButton.setOnClickListener {
            if (!Utils.isNullOrBlank(commentEditText.text.toString())) {
                searchImageByName()
                it.hideKeyboard()
            } else
                Toaster.show(this, getString(R.string.please_enter_image_name))
        }
    }

    private fun setupRecyclerView(result: List<ImageResponse>) {
        imageList = result
        val layoutManager = GridLayoutManager(this, 3)
        rvComments.layoutManager = layoutManager

        val adapter = ImageListAdapter(this, imageList)
        rvComments.adapter = adapter
    }

    private fun searchImageByName() {
        if (NetworkHelper.isNetworkConnected(applicationContext)) {
            progressDialog = ProgressDialog.show(this, "", "Please wait...", true)
            compositeDisposable.add(
                    Networking.apiService.searchImageByName(commentEditText.text.toString())
                            .map { response: CustomResponse ->
                                return@map Gson().fromJson(Gson().toJson(response.data), Array<ImageResponse>::class.java).asList()
                            }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { result ->
                                        run {
                                            progressDialog.dismiss()
                                            setupRecyclerView(result)
                                        }
                                    },
                                    { error ->
                                        NetworkHelper.handleNetworkError(error, this)
                                        progressDialog.dismiss()
                                    }
                            ))
        } else
            Toaster.show(this, getString(R.string.network_connection_error))
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
