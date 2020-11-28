package todo.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.simpletodo.R;

public class EditActivity extends AppCompatActivity {

    EditText editItem;
    Button buttonSave;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editItem= findViewById(R.id.editItem);
        buttonSave=findViewById(R.id.buttonSave);

        getSupportActionBar().setTitle("Edit Task");

        editItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create intent to go back to the main and also update
                Intent intent = new Intent();
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, editItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                setResult(RESULT_OK, intent);

                finish();
            }
        });

    }
}