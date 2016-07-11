package com.jeffsieu.tasktracker.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeffsieu.tasktracker.R;
import com.jeffsieu.tasktracker.Task;

/**
 * Created by Jeff on 26/6/2016.
 */
public class TaskActivity extends AppCompatActivity implements View.OnClickListener {
    private Task task;

    private TextView mNameView;
    private TextView mTimeView;
    private TextView mSubjectView;
    private ImageButton mTickView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        task = getIntent().getParcelableExtra("task");

        mNameView = (TextView) findViewById(R.id.item_task_name);
        mTimeView = (TextView) findViewById(R.id.item_task_time);
        mSubjectView = (TextView) findViewById(R.id.item_task_subject);
        mTickView = (ImageButton) findViewById(R.id.item_task_tick);

        mNameView.setText(task.getName());
        mTimeView.setText(task.getDateString());
        mSubjectView.setText("");

        mTickView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.equals(mTickView)) {
            finishAfterTransition();
        }
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
}
