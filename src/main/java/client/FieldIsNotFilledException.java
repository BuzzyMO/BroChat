package client;

/**
 * This class represent exception
 * for non defined data from text field
 *
 * @version   1.0 27 Nov 2020
 * @author    Oleksandr Lynnyk
 */
class FieldIsNotFilledException extends Exception{
    public FieldIsNotFilledException(String message){
        super(message);
    }
}
