package com.example.dawid.touchpin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import nxr.tpad.lib.TPad;
import nxr.tpad.lib.TPadImpl;
import nxr.tpad.lib.views.FrictionMapView;

/**
 * Created by Dawid Drozd aka Gelldur on 09.05.15.
 */
public class RandomKeyboardFragmet extends Fragment implements View.OnClickListener {


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
							 final Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.normal_pad, container, false);

		ArrayList<Integer> numbers = new ArrayList<>(9);

		_buttons = new Button[9];
		int i = 0;
		for (int id : _buttonsId) {
			numbers.add(i);
			_buttons[i] = (Button) root.findViewById(id);
			++i;
		}


		Collections.shuffle(numbers);

		i = 0;
		for (Button button : _buttons) {
			button.setText(String.valueOf(numbers.get(i++)));
			button.setOnClickListener(this);
		}

		_tpad = new TPadImpl(getActivity());

		_mapView = (FrictionMapView) root.findViewById(R.id.frictionMapView);
		_mapView.setTpad(_tpad);

		root.findViewById(R.id.buttonSwitcher).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {

				View layoutKeyboard = getActivity().findViewById(R.id.layoutKeyboard);
				if (layoutKeyboard.getVisibility() == View.VISIBLE) {
					layoutKeyboard.setVisibility(View.INVISIBLE);
				} else {
					layoutKeyboard.setVisibility(View.VISIBLE);
				}
			}
		});


		root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {

				final Bitmap bitmap = Bitmap.createBitmap(_mapView.getWidth(), _mapView.getHeight(),
														  Bitmap.Config.RGB_565);

				Canvas canvas = new Canvas(bitmap);

				Paint paint = new Paint();
				paint.setColor(Color.WHITE);
				paint.setTextSize(convertDpToPixel(50, getActivity()));
				paint.setTextAlign(Paint.Align.CENTER);
				int marginTop = (int) convertDpToPixel(30, getActivity());

				for (Button button : _buttons) {
					final Rect rect = locateView(button);
					canvas.drawText(button.getText().toString(), rect.centerX(), rect.centerY() - marginTop, paint);
				}

				_mapView.setDataBitmap(bitmap);

				root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		});

		return root;
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device density.
	 *
	 * @param dp
	 * 		A value in dp (density independent pixels) unit. Which we need to convert into
	 * 		pixels
	 *
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	public Rect locateView(View view) {
		Rect loc = new Rect();
		int[] location = new int[2];
		if (view == null) {
			return loc;
		}
		view.getLocationOnScreen(location);

		loc.left = location[0];
		loc.top = location[1];
		loc.right = loc.left + view.getWidth();
		loc.bottom = loc.top + view.getHeight();
		return loc;
	}

	@Override
	public void onDestroy() {
		_tpad.disconnectTPad();
		super.onDestroy();
	}

	@Override
	public void onClick(final View view) {
		Button button = (Button) view;

		_password += button.getText();

		if (_password.equals("1005")) {
			getActivity().finish();
			Toast.makeText(getActivity(), "Congratulations you pass!", Toast.LENGTH_SHORT).show();

		} else if (_password.length() >= 4) {
			Toast.makeText(getActivity(), "You shall not pass!", Toast.LENGTH_LONG).show();
			getActivity().finish();
		}

	}

	private String _password = "";

	private FrictionMapView _mapView;
	private Button[] _buttons;
	private TPad _tpad;
	private int[] _buttonsId =
			new int[]{R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7,
					  R.id.button8, R.id.button9};

}
