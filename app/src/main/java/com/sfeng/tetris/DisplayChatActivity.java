package com.sfeng.tetris;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayChatActivity extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_chat);

        Intent intent = getIntent();
        String user = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView) findViewById(R.id.chat);
        textView.setMovementMethod(new ScrollingMovementMethod());
        String chat = textView.getText().toString();
        textView.setText(user + " has joined the chat.\n" + chat);
        username = user;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_chat, menu);
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

    public void sendMessage(View view) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        EditText editText = (EditText) findViewById(R.id.message);
        String message = editText.getText().toString().trim();
        if (!TextUtils.isEmpty(message)) {
            TextView textView = (TextView) findViewById(R.id.chat);
            String chat = textView.getText().toString();
            // String update_text = chat + "\n" + "<font color=#ff8c00>" + username +  ": " + message + "</font>\n";
            // textView.setText(Html.fromHtml(update_text));
            textView.setText(chat + username + ": " + message + "\n");
            editText.setText("");
        }

    }
}
