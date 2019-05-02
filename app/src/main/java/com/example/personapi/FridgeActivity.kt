package com.example.personapi

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar
import android.view.View
import android.widget.Toast
import com.example.personapi.adapters.FridgeAdapter
import com.example.personapi.api_service.FirebaseApiService
import com.example.personapi.models.FridgeItemViewModel
import com.example.personapi.models.FridgeItemsValue
import com.example.personapi.view_models.FridgeDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_fridge.*
import kotlinx.android.synthetic.main.fridge_detail_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FridgeActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    lateinit var toolbar: ActionBar


    private val firebaseApi by lazy {
        FirebaseApiService.create()
    }

    private lateinit var fridgeDetailViewModel: FridgeDetailViewModel

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId){
            R.id.navigation_recipes -> {
                val myIntent = Intent(baseContext, MainActivity::class.java)
                startActivity(myIntent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_ingredients -> {
                val myIntent = Intent(baseContext, IngredientActivity::class.java)
                startActivity(myIntent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_shopping -> {
                val myIntent = Intent(baseContext, ShoppingListActivity::class.java)
                startActivity(myIntent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_fridge -> {
                val myIntent = Intent(baseContext, FridgeActivity::class.java)
                startActivity(myIntent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onBackPressed() {}

    override fun onResume() {
        super.onResume()
        disposable = firebaseApi.getFridgeItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    val ingredientList: MutableList<FridgeItemViewModel> = result.map {
                        FridgeItemViewModel(it.value)
                    }.toMutableList()
                    with(fridge_rv){
                        layoutManager = android.support.v7.widget.LinearLayoutManager(this@FridgeActivity)

                        adapter = FridgeAdapter({ item, index ->
                            onCellTap(item)
                        }, ingredientList)
                    }
                },
                { error -> Toast.makeText(this, "There are no ingredients in your fridge", Toast.LENGTH_SHORT).show() }
            )
    }

    fun onCellTap(item: FridgeItemViewModel) {
        remove_btn.isClickable = true
        remove_btn.visibility = View.VISIBLE
        name_tv.visibility = View.VISIBLE
        fridgeDetailViewModel.fridge_item.postValue(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setSelectedItemId(R.id.navigation_fridge);
        bottomNavigation.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)

        remove_btn.setOnClickListener {
            val amount = amount_tv.text.toString()
            val unit = unit_tv.text.toString()
            val name = name_tv.text.toString()

            remove_btn.visibility = View.INVISIBLE
            name_tv.visibility = View.INVISIBLE

            var items_list: MutableList<FridgeItemsValue>

            firebaseApi.getFridgeItems()
                .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                {
                        result ->
                        val fridgeList: MutableList<FridgeItemViewModel> = result.map {
                            FridgeItemViewModel(it.value)
                        }.toMutableList()
                        items_list = result.map { it.value }.toMutableList()
                        firebaseApi.deleteAllFridgeItems().enqueue(object: Callback<String?>{
                            override fun onFailure(call: Call<String?>, t: Throwable) {
                                println(t.message)
                            }
                            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                                println("Success")
                            }
                        })

                    for ((index, value) in items_list.withIndex()) {
                        if(value.name == name && value.amount == amount && value.unit == unit){
                            fridgeList.removeAt(index)
                        }else{
                            val map: HashMap<String, String> = hashMapOf("amount" to value.amount, "name" to value.name, "unit" to value.unit)
                            firebaseApi.insertFridgeItem(map).enqueue(object : Callback<String> {
                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    println(t.message)
                                }
                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                    println(response.message())
                                }
                            })
                        }
                    }

                    with(fridge_rv){
                        layoutManager = android.support.v7.widget.LinearLayoutManager(this@FridgeActivity)
                        if (items_list.size == 0){
                            Toast.makeText(this@FridgeActivity, "You have no ingredients in your fridge", Toast.LENGTH_LONG).show()
                        }
                        adapter = FridgeAdapter({ item, index ->
                            onCellTap(item)
                        }, fridgeList)
                    }

                    },
                    { error -> Toast.makeText(this, "There are no ingredients in your fridge", Toast.LENGTH_SHORT).show() }
                )
        }

        fridgeDetailViewModel = ViewModelProviders.of(this).get(FridgeDetailViewModel::class.java)


        disposable = firebaseApi.getFridgeItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    val fridgeList: MutableList<FridgeItemViewModel> = result.map {
                        FridgeItemViewModel(it.value)
                    }.toMutableList()
                    with(fridge_rv){
                        layoutManager = android.support.v7.widget.LinearLayoutManager(this@FridgeActivity)
                        for ((index, value) in fridgeList.withIndex()) {
                            if(value.amount == "" && value.name == "" && value.unit == "") {
                                fridgeList.removeAt(index)
                            }
                        }
                        if (fridgeList.size == 0){
                            Toast.makeText(this@FridgeActivity, "You have no ingredients in your fridge", Toast.LENGTH_LONG).show()
                        }else {
                            adapter = FridgeAdapter({ item, index ->
                                onCellTap(item)
                            }, fridgeList)
                        }
                    }
                },
                { error -> Toast.makeText(this, "There are no ingredients in your fridge", Toast.LENGTH_SHORT).show() }
            )
    }
}
