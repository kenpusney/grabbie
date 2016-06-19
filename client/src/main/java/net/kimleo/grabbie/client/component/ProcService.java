package net.kimleo.grabbie.client.component;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class ProcService {


    public String executeProcess(ArrayList<String> command) throws IOException {
        Process process = new ProcessBuilder(command).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader.lines().collect(Collectors.joining("\n"));
    }

}
