package com.example.redditpublications

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.redditpublications.adapter.RedditAdapter
import com.example.redditpublications.model.RedditModel
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        getPublications()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getPublications() {
        val url = "https://www.reddit.com/top.json"

        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                result -> parseResponseData(result)
            },
            {
                error -> Log.d("MyLog", "Error: $error")
            }
        )
        queue.add(request)
    }

    private fun parseResponseData(result: String) {
        val mainObject = JSONObject(result)
        val publicationsList: RecyclerView = findViewById(R.id.publicationsList)
        val publications = parseRedditPublications(mainObject)

        publicationsList.layoutManager = LinearLayoutManager(this)
        publicationsList.adapter = RedditAdapter(publications, this)
    }

    private fun parseRedditPublications(mainObject: JSONObject): List<RedditModel> {
        val resultPublications = ArrayList<RedditModel>()
        val publications = mainObject.getJSONObject("data")
            .getJSONArray("children")

        for (i in 0 until publications.length()) {
            val publication = publications[i] as JSONObject
            val item = RedditModel(
                "Author: " + publication.getJSONObject("data").getString("author_fullname"),
                "Created date: " + parseCreatedTime(publication),
                publication.getJSONObject("data").getString("thumbnail"),
                "Comments: " + publication.getJSONObject("data").getInt("num_comments").toString()
            )
            resultPublications.add(item)
        }

        return resultPublications
    }

    private fun parseCreatedTime(publication: JSONObject): String {
        val createdTime = publication.getJSONObject("data")
            .getLong("created_utc") * 1000
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - createdTime
        val createdDate = timeDifference / (1000 * 60 * 60)

        return "$createdDate hours ago"
    }
}