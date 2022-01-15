import java.util.ArrayList;

import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;



public class NdBot extends TelegramLongPollingBot {
    SendMessage usageMessage = new SendMessage();
    SendMessage infoMessage = new SendMessage();
    SendMessage errorMessage =new SendMessage();
    InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        usageMessage.setChatId(chatId);
        infoMessage.setChatId(chatId);
        errorMessage.setChatId(chatId);
        infoMessage.setText("Hi! This is NDownloader this can sent you whole gallery of hentais.You just have to enter hentai gallery code.");
        String[] textString = update.getMessage().getText().split(" ");
        if(textString[0].equals("/start")){
            try {
                execute(infoMessage);
            } catch (TelegramApiException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else if(textString[0].equals("/help")){
            try {
                execute(infoMessage);
            } catch (TelegramApiException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            try {
                Integer.parseInt(textString[0]);
            } catch (NumberFormatException valueException) {
                errorMessage.setText("You can't enter letters.You can only enter hentai code.Please check back your input.");
                try {
                    execute(errorMessage);
                } catch (TelegramApiException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            getHentai(textString[0],errorMessage, infoMessage, update);
        }
    }

    public void getHentai(String code,SendMessage errorMessage ,SendMessage infoMessage,Update update) {
        WebReader webReader = new WebReader("https://nhentai.net/g/"+code+"/");
        Document document = null;
        try {
            document = webReader.getDocument();
        } catch (HttpStatusException e) {
            errorMessage.setText("Your code didn't match.");
            try {
                execute(errorMessage);
            } catch (TelegramApiException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return;
        }


        infoMessage.setText("Pages: "+ webReader.getPages(document) +"\nLink: " + "https://nhentai.net/g/"+code+"/");
        try {
            execute(infoMessage);
        } catch (TelegramApiException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ArrayList<String> links = webReader.getImgLinks(document);
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(update.getMessage().getChatId().toString());
        ArrayList<InputMedia> arrList = new ArrayList<InputMedia>();
        sendMediaGroup.setMedias(arrList);
        
        for (String link : links) {
            arrList.add(new InputMediaPhoto(link));
            if(arrList.size() >= 10){
                try {
                    execute(sendMediaGroup);
                } catch (TelegramApiException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                arrList.clear();
            }
        }
        try {
            execute(sendMediaGroup);
        } catch (TelegramApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        arrList.clear(); 
    }

    @Override
    public String getBotUsername() {
        // TODO
        return "NDownloader";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "5013548638:AAGuNyamDAZkMOF8KMoSjMOyqhC9r5oe2JQ";
    }
}