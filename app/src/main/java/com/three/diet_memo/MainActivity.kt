package com.three.diet_memo

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.datepicker.DateSelector
import java.util.Calendar
import java.util.GregorianCalendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val writeBtn: ImageView = findViewById(R.id.writeBtn)
        writeBtn.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("운동 메모")
            val mAlertDialog = mBuilder.show()

            val dateSelectBtn = mAlertDialog.findViewById<Button>(R.id.dateSelectBtn);

            dateSelectBtn?.setOnClickListener {
                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)

                val dig = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        // 날짜 선택 시
                        dateSelectBtn.setText("${year}/${month + 1}/${dayOfMonth}")
                    }
                }, year, month, date)
                dig.show()
            }
        }
    }
}