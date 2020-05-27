package com.daishuai.rule.entity;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.support.UnitRuleGroup;

/**
 * @author Daishuai
 * @date 2020/5/23 20:02
 */
public class RuleClass {

    @Rule(priority = 1)
    public static class FizzRule {

        @Condition
        public boolean isFizz(@Fact(value = "number") Integer number) {
            return number % 5 == 0;
        }

        @Action
        public void printFizz() {
            System.out.println("fizz");
        }
    }

    @Rule(priority = 2)
    public static class BuzzRule {

        @Condition
        public boolean isBuzz(@Fact(value = "number") Integer number) {
            return number % 7 == 0;
        }

        @Action
        public void printBuzz() {
            System.out.println("buzz");
        }
    }

    public static class FizzBuzzRule extends UnitRuleGroup {

        public FizzBuzzRule(Object ... rules) {
            for (Object rule : rules) {
                addRule(rule);
            }
        }

        @Override
        public int getPriority() {
            return 0;
        }
    }

    @Rule(priority = 3)
    public static class NonFizzBuzzRule {

        @Condition
        public boolean isNonFizzBuzz(@Fact(value = "number") Integer number) {
            return number % 5 != 0 || number % 7 != 0;
        }

        @Action
        public void printInput(@Fact(value = "number") Integer number) {
            System.out.println(number);
        }
    }
}
