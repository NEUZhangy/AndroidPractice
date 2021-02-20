package edu.vt.cs.cs5254.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import edu.vt.cs.cs5254.criminalintent.databinding.FragmentCrimeBinding

private const val TAG = "CrimeListFragment"
class CrimeFragment: Fragment(){
    private  val crimeListViewModel: CrimeListViewModel by viewModels()
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var ui: FragmentCrimeBinding
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ui = FragmentCrimeBinding.inflate(layoutInflater, container, false);
        val view =  ui.root
        titleField = ui.crimeTitle
        dateButton = ui.crimeDate
        solvedCheckBox = ui.crimeSolved
        dateButton.apply{
            text= crime.date.toString()
            isEnabled = false
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        }
        titleField.addTextChangedListener(titleWatcher)
        solvedCheckBox.apply {
            setOnCheckedChangeListener{ _, isChecked->crime.isSolved= isChecked
            }
        }
    }
}