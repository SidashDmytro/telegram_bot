import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

public class SimpleEchoBot extends TelegramLongPollingBot {
    enum States {
        START,
        ORDER,
        START_POINT,
        FINISH_POINT,
        FINISH
    }

    private States mState = States.START;

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Message message = update.getMessage();
            //if (!message.isReply()) return;

            switch (mState) {
                case START: {
                    String text = message.getText();
                    if (text.equals("/start")) {
                        startMessage(message);
                        mState = States.ORDER;
                    }
                    if (text.equals("/help")) {
                        helpMessage(message);
                    }
                }
                break;

                case ORDER: {
                    orderTaxi(message);
                    mState = States.START_POINT;
                }
                break;

                case START_POINT: {
                    startPoint(message);
                    mState = States.FINISH_POINT;
                }
                break;

                case FINISH_POINT: {
                    finishPoint(message);
                    mState = States.FINISH;
                }
                break;

                case FINISH: {
                    acceptedOrder(message);
                    mState = States.START;
                }
                break;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void startMessage(Message message) throws TelegramApiException {
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
        execute(sendMessage);
    }

    public void helpMessage(Message message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Бот для заказа такси " +
                "\n\n/start - заказать такси" +
                "\n/help - справка");
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
        execute(sendMessage);
    }

    public void orderTaxi(Message message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Нажмите на кнопку 👇, чтобы поделиться Вашим номером телефона");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        keyboardButton1.setText("Поделиться номером телефона");
        keyboardButton1.setRequestContact(true);
        keyboardRow1.add(keyboardButton1);
        keyboardRowList.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        execute(sendMessage);
    }

    public void startPoint(Message message) throws TelegramApiException {
        Contact contact = message.getContact();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Напишите адрес, где Вы будете ждать такси 👇");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId());
        System.out.println("Новый заказ: " + contact.getLastName() + " " +
                contact.getFirstName() + ",  телефон " + contact.getPhoneNumber());
        execute(sendMessage);
    }

    public void finishPoint(Message message) throws TelegramApiException {
        SendMessage locationMessage = new SendMessage();
        locationMessage.setText("Напишите адрес, куда планируете ехать 👇");
        locationMessage.setParseMode(ParseMode.MARKDOWN);
        locationMessage.setChatId(message.getChatId());
        execute(locationMessage);
        System.out.println("Едем из: " + message.getText());
    }

    public void acceptedOrder(Message message) throws TelegramApiException {
        SendMessage locationMessage = new SendMessage();
        locationMessage.setText("Спасибо, Ваш заказ принят. Ожидайте такси!");
        locationMessage.setParseMode(ParseMode.MARKDOWN);
        locationMessage.setChatId(message.getChatId());
        execute(locationMessage);
        System.out.println("Конечный пункт: " + message.getText());
    }

    @Override
    public String getBotUsername() {
        return "taxi2022_bot";
    }

    @Override
    public String getBotToken() {
        return "5419733617:AAGgp8_YCST0iggyoSkmT7_rXg-gdWqnoAM";
    }
}