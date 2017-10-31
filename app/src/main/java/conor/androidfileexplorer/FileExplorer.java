package conor.androidfileexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileExplorer extends AppCompatActivity {

    private static final String ROOT_DIR = "/sdcard/";
    private FloatingActionButton fab;
    private File currentDirectory;
    private FileArrayAdapter adapter;
    private ListView fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.fileList = (ListView) findViewById(R.id.file_list);

        this.fab = (FloatingActionButton) findViewById(R.id.fab);

        currentDirectory = new File(ROOT_DIR);
        this.fill(currentDirectory);

        this.setListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_explorer, menu);
        return true;
    }

    /**
     * Helper for OnCreate(). Sets listeners for various items
     */
    private void setListeners() {
        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3)
            {
                Toast.makeText(FileExplorer.this, "Item Clicked at pos = " + position, Toast.LENGTH_SHORT).show();

                Item item = adapter.getItem(position);
                //if the item selected is a folder
                if(item.getImage().equalsIgnoreCase("directory_icon") || item.getImage().equalsIgnoreCase("directory_up")){
                    Toast.makeText(FileExplorer.this, "Folder Clicked: "+ currentDirectory, Toast.LENGTH_SHORT).show();
                    currentDirectory = new File(item.getPath());
                    fill(currentDirectory);
                }
                else    //the item selected was a file
                {
                    Toast.makeText(FileExplorer.this, "File Clicked: "+ currentDirectory, Toast.LENGTH_SHORT).show();
                    onFileClicked(item);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    /**
     *
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(this, " search selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_by_name:
                Toast.makeText(this, " sort by folder selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_by_date_modified:
                Toast.makeText(this, " sort by date modified selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_by_type:
                Toast.makeText(this, " sort by type selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_by_size:
                Toast.makeText(this, " sort by size selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.new_folder:
                Toast.makeText(this, " new folder selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.new_file:
                Toast.makeText(this, " new file selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //--------------------------------------------


    private void fill(File f)
    {
        File[] dirs = f.listFiles();
        this.setTitle("Current Dir: " + f.getName());
        List<Item> directorys = new ArrayList<Item>();
        List<Item> files = new ArrayList<Item>();
        try{
            for(File file : dirs)
            {
                Date lastModDate = new Date(file.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);

                //If the file is a directory found out how many items are in it
                if(file.isDirectory()) {
                    File[] fileBuffer = file.listFiles();
                    int buf = 0;
                    if(fileBuffer != null){
                        buf = fileBuffer.length;
                    }
                    else {
                        buf = 0;
                    }

                    String num_item = String.valueOf(buf);

                    if(buf == 0) {
                        num_item = num_item + " item";
                    }
                    else  {
                        num_item = num_item + " items";
                    }

                    String formated = lastModDate.toString();
                    //add the directory to the directory list
                    directorys.add(new Item(file.getName(), num_item, formated, file.getAbsolutePath(), "directory_icon"));
                }
                else    //the file is a file so
                {
                    //add the file to the file list
                    files.add(new Item(file.getName(),file.length() + " Bytes", date_modify, file.getAbsolutePath(),"file_icon"));
                }
            }
        }catch(Exception e)
        {

        }

        //sort all the directorys/files alphabatically
        Collections.sort(directorys);
        Collections.sort(files);
        directorys.addAll(files);

        //if the current directory is not the root dir
        if(!f.getName().equalsIgnoreCase("sdcard"))
            directorys.add(0, new Item("..", "Parent Directory", "", f.getParent(), "directory_up"));

        adapter = new FileArrayAdapter(FileExplorer.this, R.layout.file_item, directorys);
        this.fileList.setAdapter(adapter);
    }
    

    private void onFileClicked(Item item)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("GetPath", currentDirectory.toString());
        intent.putExtra("GetFileName", item.getName());
        setResult(RESULT_OK, intent);
        //finish();
    }
}
