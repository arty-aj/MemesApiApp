package com.example.memesapiapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.memesapiapp.models.Meme
import com.example.memesapiapp.ui.theme.MemesApiAppTheme
import com.example.memesapiapp.utils.RetrofitInstance
import com.example.memesapiapp.views.DetailsScreen
import com.example.memesapiapp.views.MainScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemesApiAppTheme {
                //nav controller is needed to allow for going from one to another screen in navhost
                val navController = rememberNavController()
                //list of memes
                var memesList by remember {
                    mutableStateOf(listOf<Meme>())
                }
                //Coroutine allows for asynchronous programming.
                //They allow for multiple tasks to be executed at the same time
                //without blocking the main thread.
                //Usually used for database or network access.
                val scope = rememberCoroutineScope()

                //When we want to execute a composable in on create.
                //As soon as the app is open, we want to see the result of the function and
                //create a view.
                //Inside LaunchedEffect we will create it.
                LaunchedEffect(key1 = true){
                    //IO thread = Data Thread
                    scope.launch(Dispatchers.IO){
                        //Anything here is connected to data thread.
                        //Automatically launched
                        val response = try {
                            RetrofitInstance.api.getMemesList()
                        } catch (e: IOException){
                            Toast.makeText(this@MainActivity, "IO ERROR ${e.message}", Toast.LENGTH_SHORT).show()
                            return@launch
                        }catch (e: HttpException){
                            Toast.makeText(this@MainActivity, "HTTP ERROR ${e.message}", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        if(response.isSuccessful && response.body() != null){
                            //now we connect to main thread
                            withContext(Dispatchers.Main){
                                //Without this we dont see any data
                                //Data stored in memesList
                                memesList = response.body()!!.data.memes
                            }
                        }
                    }
                }

                //Navhost controls our pages
                NavHost(navController = navController , startDestination = "MainScreen"){
                    composable(route = "MainScreen"){
                        MainScreen(
                            memesList = memesList,
                            navController = navController)
                    }
                    composable(
                        route = "DetailsScreen?name={name}&url={url}",
                        arguments = listOf(
                            navArgument(name = "name"){
                              type = NavType.StringType
                            },
                            navArgument(name = "url"){
                                type = NavType.StringType
                            }
                        )
                        ){
                        DetailsScreen(
                            name=it.arguments?.getString("name"),
                            url=it.arguments?.getString("url")
                        )
                    }
                }
            }
        }
    }
}

