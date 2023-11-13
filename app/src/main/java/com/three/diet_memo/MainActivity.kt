package com.three.diet_memo

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.GregorianCalendar

class MainActivity : AppCompatActivity() {
    val dataModelList = mutableListOf<DataModel>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 3/4. DB 저장 및 읽기를 위한 사전 준비
        val database = Firebase.database
        val myRef = database.getReference("workoutMemo")

        // 4. DB를 읽어와 ListView에 할당
        // 4-1. ListView에 할당할 Adapter 생성
        val listViewAdapter = ListViewAdapter(dataModelList)

        // 4-2. ListView 설정
        val listView = findViewById<ListView>(R.id.mainLv)
        listView.adapter = listViewAdapter

        // 4-3. DB에서 데이터 변경 시 호출되는 리스너 설정
        myRef.child(Firebase.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataModelList.clear()
                // 4-4. DB에서 가져온 스냅샷 순회 및 데이터를 가져와 데이터 모델 리스트에 추가
                for (dataModel in snapshot.children) {
                    dataModelList.add(dataModel.getValue(DataModel::class.java)!!)
                }
                // 4-5. Adapter에 데이터 변경을 알리고 리스트 뷰 업데이트
                listViewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 오류 처리
            }
        })

        // 1. 운동 메모 작성 버튼 클릭 시
        val writeBtn: ImageView = findViewById(R.id.writeBtn)
        writeBtn.setOnClickListener {
            // 1-1. 커스텀 다이얼로그 레이아웃 inflate
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

            // 1-2. 다이얼로그 생성
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("운동 메모")

            // 1-3. 다이얼로그 보이기
            val mAlertDialog = mBuilder.show()

            var dateText = ""

            // 2. 날짜 선택 버튼 클릭 시
            val dateSelectBtn = mAlertDialog.findViewById<Button>(R.id.dateSelectBtn)

            dateSelectBtn?.setOnClickListener {
                // 2-1. 현재 날짜 가져오기
                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)

                // 2-2. DatePickerDialog를 통해 날짜 선택
                val dig = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        // 2-3. 선택된 날짜를 버튼에 표시
                        dateSelectBtn.setText("${year}/${month + 1}/${dayOfMonth}")

                        // 3-1. 운동 날짜 가져오기
                        dateText = "${year}/${month + 1}/${dayOfMonth}"
                    }
                }, year, month, date)

                // 2-4. DatePickerDialog 보이기
                dig.show()
            }

            // 3. DB에 저장하기 버튼
            val saveBtn = mDialogView.findViewById<Button>(R.id.saveBtn)
            saveBtn?.setOnClickListener {

                // 3-2. 운동 메모 가져오기
                val workoutMemo =
                    mAlertDialog.findViewById<EditText>(R.id.workoutMemo)?.text.toString()

                val database = Firebase.database
                val myRef = database.getReference("workoutMemo").child(Firebase.auth.currentUser!!.uid)
                val model = DataModel(dateText, workoutMemo)

                // 3-3. DB에 데이터 저장
                myRef.push().setValue(model)

                // 3-4. 다이얼로그 닫기
                mAlertDialog.dismiss()
            }
        }
    }
}