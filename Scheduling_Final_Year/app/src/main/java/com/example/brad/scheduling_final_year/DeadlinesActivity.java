package com.example.brad.scheduling_final_year;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeadlinesActivity extends AppCompatActivity {


    DB database;

    private static final String TAG = "Data";

    private ListView DBlistView;
    private TextView EventName;
    private TextView EventDate;
    private TextView EventTime;
    private TextView EventDescription;
    private TextView EventType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadlines);
        DBlistView = (ListView) findViewById(R.id.DBlistView);
        database = new DB(this);
        fillContent();
        DBlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventName = (TextView) view.findViewById(R.id.Name);
                String name = EventName.getText().toString();

                EventDate = (TextView) view.findViewById(R.id.Date);
                String date = EventDate.getText().toString();

                EventTime = (TextView) view.findViewById(R.id.Time);
                String time = EventTime.getText().toString();

                EventType = (TextView) view.findViewById(R.id.EventType);
                String type = EventType.getText().toString();


                EventDescription = (TextView) view.findViewById(R.id.EventDescription);
                String description = EventDescription.getText().toString();

                Cursor c = database.getID(name, description, type, time, date);

                int ID = -1;
                while(c.moveToNext()){
                    ID = c.getInt(0);
                }

                Intent i = new Intent(DeadlinesActivity.this, ViewEventDetailedPopUp.class);
                Bundle b = new Bundle();

                b.putString("name", name);
                b.putString("date", date);
                b.putString("time", time);
                b.putString("type", type);
                b.putInt("id", ID);
                b.putString("description", description);
                i.putExtras(b);

                startActivity(i);
    }});}

    public void change(View view)
    {
        Intent intent = new Intent(DeadlinesActivity.this, addMeetingPopUp.class);
        startActivity(intent);
    }

    public ArrayList<Event> selectAll() {
        ArrayList<Event> list = new ArrayList<>();
        Cursor date2 = database.getDeadlines();

        while(date2.moveToNext()) {
            String name = date2.getString(1);
            String type = date2.getString(4);
            String desc = date2.getString(5);
            String date = date2.getString(2);
            String time = date2.getString(3);

            Event e = new Event(name, type, desc, date, time);

            list.add(e);
        }
        date2.close();
        return list;
    }

    public void fillContent() {
        ListView listview = findViewById(R.id.DBlistView);
        ArrayList<Event> list = selectAll();

        EventAdapter adapter = new EventAdapter(list, this);
        listview.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        fillContent();
    }

}
