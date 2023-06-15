package ru.nsu.ccfit.kuryatnikova;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class CommandsReader {
    private final StringBuilder token = new StringBuilder();
    private final PushbackInputStream inputStream;

    public CommandsReader(InputStream inputStream) throws IOException {
        this.inputStream = new PushbackInputStream(inputStream);
        advance();
    }

    public String getToken() {
        return token.toString();
    }

    //TODO variabes
    public void advance() throws IOException {
        int c;
        token.delete(0, token.length());
        while ((c = inputStream.read()) != -1) {
            if (Character.isSpaceChar(c)) continue;
            if (Character.isDigit(c) | c == '-') {
                token.append(c);
                while ((c = inputStream.read()) != -1)
                    if (Character.isDigit(c)) {
                        token.append(c);
                    } else if (c == '.') {
                        token.append(c);
                        while ((c = inputStream.read()) != -1)
                            if (Character.isDigit(c)) {
                                token.append(c);
                            } else {
                                inputStream.unread(c);
                                return;
                            }
                    } else {
                        inputStream.unread(c);
                        return;
                    }
            }
            if (c == 's') {

                c = inputStream.read();
                if(c == 'q') {
                    token.append(c);
                    return;
                } else {
                    inputStream.unread(c);
                }
            }
            token.append(c);
            return;
        }
    };

    public boolean hasNext() {
        return !token.isEmpty();
    }
}
