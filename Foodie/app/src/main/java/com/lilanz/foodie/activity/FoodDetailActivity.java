package com.lilanz.foodie.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lilanz.foodie.R;
import com.lilanz.foodie.uicontrol.FoodDetailUi;
import com.lilanz.foodie.uicontrol.MainUi;

import butterknife.ButterKnife;

public class FoodDetailActivity extends AppCompatActivity {

    private FoodDetailUi foodDetailUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);

        foodDetailUi = new FoodDetailUi(this);
    }

}
