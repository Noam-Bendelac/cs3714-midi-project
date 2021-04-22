package com.example.midiproject.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.midiproject.R;
import com.example.midiproject.model.MidiConnection;
import com.example.midiproject.model.Note;

/**
 * A fragment that holds the buttons for the drumpad activity and the edit drumpad activity.
 */
public class drumpadFragment extends Fragment {
    MidiConnection midiConnection;
    Note note11;
    Note note21;
    Note note31;
    Note note41;
    Note note12;
    Note note22;
    Note note32;
    Note note42;
    Note note13;
    Note note23;
    Note note33;
    Note note43;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        midiConnection = MidiConnection.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_drumpad, container, false);

        // Make the buttons correspond to notes
        Button temp = root.findViewById(R.id.note11);
        note11 = new Note(temp, (String) temp.getText());
        temp = root.findViewById(R.id.note21);
        note21 = new Note(temp, (String) temp.getText());
        temp = root.findViewById(R.id.note31);
        note31 = new Note(temp, (String) temp.getText());
        temp = root.findViewById(R.id.note41);
        note41 = new Note(temp, (String) temp.getText());
        temp = root.findViewById(R.id.note12);
        note12 = new Note(temp, (String) temp.getText());
        temp = root.findViewById(R.id.note22);
        note22 = new Note(temp, (String) temp.getText());
        temp = root.findViewById(R.id.note32);
        note32 = new Note(temp, (String) temp.getText());
        temp = root.findViewById(R.id.note42);
        note42 = new Note(temp, (String) temp.getText());
        temp = root.findViewById(R.id.note13);
        note13 = new Note(temp, (String) temp.getText());
        temp = root.findViewById(R.id.note23);
        note23 = new Note(temp, (String) temp.getText());
        temp = root.findViewById(R.id.note33);
        note33 = new Note(temp, (String) temp.getText());
        temp = root.findViewById(R.id.note43);
        note43 = new Note(temp, (String) temp.getText());

        // Set listeners for buttons
        setListener(note11);
        setListener(note21);
        setListener(note31);
        setListener(note41);
        setListener(note12);
        setListener(note22);
        setListener(note32);
        setListener(note42);
        setListener(note13);
        setListener(note23);
        setListener(note33);
        setListener(note43);


        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener(Note note) {
        Button button = note.getButton();
        button.setOnTouchListener((v, event) -> {
            int actionMasked = event.getActionMasked();
            switch (actionMasked) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    midiConnection.sendNoteOn(60, 127);
                    button.setPressed(true);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    midiConnection.sendNoteOff(60);
                    button.setPressed(false);
                    break;
            }
            return true;
        });
    }
}