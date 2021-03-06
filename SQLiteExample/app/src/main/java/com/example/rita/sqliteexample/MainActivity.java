package com.example.rita.sqliteexample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rita.sqliteexample.database.DBHandler;
import com.example.rita.sqliteexample.database.Product;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    TextView idView;
    EditText productBox;
    EditText quantityBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idView = (TextView) findViewById(R.id.productID);
        productBox = (EditText) findViewById(R.id.productName);
        quantityBox = (EditText) findViewById(R.id.productQuantity);
    }

    public void newProduct (View view) {
        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        int quantity = Integer.parseInt(quantityBox.getText().toString());

        Product product = new Product(productBox.getText().toString(), quantity);

        dbHandler.addProduct(product);

        productBox.setText("");
        quantityBox.setText("");
    }

    public void lookupProduct (View view) {
        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        Product product = dbHandler.findProduct(productBox.getText().toString());

        if (product != null) {
            idView.setText(String.valueOf(product.getID()));
            quantityBox.setText(String.valueOf(product.getQuantity()));
        } else {
            idView.setText("No Match Found");
        }
    }

    public void removeProduct (View view) {
        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        boolean result = dbHandler.deleteProduct(productBox.getText().toString());

        if (result)
        {
            idView.setText("Record Deleted");
            productBox.setText("");
            quantityBox.setText("");
        }
        else
            idView.setText("No Match Found");
    }

    public void listProducts (View view){
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        Product p;

        List<Product> allProducts = dbHandler.getAllProducts();

        if(!allProducts.isEmpty()){

            idView.setText("");

            for(int i = 0; i < allProducts.size(); i++){
                p = allProducts.get(i);
                idView.append("ID: " + Integer.toString(p.getID()) + " " + "Name: " + p.getProductName() +
                        " " + "Quantity: " + Integer.toString(p.getQuantity()) + " | ");
            }
        }

        else
            idView.setText("No products available");

    }

    public void callUsers(View view){
        Intent i = new Intent(this, UsersActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
