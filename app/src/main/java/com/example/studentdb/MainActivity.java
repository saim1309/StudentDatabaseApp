package com.example.studentdb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //initialization of UI elements and variables
    DatabaseHelper myDb;
    SQLiteDatabase db;
    private ListView courseListView;
    private RadioButton radioCreditTwo,radioCreditThree,radioCreditFour;
    private RadioGroup radioGroupCredits;
    private String creditSelected = "",courseSelected = "",Sfirstname="",SlastName="",Smarks="";
    private EditText id,firstName,lastName,marks;
    private Button btnAdd, btnUpdate, btnView, btnDelete, btnSearchId, btnSearchCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //calling the constructor present in DatabaseHelper Class
        myDb =new DatabaseHelper(this);

        //mapping of UI elements using Id
        radioGroupCredits = findViewById(R.id.radioGroupCredits);
        radioCreditTwo = findViewById(R.id.radioTwo);
        radioCreditThree = findViewById(R.id.radioThree);
        radioCreditFour = findViewById(R.id.radioFour);
        courseListView = findViewById(R.id.listCourse);
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);
        btnUpdate =findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnSearchId = findViewById(R.id.btnSearchId);
        btnSearchCourse = findViewById(R.id.btnSearchCourse);
        id = findViewById(R.id.etId);
        firstName =findViewById(R.id.etFname);
        lastName =findViewById(R.id.etLname);
        marks = findViewById(R.id.etMarks);
        Sfirstname = firstName.getText().toString();
        SlastName = lastName.getText().toString();
        Smarks = marks.toString();


        //listener for credit selection and saving the value selected by user
        radioGroupCredits.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedCredit) {
                switch (checkedCredit) {
                    case R.id.radioFour:
                        creditSelected = "4";
                        Toast.makeText(MainActivity.this,"Checked "+creditSelected,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioThree:
                        creditSelected = "3";
                        Toast.makeText(MainActivity.this,"Checked "+creditSelected,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioTwo:
                        creditSelected = "2";
                        Toast.makeText(MainActivity.this,"Checked "+creditSelected,Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        //when no value is selected
                        creditSelected = "999";
                        Toast.makeText(MainActivity.this,"Credit not selected",Toast.LENGTH_SHORT).show();

                }//end of switch
            }//end of listener on radioGroup
        });

        //setting up the list view for the course
        courseListView = findViewById(R.id.listCourse);
        final ArrayList<String> courseArrayList = new ArrayList<>();
        courseArrayList.add("PROG 8110");
        courseArrayList.add("PROG 8215");
        courseArrayList.add("PROG 1815");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,courseArrayList);
        courseListView.setAdapter(arrayAdapter);


        //listener for course selection and saving the value selected by user
        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                courseSelected = courseArrayList.get(i).toString();
                Toast.makeText(MainActivity.this,"Course selected "+courseSelected,Toast.LENGTH_SHORT).show();
            }
        });//end of listener


        //Adding data in database and setting onclick listener for the add button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().equals("") || courseSelected.equals("")||firstName.getText().toString().equals("")||lastName.getText().toString().equals("")||marks.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"All the fields should be filled", Toast.LENGTH_LONG).show();
                }
                else{
                    boolean isInserted = myDb.addData(firstName.getText().toString(),lastName.getText().toString(),marks.getText().toString(),courseSelected,creditSelected);
                    if(isInserted == true)
                        Toast.makeText(MainActivity.this,"Data inserted successfully",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this,"Data insertion failed",Toast.LENGTH_SHORT).show();
                    clear(id,firstName,lastName,marks);
                }


            }
        });

        //Viewing data in database and setting onclick listener for the view button
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor result = myDb.viewData();
                if(result.getCount() == 0) {
                    //no data available
                    showMessage("Error", "No data found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (result.moveToNext()){
                    buffer.append("Id : "+result.getString(0)+"\n");
                    buffer.append("First Name : "+result.getString(1)+"\n");
                    buffer.append("Last Name : "+result.getString(2)+"\n");
                    buffer.append("Marks : "+result.getString(3)+"\n");
                    buffer.append("Course : "+result.getString(4)+"\n");
                    buffer.append("Credit : "+result.getString(5)+"\n\n");
                }
                showMessage("Data",buffer.toString());
                clear(id,firstName,lastName,marks);

            }
        });


        //Deleting data in database for the given Id and setting onclick listener for the add button
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"Enter Id first", Toast.LENGTH_LONG).show();
                }
                else{
                    int deletedRows = myDb.deleteData(id.getText().toString());
                    if(deletedRows >0)
                        Toast.makeText(MainActivity.this,"Data deleted successfully",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this,"No more data to delete",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Searching data in database for the given id and setting onclick listener for the add button
        btnSearchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"Enter Id first", Toast.LENGTH_LONG).show();
                }
                else{
                    Cursor result = myDb.searchId(id.getText().toString());
                    showDetails(result);

                }

            }
        });

        //Searching data in database for the given course and setting onclick listener for the add button
        btnSearchCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor result = myDb.searchCourse(courseSelected);
                showDetails(result);
            }
        });

        //Updating data in database for the given id and setting onclick listener for the add button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"No id to update", Toast.LENGTH_LONG).show();
                }
                else{
                    if(firstName.getText().toString().equals("")&&lastName.getText().toString().equals("")&&marks.getText().toString().equals(""))
                    {
                        Cursor result = myDb.getFieldsFromDB(id.getText().toString());
                        if(result.getCount() == 0) {
                            //no data available
                            showMessage("Error", "No data found");
                            return;
                        }
                        result.moveToFirst();
                        firstName.setText(result.getString(1));
                        lastName.setText(result.getString(2));
                        marks.setText(result.getString(3));
                        courseSelected = result.getString(4);
                        creditSelected = result.getString(5);

                    }

                    else{
                        myDb.updateData(firstName.getText().toString(),lastName.getText().toString(),marks.getText().toString(), courseSelected, creditSelected, id.getText().toString());
                        Toast.makeText(MainActivity.this, "Data updated successfully",Toast.LENGTH_LONG).show();
                    }


                }


            }
        });

    }//end of onCreate

    //function to create custom AlertDialog box and setting title and message for display
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    //function to display data inside AlerDialog using cursor and StringBuffer
    public void showDetails(Cursor result){
        if(result.getCount() == 0) {
            //no data available
            showMessage("Error", "No data found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        //iterating through different rows of data in database and adding each one to StringBuffer for display
        while (result.moveToNext()){
            buffer.append("Id : "+result.getString(0)+"\n");
            buffer.append("First Name : "+result.getString(1)+"\n");
            buffer.append("Last Name : "+result.getString(2)+"\n");
            buffer.append("Marks : "+result.getString(3)+"\n");
            buffer.append("Course : "+result.getString(4)+"\n");
            buffer.append("Credit : "+result.getString(5)+"\n\n");
        }
        showMessage("Data",buffer.toString());

    }

    //function to clear and reset all the fields after certain operation is performed
    public void clear(EditText id,EditText firstName,EditText lastName,EditText marks){
        id.setText("");
        firstName.setText("");
        lastName.setText("");
        marks.setText("");

    }


}
