package cz.cuni.pedf.vovap.jirsak.geostezka;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cz.cuni.pedf.vovap.jirsak.geostezka.tasks.DragDropTask;
import cz.cuni.pedf.vovap.jirsak.geostezka.utils.BaseTaskActivity;
import cz.cuni.pedf.vovap.jirsak.geostezka.utils.Config;
import cz.cuni.pedf.vovap.jirsak.geostezka.utils.InitDB;

public class TaskDragDropActivity extends BaseTaskActivity {
    DragDropTask dd;
    LinearLayout llDD;
    RelativeLayout rlDD;
    int[] obrazky;
    int[] obrazkyCile;
    int[] obrazkyCileAfter;
    ImageView[] ivs;
    ImageView[] tvs;
    TextView resultInfo;
    Context mContext;
    Point[] pObjs;
    Point[] pTrgs;
    InitDB db = new InitDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_drag_drop);

        //nacti spravny task podle intentu
        Intent mIntent = getIntent();
        int predaneID = mIntent.getIntExtra("id", 0);
        dd = (DragDropTask) Config.vratUlohuPodleID(predaneID);
        db.open();
        if (db.vratStavUlohy(dd.getId())==0)
            db.odemkniUlohu(dd.getId());
        db.close();
        UkazZadani(dd.getNazev(), dd.getZadani());
        mContext = getApplicationContext();
        obrazky = dd.getBankaObrazku();
        obrazkyCile = dd.getBankaObrCile();
        obrazkyCileAfter = dd.getBankaObrCile2();
        pObjs = dd.getSouradniceObj();
        pTrgs = dd.getSouradniceCil();
        rlDD = (RelativeLayout) findViewById(R.id.rlDD);
        rlDD.setBackground(getResources().getDrawable(obrazky[0]));
        llDD = (LinearLayout) findViewById(R.id.llDD);
        resultInfo = (TextView) findViewById(R.id.tvDDResultInfo);
        Resources r = getResources();
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());

        ivs = new ImageView[obrazky.length];
        for (int i = 1; i < obrazky.length;i++)
        {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) width, (int) height);
            //layoutParams.topMargin = pObjs[i-1].x;
            //layoutParams.leftMargin = pObjs[i-1].y;
            ivs[i] = new ImageView(this);
            ivs[i].setImageResource(obrazky[i]);
            ivs[i].setId(i+100);
            /*if (i>1){
                layoutParams.addRule(RelativeLayout.RIGHT_OF, 99+i);
            } else {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }*/
            ivs[i].setTag(String.valueOf(obrazky[i]));
            ivs[i].setLayoutParams(layoutParams);
            llDD.addView(ivs[i]);
            ivs[i].setOnTouchListener(new MyTouchListener());

        }
        tvs = new ImageView[obrazkyCile.length];
        for (int i = 0; i<tvs.length;i++)
        {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) width, (int) height);
            layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pTrgs[i].x, r.getDisplayMetrics());
            layoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pTrgs[i].y, r.getDisplayMetrics());
            tvs[i] = new ImageView(this);
            tvs[i].setImageResource(obrazkyCile[0]);
            //tvs[i].setBackground(getResources().getDrawable(obrazkyCile[0]));
            tvs[i].setId(i+1000);
            /*
            if (i>0){
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, 999+i);
            } else {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }*/
            tvs[i].setLayoutParams(layoutParams);
            tvs[i].setOnDragListener(new MyDragEventListener());
            rlDD.addView(tvs[i]);

        }

    }
    private static class MyTouchListener implements View.OnTouchListener{
            @Override
            public boolean onTouch(View view, MotionEvent event){
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    String viewTag = String.valueOf(view.getTag());

                    ClipData.Item item = new ClipData.Item(viewTag);
                    ClipData dragData = new ClipData(
                            viewTag,
                            new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},item
                    );
                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
                    // Starts the drag
                    view.startDrag(
                            dragData, //  the data to be drag
                            myShadow, // the drag shadow builder
                            null, // no need to use local data
                            0 // flags
                    );
                    //view.setVisibility(View.INVISIBLE);
                    return true;
                }

                return false;
            }
        }
    protected class MyDragEventListener implements View.OnDragListener{
        /*
            public abstract boolean onDrag (View v, DragEvent event)
                Called when a drag event is dispatched to a view. This allows listeners to get
                a chance to override base View behavior.
        */
        // This is the method that the system calls when it dispatches a drag event to the listener
        public boolean onDrag(View view, DragEvent event){
            // Define the variable to store the action type for the incoming event
            final int action = event.getAction();

            /*
                ACTION_DRAG_ENDED
                    Signals to a View that the drag and drop operation has concluded.
                ACTION_DRAG_ENTERED
                    Signals to a View that the drag point has entered the bounding box of the View.
                ACTION_DRAG_EXITED
                    Signals that the user has moved the drag shadow outside the bounding box of the View.
                ACTION_DRAG_LOCATION
                    Sent to a View after ACTION_DRAG_ENTERED if the drag shadow is still
                    within the View object's bounding box.
                ACTION_DRAG_STARTED
                    Signals the start of a drag and drop operation.
                ACTION_DROP
                    Signals to a View that the user has released the drag shadow, and the drag
                    point is within the bounding box of the View.
            */
            // Handles each of the expected events
            switch(action){
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine if this view can accept dragged data
                    if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                        // If the view view can accept dragged data
                        view.setBackgroundColor(Color.parseColor("#FFC4E4FF"));
                        // Return true to indicate that the view can accept the dragged data
                        return true;
                    }
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    // When dragged item entered the receiver view area
                    view.setBackgroundColor(Color.parseColor("#FFB7FFD6"));

                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    // Ignore the event
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    // When dragged object exit the receiver object
                    view.setBackgroundColor(Color.parseColor("#FFFFBCBC"));
                    // Return true to indicate the dragged object exited the receiver view
                    return true;
                case DragEvent.ACTION_DROP:
                    // Get the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String dragData = (String) item.getText();
                    //view.setVisibility(View.VISIBLE);
                    // Cast the receiver view as a TextView object
                    ImageView v = (ImageView) view;

                    // Change the TextView text color as dragged object background color
                    //v.setTextColor(Integer.parseInt(dragData));
                    Log.d("GEO: Tag of element b ", String.valueOf(v.getTag()));
                    v.setTag(Integer.parseInt(dragData));
                    Log.d("GEO: Tag of element a ", String.valueOf(v.getTag()));
                    //Log.d("GEO: ID of element ", String.valueOf(v.getId()));
                    v.setImageResource(Integer.parseInt(dragData));
                    //Log.d("GEO: ", String.valueOf(dragData));
                    // Return true to indicate the dragged object dop

                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    // Remove the background color from view
                    view.setBackgroundColor(Color.TRANSPARENT);
                    if(event.getResult()){
                        resultInfo.setText("Uspesne pretazeno");
                    }else {
                        resultInfo.setText("Neuspesne pretazeno");
                    }
                    // Return true to indicate the drag ended
                    return true;
                default:
                    Log.e("Drag and Drop example","Unknown action type received.");
                    break;
            }

            return false;
        }
    }
    }
