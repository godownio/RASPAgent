package cn.org.javaweb.agent.premain;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.mvel2.MVEL;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class MainTest {

    public static void main(String[] args) throws OgnlException {
//        String expression = "new java.lang.ProcessBuilder(\"calc\").start();";
//        MVEL.eval(expression);

//        OgnlContext ognlContext = new OgnlContext();
//        Ognl.getValue("@java.lang.Runtime@getRuntime().exec('calc')", ognlContext, ognlContext.getRoot());
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("T(java.lang.Runtime).getRuntime().exec('calc')");
        EvaluationContext context = new StandardEvaluationContext();
        expression.getValue(context);
    }
}
