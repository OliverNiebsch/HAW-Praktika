package lpon.mps.auftragsverwaltung;

import java.util.ArrayList;
import java.util.List;

import lpon.mps.auftragsverwaltung.entities.Angebot;
import lpon.mps.auftragsverwaltung.repositories.AngebotRepository;
import lpon.mps.auftragsverwaltung.services.AngebotService;
import lpon.mps.auftragsverwaltung.services.AngebotServiceImpl;
import lpon.mps.stammdatenadapter.entities.Artikel;
import lpon.mps.stammdatenadapter.entities.Kunde;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from the static inner ContextConfiguration class
// This way we can override our TodoService Implementation
@ContextConfiguration(classes=lpon.mps.auftragsverwaltung.AngebotServiceMockTest.ContextConfiguration.class)
public class AngebotServiceMockTest {

	// no @Configuration, so it does only get picked up by this test
	@ComponentScan(basePackages = "lpon.mps.auftragsverwaltung")
	// Note: there is no @PropertySource("classpath:application.properties") as this is taken from DatabaseConfig.class
    static class ContextConfiguration {

		@Autowired
		private AngebotRepository angebotRepository;
		
		@Bean(name="angebotService")
		public AngebotService angebotService() {
			return new AngebotServiceImpl(){
				// this method is mocked by our fake implementation
				@Override
				public Angebot getAngebot(long id){
					List<Artikel> artikelList = new ArrayList<Artikel>();
					Artikel artikel = new Artikel("MOCK Artikel", null);
					artikelList.add(artikel);
					return new Angebot(new Kunde(), artikelList);
				}
//				@Override
//				public Todo getTodo(long id) {
//					return new Todo(topicRepository.findOne(1L),"Test","Test",0);
//				}
			};
		}
	}

//	@Autowired
//	private TodoService todoService;
//
//    @Test
//    public void findTodo() {
//    	Todo todo = todoService.getTodo(1L);
//    	Assert.notNull(todo);
//    	Assert.isNull(todo.getId());
//    	Assert.isTrue(todo.getTopic().getId()==1L);
//    	System.out.println(todo);
//    }
}


