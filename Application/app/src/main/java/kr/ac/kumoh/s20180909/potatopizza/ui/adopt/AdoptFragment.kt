package kr.ac.kumoh.s20180909.potatopizza.ui.adopt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.delay
import kr.ac.kumoh.s20180909.potatopizza.R
import kotlinx.coroutines.*
import kotlinx.coroutines.delay


class AdoptFragment : Fragment() {
    private lateinit var adoptViewModel: AdoptViewModel
    var kind: String = ""
    var place: String = ""
    var variety: String = ""
    var gender: String = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        adoptViewModel =
                ViewModelProviders.of(this).get(AdoptViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_adopt, container, false)
        val placeSpinner: Spinner = root.findViewById(R.id.positionSpinner)
        val varietySpinner: Spinner = root.findViewById(R.id.varietySpinner)
        val kindRadioGroup: RadioGroup = root.findViewById(R.id.kindRadioGroup)
        val genderRadioGroup: RadioGroup = root.findViewById(R.id.genderRadioGroup)
        val submitBtn: Button = root.findViewById(R.id.submitButton)
        var checkRadio: Boolean = false

        setAdapterSpinner(placeSpinner, R.array.spinner_region)

        kindRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val radio: RadioButton = root.findViewById(i)
            var selected: String = radio.text as String
            if(selected == "강아지")
                setAdapterSpinner(varietySpinner, R.array.spinner_dog)
            else
                setAdapterSpinner(varietySpinner, R.array.spinner_cat)
        }

        placeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    place = if(p2 == 0)
                        ""
                    else
                        placeSpinner.selectedItem.toString()
                }
        }

        varietySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                variety = varietySpinner.selectedItem.toString()
            }
        }
        submitBtn.setOnClickListener {
            val id: Int = kindRadioGroup.checkedRadioButtonId
            val id2: Int = genderRadioGroup.checkedRadioButtonId
            if (id == -1 || id2 == -1)
                checkRadio = false
            else {
                checkRadio = true
                val kindRadioButton: RadioButton = root.findViewById(id)
                val genderRadioButton: RadioButton = root.findViewById(id2)
                kind = kindRadioButton.text as String
                gender = genderRadioButton.text as String
            }
            check(checkRadio)
        }
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    //사용자가 입력한 데이터 Server로 전송
    private fun sendDataToServer() {
        adoptViewModel.requestList(kind, place, variety, gender)
    }

    private fun check(checkRadio: Boolean){
        if (checkRadio) {
            //Toast.makeText(applicationContext, "전부 다 선택됨", Toast.LENGTH_SHORT).show()
            if (place.isEmpty()) {
                Toast.makeText(context as Activity, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                GlobalScope.launch {
                    sendDataToServer()    //Server에 전송
                    delay(500L) //1초
                    startActivity(Intent(context as Activity, WaitActivity2::class.java)) //대기 activity
                }
            }
        }
        else
            Toast.makeText(context as Activity, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
    }
    private fun setAdapterSpinner(spinner: Spinner, place: Int){
        ArrayAdapter.createFromResource(
           context as Activity,
            place,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}