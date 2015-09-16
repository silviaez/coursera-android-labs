package course.labs.modernartui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class ModernActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modern);

        SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
        final LinearLayout frame = (LinearLayout) findViewById(R.id.linearLayout);
        final int columns = frame.getChildCount();

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                for(int col = 0; col < columns; col++) {
                    LinearLayout columnLayout = (LinearLayout) frame.getChildAt(col);
                    int rows = columnLayout.getChildCount();
                    for (int row = 0; row < rows; row++) {

                        LinearLayout square = (LinearLayout) columnLayout.getChildAt(row);
                        int originalColor = Color.parseColor((String) square.getTag());
                        int finalColor = getFinalColor(progress, originalColor);

                        updateBackgroundColor(square, finalColor);

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

    }

    private void updateBackgroundColor(LinearLayout square, int finalColor) {
        if (square.getBackground() instanceof LayerDrawable) {
            LayerDrawable squareDrawable = (LayerDrawable) square.getBackground();
            GradientDrawable shape = (GradientDrawable) squareDrawable.findDrawableByLayerId(R.id.rectangle_shape);
            shape.setColor(finalColor);
        }

        if(square.getBackground() instanceof ColorDrawable) {
            square.setBackgroundColor(finalColor);
        }
    }

    private int getFinalColor(int progress, int originalColor) {
        int finalColor = originalColor;

        int invertedColor = ( 0x00FFFFFF - ( originalColor | 0xFF000000 ) ) |
                ( originalColor & 0xFF000000 );

        if ( getResources().getColor( R.color.white ) != originalColor &&
                getResources().getColor( R.color.gray ) != originalColor ) {

            int origR = ( originalColor >> 16 ) & 0x000000FF;
            int origG = ( originalColor >> 8 ) & 0x000000FF;
            int origB = originalColor & 0x000000FF;

            int invR = ( invertedColor >> 16 ) & 0x000000FF;
            int invG = ( invertedColor >> 8 ) & 0x000000FF;
            int invB = invertedColor & 0x000000FF;

            finalColor =  Color.rgb(
                    (int) (origR + (invR - origR) * (progress / 100f)),
                    (int) (origG + (invG - origG) * (progress / 100f)),
                    (int) (origB + (invB - origB) * (progress / 100f))) ;
        }
        return finalColor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modern, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_moreinfo) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.visit_moma_title)
                    .setPositiveButton(R.string.visit_moma_ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.moma.org"));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.visit_moma_cancel, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .create();

            dialog.show();
            TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.CENTER);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
