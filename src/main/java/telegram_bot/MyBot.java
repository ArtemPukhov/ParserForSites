package telegram_bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dugaweld.parser.controllers.ProductController;
import ru.dugaweld.parser.model.Product;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyBot extends TelegramLongPollingBot {

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

                    // Ищем товар в базе данных
                    List<Product> product = ProductController.getProductRepository()
                            .findByNameContaining(productName);

                    // Отправляем результат поиска пользователю
                    if (product.size() == 0) {
                        message.setText("Товар не найден");
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {
                        for (Product product1 : product) {
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
