package goos.auction_sniper;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel implements
        SniperListener {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
    private final static String[] STATUS_TEXT = {
        "Joining",
        "Bidding",
        "Winning",
        "Lost",
        "Won"
    };

    private SniperSnapshot snapshot = STARTING_UP;

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshot);
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot snapshot) {
        this.snapshot = snapshot;
        fireTableRowsUpdated(0, 0);
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    public enum Column {
        ITEM_IDENTIFIER("Item") {
            @Override
            public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.itemId;
            }
        },
        LAST_PRICE("Last Price") {
            @Override
            public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.lastPrice;
            }
        },
        LAST_BID("Last Bid") {
            @Override
            public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.lastBid;
            }
        },
        SNIPER_STATUS("State") {
            @Override
            public Object valueIn(SniperSnapshot snapshot) {
                return textFor(snapshot.state);
            }
        };

        Column(String name) {
            this.name = name;
        }

        public final String name;

        public static Column at(int offset) {
            return values()[offset];
        }

        abstract public Object valueIn(SniperSnapshot snapshot);
    }
}
