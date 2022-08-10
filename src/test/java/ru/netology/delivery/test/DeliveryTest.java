package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        fillUserData(validUser);
        fillMeetingDate(firstMeetingDate);
        enableCheckbox();
        clickToPlanBtn();
        validatePlanResult(firstMeetingDate);
        fillMeetingDate(secondMeetingDate);
        clickToPlanBtn();
        validateReplanResult();
        clickReplanBtn();
        validatePlanResult(secondMeetingDate);

    }

    private void fillUserData(DataGenerator.UserInfo userInfo) {
        $("[placeholder='Город']").setValue(userInfo.getCity());
        $("[name='name']").setValue(userInfo.getName());
        $("[name='phone']").setValue(userInfo.getPhone());
    }

    private void fillMeetingDate(String meetDate) {
        $("[placeholder='Дата встречи']").sendKeys(Keys.LEFT_CONTROL + "a");
        $("[placeholder='Дата встречи']").sendKeys(Keys.DELETE);
        $("[placeholder='Дата встречи']").setValue(meetDate).pressEscape();
    }

    private void enableCheckbox() {
        $x("//*[@class='checkbox__box']").click();
    }

    private void clickToPlanBtn() {
        $x("//button[.//span[text()='Запланировать']]").click();
    }

    private void clickReplanBtn() {
        $x("//div[@data-test-id='replan-notification']//button").click();
    }

    private void validatePlanResult(String meetDate) {

        $x("//div[@data-test-id='success-notification']//div[@class='notification__title']")
                .should(Condition.text("Успешно!"))
                .shouldBe(Condition.visible);
        $x("//div[@data-test-id='success-notification']//div[@class='notification__content']")
                .should(Condition.text("Встреча успешно запланирована на " + meetDate))
                .shouldBe(Condition.visible);

    }

    private void validateReplanResult() {

        $x("//div[@data-test-id='replan-notification']//div[@class='notification__title']")
                .should(Condition.text("Необходимо подтверждение"))
                .shouldBe(Condition.visible);
        $x("//div[@data-test-id='replan-notification']//div[@class='notification__content']")
                .should(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать??"))
                .shouldBe(Condition.visible);
    }

}