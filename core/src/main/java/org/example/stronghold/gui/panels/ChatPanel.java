package org.example.stronghold.gui.panels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.model.Message;
import org.example.stronghold.model.User;
import org.example.stronghold.model.template.Chat;

public class ChatPanel extends Panel {

    TextButton publicChatButton, privateChatButton, roomsButton;
    TextField messageField;
    TextButton sendButton;
    Table tabsTable, contentTable;
    ScrollPane scrollPane;

    Chat currentChat = null;

    public ChatPanel(ControlPanel controlPanel) {
        super(controlPanel);
        create();
    }

    private void create() {
        tabsTable = new Table(game.skin);
        contentTable = new Table(game.skin);
        scrollPane = new ScrollPane(contentTable);
        add(tabsTable).growX().row();
        add(scrollPane).grow();
        publicChatButton = new TextButton("Public", game.skin);
        privateChatButton = new TextButton("Private", game.skin);
        roomsButton = new TextButton("Rooms", game.skin);
        tabsTable.add(publicChatButton).growX();
        tabsTable.add(privateChatButton).growX();
        tabsTable.add(roomsButton).growX();
        sendButton = new TextButton("Send", game.skin);
        publicChatButton.addListener(new SimpleChangeListener(() -> {
            try {
                showChat(
                    (Chat) game.conn.sendOperatorRequest("game", "getPublicChat", new HashMap<>()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }));
        privateChatButton.addListener(new SimpleChangeListener(this::showPrivateChats));
        roomsButton.addListener(new SimpleChangeListener(this::showRooms));
    }

    private void showChat(Chat chat) {
        contentTable.clearChildren();
        try {
            chat = (Chat) game.conn.sendObjectRequest("Chat", chat.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        currentChat = chat;
        for (Message message : chat.getMessages()) {
            if (!getUser().equals(message.getSender())) {
                message.setSeen(true);
            }
            contentTable.add(new Image(new Texture(message.getSender().getAvatarFileHandle())));
            contentTable.add(
                new Label(message.getSender().getUsername() + ": " + message.getContent(),
                    game.skin)).growX().row();
            contentTable.add(new Label(message.getDate(), game.skin)).growX();
            String seen = message.isSeen() ? "seen" : "not seen";
            contentTable.add(new Label(seen, game.skin)).growX().row();
            TextButton deleteButton = new TextButton("Delete",
                game.skin), editButton = new TextButton(
                "Edit", game.skin);
            contentTable.add(deleteButton).growX();
            contentTable.add(editButton).growX().row();
            Chat copyChat = chat;
            deleteButton.addListener(new SimpleChangeListener(() -> {
                try {
                    game.conn.sendOperatorRequest("game", "deleteMessage", new HashMap<>() {
                        {
                            put("chat", copyChat);
                            put("message", message);
                        }
                    });
                    showChat(copyChat);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }));
            editButton.addListener(new SimpleChangeListener(() -> {
                try {
                    game.conn.sendOperatorRequest("game", "editMessage", new HashMap<>() {
                        {
                            put("chat", copyChat);
                            put("message", message);
                            put("content", messageField.getText());
                        }
                    });
                    showChat(copyChat);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }));
        }
        messageField = new TextField("", game.skin);
        contentTable.add(messageField).growX();
        contentTable.add(sendButton).growX().row();
        sendButton.addListener(new SimpleChangeListener(this::sendMessage));
        scrollPane.setScrollPercentY(100);
    }

    private void sendMessage() {
        if (currentChat != null && !messageField.getText().equals("")) {
            try {
                game.conn.sendOperatorRequest("game", "sendMessage", new HashMap<>() {
                    {
                        put("chat", currentChat);
                        put("content", messageField.getText());
                        put("user", getUser());
                    }
                });
                messageField.setText("");
                showChat(currentChat);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private User getUser() {
        return screen.getMyself().getUser();
    }

    private void showPrivateChats() {
        currentChat = null;
        contentTable.clearChildren();
        try {
            List<User> users = (List<User>) game.conn
                .sendOperatorRequest("auth", "getUsers", new HashMap<>());
            for (User user : users) {
                if (!user.equals(getUser())) {
                    TextButton button = new TextButton(user.getUsername(), game.skin);
                    button.addListener(new SimpleChangeListener(() -> {
                        try {
                            game.conn.sendOperatorRequest("game", "createChat", new HashMap<>() {
                                {
                                    put("users", List.of(getUser(), user));
                                }
                            });
                            Set<User> userSet = new HashSet<>(List.of(getUser(), user));
                            List<Chat> chats = (List<Chat>) game.conn.sendOperatorRequest("game",
                                "getChats", new HashMap<>() {
                                    {
                                        put("user", getUser());
                                    }
                                });
                            for (Chat chat : chats) {
                                if (chat.getUsers().equals(userSet)) {
                                    showChat(chat);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }));
                    contentTable.add(button).growX().row();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showRooms() {
        currentChat = null;
        contentTable.clearChildren();
        try {
            List<Chat> chats = (List<Chat>) game.conn.sendOperatorRequest("game",
                "getChats", new HashMap<>() {
                    {
                        put("user", getUser());
                    }
                });
            for (Chat chat : chats) {
                if (chat.getUsers().size() > 2) {
                    TextButton button = new TextButton(chat.getName(), game.skin);
                    button.addListener(new SimpleChangeListener(() -> {
                        showChat(chat);
                    }));
                    contentTable.add(button).growX().row();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
