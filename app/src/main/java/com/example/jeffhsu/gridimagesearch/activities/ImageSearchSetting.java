package com.example.jeffhsu.gridimagesearch.activities;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jeffhsu.gridimagesearch.Listeners.CustomOnItemSelectedListener;
import com.example.jeffhsu.gridimagesearch.R;

public class ImageSearchSetting extends ActionBarActivity {

    private Spinner spSize, spColor, spType;
    private EditText etSite;
    private Button btnSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search_setting);

        spSize = (Spinner) findViewById(R.id.spSize);
        spColor = (Spinner) findViewById(R.id.spColor);
        spType = (Spinner) findViewById(R.id.spType);
        EditText etSite = (EditText) findViewById(R.id.etSite);
        btnSet = (Button) findViewById(R.id.btnSet);
        addListnerOnSpinnerItemSelection();

        SharedPreferences prefs = getSharedPreferences("MyPref",0);
        int size = prefs.getInt("size",0);
        int color = prefs.getInt("color", 0);
        int type = prefs.getInt("type", 0);
        String site = prefs.getString("site", null);

        if (size!=0) {
            spSize.setSelection(size);
        }
        if (color!=0) {
            spColor.setSelection(color);
        }
        if (type!=0) {
            spType.setSelection(type);
        }
        if (site!=null && !site.equals("")) {
            etSite.setText(site);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_search_setting, menu);
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

    public void addListnerOnSpinnerItemSelection() {

        spSize.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spColor.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spType.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }

    public void setOptions(View view) {

//        Toast.makeText(this, "Size is " + spSize.getSelectedItem().toString() +
//                        "\nColor is " + spColor.getSelectedItem().toString() +
//                        "\nType is " + spType.getSelectedItem().toString(),
//                Toast.LENGTH_LONG).show();
        SharedPreferences pref = this.getSharedPreferences("MyPref",0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("size",spSize.getSelectedItemPosition());
        editor.putInt("color",spColor.getSelectedItemPosition());
        editor.putInt("type",spType.getSelectedItemPosition());


        EditText etSite = (EditText) findViewById(R.id.etSite);
        String site = etSite.getText()==null?null:etSite.getText().toString();
        editor.putString("site", site);
        editor.commit();

        finish();
    }

}
