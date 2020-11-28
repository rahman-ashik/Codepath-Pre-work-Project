package todo.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simpletodo.R;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION="item_position";
    public static final int EDIT_TEXT_CODE=1;


    List<String> tasks;
    Button button_Add;
    EditText editText;
    RecyclerView ListofItems;
    ItemsAdapter itemsAdapter;

    

    public MainActivity() throws IOException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_Add= findViewById(R.id.button_Add);
        editText= findViewById(R.id.editText);
        ListofItems= findViewById(R.id.ListofItems);


        readItems();

        System.err.println("size is " + tasks.size());
      //  System.err.println(tasks.get(1));

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                tasks.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Task Removed!",Toast.LENGTH_SHORT).show();
                writeItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener;
        onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity","position: "+ position);
                //create the edit activity
                Intent i = new Intent (MainActivity.this, EditActivity.class);
                //pass the data
                i.putExtra(KEY_ITEM_TEXT, tasks.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //display it
                startActivityForResult(i,EDIT_TEXT_CODE);

            }
        };
        itemsAdapter= new ItemsAdapter(tasks, onLongClickListener, onClickListener);
        ListofItems.setAdapter(itemsAdapter);
        ListofItems.setLayoutManager(new LinearLayoutManager(this));


        button_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem= editText.getText().toString();
                tasks.add(newItem);
                itemsAdapter.notifyItemInserted(tasks.size()-1);
                editText.setText("");
                Toast.makeText(getApplicationContext(),"Task Added!",Toast.LENGTH_SHORT).show();
                writeItems();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            String itemText= data.getStringExtra(KEY_ITEM_TEXT);
            int position= data.getExtras().getInt(KEY_ITEM_POSITION);

            tasks.set(position, itemText);
            itemsAdapter.notifyItemChanged(position);
            writeItems();
            Toast.makeText(getApplicationContext(),"Task Updated!",Toast.LENGTH_SHORT).show();
        }
        else {
            Log.w("MainActivity","Update text error");
        }

    }

    private File getFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //read file
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            tasks = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            tasks = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}