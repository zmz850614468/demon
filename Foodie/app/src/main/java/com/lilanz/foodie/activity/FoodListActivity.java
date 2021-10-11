package com.lilanz.foodie.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lilanz.foodie.R;
import com.lilanz.foodie.uicontrol.FoodListUi;

import butterknife.ButterKnife;

public class FoodListActivity extends AppCompatActivity {

    private FoodListUi foodListUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        ButterKnife.bind(this);

        foodListUi = new FoodListUi(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
