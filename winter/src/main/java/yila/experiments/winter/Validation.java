package yila.experiments.winter;

import yila.experiments.winter.visible.BasicError;
import yila.experiments.winter.visible.WinterError;

/**
 * JFL 17/11/18
 */
public enum Validation {
    OK;

    static yila.experiments.winter.visible.Error buildError(WinterError error) {
        return new BasicError<>(error);
    }
}
