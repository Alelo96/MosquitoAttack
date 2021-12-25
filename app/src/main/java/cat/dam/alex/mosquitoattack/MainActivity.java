package cat.dam.alex.mosquitoattack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //MEJORAS: hacer que cada vez que se mata a un mosquito se gane 1s

    //todo: mosquito speed no se lee bien en el countdown, no se pueden crear mas mosquitos a la vez

    public final int DEFAUL_TIME = 50;
    public int time = DEFAUL_TIME;
    public int score = 0;
    public int mosquitoTimer = 6;
    public int mosquitoCounter = 0;
    public int mosquitoSpeed = 1500;
    public Random r = new Random();

    LinearLayout ll_content;

    ArrayList <ImageView> mosquitoes = new ArrayList<>();

    AnimationDrawable mosquito_anim_list;
    AnimationDrawable smashed_anim_list;

    TextView tv_time;
    TextView tv_score;

    Button btn_start;

    ImageView iv_mosquito;

    //Temporitzador de temps
    CountDownTimer countDown = new CountDownTimer(time*1000, 1000) {
        public void onTick(long millisUntilFinished) {
            tv_time.setText("Time: " + millisUntilFinished / 1000);
        }

        public void onFinish() {
            tv_time.setText("Time: "+0);
            lostGame();
        }
    };

    //Temporitzador de moviment del mosquit
    CountDownTimer mosquitoMovement =  new CountDownTimer(500000000, mosquitoSpeed) {
        public void onTick(long millisUntilFinished) {

            for(ImageView m : mosquitoes){
                m.setX(r.nextInt(1000));
                m.setY(r.nextInt(1000));
            }
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

            }
        });
    }


    public void randomMosquito(){
        //Hacer aparecer mosquito random
    }

    public void initMosquito(){

        mosquitoes.add((ImageView) findViewById(R.id.iv_mosquito));
        System.out.println(mosquitoes.size());
        createOnClickListener( mosquitoes.get(mosquitoes.size()-1));

        mosquitoes.get(mosquitoes.size()-1).setBackgroundResource(R.drawable.mosquito_anim_list);
        mosquitoes.get(mosquitoes.size()-1).setVisibility(View.VISIBLE);

        mosquitoes.get(mosquitoes.size()-1).setX(r.nextInt(1000));
        mosquitoes.get(mosquitoes.size()-1).setY(r.nextInt(1000));

        if(score % 5 == 0){
            mosquitoSpeed = mosquitoSpeed-500;
        }
        System.out.println(mosquitoSpeed);
        initMosquitoAnim();
        initMosquitoMovement();
    }

    public void initMosquitoAnim(){
        mosquito_anim_list = (AnimationDrawable) mosquitoes.get(mosquitoes.size()-1).getBackground();
        mosquito_anim_list.start();
    }

    public void initMosquitoMovement(){
        mosquitoMovement.start();
    }

    public void smashedMosquito(ImageView mosquito){
        //todo: disable onClick
        score++;
        tv_score.setText("Score: " + score);
        new CountDownTimer(800, 400) {
            public void onTick(long millisUntilFinished) {
                time++;

                //Carreguem l'animació de la sang
                mosquito.setBackgroundResource(R.drawable.smashed_anim_list);
                smashed_anim_list = (AnimationDrawable) mosquito.getBackground();
                smashed_anim_list.start();

            }

            public void onFinish() {
                mosquito.setVisibility(View.GONE);
                initMosquito();

            }
        }.start();
    }


    public void createOnClickListener(ImageView m){
        m.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                smashedMosquito(m); //Començem l'animació del mosquit xafat
                mosquitoMovement.cancel(); //I parem el moviment d'aquest
            }
        });
    }
    public void lostGame(){
        time = DEFAUL_TIME;
        lostGameDialog();
        score = 0;
        tv_score.setText("Score: "+score);
        System.out.println("Has perdut");
        for (ImageView m:mosquitoes) {
            m.setVisibility(View.GONE);
        }
        btn_start.setVisibility(View.VISIBLE);
    }

    public void lostGameDialog(){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("HAS PERDUT!!");
        dialogo1.setMessage("La teva puntuació ha sigut de: "+score);
        dialogo1.setCancelable(false); //Fem que no es pugui sortir presionant fora del cuadre

        //En cas de presionar el boto positiu
        dialogo1.setPositiveButton("Sortir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                finish();
            }
        });

        //En cas de presionar del boto negatiu
        dialogo1.setNegativeButton("Reiniciar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                closeOptionsMenu(); //Tancament del cuadre d'opcions
            }
        });
        dialogo1.show();
    }
}