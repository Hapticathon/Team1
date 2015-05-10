package com.example.dawid.touchpin;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.dexode.core.view.HideNavigationBarComponent;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new RandomKeyboardFragmet(),
																   RandomKeyboardFragmet.class.getName()).add(
					new HideNavigationBarComponent(), "hider").commit();
		}
	}
}
