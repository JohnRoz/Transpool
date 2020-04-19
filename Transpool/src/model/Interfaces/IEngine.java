package model.Interfaces;

import model.Enums.UserAction;

public interface IEngine {
    String commitAction(UserAction action);
}
