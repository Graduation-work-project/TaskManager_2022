package com.example.myapplication.Diary;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.AlarmUi.AlarmActivity;
import com.example.myapplication.Community_button;
import com.example.myapplication.Community_ui.SetUpActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import com.example.myapplication.ToDoList;
import com.example.myapplication.adapter.DiaryListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.myapplication.Diary.Diary_main.getImageFromByte;
import static com.example.myapplication.Diary.Diary_main.imageViewToByte;
import static com.example.myapplication.Diary.Diary_main.sqLiteHelper;

//그리드 뷰 및 버튼 클릭
public class DiaryList extends AppCompatActivity {

    EditText input;

    GridView gridView;
    ArrayList<com.example.myapplication.Diary.Diary> list;
    DiaryListAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_list_activity);

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setItemIconTintList(null);
        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.community);

        //perform itemselectedlistener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.todolist:
                        startActivity(new Intent(getApplicationContext()
                                , ToDoList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.community:

                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.alarm:
                        startActivity(new Intent(getApplicationContext()
                                , AlarmActivity.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.info:
                        startActivity(new Intent(getApplicationContext()
                                , SetUpActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new DiaryListAdapter(this, R.layout.diary_items, list);
        gridView.setAdapter(adapter);

        //플로팅 버튼
        FloatingActionButton btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryList.this, Diary_main.class);
                startActivity(intent);
            }
        });

        //플로팅 버튼
        FloatingActionButton fb_back1 = findViewById(R.id.fb_back1);
        fb_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryList.this, Community_button.class);
                startActivity(intent);
            }
        });

        sqLiteHelper = new com.example.myapplication.Diary.SQLiteHelper(this, "DiaryDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS DIARY(Id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, content VARCHAR, image BLOB)");

        // get all data from sqlite
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM DIARY");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            byte[] image = cursor.getBlob(3);

            list.add(new com.example.myapplication.Diary.Diary(title, content, image, id));
        }
        adapter.notifyDataSetChanged();

        //그리드뷰 짧게 눌렀을 때 --> 조회
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setContentView(R.layout.activity_show);

                //데이터 가져오기
                com.example.myapplication.Diary.Diary show = list.get(position);

                //show_activity에 있는 것 선언
                TextView show_title = findViewById(R.id.show_title);
                TextView show_content = findViewById(R.id.show_content);
                ImageView show_image = findViewById(R.id.show_image);
                Button btn_close = findViewById(R.id.btn_close);
                TextView date = findViewById(R.id.date);

                //현재 시간 선언 및 가져오기
                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                date.setText(currentTime);

                //해당 인자에 데이터 가져오기
                show_title.setText(show.getTitle());
                show_content.setText(show.getContent());
                show_image.setImageBitmap(getImageFromByte(show.getImage()));

                //다시 리스트로 돌아가기
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DiaryList.this, DiaryList.class);
                        startActivity(intent);
                    }
                });
            }
        });


        //그리드뷰 길게 눌렀을 때-> 조회, 삭제
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"수정하기", "삭제하기"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(DiaryList.this);

                dialog.setTitle("옵션을 선택하세요");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            Cursor c = sqLiteHelper.getData("SELECT id FROM DIARY");
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }

                            //수정 화면
                            setContentView(R.layout.update_activity);

                            //데이터 전체 가져오기
                            com.example.myapplication.Diary.Diary update = list.get(position);

                            //layout 선언
                            ImageView imageViewDiary = (ImageView) findViewById(R.id.imageViewDiary);
                            final EditText edtTitle = (EditText) findViewById(R.id.edtTitle);
                            final EditText edtContent = (EditText) findViewById(R.id.edtContent);
                            Button btnUpdate = (Button) findViewById(R.id.btnUpdate);

                            //해당 인자에 데이터 넣기
                            edtTitle.setText(update.getTitle());
                            edtContent.setText(update.getContent());
                            imageViewDiary.setImageBitmap(getImageFromByte(update.getImage()));

                            //커서 맨끝으로 옮기기
                            edtTitle.setSelection(edtTitle.getText().length());
                            edtContent.setSelection(edtContent.getText().length());

                            //플로팅 버튼
                            Button btnUpdate_bck = findViewById(R.id.btnUpdate_bck);
                            btnUpdate_bck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(DiaryList.this, DiaryList.class);
                                    startActivity(intent);
                                }
                            });



                            //사진 클릭시 새로 선택하는 함수
                            imageViewDiary.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(DiaryList.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 888);

                                }
                            });

                            //수정하기 버튼 클릭시 동작하는 함수
                            btnUpdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        //추가해봄
                                        sqLiteHelper.updateData(edtTitle.getText().toString().trim(),
                                                edtContent.getText().toString().trim(),
                                                imageViewToByte(imageViewDiary),
                                                arrID.get(position));


                                        String title = edtTitle.getText().toString();
                                        String content = edtContent.getText().toString();
                                        byte[] image = imageViewToByte(imageViewDiary);



                                        //수정 완료 팝업
                                        Toast.makeText(getApplicationContext(), "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                        //다시 리스트로 돌아가기
                                        Intent intent = new Intent(DiaryList.this, DiaryList.class);

                                        startActivity(intent);
                                    } catch (Exception error) {
                                        Log.e("수정이 되지 않았습니다.", error.getMessage());
                                    }
                                    UpdateDiaryList();

                                }

                            });


                        } else {
                            // 삭제
                            Cursor c = sqLiteHelper.getData("SELECT id FROM DIARY");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }
    ImageView imageViewDiary;

    //삭제
    private void showDialogDelete(final int idDiary) {
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(DiaryList.this);

        dialogDelete.setTitle("주의");
        dialogDelete.setMessage("정말 삭제하시겠습니까?");
        dialogDelete.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    sqLiteHelper.deleteData(idDiary);
                    Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("삭제가 되지 않았습니다.", e.getMessage());
                }

                UpdateDiaryList();
            }
        });

        dialogDelete.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    //그리드 뷰 화면에서 없애기
    private void DeleteDiaryList() {
        // get all data from sqlite
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM DIARY");
        list.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            byte[] image = cursor.getBlob(3);

        }
        adapter.notifyDataSetChanged();
    }

    //그리드뷰 화면 수정 받아오기 --> 수정 / 위 코드에서 고쳐보기
    private void UpdateDiaryList() {
        // get all data from sqlite

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM DIARY");
        list.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            byte[] image = cursor.getBlob(3);

            list.add(new com.example.myapplication.Diary.Diary(title, content, image, id));
        }
        adapter.notifyDataSetChanged();
    }


    //사진 관련
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            } else {
                Toast.makeText(getApplicationContext(), "파일 접근에 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                ImageView imageViewDiary = (ImageView) findViewById(R.id.imageViewDiary);
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewDiary.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, Community_button.class); //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //인텐트 플래그 설정
        startActivity(intent);  //인텐트 이동
        finish();   //현재 액티비티 종료
    }

}