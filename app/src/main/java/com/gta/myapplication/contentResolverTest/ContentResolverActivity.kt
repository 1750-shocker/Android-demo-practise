package com.gta.myapplication.contentResolverTest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.gta.myapplication.R
import com.gta.myapplication.databinding.ActivityContentResolverBinding

class ContentResolverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentResolverBinding
    private val contactList = ArrayList<String>()
    private lateinit var adapter:ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_content_resolver)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactList)
        binding.listView.adapter = adapter
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),1)
        else
            readContacts()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        when(requestCode){
            1->if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                readContacts()
            else
                finish()
        }
    }
    private fun readContacts() {
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)?.apply {
            val nameIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (moveToNext()) {
                val displayName = if (nameIndex >= 0) getString(nameIndex) else ""
                val number = if (numberIndex >= 0) getString(numberIndex) else ""
                if (displayName.isNotBlank() || number.isNotBlank()) {
                    contactList.add("$displayName\n$number")
                }
            }
            adapter.notifyDataSetChanged()
            close()
        }
    }
}
