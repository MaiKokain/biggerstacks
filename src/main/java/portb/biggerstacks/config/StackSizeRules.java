package portb.biggerstacks.config;

import portb.biggerstacks.configlib.xml.RuleSet;

public class StackSizeRules
{
    private static RuleSet ruleSet;
    
    public static RuleSet getRuleSet()
    {
        return ruleSet;
    }
    
    public static void setRuleSet(RuleSet ruleSet)
    {
        StackSizeRules.ruleSet = ruleSet;
    }
}
