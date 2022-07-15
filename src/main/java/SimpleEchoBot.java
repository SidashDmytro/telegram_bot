import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class SimpleEchoBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                String text = message.getText().trim();

                if (text.equals("/start")) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("Для заказа такси 🚕 нажмите кнопку ниже 👇");
                    sendMessage.setParseMode(ParseMode.MARKDOWN);
                    sendMessage.setChatId(message.getChatId());

                    // создаем клавиатуру

                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    List<KeyboardRow> keyboardRowList = new ArrayList<>();
                    KeyboardRow keyboardRow1 = new KeyboardRow();
                    KeyboardButton keyboardButton1 = new KeyboardButton();
                    keyboardButton1.setText("Заказать такси");
                    keyboardRow1.add(keyboardButton1);
                    keyboardRowList.add(keyboardRow1);
                    replyKeyboardMarkup.setKeyboard(keyboardRowList);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        //todo add logging to the project.
                        e.printStackTrace();
                    }
                } else if (text.equals("Заказать такси")) {

                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("Нажмите на кнопку 👇, чтобы поделиться Вашим номером телефона");
                    sendMessage.setParseMode(ParseMode.MARKDOWN);
                    sendMessage.setChatId(message.getChatId());

                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    List<KeyboardRow> keyboardRowList = new ArrayList<>();
                    KeyboardRow keyboardRow1 = new KeyboardRow();
                    KeyboardButton keyboardButton1 = new KeyboardButton();
                    keyboardButton1.setText("Поделиться номером телефона");
                    keyboardButton1.setRequestContact(true);
                    keyboardRow1.add(keyboardButton1);
                    keyboardRowList.add(keyboardRow1);
                    replyKeyboardMarkup.setKeyboard(keyboardRowList);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        //todo add logging to the project.
                        e.printStackTrace();
                    }
                }
            } else if (message.hasContact()) {
                Contact contact = message.getContact();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Нажмите на кнопку ниже, чтобы поделиться вашим местоположением");
                sendMessage.setParseMode(ParseMode.MARKDOWN);
                sendMessage.setChatId(message.getChatId());

                System.out.println("Новый заказ: " + contact.getLastName() + " " +
                        contact.getFirstName() + ",  телефон " + contact.getPhoneNumber());

                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                replyKeyboardMarkup.setResizeKeyboard(true);
                List<KeyboardRow> keyboardRowList2 = new ArrayList<>();
                KeyboardRow keyboardRow2 = new KeyboardRow();
                KeyboardButton keyboardButton2 = new KeyboardButton();
                keyboardButton2.setText("Отправить мои координаты (опция доступна только с мобильного!)");
                keyboardButton2.setRequestLocation(true);
                keyboardRow2.add(keyboardButton2);
                keyboardRowList2.add(keyboardRow2);
                replyKeyboardMarkup.setKeyboard(keyboardRowList2);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);

                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    //todo add logging to the project.
                    e.printStackTrace();
                }
            } else if (message.hasLocation()) {
                Location location = message.getLocation();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Ваши координаты: " + location.getLatitude() + ", " + location.getLongitude());
                sendMessage.setParseMode(ParseMode.MARKDOWN);
                sendMessage.setChatId(message.getChatId());
                System.out.println("Адрес: " + location.getLatitude() + ", " + location.getLongitude());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "taxi2022_bot ";
    }

    @Override
    public String getBotToken() {
        return "5419733617:AAGgp8_YCST0iggyoSkmT7_rXg-gdWqnoAM";
    }
}