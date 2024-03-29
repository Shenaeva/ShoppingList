package com.example.shoppinglist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppinglist.model.Product;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Объявляем view, находим в R.id по соответствующему идентификатору
        final ListView mainListView = findViewById(R.id.mainListView);
        final Button addButton = findViewById(R.id.addButton);

        final EditText prodName = findViewById(R.id.prodNameEditText);
        final EditText prodPrice = findViewById(R.id.prodPriceEditText);
        final EditText prodAmount = findViewById(R.id.prodAmountEditText);

        final TextView sumTextView = findViewById(R.id.sumTextView);
        final Button countButton = findViewById(R.id.countButton);

        //Коллекция (лист) класса "Продукт"
        final List<Product> productList = new ArrayList<>();
        //Адаптер коллекции продуктов
        final ArrayAdapter<Product> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, productList);

        //устанавливаем адаптер
        mainListView.setAdapter(adapter);

        //устанавливаем слушатель на кнопку добавления продукта в список
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = makeProduct(prodName, prodPrice, prodAmount);
                if (product != null){
                    productList.add(product);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //устанавливаем слушатель на кнопку подсчета суммы
        countButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = String.format("%.2f", countSum(productList));
                sumTextView.setText(s);
            }
        });

        //устанавливаем слушатель на ListView - выводим объект в тост и удаляем его из адаптера
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View itemClicked, int i, long l) {
                //делаем тост
                Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
                        Toast.LENGTH_SHORT).show();

                adapter.remove(adapter.getItem(i));
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Собирает из значений EditText новый экземпляр класса Product
     * @param prodName  имя продукта
     * @param prodPrice цена продукта
     * @param prodAmount количество продуктов
     * @return  продукт
     */
    private Product makeProduct(EditText prodName, EditText prodPrice, EditText prodAmount){
        try {
            if (prodName.getText().toString().equals("") || prodName.getText() == null) {
                Toast.makeText(getApplicationContext(), "Enter product name",
                        Toast.LENGTH_SHORT).show();
                return null;
            }
            if (prodPrice.getText().toString().equals("") || prodName.getText() == null) {
                Toast.makeText(getApplicationContext(), "Enter product price",
                        Toast.LENGTH_SHORT).show();
                return null;
            }
            if (prodAmount.getText().toString().equals("") || prodName.getText() == null) {
                Toast.makeText(getApplicationContext(), "Enter product amount",
                        Toast.LENGTH_SHORT).show();
                return null;
            }

            Product product = new Product(
                    prodName.getText().toString(),
                    Integer.parseInt(prodAmount.getText().toString()),
                    Double.parseDouble(prodPrice.getText().toString())
            );
            prodName.setText("");
            prodAmount.setText("");
            prodPrice.setText("");

            return product;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Считает сумму по всем добавленным продуктам
     * @param products  коллекция продуктов
     * @return  сумма
     */
    private double countSum(List<Product> products){

        double sum = 0;

        for (Product product : products) {
            sum += product.getPrice()*product.getAmount();
        }

        return sum;
    }
}
