package com.deviz.keypadlite

import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        val btnEnter: ImageButton = view.findViewById(R.id.btnEnter)

        btnDelete.setOnClickListener {
            if (enteredPin.isNotEmpty()) {
                enteredPin.deleteCharAt(enteredPin.length - 1)
                updatePinIndicator()
                if (enteredPin.isEmpty()) {
                    btnDelete.isEnabled = false
                }
            }
        }

        btnEnter.setOnClickListener {
            if (enteredPin.length == pinLength) {
                Toast.makeText(this, "비밀번호가 설정되었습니다", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()
            }
        }

        // 숫자 키패드 버튼 동적으로 생성
        val buttonValues = (1..9).toMutableList().apply { add(0) }
        val emptyIndices = (0 until 12).shuffled().take(2).toSet()  // 빈 공간의 인덱스를 랜덤으로 선택
        gridLayout.removeAllViews() // 기존 버튼 제거

        // 디스플레이 크기를 얻음
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        // 각 버튼 사이의 여백을 dp 단위에서 픽셀 단위로 변환
        val marginInDp = 2
        val marginInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginInDp.toFloat(), displayMetrics).toInt()

        // 버튼의 비율 88:54
        val buttonRatioWidth = 88
        val buttonRatioHeight = 54

        // 버튼의 너비를 계산: (화면 너비 - 총 여백) / 버튼 수
        val totalMargin = marginInPx * (gridLayout.columnCount - 1)
        val availableWidth = screenWidth - totalMargin
        val buttonWidth = availableWidth / gridLayout.columnCount
        val buttonHeight = buttonWidth * buttonRatioHeight / buttonRatioWidth

        for (i in 0 until 12) {
            val button = ImageButton(this)
            button.layoutParams = GridLayout.LayoutParams().apply {
                rowSpec = GridLayout.spec(i / 4)
                columnSpec = GridLayout.spec(i % 4)
                width = buttonWidth
                height = buttonHeight
                setMargins(marginInPx, marginInPx, marginInPx, marginInPx)
            }

            if (i in emptyIndices) {
                button.background = null // 빈 공간
            } else {
                val value = buttonValues.removeFirst()
                button.background = createStateListDrawable(value)
                button.setOnClickListener {
                    if (enteredPin.length < pinLength) {
                        enteredPin.append(value)
                        updatePinIndicator()
                        btnDelete.isEnabled = true
                    }
                }
            }
            gridLayout.addView(button)
        }

        bottomSheetDialog.show()
    }

    private fun createStateListDrawable(value: Int): StateListDrawable {
        val states = StateListDrawable()
        val offDrawableId = resources.getIdentifier("btn_$value", "drawable", packageName)
        val onDrawableId = resources.getIdentifier("btn_${value}_on", "drawable", packageName)
        states.addState(intArrayOf(android.R.attr.state_pressed), resources.getDrawable(onDrawableId, null))
        states.addState(intArrayOf(), resources.getDrawable(offDrawableId, null))
        return states
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