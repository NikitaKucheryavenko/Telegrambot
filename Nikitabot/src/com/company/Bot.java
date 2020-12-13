package com.company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import java.io.IOException;


public class Bot extends TelegramLongPollingBot {
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        System.out.println(message.hasText());
        System.out.println(update.getMessage().getFrom().getFirstName()+":"+update.getMessage().getText());
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        if (message.getText() != null) {
            System.out.println(1);
            switch (message.getText()) {
                case "/start":
                    try {
                        sendMessage(sendMessage.setText("Отправьте свою локацию и получите информацию " +
                                "о погоде вашего местоположения"));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "/subscribe":
                case "/unsubscribe":
                    try {
                        sendMessage(sendMessage.setText("Функция временно не работает, просим прощения за " +
                                "доставленные неудобства"));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    try {
                        sendMessage(sendMessage.setText("Команда не распознана"));
                    }catch(TelegramApiException e){
                        e.printStackTrace();
                    }
            }
        }
        else if(message.hasLocation()){
            try {
                sendMessage(sendMessage.setText(parsing(update.getMessage().getLocation().getLatitude().doubleValue(),
                        update.getMessage().getLocation().getLongitude().doubleValue())));
            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                sendMessage(sendMessage.setText("Я не понимаю вас"));
            }catch(TelegramApiException e){
                e.printStackTrace();
            }
        }
    }

    public String getBotUsername() {
        return "stormcloak_bot";
    }

    public String getBotToken() {
        return "1395887585:AAG7rSJMn05JTnV1R44c7smhhsTSZlP69Yc";
    }

    public String parsing(Double lat, Double lon) throws IOException {
        String string = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%.5f&lon=%.5f&mode=xml&units=metric&appid=a417a9afcbcad213b77ced6b64f2e589",
                lat,lon);
        System.out.println(string);
        Document doc = Jsoup.connect(string).get();
        Elements element = doc.getElementsByTag("temperature");
        String MaxTemp = element.attr("max");
        String MinTemp = element.attr("min");
        String LiveTemp = element.attr("value");
        String text = "Сейчас на улице: "+LiveTemp + "\n" + "Максимальная температура: "+MaxTemp+
                "\n" + "Минимальная температура: "+ MinTemp;
        return text;
    }

}

//    public void onUpdateReceived(Update update){
//        Message message = update.getMessage();
//        if (message != null && message.hasText()){
//            switch(message.getText()){
//                case "/help":
//                    sendMsg(message,"all is okay");
//            }
//        }
//    }