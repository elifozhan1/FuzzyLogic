package arac;

import java.net.URISyntaxException;

import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Rule;

public class Program {
	public static void main(String[] args) throws URISyntaxException {
		Tasit tasit = new Tasit(50,0);
		System.out.println("Hız: "+tasit.hiz());
		var rules = tasit.getModel().getFunctionBlock("Model").getFuzzyRuleBlock("kuralblok");
		for(Rule rule : rules) {
			if (rule.getDegreeOfSupport() > 0)
				System.out.println(rule);
		}
		JFuzzyChart.get().chart(tasit.getModel());
	}

}