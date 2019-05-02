package com.example.personapi

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.personapi.adapters.RecipeAdapter
import com.example.personapi.api_service.FirebaseApiService
import com.example.personapi.models.RecipeItemViewModel
import com.example.personapi.view_models.RecipeDetailViewModel
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    lateinit var toolbar: ActionBar
    lateinit var sharedPreferences: SharedPreferences

    private val firebaseApiService by lazy {
        FirebaseApiService.create()
    }

    private fun save(key: String, value: String){
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    private lateinit var recipeDetailViewModel: RecipeDetailViewModel


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


    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("appName", Context.MODE_PRIVATE)

        loggedInStatus()

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setSelectedItemId(R.id.navigation_recipes);
        bottomNavigation.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)

        recipeDetailViewModel = ViewModelProviders.of(this).get(RecipeDetailViewModel::class.java)

        disposable = firebaseApiService.getRecipes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    val recipeList: MutableList<RecipeItemViewModel> = result.map {
                        RecipeItemViewModel(it.value)
                    }.toMutableList()

                    with(recipe_rv){
                        layoutManager = android.support.v7.widget.LinearLayoutManager(this@MainActivity)

                        adapter = RecipeAdapter({ item, index ->
                            onCellTap(item)
                        }, recipeList)
                    }

                },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )

    }

    fun onCellTap(item: RecipeItemViewModel) {
        save("recipeName", item.name)
        save("imgUrl", item.imgUrl)
        save("ingredients", item.ingredients)
        save("units", item.stringUnits)
        save("amounts", item.stringAmounts)
        save("instructions", item.stringInstructions)
        save("ratings", item.ratings.toString())
        val myIntent = Intent(baseContext, RecipeDetail::class.java)
        startActivity(myIntent)
    }

    override fun onBackPressed() {}

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.about_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("About")
                builder.setMessage("Created by Robin Skibola and Marin Andros")
                val dialog: AlertDialog = builder.create()
                dialog.show()
                true
            }
            R.id.help -> {
                val builder = AlertDialog.Builder(this@MainActivity)
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

    private fun loggedInStatus(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}
