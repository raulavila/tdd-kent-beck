package com.raulavila.money;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MoneyTest {

    @Test
    public void testCurrency() throws Exception {
        assertThat(Money.dollar(1).currency()).isEqualTo("USD");
        assertThat(Money.franc(1).currency()).isEqualTo("CHF");
    }

    @Test
    public void dollarMultiplication() throws Exception {
        Money five = Money.dollar(5);
        
        assertThat(five.times(2)).isEqualTo(Money.dollar(10));
        assertThat(five.times(3)).isEqualTo(Money.dollar(15));
    }

    @Test
    public void francMultiplication() throws Exception {
        Money five = Money.franc(5);
        
        assertThat(five.times(2)).isEqualTo(Money.franc(10));
        assertThat(five.times(3)).isEqualTo(Money.franc(15));
    }

    @Test
    public void equality() throws Exception {
        assertThat(Money.dollar(1)).isEqualTo(Money.dollar(1));
        assertThat(Money.dollar(1)).isNotEqualTo(Money.dollar(6));

        assertThat(Money.franc(1)).isEqualTo(Money.franc(1));
        assertThat(Money.franc(1)).isNotEqualTo(Money.franc(6));

        assertFalse(Money.franc(1).equals(Money.dollar(1)));
    }

    @Test
    public void simpleAddition() throws Exception {
        Money five = Money.dollar(5);
        
        Expression sum = five.plus(five);
        
        Bank bank = new Bank();
        
        Money reduced = bank.reduce(sum, "USD");
        
        assertThat(reduced).isEqualTo(Money.dollar(10));
    }

    @Test
    public void testPlusReturnsSum() throws Exception {
        Money five = Money.dollar(5);
        
        Expression result = five.plus(five);
        
        Sum sum = (Sum) result;

        assertThat(sum.augend).isEqualTo(five);
        assertThat(sum.addend).isEqualTo(five);
    }

    @Test
    public void reduceSum() throws Exception {
        Expression sum = new Sum(Money.dollar(3), Money.dollar(4));
        
        Bank bank = new Bank();
        
        Money result = bank.reduce(sum, "USD");

        assertThat(result).isEqualTo(Money.dollar(7));
    }

    @Test
    public void reduceMoney() throws Exception {
        Bank bank = new Bank();
        
        Money result = bank.reduce(Money.dollar(1), "USD");
        
        assertThat(result).isEqualTo(Money.dollar(1));
    }

    @Test
    public void reduceMoneyWithDifferentCurrency() throws Exception {
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        
        Money result = bank.reduce(Money.franc(2), "USD");
        
        assertThat(result).isEqualTo(Money.dollar(1));
    }

    @Test
    public void identityRate() throws Exception {
        assertThat(new Bank().rate("USD", "USD")).isEqualTo(1);
    }

    @Test
    public void mixedAddition() throws Exception {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);

        Money result = bank.reduce(fiveBucks.plus(tenFrancs), "USD");

        assertThat(result).isEqualTo(Money.dollar(10));
    }

    @Test
    public void sumPlusMoney() throws Exception {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);

        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);

        Expression sum = new Sum(fiveBucks, tenFrancs).plus(fiveBucks);
        Money result = bank.reduce(sum, "USD");

        assertThat(result).isEqualTo(Money.dollar(15));
    }

    @Test
    public void sumTimes() throws Exception {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);

        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);

        Expression sum = new Sum(fiveBucks, tenFrancs).times(2);
        Money result = bank.reduce(sum, "USD");

        assertThat(result).isEqualTo(Money.dollar(20));
    }

}
