package com.elhjuojy.note_app.activites;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.elhjuojy.note_app.R;
import com.elhjuojy.note_app.adapters.NotesAdapter;
import com.elhjuojy.note_app.database.NotesDatabase;
import com.elhjuojy.note_app.entities.Notes;
import com.elhjuojy.note_app.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesListener {
    public static final int REQUEST_CODE_ADD_NOTE =1;
    public static  final int REQUEST_CODE_UPDATE_NOTE =2;
    public static  final int REQUEST_CODE_SHOW_NOTES =3;

    private RecyclerView notesRecyclerView;
    private  List<Notes> notelist;
    private NotesAdapter notesAdapter;


    private  int noteClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);

        imageAddNoteMain.setOnClickListener((v)->
                {
                    startActivityForResult(
                            new Intent(getApplicationContext(),CreateNoteActivity.class),
                            REQUEST_CODE_ADD_NOTE
                    );
                }

        );













//        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
//
//        imageAddNoteMain.setOnClickListener(c ->{
//            Intent myIntent = new Intent(MainActivity.this, CreateNoteActivity.class);
//            startActivity(myIntent);
//        });


        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        );




        notelist = new ArrayList<>();
        notesAdapter =  new NotesAdapter(notelist,this);
        notesRecyclerView.setAdapter(notesAdapter);

        getNotes(REQUEST_CODE_SHOW_NOTES,false);


        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                       notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(notelist.size()!=0){
                    notesAdapter.searchNotes(s.toString());
                }

            }
        });



    }

    @Override
    public void onNoteClicked(Notes note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(),CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate",true);
        intent.putExtra("note",note);
        startActivityForResult(intent,REQUEST_CODE_UPDATE_NOTE);

    }

    private void getNotes(final int requestCode,final boolean isNoteDeleted){

                Log.d("requestCode", String.valueOf(requestCode));

//        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void,Void, List<Notes>>{


            @Override
            protected List<Notes> doInBackground(Void... voids) {
                return NotesDatabase.getDatabse(getApplicationContext()).noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Notes> notes) {
                super.onPostExecute(notes);
                
                if(requestCode==REQUEST_CODE_SHOW_NOTES){
                    notelist.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                }else if (requestCode==REQUEST_CODE_ADD_NOTE) {
                    notelist.add(0,notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);


                }
                else if (requestCode==REQUEST_CODE_UPDATE_NOTE){

                    notelist.remove(noteClickedPosition);
                    notelist.add(noteClickedPosition,notes.get(noteClickedPosition));
                    notesAdapter.notifyItemChanged(noteClickedPosition);

                    if(isNoteDeleted){
                        notesAdapter.notifyItemRemoved(noteClickedPosition);
                    }else{
                        notelist.add(noteClickedPosition,notes.get(noteClickedPosition));
                        notesAdapter.notifyItemChanged(noteClickedPosition
                        );

                    }

                }

            }

        }
        new GetNotesTask().execute();

    }


    //we he back from the back activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("req code ",String.valueOf(requestCode));
        if(requestCode== REQUEST_CODE_ADD_NOTE && resultCode==RESULT_OK){
            getNotes(REQUEST_CODE_ADD_NOTE,false);
        }else if(requestCode==REQUEST_CODE_UPDATE_NOTE && requestCode == RESULT_OK){
            if(data != null){
                getNotes(REQUEST_CODE_UPDATE_NOTE,data.getBooleanExtra("isNoteDeleted",false));
            }
        }

    }
}