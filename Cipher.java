import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Cipher{
    //rotors
    private static final String I = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
    private static final String II = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
	private static final String III = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
	private static final String IV = "ESOVPZJAYQUIRHXLNFTGKDCMWB";
	private static final String V = "VZBRGITYUPSDNHLXAWMJQOFECK";

    //reflectors
    private static final String A = "EJMZALYXVBWFCRQUONTSPIKHGD";
	private static final String B = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
	private static final String C = "FVPJIAOYEDRZXWGCTKUQSBNMHL";

    public static String rotorAssigner(String arr) {
        switch(arr) {
            case "V":
                return V;

            case "IV":
                return IV;

            case "III":
                return III;

            case "II":
                return II;

            default:
                return I;
        }
    }

    public static void encrypted(int shift[], String rot[], String ref, String userInput) {
        //shift rotors
        String[] shiftedRot = new String[3];
        for(int i = 0; i < 3; i++){
            if(shift[i] != 0){
                char[] modRot = rot[i].toCharArray(); String current = rot[i];
                for(int r = 0; r <= shift[i]; r++){
                    char last = modRot[25];
                    for(int j = current.length()-1; j > 0; j--){
                        modRot[j] = modRot[j-1];    
                    }
                    modRot[0] = last;
                }
                shiftedRot[i] = String.valueOf(modRot);
            }
            
            else{
                shiftedRot[i] = rot[i];
            }
        }

        //encrypt by shifted rotors
        String changingInput = userInput;
        char[] storing = new char[userInput.length()];
        for(int i = 0; i < 3; i++){
            char[] rotor = shiftedRot[i].toCharArray();
            char[] input = changingInput.toCharArray();
            for(int j = 0; j < userInput.length(); j++){
                storing[j] = rotor[(int)input[j]-65];
            }
            changingInput = String.valueOf(storing);
        }

        //reflect 
        char[] reflector = ref.toCharArray();
        char[] input = changingInput.toCharArray();
        for(int j = 0; j < userInput.length(); j++){
            storing[j] = reflector[(int)input[j]-65];
        }
        changingInput = String.valueOf(storing);

        //invert rotors
        String[] invertedRot = new String[3];
        for(int i = 0; i < 3; i++){
            if(shift[i] != 0){
                char[] modRot = shiftedRot[i].toCharArray(); String current = shiftedRot[i];
                for(int r = 0; r <= 13; r++){
                    char last = modRot[25];
                    for(int j = current.length()-1; j > 0; j--){
                        modRot[j] = modRot[j-1];    
                    }
                    modRot[0] = last;
                }
                invertedRot[i] = String.valueOf(modRot);
            }
            else{
                invertedRot[i] = shiftedRot[i];
            }
        }

        //encrypt with inverted rotors
        for(int i = 0; i < 3; i++){
            char[] rotor = invertedRot[i].toCharArray();
            input = changingInput.toCharArray();
            for(int j = 0; j < userInput.length(); j++){
                storing[j] = rotor[(int)input[j]-65];
            }

            changingInput = String.valueOf(storing);
        }
        System.out.println("\n Encrypted: "+ changingInput);
    }
   public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        //get rotor configuration
        System.out.print(" Enigma Cipher Progam \n\n Please select rotor configurations of the three rotors (I - V): ");
        String rotConfig = input.readLine().toUpperCase();
        String[] rotArr = rotConfig.split(" ");
        String[] indRot = new String[3];
        for(int i = 0; i < 3; i++ ){
            indRot[i] = rotorAssigner(rotArr[i]);
        }

        //get reflector type
        System.out.print("\n Now select reflector type (A, B, or C): ");
        char ref = input.readLine().toUpperCase().charAt(0);
        String refConfig = "holder";
        switch (ref){
            case 'A':
                refConfig = A;
                break;
            case 'B':
                refConfig = B;
                break;
            default:
                refConfig = C;
                break;
        }

        //get rotor daily setting
        System.out.print("\n Rotor daily setting (1-26, i.e. 4 13 23): ");
        String[] daySet = input.readLine().split(" ");
        int shiftRot[] = new int[3];
        for(int i = 0; i < daySet.length; i++){
            try {
                shiftRot[i] = Integer.parseInt(daySet[i]);
            } catch (Exception e) {
            }
        }

        //encryption loop using users inputted settings
        String cont = "Y";
        while(!cont.equals("N")){
            System.out.print("\n Please enter an input to be encrypted: ");
            String userInput =  input.readLine().toUpperCase();
            userInput = userInput.replaceAll("\\s","");
            encrypted(shiftRot, indRot, refConfig, userInput);
            System.out.print("\n Continue? (Y/N): ");
            cont = input.readLine().toUpperCase();
        }
        System.out.println("\n Program terminated.");
    }
}
