package com.example.gill.todolist1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNote extends Activity {

    EditText editNote;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        editNote=(EditText)findViewById(R.id.takenote);
        b1=(Button)findViewById(R.id.done);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the Entered  message
                String message=editNote.getText().toString();
                Intent intentMessage=new Intent();

                // put the message in Intent
                intentMessage.putExtra("MESSAGE", message);
                // Set The Result in Intent
                setResult(2, intentMessage);
                // finish The activity
                finish();
            }
        });
    }

}
