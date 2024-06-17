package com.pepe.miniapp;

import com.pepe.miniapp.models.Reffs;
import com.pepe.miniapp.models.User;
import com.pepe.miniapp.repositories.ReffsRepository;
import com.pepe.miniapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigInteger;
import java.util.*;

@Component
public class Bot extends TelegramLongPollingBot {

    private final String botUsername;
    private final MessageSource messageSource;
    private final UserRepository userRepository;
    private final ReffsRepository reffsRepository;

    public Bot(TelegramBotsApi telegramBotsApi,
               @Value("${telegram-bot.username}") String botUsername,
               @Value("${telegram-bot.token}") String botToken,
               MessageSource messageSource,
               UserRepository userRepository,
               ReffsRepository reffsRepository
    ) throws TelegramApiException {
        super(botToken);
        this.botUsername = botUsername;
        this.messageSource = messageSource;
        this.userRepository = userRepository;
        this.reffsRepository = reffsRepository;
        telegramBotsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().getText().contains("/start")) {
           try {
               execute(startMessage(update));
           } catch (TelegramApiException e) {
               throw new RuntimeException(e);
           }
        } else if (update.hasMessage() && update.getMessage().getText().contains("/referral")) {
            String messageText = messageSource.getMessage("referralDescription", null, Locale.of("en_en"));
            messageText = insertMonospace(messageText, "https://t.me/NextGenPepeBot?start=" + update.getMessage().getFrom().getId());
            SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), messageText);
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            InlineKeyboardButton inviteFriendButton = new InlineKeyboardButton("INVITE A FREN");
            String text = "Join the NEXT GEN PEPE and ride with me in the same squad!\n" +
                    "\n" +
                    "For tapping my referral link, you will get bonus points for free and get a chance to win a weekly contest with a prize pool of 140$ in SOL! No investments needed!\n" +
                    "\n" +
                    "Take 1 minute of your time, click this link, and the information you will learn might change your life forever.\n\nhttps://t.me/NextGenPepeBot?start=" + update.getMessage().getFrom().getId();
            inviteFriendButton.setUrl("tg://msg?text=" + text);
            buttons.add(inviteFriendButton);
            message.setParseMode("HTML");
            message.setReplyMarkup(InlineKeyboardMarkup.builder().keyboard(Collections.singletonList(buttons)).build());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasMessage() && update.getMessage().getText().contains("/buy")) {
            String messageText = messageSource.getMessage("howToBuy", null, Locale.of("en_en"));
            messageText = insertLink(messageText, "click here to watch a quick video tutorial on how to buy!", "https://www.youtube.com/watch?v=Ln3ARbe9rQA&ab_channel=NEXTGEN%24PEPE");
            SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), messageText);
            message.setParseMode("HTML");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("how_to_earn")) {
            String messageText = messageSource.getMessage("howToEarn", null, Locale.of("en_en"));
            SendMessage message = new SendMessage(update.getCallbackQuery().getFrom().getId().toString(), messageText);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("next_gen_power")) {
            String messageText = messageSource.getMessage("nextGenPower", null, Locale.of("en_en"));
            SendMessage message = new SendMessage(update.getCallbackQuery().getFrom().getId().toString(), messageText);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private SendMessage startMessage(Update update) {
        String[] parts = update.getMessage().getText().split(" ");
        if (parts.length > 1) {
            Long reffId = Long.valueOf(parts[1]);
            setReff(reffId, update.getMessage().getFrom().getId());
        }
        if (!userRepository.existsByTelegramId(update.getMessage().getFrom().getId())) {
            User user = new User(update.getMessage().getFrom().getId());
            user.setName(update.getMessage().getFrom().getFirstName());
            userRepository.save(user);
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        String messageText = messageSource.getMessage("start", null, Locale.of("en_en"));
        sendMessage.setText(messageText);

        Object[] args = new Object[1];
        args[0] = "https://t.me/NextGenPepeBot?start=" + update.getMessage().getFrom().getId();

        String inviteText = messageSource.getMessage("inviteFren", args, Locale.of("en_en")) + "https://t.me/NextGenPepeBot?start=" + update.getMessage().getFrom().getId();
        List<InlineKeyboardButton> howToEarnButton = Collections.singletonList(InlineKeyboardButton.builder().callbackData("how_to_earn").text("Learn how to earn").build());
        List<InlineKeyboardButton> pepenizeFrenButton = Collections.singletonList(InlineKeyboardButton.builder().url("tg://msg?text=" + inviteText).text("Pepenize a fren").build());
        List<InlineKeyboardButton> playInOneClick = Collections.singletonList(InlineKeyboardButton.builder().webApp(new WebAppInfo("https://nextgenpepe.xyz")).text("Play in 1 click").build());
        List<InlineKeyboardButton> nextGenPower = Collections.singletonList(InlineKeyboardButton.builder().callbackData("next_gen_power").text("NEXT GEN POWER").build());
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(howToEarnButton);
        buttons.add(pepenizeFrenButton);
        buttons.add(playInOneClick);
        buttons.add(nextGenPower);

        sendMessage.setReplyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build());

        return sendMessage;
    }

    private static String insertMonospace(String text, String code) {
        return text.replace("link", "<code>" + escapeHtml(code) + "</code>");
    }

    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private static String insertLink(String text, String placeholder, String url) {
        String link = "<a href=\"" + url + "\">" + placeholder + "</a>";
        return text.replace("[" + placeholder + "]", link);
    }

    private void setReff(Long reffererId, Long referralId) {
        if (userRepository.existsByTelegramId(reffererId) && !userRepository.existsByTelegramId(referralId)) {
            User refferer = userRepository.findByTelegramId(reffererId).orElse(null);
            assert refferer != null;
            Reffs reffs = reffsRepository.findByUser(refferer).orElse(new Reffs(refferer));
            User referral = new User(referralId);
            referral.setReff(refferer);
            userRepository.save(referral);
            reffs.getReffs().add(referral);
            reffs.setReffs(reffs.getReffs());
            reffsRepository.save(reffs);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
