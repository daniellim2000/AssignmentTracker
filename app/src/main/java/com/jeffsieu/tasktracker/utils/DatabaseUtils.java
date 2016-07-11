package com.jeffsieu.tasktracker.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeffsieu.tasktracker.R;
import com.jeffsieu.tasktracker.activity.MainActivity;
import com.jeffsieu.tasktracker.Task;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Jeff on 4/7/2016.
 */
public class DatabaseUtils {
	private static DatabaseReference sDatabase = FirebaseDatabase.getInstance().getReference().child("test");

	public static void setUser(String name) {
		sDatabase = FirebaseDatabase.getInstance().getReference().child(name);
	}

	public static void handleSignInResult(GoogleSignInResult result, Activity activity) {
		Log.d("Sign in", "handleSignInResult:" + result.isSuccess()+result.getSignInAccount());
		if (result.isSuccess()) {
			GoogleSignInAccount account = result.getSignInAccount();
			firebaseAuthWithGoogle(account, activity);
		}
	}

	private static void firebaseAuthWithGoogle(GoogleSignInAccount acct, final Activity activity) {
		Log.d("Log", "firebaseAuthWithGoogle:" + acct.getId());

		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		MainActivity.sReference.mAuth.signInWithCredential(credential)
				.addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {

					@Override
					public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
						Log.d("Log", "signInWithCredential:onComplete:" + task.isSuccessful());

						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						if (!task.isSuccessful()) {
							Log.w("Log", "signInWithCredential", task.getException());
							Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
						}
						// ...
					}
					// ...

				});

		DatabaseUtils.setUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
		DatabaseUtils.importFromDatabase(activity.findViewById(R.id.activity_main_fragment));
	}

	public static void importFromDatabase(final View rootView) {
		final Snackbar snackbar = Snackbar.make(rootView, "Syncing tasks from database", Snackbar.LENGTH_INDEFINITE);
		snackbar.show();
		DatabaseReference ref = sDatabase.child("tasks");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				int count = 0;
				for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()) {
					String name = (String) taskSnapshot.child("name").getValue();
					Calendar date = taskSnapshot.child("date").getValue(GregorianCalendar.class);
					Task task = new Task();
					task.setDate(date);
					task.setName(name);
					MainActivity.tasks.add(task);
					MainActivity.taskKeys.add(taskSnapshot.getKey());
					count++;
				}
				MainActivity.sReference.getDashboardFragment().updateDashboard();
				snackbar.dismiss();
				if (count < 1)
					Snackbar.make(rootView, "No tasks found", Snackbar.LENGTH_SHORT).show();
			}

			@Override
			public void onCancelled(DatabaseError firebaseError) {
			}
		});
		ref.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				System.out.println("le child added");
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				System.out.println("le child changed");
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {

				System.out.println("le child removed");
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	public static void updateDatabase() {
		sDatabase.child("tasks").setValue(MainActivity.tasks);
	}

	public static void pushTask(Task task) {
		DatabaseReference reference = sDatabase.child("tasks").push();
		reference.setValue(task);
		MainActivity.taskKeys.add(reference.getKey());
	}

	public static void removeTask(String key) {
		sDatabase.child("tasks").child(key).removeValue();
	}
}