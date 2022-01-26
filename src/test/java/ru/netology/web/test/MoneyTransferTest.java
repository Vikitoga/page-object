package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }


    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        val dashboardPage = new DashboardPage();
        int amount = 200;
        val firstCardId = DataHelper.getFirstCard().getCardID();
        val secondCardId = DataHelper.getSecondCard().getCardID();
        val firstCardBalance = dashboardPage.getCardBalance(firstCardId) + amount;
        val secondCardBalance = dashboardPage.getCardBalance(secondCardId) - amount;
        val transferPage = dashboardPage.depositCard(firstCardId); //Нажимаем кнопку Пополнить у первой карты
        transferPage.transferMoney(amount, DataHelper.getSecondCard());
        assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCardId));
        assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardId));
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        val dashboardPage = new DashboardPage();
        int amount = 3000;
        val firstCardId = DataHelper.getFirstCard().getCardID();
        val secondCardId = DataHelper.getSecondCard().getCardID();
        val firstCardBalance = dashboardPage.getCardBalance(firstCardId) - amount;
        val secondCardBalance = dashboardPage.getCardBalance(secondCardId) + amount;
        val transferPage = dashboardPage.depositCard(secondCardId); //Нажимаем кнопку Пополнить у второй карты
        transferPage.transferMoney(amount, DataHelper.getFirstCard());
        assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCardId));
        assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardId));
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCardOverLimit() {
        val dashboardPage = new DashboardPage();
        int amount = 13000;
        val firstCardId = DataHelper.getFirstCard().getCardID();
        val secondCardId = DataHelper.getSecondCard().getCardID();
        val firstCardBalance = dashboardPage.getCardBalance(firstCardId) - amount;
        val secondCardBalance = dashboardPage.getCardBalance(secondCardId) + amount;
        val transferPage = dashboardPage.depositCard(secondCardId); //Нажимаем кнопку Пополнить у второй карты
        transferPage.transferMoney(amount, DataHelper.getFirstCard());
        assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCardId));
        assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardId));
    }
}
