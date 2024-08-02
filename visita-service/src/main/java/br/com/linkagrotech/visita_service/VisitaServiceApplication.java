package br.com.linkagrotech.visita_service;

import br.com.linkagrotech.visita_service.model.Visita;
import br.com.linkagrotech.visita_service.repository.VisitaRepositorio;
import br.com.linkagrotech.visita_service.sync.repositorio.RepositorioEntidadeSincronizavel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.IntStream;

@SpringBootApplication
public class VisitaServiceApplication {

    @Autowired
    VisitaRepositorio visitaSincronizavelRepo;

    public static void main(String[] args) {
        SpringApplication.run(VisitaServiceApplication.class, args);
    }

    @PostConstruct
    public void popularComVisitas(){

//
//           visitaSincronizavelRepo.deleteAll();
//
//           var visitas = new ArrayList<Visita>();
//
//           IntStream.range(0,5).forEach(i->{
//               var visita = new Visita();
//               Calendar calendar = Calendar.getInstance();
//               calendar.add(Calendar.MILLISECOND,5*i);
//               visita.setId((long)i);
//               visita.setCreatedAt(calendar.getTime());
//               calendar.add(Calendar.SECOND,65*i);
//               if(i%2==0){
//                   calendar.add(Calendar.SECOND,55);
//                   visita.setUpdatedAt(calendar.getTime());
//               }
//               if(i%3==0){
//                   calendar.add(Calendar.SECOND,10);
//                   visita.setDeletedAt(calendar.getTime());
//               }
//               visitas.add(visita);
//           });
//
//           visitaSincronizavelRepo.saveAll(visitas);

    }

}
