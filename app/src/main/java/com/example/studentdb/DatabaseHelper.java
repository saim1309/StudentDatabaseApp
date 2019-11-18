package com.example.studentdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DatabaseName = "StudentDb";
    public static final String TableName = "StudentTable";
    public static final String Col1 = "Id";
    public static final String Col2 = "FirstName";
    public static final String Col3 = "LastName";
    public static final String Col4 = "Marks";
    public static final String Col5 = "Course";
    public static final String Col6 = "Credits";

    //constructor which is responsible for database creation
    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseName, null, 1);
    }

    //this method created the database with the given columns names and its data types.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table "+TableName+ " (Id INTEGER PRIMARY KEY AUTOINCREMENT, FirstName VARCHAR, LastName VARCHAR, Marks VARCHAR, Course VARCHAR,Credits VARCHAR) " );
    }

    //Drop if any already EXISTSing table if the program is re-run and re-created the table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TableName);
        onCreate(sqLiteDatabase);
    }

    //function to add data in database by taking value from user input fields
    public boolean addData(String firstName, String lastName, String marks, String course, String credits){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col2,firstName);
        contentValues.put(Col3,lastName);
        contentValues.put(Col4,marks);
        contentValues.put(Col5,course);
        contentValues.put(Col6,credits);
        //the below query return -1 of record insertion fails
        long result = sqLiteDatabase.insert(TableName,null,contentValues);
        if (result == -1)
            return false;
        else
            return true;

    }

    //function to search record on the given id
    public Cursor searchId(String Id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("select * from "+TableName+" where ID = ?", new String[] {Id});
        return result;
    }

    //to populate values in the fields when the id given --> so that it can be updated
    public Cursor getFieldsFromDB(String Id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("select * from "+TableName+" where ID = ?", new String[] {Id});
        return result;
    }

    //function to search record on the given course
    public Cursor searchCourse(String Course){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("select * from "+TableName+" where Course = ?", new String[] {Course});
        return result;
    }

    //function to delete record on the given id
    public int deleteData(String Id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TableName,"Id=?",new String[]{Id});
    }

    //function to update record in database
    public void updateData(String firstName, String lastName, String marks,String course, String credit, String id )
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("Update StudentTable Set FirstName = '" + firstName + "', LastName = '" + lastName + "'" +
                ",Marks = '" + marks + "',Course = '" + course + "', Credits = '" + credit + "' where Id = '" + id + "'");
    }

    //function to view data in the database
    public Cursor viewData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM "+TableName,null);
        return result;
    }
}
