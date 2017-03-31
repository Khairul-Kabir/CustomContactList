package com.example.sshak.customcontactlist.database;

/**
 * Created by sshak on 3/31/2017.
 */

public class TableAttributes {public static final String CONTACT_TABLE_NAME = "Contact";
    public static final String NAME = "name";
    public static final String PHONE = "phone";


    public String contactTableCreateQuery(){
        return "CREATE TABLE "+CONTACT_TABLE_NAME+"(contact_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME+" TEXT," +
                PHONE+" TEXT,";
    }
}