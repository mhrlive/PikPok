package com.mhrlive.pikpok

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mhrlive.pikpok.data.Post
import com.mhrlive.pikpok.ui.PostsAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val posts = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        initViews()
        loadPosts()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        postsAdapter = PostsAdapter(posts)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postsAdapter
        }
    }

    private fun loadPosts() {
        firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                posts.clear()
                snapshot?.documents?.forEach { document ->
                    val post = document.toObject(Post::class.java)
                    post?.let { posts.add(it) }
                }
                postsAdapter.notifyDataSetChanged()
            }
    }
}