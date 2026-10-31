package entities;

import java.util.ArrayList;
import java.util.List;

public class cart { // Indkøbskurv der ligger i sessionen

    private final List<OrderLine> lines = new ArrayList<>();

    public void add(OrderLine line) { // Læg i kurv
        lines.add(line);
    }

    public void remove(int index) { // Fjern linje (US-7)
        if (index >= 0 && index < lines.size()) {
            lines.remove(index);
        }
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public int getTotal() { // Samlet pris (US-3)
        return lines.stream().mapToInt(OrderLine::getLinePrice).sum();
    }

    public int getCount() { // Antal cupcakes i alt
        return lines.stream().mapToInt(OrderLine::getQuantity).sum();
    }

    public boolean isEmpty() { // Er kurven tom
        return lines.isEmpty();
    }

    public void clear() { // Tøm kurv efter betaling
        lines.clear();
    }
}