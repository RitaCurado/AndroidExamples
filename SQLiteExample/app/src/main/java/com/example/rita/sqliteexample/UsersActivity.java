package com.example.rita.sqliteexample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rita.sqliteexample.database.DBHandler;
import com.example.rita.sqliteexample.database.User;

import java.util.List;


public class UsersActivity extends ActionBarActivity {

    TextView idView;
    EditText userBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        idView = (TextView) findViewById(R.id.userID);
        userBox = (EditText) findViewById(R.id.userName);
    }

    public void newUser (View view) {
        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        User user = new User(userBox.getText().toString());

        dbHandler.addUser(user);

        userBox.setText("");
    }

    public void lookupUser (View view) {
        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        User user = dbHandler.findUser(userBox.getText().toString());

        if (user != null) {
            idView.setText(String.valueOf(user.getId()));
        } else {
            idView.setText("No Match Found");
        }
    }

    public void removeUser (View view) {
        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        boolean result = dbHandler.deleteUser(userBox.getText().toString());

        if (result)
        {
            idView.setText("Record Deleted");
            userBox.setText("");
        }
        else
            idView.setText("No Match Found");
    }

    public void listUsers (View view){
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        User s;

        List<User> allUsers = dbHandler.getAllUsers();

        if(!allUsers.isEmpty()){

            idView.setText("");

            for(int i = 0; i < allUsers.size(); i++){
                s = allUsers.get(i);
                idView.append("ID: " + Integer.toString(s.getId()) + " " + "Name: " + s.getName() + " | ");
            }
        }

        else
            idView.setText("No products available");

    }

    ////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
