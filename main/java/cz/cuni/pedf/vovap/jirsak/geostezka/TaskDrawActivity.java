package cz.cuni.pedf.vovap.jirsak.geostezka;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cz.cuni.pedf.vovap.jirsak.geostezka.tasks.DrawTask;
import cz.cuni.pedf.vovap.jirsak.geostezka.utils.BaseTaskActivity;
import cz.cuni.pedf.vovap.jirsak.geostezka.utils.Config;
import cz.cuni.pedf.vovap.jirsak.geostezka.utils.DrawTaskCanvas;
import cz.cuni.pedf.vovap.jirsak.geostezka.utils.InitDB;

public class TaskDrawActivity extends BaseTaskActivity {
    private static final String LOG_TAG = "GEO TaskDrawActivity";
    public DrawTask dt;
    InitDB db;
    boolean finished;
    DrawTaskCanvas canvas;
    public ImageView back;

	//ImageView bckImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //nacti spravny task podle intentu
        Intent mIntent = getIntent();
        int predaneID = mIntent.getIntExtra("id", 7);
        dt = (DrawTask) Config.vratUlohuPodleID(predaneID);
        super.init(dt.getNazev(), dt.getZadani());
		setContentView(dt.getLayout());
		RelativeLayout bck = (RelativeLayout) findViewById(R.id.dtBckImage);
		bck.setBackgroundResource(dt.getBckFinal());
        back = (ImageView)findViewById(R.id.confirmTask);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskDrawActivity.this,DashboardActivity.class));
            }
        });
		canvas = (DrawTaskCanvas) findViewById(R.id.dtCanvas);
		canvas.setBtmResourceId(dt.getBckStart());
        canvas.setCtx(this);

		db = new InitDB(this);
        db.open();
        if (db.vratStavUlohy(dt.getId()) == Config.TASK_STATUS_NOT_VISITED) {
			db.odemkniUlohu(dt.getId());
			UkazZadani(dt.getNazev(), dt.getZadani());
		}else if(db.vratStavUlohy(dt.getId()) == Config.TASK_STATUS_DONE) {
            finished = true;
            //canvas.setVisibility(View.INVISIBLE);
            canvas.finishTask();
        }
        db.close();
    }

    public boolean isFinished(){return finished;}

    @Override
    public void runFromResultDialog(boolean result, boolean closeTask) {
        if(result) {
            /// bylo pouze zobrazeni spravne odpovedi
            if(closeTask) {
                startActivity(new Intent(TaskDrawActivity.this, DashboardActivity.class));
                finish();
            }
        }else {
            Log.d(LOG_TAG, "FAULT RESULT do nothing");
        }

    }
}
