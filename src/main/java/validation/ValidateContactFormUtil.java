package validation;

import org.springframework.stereotype.Service;

@Service
public class ValidateContactFormUtil {

    public static boolean validateFirstName(String s)
    {
        boolean errors=false;

       for( int i = 0; i<s.length(); i++){
           if (( (int) s.charAt(i) < 65 && (int) s.charAt(i) != 45 && (int) s.charAt(i) != 32 ) || ( (int)s.charAt(i) > 90 && (int) s.charAt(i) < 97 ) || (int)s.charAt(i)>122 ){
                return true;
           }
       }

        return errors;
    }

    public static boolean validateLastName(String s){
        boolean errors=false;

        for( int i = 0; i<s.length(); i++){
            if (( (int) s.charAt(i) < 65 && (int) s.charAt(i) != 45 && (int) s.charAt(i) != 32 && (int)s.charAt(i) != 34 && (int)s.charAt(i) != 39) || ( (int)s.charAt(i) > 90 && (int) s.charAt(i) < 97 ) || (int)s.charAt(i)>122 ){
                return true;
            }
        }

        return errors;
    }
}
