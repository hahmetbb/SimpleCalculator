package com.example.calculatorkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.calculatorkt.databinding.ActivityMainBinding
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun btnNumberEvent(view: View) {
        val btnSelect = view as TextView
        val btnText = btnSelect.text.toString()
        var solutionValue = binding.txtSolution.text.toString()
        val resultValue = binding.txtResult.text.toString()

        if (btnText == "AC") {
            binding.txtSolution.text = ""
            binding.txtResult.text = "0"
            return
        } else if (btnText == "=") {
            if (solutionValue.isNotEmpty()) {
                binding.txtSolution.text = binding.txtResult.text
                return
            } else {
                binding.txtSolution.text = ""
                binding.txtResult.text = "0"
                return
            }
        } else if(btnText == "%") {
            if (solutionValue.isNotEmpty()){
                solutionValue = (resultValue.toDouble()/100).toString()
            } else {
                binding.txtSolution.text = ""
                binding.txtResult.text = "0"
                return
            }
        } else if (btnText == "C") {
            if (solutionValue.isEmpty() ||solutionValue.length == 1){
                binding.txtSolution.text = ""
                binding.txtResult.text = "0"
                return
            } else {
                solutionValue = solutionValue.substring(0, solutionValue.length - 1)
            }
        } else if (btnText == "+" || btnText == "-" || btnText == "*" || btnText == "/"){
            if (solutionValue.isEmpty()) {
                binding.txtSolution.text = ""
                binding.txtResult.text = "0"
                return
            } else if (solutionValue.endsWith("/" )){
                solutionValue = solutionValue.replace("/", btnText)
            } else if (solutionValue.endsWith("*" )){
                solutionValue = solutionValue.replace("*", btnText)
            } else if (solutionValue.endsWith("-" )){
                solutionValue = solutionValue.replace("-", btnText)
            } else if (solutionValue.endsWith("+" )){
                solutionValue = solutionValue.replace("+", btnText)
            } else {
                solutionValue += btnText
            }
        } else {
            solutionValue += btnText
        }
        binding.txtSolution.text = solutionValue

        val finalResult = getResult(solutionValue)
        if (finalResult != "Error") {
            binding.txtResult.text = finalResult
        }
    }

    private fun getResult(data: String): String {
        return try {
            val context: Context = Context.enter()
            context.optimizationLevel = -1
            val scriptable: Scriptable = context.initStandardObjects()
            var finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString()
            if (finalResult.endsWith(".0")){
                finalResult = finalResult.replace(".0","")
            }
            finalResult
        } catch (e: Exception) {
            "Error"
        }
    }

}