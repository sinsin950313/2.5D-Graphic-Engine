package seaweed.aeproject.AlphaEngine;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import seaweed.aeproject.R;

import static seaweed.aeproject.AlphaEngine.EngineCorePackage.EngineActivity.engineActivity;

public class Main {

    private View view;
    private TextView textView;

    private boolean touch;
    private boolean dialog_active;

    public Main()
    {
        view = engineActivity.getLayoutInflater().inflate(R.layout.main, null, false);
        textView = view.findViewById(R.id.dialog_main);
        textView.setBackgroundColor(Color.BLACK);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(22);
        textView.setVisibility(View.INVISIBLE);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touch = true;
            }
        });
    }

    public void Set_Text(String text)
    {
        textView.setText(text);
    }
    public View Return_View()
    {
        return view;
    }
    public void setTouch(boolean touch)
    {
        this.touch = touch;
    }
    public boolean isTouch() { return touch; }
    public boolean isDialogActive() {
        return dialog_active;
    }
    public void setDialogActive(boolean active) {
        this.dialog_active = active;
        if(active) {
            textView.setVisibility(View.VISIBLE);
            textView.bringToFront();
        }
        else {
            textView.setVisibility(View.INVISIBLE);
        }
    }
}
