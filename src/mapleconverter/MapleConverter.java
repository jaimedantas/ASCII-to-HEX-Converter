
package mapleconverter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import javax.swing.JOptionPane;


public class MapleConverter {
    private static MainWindow JanelaPrincipal;
    Charset charsetEBCDIC = Charset.forName("CP037");
    Charset charsetACSII = Charset.forName("US-ASCII");
    
    MapleConverter(){
        JanelaPrincipal = new MainWindow();
        JanelaPrincipal.addConectarListenerConverter(new ConnectListenerConverter());
        JanelaPrincipal.addConectarListenerAbout(new ConnectListenerAbout());
        JanelaPrincipal.pack();
        JanelaPrincipal.setLocationRelativeTo(null);
        JanelaPrincipal.setTitle("Maple Text Converter");
        JanelaPrincipal.setDefaultCloseOperation(JanelaPrincipal.EXIT_ON_CLOSE);
        JanelaPrincipal.input.setAutoscrolls(true);
        JanelaPrincipal.input.setLineWrap(true);
        JanelaPrincipal.output.setAutoscrolls(true);
        JanelaPrincipal.output.setLineWrap(true);
        JanelaPrincipal.setVisible(true);
    }
    public static void main(String[] args) {
        MapleConverter Tela = new MapleConverter();
    }
    class ConnectListenerConverter implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                String input = JanelaPrincipal.input.getText();
                if (JanelaPrincipal.jCheckBox1.isSelected()) input = input.replace(" ","");
                if(JanelaPrincipal.ASCCTIHex.isSelected()) JanelaPrincipal.output.setText(converterToHex(input));
                else if(JanelaPrincipal.HexToASCII.isSelected()) JanelaPrincipal.output.setText(convertToASCII(input));
                else if(JanelaPrincipal.AsciiToEbcdic.isSelected()) JanelaPrincipal.output.setText((convertToEBCDIC(input)));
                else if(JanelaPrincipal.EbcdicToAscii.isSelected()) JanelaPrincipal.output.setText((convertFromEBCDIC(input)));

            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Invalid Conversion!\n"+ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    class ConnectListenerAbout implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            JOptionPane.showMessageDialog(null, "Maple Text Converter\nCreated by Jaime Dantas\nwww.jaimedantas.com", "About", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public String converterToHex(String text){
        char[] chars = text.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
           hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString().toUpperCase();        
    }
    public String convertToASCII(String text){
         StringBuilder output = new StringBuilder("");
      for (int i = 0; i < text.length(); i += 2)
      {
         String str = text.substring(i, i + 2);
         output.append((char) Integer.parseInt(str, 16));
      }
      return output.toString();
    }
    
    public String convertToEBCDIC(String text) throws UnsupportedEncodingException{
        byte[] bytes = text.getBytes("CP1047");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);
        for (int i = 0; i < bytes.length; i++) {
            System.out.printf("%X",bytes[i]);
        }
        System.out.flush();
        System.setOut(old);
        return baos.toString();
    }
    
    public String convertFromEBCDIC(String text) throws UnsupportedEncodingException{
        int countOfHexValues = text.length() / 2;
        byte[] bytes = new byte[countOfHexValues];
        for(int i = 0; i < countOfHexValues; i++) {
            int hexValueIndex = i * 2;
            // take one hexadecimal string value
            String hexValue = text.substring(hexValueIndex, hexValueIndex + 2);
            // convert it to a byte
            bytes[i] = (byte) (Integer.parseInt(hexValue, 16) & 0xFF);
        }
        // constructs a String by decoding bytes as EBCDIC
        String string = new String(bytes, "CP1047");
        return string;
    }


    public void teste(){
      String demoString = "teste";
      //Original String
      System.out.println("Original String: "+ demoString);
       
      String hexEquivalent = converterToHex(demoString);
      //Hex value of original String
      System.out.println("Hex String: "+ hexEquivalent);
       
      String asciiEquivalent = convertToASCII(hexEquivalent);
      //ASCII value obtained from Hex value
      System.out.println("Ascii String: "+ asciiEquivalent);

    }



    
}
