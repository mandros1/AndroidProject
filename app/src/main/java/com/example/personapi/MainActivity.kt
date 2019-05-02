package com.example.personapi

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar
import android.widget.Toast
import com.example.personapi.adapters.PersonAdapter
import com.example.personapi.api_service.PeopleApiService
import com.example.personapi.models.Name
import com.example.personapi.models.Person
import com.example.personapi.models.PersonItemViewModel
import com.example.personapi.view_models.PersonDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.person_detail_fragment.*

class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    lateinit var toolbar: ActionBar

    private val personApiService by lazy {
        PeopleApiService.create()
    }

    private lateinit var personDetailViewModel: PersonDetailViewModel


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

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)

        personDetailViewModel = ViewModelProviders.of(this).get(PersonDetailViewModel::class.java)

        disposable = personApiService.getPeople(
            "3",
            "10",
            "abc",
            "gender,name,nat")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    val personList: MutableList<PersonItemViewModel> = result.results.map {
                        PersonItemViewModel(it)
                    }.toMutableList()
                    result.results
                    val name = result.results[0].name
                    Toast.makeText(this, "name is $name", Toast.LENGTH_LONG).show()

                    with(person_rv){
                        layoutManager = android.support.v7.widget.LinearLayoutManager(this@MainActivity)

                        adapter = PersonAdapter({ item, index ->
                            onCellTap(item)
                        }, personList)
                    }

                },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )


        val init = Person("",
            Name("", "", "Welcome"),
            "")
        val personVM = PersonItemViewModel(init)
        personDetailViewModel.person.postValue(personVM)
    }

    fun onCellTap(item: PersonItemViewModel) {
        // Send cell value
        if(item.gender == "male"){
            person_iv.setImageResource(R.drawable.ic_male)
        } else if(item.gender == "female") {
            person_iv.setImageResource(R.drawable.ic_female)
        }
        personDetailViewModel.person.postValue(item)
    }

}
