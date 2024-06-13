package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountInput = $("[data-test-id=amount] .input__control");
    private final SelenideElement amountFrom = $("[data-test-id=from] .input__control");
    private final SelenideElement button = $("[data-test-id=action-transfer]");
    private final SelenideElement errorMassage = $("[data-test-id=error-notification] .notification__content");
    private final SelenideElement transferHead = $(byText("Пополнение карты"));

    public TransferPage() {
        transferHead.shouldBe(visible);
    }
    public DashboardPage makeValidTransfer(String sum, DataHelper.CardInfo cardInfo) {
        makeTransfer(sum, cardInfo);
        return new DashboardPage();
    }
    public void makeTransfer(String sum, DataHelper.CardInfo cardInfo) {
        amountInput.setValue(sum);
        amountFrom.setValue(cardInfo.getCardNumber());
        button.click();
    }

    public void findErrorMassage(String expectedText) {
        errorMassage.shouldHave(text(expectedText));
    }


}
