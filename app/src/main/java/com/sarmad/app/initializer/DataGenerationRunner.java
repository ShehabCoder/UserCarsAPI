package com.sarmad.app.initializer;

import com.sarmad.app.model.CarModel;
import com.sarmad.app.model.User;
import com.sarmad.app.model.UserCar;
import com.sarmad.app.repository.CarModelRepository;
import com.sarmad.app.repository.UserCarRepository;
import com.sarmad.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class DataGenerationRunner implements CommandLineRunner {


    private static final String[] FIRST_NAMES = {"Ahmed", "Mohamed", "Ali", "Hassan", "Omar"};
    private static final String[] SECOND_NAMES = {"Ali", "Hussein", "Mohamed", "Khaled", "Youssef"};
    private static final String[] MODEL_NAMES = {"Lanser", "BMW", "Audi", "Ford", "Toyota"};
    private static final String[] TYPES = {"Hatchback", "Sedan", "SUV", "Coupe", "Convertible"};
    private static final String[] COLORS = {"Red", "Blue", "Green", "Yellow", "Black", "White", "Gray"};
    private Random random = new Random();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCarRepository userCarRepository;
    @Autowired
    private CarModelRepository carModelRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if the database exists, and create it if it doesn't
        checkAndCreateDatabase();

        // Generate data if no existing data is found
        if (userRepository.count() == 0 && carModelRepository.count() == 0 && userCarRepository.count() == 0) {
            generateData();
        }
    }

    private void checkAndCreateDatabase() {
        // Check if the database has any collections
        boolean dbExists = !mongoTemplate.getDb().listCollectionNames().into(new ArrayList<>()).isEmpty();

        if (!dbExists) {
            // Create a collection to ensure the database is created
            mongoTemplate.createCollection("initialCollection"); // Replace with your initial collection name
            System.out.println("Database created: " + mongoTemplate.getDb().getName());
        } else {
            System.out.println("Database already exists: " + mongoTemplate.getDb().getName());
        }
    }

    private void generateData() {
        // Generate Users
//        for (int i = 0; i < 5; i++) {
//            User user = new User();
//            user.setUserId(i);
//            user.setFirstName(FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
//            user.setSecondName(SECOND_NAMES[random.nextInt(SECOND_NAMES.length)]);
//            user.setLoginId("Login" + i);
//            user.setPassword("Password" + i);
//            userRepository.save(user);
//        }

//        // Fetch all User IDs
//        List<Integer> userIds = userRepository.findAll()
//                .stream()
//                .map(User::getUserId)
//                .collect(Collectors.toList());

        // Generate Car Models
        for (int i = 0; i < 1000; i++) {
            CarModel carModel = new CarModel();
            carModel.setCarModelId(i);
            carModel.setModelName(MODEL_NAMES[random.nextInt(MODEL_NAMES.length)]);
            carModel.setType(TYPES[random.nextInt(TYPES.length)]);
            carModel.setManufacturerYear(2000 + random.nextInt(25));
            carModelRepository.save(carModel);
        }

        // Fetch all Car Model IDs
        List<Integer> carModelIds = carModelRepository.findAll()
                .stream()
                .map(CarModel::getCarModelId)
                .collect(Collectors.toList());

        // Generate User Cars

        for (int i = 0; i < 2000000; i++) {
            UserCar userCar = new UserCar();
            userCar.setUserId(random.nextInt(190000));
            userCar.setCarModelId(carModelIds.get(random.nextInt( carModelIds.size())));
            userCar.setCarPlateNumber("ABCSAW"+ i + random.nextLong(1789740000));
            userCar.setColor(COLORS[random.nextInt(COLORS.length)]);
            userCarRepository.save(userCar);
        }
    }
}
