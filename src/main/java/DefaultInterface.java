public interface DefaultInterface {

    public String tryMe(String message);

    public default String tryMeAgain(String message)  {
       return "You didn't send nothing to me";
    } ;
}
