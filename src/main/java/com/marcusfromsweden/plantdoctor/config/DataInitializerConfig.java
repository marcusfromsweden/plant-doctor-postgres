package com.marcusfromsweden.plantdoctor.config;

        import com.marcusfromsweden.plantdoctor.entity.GrowingLocation;
        import com.marcusfromsweden.plantdoctor.entity.Plant;
        import com.marcusfromsweden.plantdoctor.entity.PlantSpecies;
        import com.marcusfromsweden.plantdoctor.repository.GrowingLocationRepository;
        import com.marcusfromsweden.plantdoctor.repository.PlantRepository;
        import com.marcusfromsweden.plantdoctor.repository.PlantSpeciesRepository;
        import com.marcusfromsweden.plantdoctor.util.CustomProperties;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.boot.CommandLineRunner;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;

        import java.time.LocalDate;

        @Configuration
        public class DataInitializerConfig {

            private static final Logger log = LoggerFactory.getLogger(DataInitializerConfig.class);

            private final PlantSpeciesRepository plantSpeciesRepository;
            private final GrowingLocationRepository growingLocationRepository;
            private final PlantRepository plantRepository;
            private final CustomProperties customProperties;

            public DataInitializerConfig(PlantSpeciesRepository plantSpeciesRepository,
                                         GrowingLocationRepository growingLocationRepository,
                                         PlantRepository plantRepository,
                                         CustomProperties customProperties) {
                this.plantSpeciesRepository = plantSpeciesRepository;
                this.growingLocationRepository = growingLocationRepository;
                this.plantRepository = plantRepository;
                this.customProperties = customProperties;
            }

            @Bean
            public CommandLineRunner dataInitializer() {
                return args -> {
                    if (customProperties.isDeleteAndPopulateTables()) {
                        deleteTableData();
                        populateTableData();
                    } else {
                        log.info("Skipping data initialization as delete-and-populate-tables is set to false.");
                    }
                };
            }

            private void deleteTableData() {
                log.info("Deleting all data from tables.");
                plantRepository.deleteAll();
                plantSpeciesRepository.deleteAll();
                growingLocationRepository.deleteAll();
                log.info("All data deleted.");
            }

            private void populateTableData() {
                log.info("Adding PlantSpecies");
                PlantSpecies regularBasil = new PlantSpecies();
                regularBasil.setName("Basil");
                regularBasil.setDescription("Regular basil of the mint family.");

                PlantSpecies favouriteRadish = new PlantSpecies();
                favouriteRadish.setName("Radish");
                favouriteRadish.setDescription("A root vegetable of the Brassicaceae family.");

                plantSpeciesRepository.save(regularBasil);
                plantSpeciesRepository.save(favouriteRadish);

                log.info("Adding GrowingLocations");
                GrowingLocation pot1 = new GrowingLocation();
                pot1.setLocationName("Pot 1");
                pot1.setOccupied(false);

                GrowingLocation pot2 = new GrowingLocation();
                pot2.setLocationName("Pot 2");
                pot2.setOccupied(false);

                growingLocationRepository.save(pot1);
                growingLocationRepository.save(pot2);

                log.info("Adding Plants");
                Plant rosePlant = new Plant();
                rosePlant.setPlantSpecies(regularBasil);
                rosePlant.setGrowingLocation(pot1);
                rosePlant.setComment("This is a comment.");
                rosePlant.setPlantingDate(LocalDate.parse("2021-01-01"));
                rosePlant.setGerminationDate(LocalDate.parse("2021-01-20"));

                Plant tulipPlant = new Plant();
                tulipPlant.setPlantSpecies(favouriteRadish);
                tulipPlant.setGrowingLocation(pot2);
                tulipPlant.setComment("This is another comment.");
                tulipPlant.setPlantingDate(LocalDate.parse("2021-01-11"));
                tulipPlant.setGerminationDate(LocalDate.parse("2021-01-30"));

                plantRepository.save(rosePlant);
                plantRepository.save(tulipPlant);

                log.info("Test data initialized.");
            }
        }