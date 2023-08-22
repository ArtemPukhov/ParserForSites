package telegram_bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dugaweld.parser.controllers.ProductController;
import ru.dugaweld.parser.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MyBot extends TelegramLongPollingBot {

    private Map<Long, String> userData = new HashMap<>();
    private Map<Long, Integer> userCount = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));

            if (messageText.equals("/start")) {
                message.setText("Привет! Чтобы найти товар, напиши его название");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {

                // Ожидаем ответ от пользователя
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Обрабатываем ответ пользователя
                if (update.hasMessage() && update.getMessage().hasText()) {
                    String productName = update.getMessage().getText();

                    // Проверяем, была ли нажата кнопка "Показать еще"
                    if (productName.equals("Показать еще")) {
                        // Получаем сохраненные значения productName и count из пользовательских данных
                        productName = userData.get(chatId);
                        int count = userCount.get(chatId);
                        // Увеличиваем счетчик для следующих результатов поиска
                        count += 5;
                        userCount.put(chatId, count);
                    } else {
                        // Сохраняем значения productName и count в пользовательских данных
                        userData.put(chatId, productName);
                        userCount.put(chatId, 0);
                    }

                    // Ищем товар в базе данных
                    List<Product> product = ProductController.getProductRepository().findByNameContaining(productName);

                    // Отправляем результат поиска пользователю
                    if (product.size() == 0) {
                        message.setText("Товар не найден");
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {
                        int count = userCount.get(chatId);
                        for (int i = count; i < count + 5 && i < product.size(); i++) {
                            Product product1 = product.get(i);
                            String result = "Название: " + product1.getName() + "\n" +
                                    "Цена: " + product1.getPrice() + "\n" +
                                    "Ссылка: " + product1.getUrl();

                            message.setText(result);

                            try {
                                execute(message);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }

                        // Создаем клавиатуру
                        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                        keyboardMarkup.setResizeKeyboard(true);
                        List<KeyboardRow> keyboard = new ArrayList<>();

                        // Создаем ряд для кнопок
                        KeyboardRow row = new KeyboardRow();
                        KeyboardButton nextButton = new KeyboardButton("Показать еще");
                        row.add(nextButton);
                        keyboard.add(row);

                        // Устанавливаем клавиатуру в сообщении
                        message.setReplyMarkup(keyboardMarkup);
                        keyboardMarkup.setKeyboard(keyboard);

                        // Отправляем сообщение с клавиатурой пользователю
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        // Возвращает имя пользователя бота
        return "";
    }

    @Override
    public String getBotToken() {
        // Возвращает токен доступа к API бота
        return "";
    }

    private void sendMsg(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
