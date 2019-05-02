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
import com.example.personapi.api_service.FirebaseApiService
import com.example.personapi.models.ShoppingItemViewModel
import com.example.personapi.models.ShoppingListValue
import com.example.personapi.view_models.ShoppingDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_shopping_list.*
import kotlinx.android.synthetic.main.shopping_detail_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.personapi.adapters.ShoppingListAdapter


class ShoppingListActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    lateinit var toolbar: ActionBar


    private val firebaseApi by lazy {
        FirebaseApiService.create()
    }

    private lateinit var shoppingDetailViewModel: ShoppingDetailViewModel

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
        disposable = firebaseApi.getShoppingCart()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    val ingredientList: MutableList<ShoppingItemViewModel> = result.map {
                        ShoppingItemViewModel(it.value)
                    }.toMutableList()
                    with(shopping_rv){
                        layoutManager = android.support.v7.widget.LinearLayoutManager(this@ShoppingListActivity)

                        adapter = ShoppingListAdapter({ item, index ->
                            onCellTap(item)
                        }, ingredientList)
                    }
                },
                { error -> Toast.makeText(this, "There are no ingredients in your shopping cart", Toast.LENGTH_SHORT).show() }
            )
    }

    fun onCellTap(item: ShoppingItemViewModel) {
        remove_btn.isClickable = true
        remove_btn.visibility = View.VISIBLE
        add_btn.isClickable = true
        add_btn.visibility = View.VISIBLE
        first_tv.visibility = View.VISIBLE
        second_tv.visibility = View.VISIBLE
        shoppingDetailViewModel.shopping_item.postValue(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setSelectedItemId(R.id.navigation_shopping);
        bottomNavigation.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)

        remove_btn.setOnClickListener {
            val amount = amount_tv.text.toString()
            val unit = unit_tv.text.toString()
            val name = name_tv.text.toString()

            add_btn.visibility = View.INVISIBLE
            second_tv.visibility = View.INVISIBLE
            first_tv.visibility = View.INVISIBLE
            name_tv.visibility = View.INVISIBLE
            remove_btn.visibility = View.INVISIBLE

            var items_list: MutableList<ShoppingListValue>

            firebaseApi.getShoppingCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        result ->
                        val shoppingList: MutableList<ShoppingItemViewModel> = result.map {
                            ShoppingItemViewModel(it.value)
                        }.toMutableList()
                        items_list = result.map { it.value }.toMutableList()

                        firebaseApi.deleteAllShoppingItems().enqueue(object: Callback<String?>{
                            override fun onFailure(call: Call<String?>, t: Throwable) {
                                println(t.message)
                            }
                            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                                println("Success")
                            }
                        })

                        for ((index, value) in items_list.withIndex()) {
                            if(value.name == name && value.amount == amount && value.unit == unit){
                                shoppingList.removeAt(index)
                            }else{
                                val map: HashMap<String, String> = hashMapOf("amount" to value.amount, "name" to value.name, "unit" to value.unit)
                                firebaseApi.insertShoppingItem(map).enqueue(object : Callback<String> {
                                    override fun onFailure(call: Call<String>, t: Throwable) {
                                        println(t.message)
                                    }
                                    override fun onResponse(call: Call<String>, response: Response<String>) {
                                        println(response.message())
                                    }
                                })
                            }
                        }

                        with(shopping_rv){
                            layoutManager = android.support.v7.widget.LinearLayoutManager(this@ShoppingListActivity)
                            if (shoppingList.size == 0){
                                Toast.makeText(this@ShoppingListActivity, "You have no ingredients in your shopping cart", Toast.LENGTH_LONG).show()
                            }
                            adapter = ShoppingListAdapter({ item, index ->
                                onCellTap(item)
                            }, shoppingList)
                        }
                    },
                    { error -> Toast.makeText(this, "There are no ingredients in your shopping cart", Toast.LENGTH_SHORT).show() }
                )
        }

        add_btn.setOnClickListener {
            val amount = amount_tv.text.toString()
            val name = name_tv.text.toString()
            val unit = unit_tv.text.toString()

            val map: HashMap<String, String> = hashMapOf("amount" to amount, "name" to name, "unit" to unit)

            firebaseApi.insertFridgeItem(map).enqueue(object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    println(t.message)
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    println(response.message())
                }
            })

            name_tv.setText("")
            unit_tv.setText("")
            amount_tv.setText("")
            Toast.makeText(applicationContext, "Successfully stored the ingredient object to your fridge", Toast.LENGTH_LONG).show()
        }

        shoppingDetailViewModel = ViewModelProviders.of(this).get(ShoppingDetailViewModel::class.java)

        disposable = firebaseApi.getShoppingCart()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    val shoppingList: MutableList<ShoppingItemViewModel> = result.map {
                        ShoppingItemViewModel(it.value)
                    }.toMutableList()
                    with(shopping_rv){
                        layoutManager = android.support.v7.widget.LinearLayoutManager(this@ShoppingListActivity)
                        for ((index, value) in shoppingList.withIndex()) {
                            if(value.amount == "" && value.name == "" && value.unit == "") {
                                shoppingList.removeAt(index)
                            }
                        }
                        if (shoppingList.size == 0){
                            Toast.makeText(this@ShoppingListActivity, "You have no ingredients in your shopping cart", Toast.LENGTH_LONG).show()
                        }else {
                            adapter = ShoppingListAdapter({ item, index ->
                                onCellTap(item)
                            }, shoppingList)
                        }
                    }
                },
                { error -> Toast.makeText(this, "There are no ingredients in your shopping cart", Toast.LENGTH_SHORT).show() }
            )
    }
}
