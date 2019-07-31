package com.aizenangel.myawesomequizz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.aizenangel.myawesomequizz.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MilicaRodjendan.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER"+")";

       db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
       fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
       db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
       onCreate(db);
    }

    private void fillQuestionsTable(){
        Question q = new Question("Sta Milica najviše voli da radi na fakultetu?",
                "Ometa Kaću i Emu dok prate predavanja",
                "Sa Kaćom i Emom ometa predavanja",
                "Gleda u telefon",
                "Ne zna ni ona sama....",
                2);
        addQuestion(q);

        q = new Question("Ovca kaže 'BEE', krava kaže 'MUU', a Milica kaže:",
                "2^10 = 1024 ^-^",
                "Španci su najlepši ljudi na svetu...",
                "Pika-Pika!",
                "E, znaš šta sam shvatila?",
                4);
        addQuestion(q);

        q = new Question("Koliko autobuskih linija prolazi kroz Braće Jerković?",
                "5",
                "6",
                "7",
                "8",
                4);
        addQuestion(q);


        q = new Question("Milica je počela da trenira, da bi:",
                "pločice u kuhinji žamenila svojim",
                "smršala",
                "otvorila yt kanal",
                "promovisala zdrav život posle 3min šetanja na traci",
                4);
        addQuestion(q);


        q = new Question("Milica ima:",
                "5 ispita za septembar",
                "2 leve noge",
                "mladjeg brata",
                "metar i žilet",
                2);
        addQuestion(q);

        q = new Question("Po kome je selo Braće Jerković dobilo ime?",
                "po dva heroja koji su poginuli braneći ga",
                "po dva čoveka koji su izgradili selo",
                "po dva heroja iz WW2",
                "po Titovim nećacima",
                3);
        addQuestion(q);

        q = new Question("Jedan od Miličinih specijalnih talenata je",
                "lepo kuvanje",
                "zaboravljanje",
                "pljuvanje u daljinu",
                "čitanje ljudi",
                2);
        addQuestion(q);

        q = new Question("Sa koliko sela se graniči Braće Jerković",
                "3",
                "4",
                "5",
                "6",
                1);
        addQuestion(q);

        q = new Question("Miličina omiljena vežba u teretani je:",
                "gledanja zgodnih likova",
                "čučnjevi",
                "širenje nogu",
                "sklekovi",
                3);
        addQuestion(q);

        q = new Question("Najbolje rangirana misterija na svetu, prema IMDB-u je:",
                "Mulholland Dr.",
                "Shutter Island",
                "Zodiac",
                "Dora istražuje",
                1);
        addQuestion(q);

        q = new Question("Šta radi čovek kad je prehladjen?",
                "pije čajeve",
                "KIAA",
                "kuka",
                "plače",
                2);
        addQuestion(q);

        q = new Question("Španija je poznata po: ",
                "učešću u oba sveCka rada",
                "maslinovom ulju",
                "plažama",
                "satovima",
                2);
        addQuestion(q);


    }

    private void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());

        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do{
              Question question = new Question();
              question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
              question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
              question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
              question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
              question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
              question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
              questionList.add(question);
            }while(c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
