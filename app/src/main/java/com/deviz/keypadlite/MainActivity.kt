package com.deviz.keypadlite

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    private val pinLength = 6
    private val enteredPin = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val passwordContainer: LinearLayout = findViewById(R.id.passwordContainer)
        passwordContainer.setOnClickListener {
            showKeypad()
        }
    }

    private fun showKeypad() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.keypad_bottom_sheet, null)
        bottomSheetDialog.setContentView(view)

        val gridLayout: GridLayout = view.findViewById(R.id.gridLayout)

        // 숫자 키패드 버튼 동적으로 생성
        val buttonValues = (1..9).shuffled() + listOf(null, 0, null)
        gridLayout.removeAllViews() // 기존 버튼 제거
        for (value in buttonValues) {
            val button = ImageButton(this)
            button.layoutParams = GridLayout.LayoutParams().apply {
                width = 200
                height = 200
                setMargins(10, 10, 10, 10)
            }
            button.setBackgroundResource(when (value) {
                0 -> R.drawable.btn_0
                1 -> R.drawable.btn_1
                2 -> R.drawable.btn_2
                3 -> R.drawable.btn_3
                4 -> R.drawable.btn_4
                5 -> R.drawable.btn_5
                6 -> R.drawable.btn_6
                7 -> R.drawable.btn_7
                8 -> R.drawable.btn_8
                9 -> R.drawable.btn_9
                else -> android.R.color.transparent // null 값 처리
            })
            button.setOnClickListener {
                if (value != null) {
                    if (enteredPin.length < pinLength) {
                        enteredPin.append(value)
                        updatePinIndicator()
                        if (enteredPin.length == pinLength) {
                            // PIN 완성시 처리
                            checkPin(enteredPin.toString())
                            bottomSheetDialog.dismiss()
                        }
                    }
                } else {
                    // 지우기 버튼 처리
                    if (enteredPin.isNotEmpty()) {
                        enteredPin.deleteCharAt(enteredPin.length - 1)
                        updatePinIndicator()
                    }
                }
            }
            gridLayout.addView(button)
        }

        bottomSheetDialog.show()
    }

    private fun updatePinIndicator() {
        val passwordContainer: LinearLayout = findViewById(R.id.passwordContainer)
        for (i in 0 until pinLength) {
            val circle = passwordContainer.getChildAt(i)
            circle.setBackgroundResource(
                if (i < enteredPin.length) R.drawable.circle_filled_background else R.drawable.circle_background
            )
        }
    }

    private fun checkPin(pin: String) {
        // PIN 확인 로직 추가
        // 예: 서버와 통신하거나, 저장된 PIN과 비교
    }
}