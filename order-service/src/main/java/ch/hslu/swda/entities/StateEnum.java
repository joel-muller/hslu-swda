package ch.hslu.swda.entities;

public enum StateEnum {
    STORED(1),
    ARTICLE_VALIDATED(2),
    CUSTOMER_VALID(3),
    READY(4);

    private final int value;

    StateEnum(int value) {
        this.value = value;
    }

    public int toNumber() {
        return value;
    }

    public static StateEnum fromNumber(int number) {
        for (StateEnum state : StateEnum.values()) {
            if (state.value == number) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid StateEnum number: " + number);
    }

}
