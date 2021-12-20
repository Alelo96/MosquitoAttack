package cat.dam.alex.mosquitoattack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //MEJORAS: hacer que cada vez que se mata a un mosquito se gane 1s

    //todo: que se pueda hacer click en la animacion y hacer posicion Y del mosquito aleatoria

    public final int DEFAUL_TIME = 30;
    public int time = DEFAUL_TIME;
    public int score = 0;
    public int mosquitoTimer = 6;
    public int mosquitoCounter = 0;
    public Random r = new Random();

    LinearLayout ll_content;

    AnimationDrawable mosquito_anim_list;
    AnimationDrawable smashed_anim_list;

    TextView tv_time;
    TextView tv_score;

    Button btn_start;

    ImageView iv_mosquito;


    CountDownTimer countDown = new CountDownTimer(30000, 1000) {

        public void onTick(long millisUntilFinished) {
            tv_time.setText("Time: " + millisUntilFinished / 1000);
        }

        public void onFinish() {
            tv_time.setText("Time: "+0);
            lostGame();
        }
    };
    CountDownTimer mosquitoMovement =  new CountDownTimer(50000000, 700) {

        public void onTick(long millisUntilFinished) {
            iv_mosquito.setX(r.nextInt(1000));
            iv_mosquito.setY(r.nextInt(1000));
        }

        public void onFinish() {
            tv_time.setText("Time: "+0);
            lostGame();

        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_score = (TextView) findViewById(R.id.tv_score);

        btn_start = (Button) findViewById(R.id.btn_start);



        tv_time.setText("Time: " + time);
        tv_score.setText("Score: " + score);

        mosquito_anim_list = new AnimationDrawable();
        smashed_anim_list = new AnimationDrawable();

        btn_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                countDown.start();//Inici de comptador
                btn_start.setVisibility(View.GONE); //Treiem el boto start
                initMosquito(); //Iniciem un mosquit

                iv_mosquito.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){
                        smashedMosquito(); //Començem l'animació del mosquit xafat
                        mosquitoMovement.cancel(); //I parem el moviment d'aquest
                    }
                });
            }
        });
    }


    public void randomMosquito(){
        //Hacer aparecer mosquito random
    }

    public void initMosquito(){
        iv_mosquito = (ImageView) findViewById(R.id.iv_mosquito);
        iv_mosquito.setBackgroundResource(R.drawable.mosquito_anim_list);
        iv_mosquito.setVisibility(View.VISIBLE);

        iv_mosquito.setX(r.nextInt(1000));
        iv_mosquito.setY(r.nextInt(1000));

        initMosquitoAnim();
        initMosquitoMovement();
    }

    public void initMosquitoAnim(){
        mosquito_anim_list = (AnimationDrawable) iv_mosquito.getBackground();
        mosquito_anim_list.start();
    }

    public void initMosquitoMovement(){
        mosquitoMovement.start();
    }

    public void smashedMosquito(){
        //todo: disable onClick
        score++;
        tv_score.setText("Score: " + score);
        new CountDownTimer(800, 400) {
            public void onTick(long millisUntilFinished) {
                time++;

                //Carreguem l'animació de la sang
                iv_mosquito.setBackgroundResource(R.drawable.smashed_anim_list);
                smashed_anim_list = (AnimationDrawable) iv_mosquito.getBackground();
                smashed_anim_list.start();

            }

            public void onFinish() {
                iv_mosquito.setVisibility(View.GONE);
//                btn_start.setVisibility(View.VISIBLE);
                initMosquito();

            }
        }.start();
    }


    public void lostGame(){
        time = DEFAUL_TIME;
        score = 0;
        tv_score.setText("Score: "+score);
        System.out.println("Has perdut");
        iv_mosquito.setVisibility(View.GONE);
        btn_start.setVisibility(View.VISIBLE);
    }
}