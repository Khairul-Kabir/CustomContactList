package com.example.sshak.customcontactlist;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.sshak.customcontactlist.database.DatabaseHelper;
import com.example.sshak.customcontactlist.model.ContactVO;

import java.util.ArrayList;
import java.util.List;

public class AllContacts extends Activity {

    RecyclerView rvContacts;
    DatabaseHelper db;
    Button btnLoadmore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_all_contacts);

        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        //rvContacts.setHasFixedSize(true);
        db = new DatabaseHelper(AllContacts.this);
        btnLoadmore = (Button)findViewById(R.id.buttonLoadMore);
        btnLoadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllContacts();
            }
        });

        getAllContacts();
    }

    private void getAllContacts() {
        List<ContactVO> contactVOList = new ArrayList();
        ContactVO contactVO;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVO = new ContactVO();
                    contactVO.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactVO.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    contactVOList.add(contactVO);
                    db.insertContact(contactVO);
                }

            }
            //while (contactVOList.size()==10){
                AllContactsAdapter contactAdapter = new AllContactsAdapter(contactVOList, getApplicationContext());
                rvContacts.setLayoutManager(new LinearLayoutManager(this));
                rvContacts.setHasFixedSize(true);
                rvContacts.setAdapter(contactAdapter);
            //}
        }
    }
}
