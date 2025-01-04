package ua.yatsergray.football.manager.exception;

public class InsufficientTeamBankAccountBalanceException extends Exception {

    public InsufficientTeamBankAccountBalanceException(String message) {
        super(message);
    }
}
