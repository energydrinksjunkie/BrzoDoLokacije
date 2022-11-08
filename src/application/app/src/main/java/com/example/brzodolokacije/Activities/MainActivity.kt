package com.example.brzodolokacije.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.brzodolokacije.API.Api
import com.example.brzodolokacije.Client.Client
import com.example.brzodolokacije.Managers.SessionManager
import com.example.brzodolokacije.Models.DefaultResponse
import com.example.brzodolokacije.databinding.ActivityMainBinding
import com.example.brzodolokacije.Fragments.ExploreFragment
import com.example.brzodolokacije.Fragments.HomeFragment
import com.example.brzodolokacije.Fragments.ProfileFragment
import com.example.brzodolokacije.R
import io.ak1.OnBubbleClickListener
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        bubbleTabBar.addBubbleListener(object : OnBubbleClickListener{
            override fun onBubbleClick(id: Int) {
                Log.d("iddd", id.toString())
                when(id){
                    R.id.explore -> replaceFragment(ExploreFragment())
                    R.id.home -> replaceFragment(HomeFragment())
                    R.id.profile -> replaceFragment(ProfileFragment())

                    else -> {}
                }

            }
        })

    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    fun logOut()
    {
        val sessionManager = SessionManager(this)
        val retrofit = Client(this).buildService(Api::class.java)
        retrofit.authentication().enqueue(object:
            Callback<DefaultResponse>
        {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if(response.body()?.error.toString() == "false")
                {
                    sessionManager.deleteAuthToken()
                    sessionManager.deleteUsername()

                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity,t.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }
}