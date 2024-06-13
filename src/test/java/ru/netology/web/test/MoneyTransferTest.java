package ru.netology.web.test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;


public class MoneyTransferTest {
    DashboardPage dashboardPage;
    DataHelper.CardInfo firstCardInfo;
    DataHelper.CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCardInfo = get1CardInfo();
        secondCardInfo = get2CardInfo();
        firstCardBalance = dashboardPage.getCardBalance(0);
        secondCardBalance = dashboardPage.getCardBalance(1);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecond() {

        int sum = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = dashboardPage.getCardBalance(0) - sum;
        var expectedBalanceSecondCard = dashboardPage.getCardBalance(1) + sum;
        var TransferPage = dashboardPage.selectCardForTransfer(secondCardInfo);
        dashboardPage = TransferPage.makeValidTransfer(String.valueOf(sum), firstCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceForFirstCard = dashboardPage.getCardBalance(0);
        var actualBalanceForSecondCard = dashboardPage.getCardBalance(1);
        assertAll(() -> assertEquals(expectedBalanceFirstCard,
                        actualBalanceForFirstCard),
                () -> assertEquals(expectedBalanceSecondCard, actualBalanceForSecondCard));
    }

    @Test
    void shouldTransferMoneyFromSecondToFirst() {

        int sum = generateValidAmount(secondCardBalance);
        var expectedBalanceFirstCard = dashboardPage.getCardBalance(1) - sum;
        var expectedBalanceSecondCard = dashboardPage.getCardBalance(0) + sum;
        var TransferPage = dashboardPage.selectCardForTransfer(firstCardInfo);
        dashboardPage = TransferPage.makeValidTransfer(String.valueOf(sum), secondCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceForFirstCard = dashboardPage.getCardBalance(1);
        var actualBalanceForSecondCard = dashboardPage.getCardBalance(0);
        assertAll(() -> assertEquals(expectedBalanceFirstCard,
                        actualBalanceForFirstCard),
                () -> assertEquals(expectedBalanceSecondCard, actualBalanceForSecondCard));
    }

    @Test
    void shouldErrorMassege() {
        int sum = generateValidAmount(secondCardBalance);
        var thirdCardInfo = get3CardInfo();
        var TransferPage = dashboardPage.selectCardForTransfer(firstCardInfo);
        dashboardPage = TransferPage.makeValidTransfer(String.valueOf(sum), thirdCardInfo);
        TransferPage.findErrorMassage("Ошибка! Произошла ошибка");
    }

}
