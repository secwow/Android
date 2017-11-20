package dima.rebenko.notebook.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;
import bsh.EvalError;
import bsh.Interpreter;
import dima.rebenko.notebook.R;


public class CalclulatorFragment extends AppCompatActivity implements View.OnClickListener {

    Button one, two, three, four, five, six, seven, eight, nine, zero;
    Button multiply, divide, plus, minus;
    Button equal;
    TextView result;

    private List<String> operation = Arrays.asList("+", "-", "*", "/");
    private  List<Integer> opeationID = Arrays.asList(R.id.minus, R.id.plus, R.id.divide, R.id.multiply, R.id.equal);
    private List<Integer> numberID = Arrays.asList(R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero);;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calclulator);
        one = (Button)findViewById(R.id.one);
        two = (Button)findViewById(R.id.two);
        three = (Button)findViewById(R.id.three);
        four = (Button)findViewById(R.id.four);
        five = (Button)findViewById(R.id.five);
        six = (Button)findViewById(R.id.six);
        seven = (Button)findViewById(R.id.seven);
        eight = (Button)findViewById(R.id.eight);
        nine = (Button)findViewById(R.id.nine);
        zero = (Button)findViewById(R.id.zero);
        minus = (Button)findViewById(R.id.minus);
        plus = (Button)findViewById(R.id.plus);
        multiply = (Button)findViewById(R.id.multiply);
        divide = (Button)findViewById(R.id.divide);
        equal = (Button)findViewById(R.id.equal);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        plus.setOnClickListener(this);
        multiply.setOnClickListener(this);
        divide.setOnClickListener(this);
        equal.setOnClickListener(this);
        result = (TextView)findViewById(R.id.calculation_result_view);
        result.setOnClickListener(this);
    }

    private void addToExpr(String oper){
        String exp =  String.valueOf(result.getText());
        if (exp.length() == 0)
        {
            if ("".equals(oper) || operation.contains(oper) || "0".equals(oper)) {
                return;
            }
            result.setText(String.format("%s%s", result.getText(), oper));
            return;
        }

        if (operation.contains(oper)) {

            if (operation.contains(String.valueOf(exp.charAt(exp.length()-1)))) {
                result.setText(String.format("%s%s", exp.substring(0, exp.length() - 1), oper));
                return;
            }
        }
        if (oper.equals("0")) {
            if (!operation.contains(String.valueOf(exp.charAt(exp.length()-1))))
            {
                result.setText(String.format("%s%s", result.getText(), oper));
            }
            return;
        }

        if (oper.equals("=")){
            if (!operation.contains(String.valueOf(exp.charAt(exp.length()-1))))
            {
                Interpreter interpreter = new Interpreter();
                try {
                    interpreter.eval("result=" + String.valueOf(exp));
                    result.setText(interpreter.get("result").toString());
                } catch (EvalError evalError) {
                    evalError.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(),"Error operator cannot be last", Toast.LENGTH_LONG).show();
            }
            return;
        }
        else {
            result.setText(String.format("%s%s", result.getText(), oper));
        }
    }

    @Override
    public void onClick(View view) {
        String message = "";
        if (numberID.contains(view.getId()) || opeationID.contains(view.getId()))
        {
            message = ((TextView)view).getText().toString();
            this.addToExpr(message);
        }
        if(view.equals(result)){
            result.setText("");
        }
    }

}
