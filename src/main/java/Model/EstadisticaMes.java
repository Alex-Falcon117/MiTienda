package Model;

import java.util.Set;

public class EstadisticaMes {

    private final Set mes;
    private final int total;

    public EstadisticaMes(Set mes, int total) {
        this.mes = mes;
        this.total = total;
    }

    public Set getMes() {
        return mes;
    }

    public int getTotal() {
        return total;
    }

}
