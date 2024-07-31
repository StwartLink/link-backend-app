package br.com.linkagrotech.visita_service;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.sync.RepositorioEntidadeSincronizavel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@SpringBootApplication
public class VisitaServiceApplication {

    @Autowired
    RepositorioEntidadeSincronizavel<Visita,Long> visitaSincronizavelRepo;

    public static void main(String[] args) {
        SpringApplication.run(VisitaServiceApplication.class, args);
    }

    @PostConstruct
    public void popularComVisitas(){


           visitaSincronizavelRepo.deleteAll();

           var visitas = new ArrayList<Visita>();

           IntStream.range(0,5).forEach(i->{
               var visita = new Visita();
               Calendar calendar = Calendar.getInstance();
               calendar.add(Calendar.MILLISECOND,5*i);
               visita.setCreatedAt(calendar.getTime());
               visitas.add(visita);
           });

           visitaSincronizavelRepo.saveAll(visitas);

    }

}
