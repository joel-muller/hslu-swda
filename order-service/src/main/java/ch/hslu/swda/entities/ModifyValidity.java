package ch.hslu.swda.entities;

import ch.hslu.swda.messages.VerifyResponse;

public class ModifyValidity implements Modifiable {
    private final VerifyResponse response;

    public ModifyValidity(VerifyResponse response) {
        this.response = response;
    }
    @Override
    public void modify(Order order) {
        if (response.valid()) {
            order.getState().setValid(true);
        } else {
            order.getState().setCancelled(true);
        }
    }
}
