package com.example.basedatos


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var itemAdapter: ItemAdapter
    private val itemList = mutableListOf<Pair<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        val editTextItem = findViewById<EditText>(R.id.editTextItem)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val recyclerViewItems = findViewById<RecyclerView>(R.id.recyclerViewItems)

        // Setup RecyclerView
        recyclerViewItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        itemAdapter = ItemAdapter(itemList)
        recyclerViewItems.adapter = itemAdapter

        buttonAdd.setOnClickListener {
            val item = editTextItem.text.toString()
            if (item.isNotEmpty()) {
                // Add item to Firebase Database
                val newItemRef = database.child("items").push()
                newItemRef.setValue(item)
                editTextItem.text.clear()
            }
        }

        // Read from Firebase Database
        database.child("items").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (itemSnapshot in snapshot.children) {
                    val itemId = itemSnapshot.key.orEmpty()
                    val itemText = itemSnapshot.getValue(String::class.java).orEmpty()
                    itemList.add(itemId to itemText)
                }
                itemAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }
}
