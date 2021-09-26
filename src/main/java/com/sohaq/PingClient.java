package com.sohaq;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PingClient {
    private static final int MAX_TIMEOUT = 1000;    // milliseconds

    public static void main(String[] args) throws Exception {
        List<PingResponse> res = doPing("https://www.google.com");
        assert res != null;
        System.out.println(res);
    }

    public static List<PingResponse> doPing(String serviceUrl) throws Exception {
        // Get command line arguments.
        if (serviceUrl == null) {
            System.out.println("Required arguments: Server port");
            return null;
        }
        // Port number to access
        // Server to Ping (has to have the PingServer running)
        String hostname = new URL(serviceUrl).getHost();
        URL url = new URL(serviceUrl);
        int port = url.getDefaultPort();

        InetAddress server = InetAddress.getByName(hostname);
        // Create a data gram socket for sending and receiving UDP packets
        // through the port specified on the command line.
        DatagramSocket socket = new DatagramSocket(port);
        int sequence_number = 0;
        // Processing loop.
        List<PingResponse> list = new ArrayList<>();
        while (sequence_number < 10) {
            // Timestamp in ms when we send it
            Date now = new Date();
            long msSend = now.getTime();
            // Create string to send, and transfer i to a Byte Array
            String pingCommand = "PING " + sequence_number + " " + msSend + " \n";
            byte[] buf = pingCommand.getBytes();
            // Create a data gram packet to send as an UDP packet.
            DatagramPacket ping = new DatagramPacket(buf, buf.length, server, port);

            // Send the Ping data gram to the specified server
            socket.send(ping);
            // Try to receive the packet - but it can fail (timeout)
            try {
                // Set up the timeout 1000 ms = 1 sec
                socket.setSoTimeout(MAX_TIMEOUT);
                // Set up an UPD packet for receiving
                DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
                // Try to receive the response from the ping
                socket.receive(response);
                // If the response is received, the code will continue here, otherwise it will continue in the catch
                // timestamp for when we received the packet
                now = new Date();
                long msReceived = now.getTime();
                // Print the packet and the delay
                PingResponse pingResponse = printData(response, msReceived - msSend);
                pingResponse.setPort(port);
                pingResponse.setHost(hostname);
                list.add(pingResponse);
            } catch (IOException e) {
                // Print which packet has timed out
                PingResponse pr = new PingResponse("Timeout for packet " + sequence_number, 0);
                list.add(pr);
                System.out.println("Timeout for packet " + sequence_number);
            }
            // next packet
            sequence_number++;
//            Thread.sleep(1000);
        }
        return list;
    }

    /*
     * Print ping data to the standard output stream.
     * slightly changed from PingServer
     */
    private static PingResponse printData(DatagramPacket request, long delayTime) throws Exception {
        // Obtain references to the packet's array of bytes.
        byte[] buf = request.getData();

        // Wrap the bytes in a byte array input stream,
        // so that you can read the data as a stream of bytes.
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);

        // Wrap the byte array output stream in an input stream reader,
        // so you can read the data as a stream of characters.
        InputStreamReader isr = new InputStreamReader(bais);

        // Wrap the input stream reader in a buffered reader,
        // so you can read the character data a line at a time.
        // (A line is a sequence of chars terminated by any combination of \r and \n.)
        BufferedReader br = new BufferedReader(isr);

        // The message data is contained in a single line, so read this line.
        String line = br.readLine();
        // Print host address and data received from it.
        String ip = request.getAddress().getHostAddress();
        System.out.println("Received from " + request.getAddress().getHostAddress() + ": " + line + " Delay: " + delayTime);
        return new PingResponse(ip, delayTime);
    }
}
