import java.util.*;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;



public class Client {
        public String [] peer_array;
        public String date = "";
        private Scanner keyboard = new Scanner (System.in);

        Peer[] peers = new Peer[0];

        public static void main (String[] args) {
            Client client = new Client();
            try {
                client.start();
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }
        }

    public void start() throws IOException {
        Scanner keyboard = new Scanner(System.in);
        String command = "";
        Socket sock = new Socket("136.159.5.22", 55921);
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));

        ArrayList<Peer> peer_list = new ArrayList<Peer>();

        boolean loop = true;

        while(loop){
            String response_string = stdIn.readLine();
            switch (response_string){
                case "get team name":
                    getTeamName(sock);
                    break;

                case "get code":
                    getCode(sock);
                    break;

                case "get report":
                    sendReport(sock, peer_list);
                    break;

                case "close":
                    loop = false;
                    break;

                case "receive peers":
                    recievePeers(stdIn, peer_list);
                    break;
            } 

        }



        sock.close();
    }


    private void getTeamName(Socket sock) throws IOException {
        System.out.println("Enter Team Name");
        String command = keyboard.nextLine();
        command.trim();
        String message = command + "\n";
        sock.getOutputStream().write(message.getBytes());
    } 

    private void getCode(Socket sock) throws IOException {
        System.out.println("Enter Language of Code");
        String command = keyboard.nextLine();
        command.trim();
        String message = command + "\n";

        String code = new String(Files.readAllBytes(Paths.get("code.txt")));
        message = message + code + "\n...\n";
        sock.getOutputStream().write(message.getBytes());
    } 

    private void recievePeers(BufferedReader stdIn, ArrayList<Peer> peer_list) throws IOException {
            Date currentDate = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("YY-MM-DD hh:mm:ss");
            date = timeFormat.format(currentDate);

            byte[] response = new byte[1024];
            String message = stdIn.readLine().trim();
            int number_of_peers = Integer.parseInt(message);
            peer_array = new String[number_of_peers];

            for(int i = 0; i < peer_array.length; i++) {
                String in = stdIn.readLine().trim();
                String [] current = in.split(":");
                Peer p = new Peer(current[0], current[1]);
                peer_list.add(p);
            }
    }
        

    private void sendReport(Socket sock, ArrayList<Peer> peer_list) throws IOException {

        String message = peer_list.size() + "\n";

        for(int i = 0; i < peer_list.size(); i++) {
            message = message + peer_list.get(i).display() + "\n";
        }

        message = message + "1\n" + "136.159.5.22:55921\n" + date + "\n" + peer_list.size() + "\n";

        for(int i = 0; i < peer_list.size(); i++) {
            message = message + peer_list.get(i).display() + "\n";
        }

        message = message + "\n";

        System.out.println(message);
        sock.getOutputStream().write(message.getBytes());

    }


    private void promptEnterKey() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("press enter to recieve peers");
        scanner.nextLine();

    }
}