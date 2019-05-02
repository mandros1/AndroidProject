package com.example.personapi

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.personapi.adapters.FridgeAdapter
import com.example.personapi.adapters.IngredientAdapter
import com.example.personapi.api_service.FirebaseApiService
import com.example.personapi.models.FridgeItemViewModel
import com.example.personapi.models.FridgeItemsValue
import com.example.personapi.models.IngredientsItemViewModel
import com.example.personapi.models.ShoppingListValue
import com.example.personapi.view_models.IngredientDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_fridge.*
import kotlinx.android.synthetic.main.activity_ingredient.*
import kotlinx.android.synthetic.main.ingredient_detail_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IngredientActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    lateinit var toolbar: ActionBar

    private val firebaseApi by lazy {
        FirebaseApiService.create()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.about_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                val builder = AlertDialog.Builder(this@IngredientActivity)
                builder.setTitle("About")
                builder.setMessage("Created by Robin Skibola and Marin Andros")
                val dialog: AlertDialog = builder.create()
                dialog.show()
                true
            }
            R.id.help -> {
                val builder = AlertDialog.Builder(this@IngredientActivity)
                builder.setTitle("How to use")
                builder.setMessage("Browse recipes that you like, click on one to get a broader picture" +
                        "of what it contains and how to prepare it. When on the detail page of the recipe" +
                        " you can choose to add all the necessary ingredients to the shopping cart. Browse " +
                        "ingredients and add them to the shopping list or directly to your fridge if you already own them" +
                        ". Items can be moved to your fridge or be completely removed from the shopping cart. " +
                        "Already owned ingredients can be found under My Fridge tab. Also see our amazing chat at the end.")
                val dialog: AlertDialog = builder.create()
                dialog.show()
                true
            }
            R.id.logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private lateinit var ingredientDetailViewModel: IngredientDetailViewModel

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

    override fun onResume() {
        super.onResume()
        disposable = firebaseApi.getIngredients()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    val ingredientList: MutableList<IngredientsItemViewModel> = result.map {
                        IngredientsItemViewModel(it.value)
                    }.toMutableList()
                    with(ingredients_rv){
                        layoutManager = android.support.v7.widget.LinearLayoutManager(this@IngredientActivity)

                        adapter = IngredientAdapter({ item, index ->
                            onCellTap(item)
                        }, ingredientList)
                    }
                },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )
    }

    override fun onBackPressed() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setSelectedItemId(R.id.navigation_ingredients);
        bottomNavigation.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)

        add_ingredient_floating_btn.setOnClickListener {
            val myIntent = Intent(baseContext, AddIngredientActivity::class.java)
            startActivity(myIntent)
        }

        ingredientDetailViewModel = ViewModelProviders.of(this).get(IngredientDetailViewModel::class.java)

        amount_et.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val calories_string = getCaloriesAmount()
                if(calories_string != "") {
                    calories_tv.setText("Calories: $calories_string kCal")
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        disposable = firebaseApi.getIngredients()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    val ingredientList: MutableList<IngredientsItemViewModel> = result.map {
                        IngredientsItemViewModel(it.value)
                    }.toMutableList()
                    with(ingredients_rv){
                        layoutManager = android.support.v7.widget.LinearLayoutManager(this@IngredientActivity)

                        adapter = IngredientAdapter({ item, index ->
                            onCellTap(item)
                        }, ingredientList)
                    }
                },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )

        fridge_btn.setOnClickListener {
            val calories = getCaloriesAmount()
            if(calories != ""){
                val amount = amount_et.text.toString()
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
                amount_et.setText("")
                calories_tv.setText("")
                amount_et.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "Successfully stored the ingredient object to your fridge", Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(this@IngredientActivity,
                    "Please add ingredient's amount before adding it to the FridgeActivity",
                    Toast.LENGTH_LONG).show()
            }
        }

        shopping_btn.setOnClickListener {
            val calories = getCaloriesAmount()
            if(calories != ""){
                val amount = amount_et.text.toString()
                val name = name_tv.text.toString()
                val unit = unit_tv.text.toString()

                val map: HashMap<String, String> = hashMapOf("amount" to amount, "name" to name, "unit" to unit)
                firebaseApi.insertShoppingItem(map).enqueue(object: Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        println(t.message)
                    }
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        println(response.message())
                    }
                })
                name_tv.setText("")
                unit_tv.setText("")
                amount_et.setText("")
                calories_tv.setText("")
                amount_et.visibility = View.INVISIBLE
                Toast.makeText(applicationContext,
                    "Successfully stored the ingredient object to your shopping list",
                    Toast.LENGTH_LONG)
                    .show()

            }else {
                Toast.makeText(this@IngredientActivity,
                    "Please add ingredient's amount before adding it to your shopping list",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getCaloriesAmount(): String {
        val content = amount_et.text.toString()
        val calories_per_unit = calories_amt_tv.text.toString()
        if(content != "" && calories_per_unit != "") {
            try {
                val amount = content.toInt()
                val cal_per_amount = calories_per_unit.toInt()
                val calories = (amount * cal_per_amount) / 1000.0
                val cal_string = "%.2f".format(calories)
                return cal_string
            } catch (nfe: NumberFormatException) {
                Toast.makeText(this@IngredientActivity, "Amount needs to be an integer", Toast.LENGTH_LONG).show()
            }
        }
        return ""
    }

    fun onCellTap(item: IngredientsItemViewModel) {
        fridge_btn.isClickable=true
        fridge_btn.visibility= View.VISIBLE
        amount_et.visibility= View.VISIBLE
        shopping_btn.isClickable=true
        shopping_btn.visibility= View.VISIBLE
        calories_tv.visibility= View.VISIBLE
        amount_et.setText("")
        calories_tv.setText("")
        ingredientDetailViewModel.ingredient.postValue(item)
    }
}
