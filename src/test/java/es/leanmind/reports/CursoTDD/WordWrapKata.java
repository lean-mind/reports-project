package es.leanmind.reports.CursoTDD;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class WordWrapKata {

    private String wrap(String text, int columns){
        if (text.length() <= columns) {
            return text;
        }
        int cutPoint = columns;
        if (text.indexOf(' ') > 0){
            cutPoint = text.indexOf(' ');
        }
        String wrappedText = text.substring(0, cutPoint) + "\n";
        String remainingText = text.substring(cutPoint);
        return wrappedText  + wrap(remainingText, columns);
    }

    @Test
    public void wraps_text() throws Exception {
        assertThat(wrap("hola", 8)).isEqualTo("hola");
        assertThat(wrap("hola", 2)).isEqualTo("ho\nla");
        assertThat(wrap("lalala", 2)).isEqualTo("la\nla\nla");
        assertThat(wrap("hola mundo", 7)).isEqualTo("hola\nmundo");
    }
}
